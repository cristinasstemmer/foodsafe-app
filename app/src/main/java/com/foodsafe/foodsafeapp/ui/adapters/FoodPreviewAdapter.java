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
import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.ui.dialogs.FoodDetailDialog;

import java.util.ArrayList;
import java.util.List;

public class FoodPreviewAdapter extends RecyclerView.Adapter<FoodPreviewAdapter.ViewHolder> {

    private List<Alimento> foodList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alimento food = foodList.get(position);
        holder.tvFoodName.setText(food.getNome());

        Context context = holder.itemView.getContext();
        String imageName = food.getImagemUri();
        int resourceId = 0;
        if (imageName != null && !imageName.isEmpty()) {
            resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        }

        Glide.with(context)
                .load(resourceId)
                .placeholder(R.drawable.ic_food_placeholder)
                .error(R.drawable.ic_food_placeholder)
                .into(holder.ivFoodImage);

        holder.itemView.setOnClickListener(v -> {
            FoodDetailDialog.show(v.getContext(), food);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setFoodList(List<Alimento> foodList) {
        this.foodList = foodList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage;
        TextView tvFoodName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.iv_alimento_preview_image);
            tvFoodName = itemView.findViewById(R.id.tv_alimento_preview_title);
        }
    }
}
