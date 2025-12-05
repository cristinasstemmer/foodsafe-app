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

    public interface FoodActionCallbacks {
        void onDeleteClicked(FoodItem foodItem);
        void onEditClicked(FoodItem foodItem);
        // Pode-se adicionar onFavoriteClicked(FoodItem foodItem) se a lógica for tratada na Activity
    }

    private final Context context;
    private final List<FoodItem> foodList;
    private final FoodActionCallbacks callbacks;

    public FoodListAdapter(Context context, List<FoodItem> foodList, FoodActionCallbacks callbacks) {
        this.context = context;
        this.foodList = foodList;
        this.callbacks = callbacks;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        TextView tvAllergenos;
        ImageView ivFavorite;

        ImageView ivEdit;
        ImageView ivDelete;
        ImageView ivImage;

        // Views comentadas (não mapeadas no item_alimento.xml atual, mas mantidas por segurança)
        // TextView tvRating;
        // ImageView ivDietIcon;
        // Button btnDetails;


        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_alimento_nome);
            tvAllergenos = itemView.findViewById(R.id.tv_alimento_alergenos);
            ivFavorite = itemView.findViewById(R.id.iv_favoritar);

            ivEdit = itemView.findViewById(R.id.iv_editar);
            ivDelete = itemView.findViewById(R.id.iv_deletar);
            ivImage = itemView.findViewById(R.id.iv_alimento_imagem);

            // Mapeamento original (comentado por falta de IDs no item_alimento.xml)
            // tvDescription = itemView.findViewById(R.id.tv_food_description);
            // tvRating = itemView.findViewById(R.id.tv_food_rating);
            // ivDietIcon = itemView.findViewById(R.id.iv_diet_icon);
            // btnDetails = itemView.findViewById(R.id.btn_view_details);
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

        // Assumindo que FoodItem tem o método getAlergenosString() para o segundo TextView
        // holder.tvAllergenos.setText("Alergênos: " + currentItem.getAlergenosString());

        // Exibição da Imagem (assumindo que FoodItem tem getImagemResId())
        // holder.ivImage.setImageResource(currentItem.getImagemResId());

        // --- Configuração dos Listeners de Ação (CRUD) ---

        // Listener do Favoritar
        holder.ivFavorite.setOnClickListener(v -> {
            // Lógica para Favoritar (pode ser tratada aqui ou no callback)
            // Exemplo simples:
            // currentItem.toggleFavorite();
            // holder.ivFavorite.setImageResource(currentItem.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite);
        });

        holder.ivDelete.setOnClickListener(v -> {
            if (callbacks != null) {
                callbacks.onDeleteClicked(currentItem);
            }
        });

        holder.ivEdit.setOnClickListener(v -> {
            if (callbacks != null) {
                callbacks.onEditClicked(currentItem);
            }
        });

        // Removidas chamadas a métodos como getRatingInfo(), getImageResId(), etc.,
        // pois os IDs correspondentes não estão no layout item_alimento.xml fornecido.
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // Método para atualizar a lista (útil para filtros e CRUD)
    public void updateList(List<FoodItem> newList) {
        this.foodList.clear();
        this.foodList.addAll(newList);
        notifyDataSetChanged();
    }
}