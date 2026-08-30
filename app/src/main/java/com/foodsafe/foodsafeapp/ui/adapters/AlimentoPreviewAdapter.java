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

public class AlimentoPreviewAdapter extends RecyclerView.Adapter<AlimentoPreviewAdapter.ViewHolder> {

    private List<Alimento> alimentoList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alimento alimento = alimentoList.get(position);
        holder.tvAlimentoName.setText(alimento.getNome());

        Context context = holder.itemView.getContext();
        String imageName = alimento.getImagemUri();
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        Glide.with(context)
                .load(resourceId)
                .placeholder(R.drawable.ic_food_placeholder)
                .error(R.drawable.ic_food_placeholder)
                .into(holder.ivAlimentoImage);

        holder.itemView.setOnClickListener(v -> {
            FoodDetailDialog.show(v.getContext(), alimento);
        });
    }

    @Override
    public int getItemCount() {
        return alimentoList.size();
    }

    public void setAlimentoList(List<Alimento> alimentoList) {
        this.alimentoList = alimentoList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlimentoImage;
        TextView tvAlimentoName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAlimentoImage = itemView.findViewById(R.id.iv_alimento_preview_image);
            tvAlimentoName = itemView.findViewById(R.id.tv_alimento_preview_title);
        }
    }
}
