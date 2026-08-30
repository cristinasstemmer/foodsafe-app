package com.foodsafe.foodsafeapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Receita;
import com.foodsafe.foodsafeapp.ui.dialogs.RecipeDetailDialog;
import com.foodsafe.foodsafeapp.ui.views.RecipeViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Receita> recipesFull = new ArrayList<>();
    private List<Receita> recipesFiltered = new ArrayList<>();
    private final RecipeViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private final TextView tvNoResults;

    public RecipeAdapter(RecipeViewModel viewModel, LifecycleOwner lifecycleOwner, TextView tvNoResults) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.tvNoResults = tvNoResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receita recipe = recipesFiltered.get(position);
        holder.tvRecipeName.setText(recipe.getNome());
        holder.tvRecipeDescription.setText(recipe.getDescricao());

        Context context = holder.itemView.getContext();
        String imageName = recipe.getImagemUri();
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        Glide.with(context)
                .load(resourceId)
                .placeholder(R.drawable.ic_food_placeholder)
                .error(R.drawable.ic_food_placeholder)
                .into(holder.ivRecipeImage);

        viewModel.isFavorite(recipe.getId()).observe(lifecycleOwner, favoritoReceita -> {
            if (favoritoReceita != null) {
                holder.ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
                holder.ivFavorite.setTag("favorited");
            } else {
                holder.ivFavorite.setImageResource(R.drawable.ic_favorite);
                holder.ivFavorite.setTag("not_favorited");
            }
        });

        holder.ivFavorite.setOnClickListener(v -> {
            if ("favorited".equals(holder.ivFavorite.getTag())) {
                viewModel.unfavoriteRecipe(recipe);
            } else {
                viewModel.favoriteRecipe(recipe);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            RecipeDetailDialog.show(v.getContext(), recipe);
        });
    }

    @Override
    public int getItemCount() {
        return recipesFiltered.size();
    }

    public void setRecipes(List<Receita> recipes) {
        this.recipesFull = new ArrayList<>(recipes);
        this.recipesFiltered = new ArrayList<>(recipes);
        notifyDataSetChanged();
        updateNoResultsView();
    }

    private void updateNoResultsView() {
        if (tvNoResults != null) {
            tvNoResults.setVisibility(recipesFiltered.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    public void filter(String query, boolean safeOnly, List<String> userRestrictions, List<String> excludeAllergens) {
        List<Receita> filteredList = new ArrayList<>();
        List<String> dietaryTags = Arrays.asList("vegan", "vegetarian", "gluten-free", "lactose-free");

        for (Receita recipe : recipesFull) {
            // Text search filter
            boolean matchesQuery = query.isEmpty() || recipe.getNome().toLowerCase().contains(query.toLowerCase());

            // "Safe only" filter (based on user's own restrictions)
            boolean isSafe = true;
            if (safeOnly && userRestrictions != null && !userRestrictions.isEmpty()) {
                List<String> recipeAllergens = recipe.getContem_alergenos();
                if (recipeAllergens != null) {
                    for (String userRestriction : userRestrictions) {
                        if (recipeAllergens.stream().anyMatch(a -> a.equalsIgnoreCase(userRestriction))) {
                            isSafe = false;
                            break;
                        }
                    }
                }
            }

            // Filter from modal (exclude allergens OR filter for dietary tags)
            boolean isExcluded = false;
            if (excludeAllergens != null && !excludeAllergens.isEmpty()) {
                for (String exclusion : excludeAllergens) {
                    String lowerCaseExclusion = exclusion.toLowerCase();

                    if (dietaryTags.contains(lowerCaseExclusion)) {
                        // This is a dietary tag, so we filter FOR it.
                        // Exclude the recipe if it DOES NOT have the tag.
                        String recipeDietaryTags = recipe.getRestricoes() != null ? recipe.getRestricoes().toLowerCase() : "";
                        if (!recipeDietaryTags.contains(lowerCaseExclusion)) {
                            isExcluded = true;
                            break;
                        }
                    } else {
                        // This is an allergen, so we filter it OUT.
                        // Exclude the recipe if it CONTAINS the allergen.
                        List<String> recipeAllergens = recipe.getContem_alergenos();
                        if (recipeAllergens != null && recipeAllergens.stream().anyMatch(a -> a.equalsIgnoreCase(lowerCaseExclusion))) {
                            isExcluded = true;
                            break;
                        }
                    }
                }
            }

            if (matchesQuery && isSafe && !isExcluded) {
                filteredList.add(recipe);
            }
        }

        recipesFiltered = filteredList;
        notifyDataSetChanged();
        updateNoResultsView();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRecipeImage;
        TextView tvRecipeName;
        TextView tvRecipeDescription;
        ImageView ivFavorite;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            tvRecipeDescription = itemView.findViewById(R.id.tv_recipe_description);
            ivFavorite = itemView.findViewById(R.id.iv_favorite_icon);
        }
    }
}
