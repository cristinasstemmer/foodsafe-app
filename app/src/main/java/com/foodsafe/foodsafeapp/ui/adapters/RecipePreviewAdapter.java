package com.foodsafe.foodsafeapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Receita;
import com.foodsafe.foodsafeapp.ui.dialogs.RecipeDetailDialog;

import java.util.ArrayList;
import java.util.List;

public class RecipePreviewAdapter extends RecyclerView.Adapter<RecipePreviewAdapter.ViewHolder> {

    private List<Receita> recipeList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_card_small, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receita recipe = recipeList.get(position);
        holder.tvRecipeName.setText(recipe.getNome());

        Context context = holder.itemView.getContext();
        String imageName = recipe.getImagemUri();
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        Glide.with(context)
                .load(resourceId)
                .placeholder(R.drawable.ic_food_placeholder)
                .error(R.drawable.ic_food_placeholder)
                .into(holder.ivRecipeImage);

        holder.itemView.setOnClickListener(v -> {
            RecipeDetailDialog.show(v.getContext(), recipe);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setRecipeList(List<Receita> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRecipeImage;
        TextView tvRecipeName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.iv_recipe_image_small);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_title_small);
        }
    }
}
