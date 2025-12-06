package com.foodsafe.foodsafeapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Receita;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> implements Filterable {

    private List<Receita> recipes = new ArrayList<>();
    private List<Receita> recipesFull = new ArrayList<>();
    private final RecipeViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;

    public RecipeAdapter(RecipeViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receita recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getNome());
        holder.tvRecipeDescription.setText(recipe.getDescricao());
        holder.ivRecipeImage.setImageResource(R.drawable.ic_recipes);

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
        return recipes.size();
    }

    public void setRecipes(List<Receita> recipes) {
        this.recipes = recipes;
        this.recipesFull = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    private final Filter recipeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Receita> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(recipesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Receita item : recipesFull) {
                    if (item.getNome().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipes.clear();
            recipes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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