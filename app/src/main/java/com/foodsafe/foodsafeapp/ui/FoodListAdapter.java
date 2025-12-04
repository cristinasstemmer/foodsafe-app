package com.foodsafe.foodsafeapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.FoodItem;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private final Context context;
    private final List<FoodItem> foodList;

    public FoodListAdapter(Context context, List<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        TextView tvRating;
        ImageView ivImage;
        ImageView ivDietIcon;
        Button btnDetails;
        ImageView ivFavorite;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_alimento_nome);
//            tvDescription = itemView.findViewById(R.id.tv_food_description);
//            tvRating = itemView.findViewById(R.id.tv_food_rating);
//            ivImage = itemView.findViewById(R.id.iv_food_image);
//            ivDietIcon = itemView.findViewById(R.id.iv_diet_icon);
//            btnDetails = itemView.findViewById(R.id.btn_view_details);
            ivFavorite = itemView.findViewById(R.id.iv_favoritar);
        }
    }


    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alimento, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem currentItem = foodList.get(position);

        holder.tvTitle.setText(currentItem.getTitle());
        holder.tvDescription.setText(currentItem.getDescription());
        holder.tvRating.setText(currentItem.getRatingInfo());

        holder.ivImage.setImageResource(currentItem.getImageResId());
        holder.ivDietIcon.setImageResource(currentItem.getDietIconResId());

        holder.btnDetails.setOnClickListener(v -> {
     });

        holder.ivFavorite.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}