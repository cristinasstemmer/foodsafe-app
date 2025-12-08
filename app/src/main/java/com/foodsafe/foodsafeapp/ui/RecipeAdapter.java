package com.foodsafe.foodsafeapp.ui;

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
import java.util.ArrayList;
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

        Glide.with(holder.itemView.getContext())
                .load(recipe.getImagemUrl())
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

    public void filter(String query, List<String> dietaryPrefs, boolean safeOnly, List<String> userRestrictions, List<String> excludeAllergens) {
        List<Receita> filteredList = new ArrayList<>();

        for (Receita recipe : recipesFull) {
            String recipeRestrictions = recipe.getRestricoes() != null ? recipe.getRestricoes().toLowerCase() : "";

            // Text search filter
            boolean matchesQuery = query.isEmpty() || recipe.getNome().toLowerCase().contains(query.toLowerCase());

            // Dietary preferences filter
            boolean matchesDiet = true;
            if (dietaryPrefs != null && !dietaryPrefs.isEmpty()) {
                for (String pref : dietaryPrefs) {
                    if (!recipeRestrictions.contains(pref.toLowerCase())) {
                        matchesDiet = false;
                        break;
                    }
                }
            }

            // "Safe only" filter
            boolean isSafe = true;
            if (safeOnly && userRestrictions != null && !userRestrictions.isEmpty()) {
                for (String userRestriction : userRestrictions) {
                    if (recipeRestrictions.contains(userRestriction.toLowerCase())) {
                        isSafe = false;
                        break;
                    }
                }
            }
            
            // Temporary "Exclude allergens" filter
            boolean isExcluded = false;
            if (excludeAllergens != null && !excludeAllergens.isEmpty()) {
                for (String exclusion : excludeAllergens) {
                    if (recipeRestrictions.contains(exclusion.toLowerCase())) {
                        isExcluded = true;
                        break;
                    }
                }
            }

            if (matchesQuery && matchesDiet && isSafe && !isExcluded) {
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
