package com.foodsafe.foodsafeapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.List;

public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.AlimentoViewHolder> {

    private final Context context;
    private final List<Alimento> listaAlimentos;

    public AlimentoAdapter(Context context, List<Alimento> listaAlimentos) {
        this.context = context;
        this.listaAlimentos = listaAlimentos;
    }

    @NonNull
    @Override
    public AlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alimento, parent, false);
        return new AlimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentoViewHolder holder, int position) {
        Alimento alimentoAtual = listaAlimentos.get(position);

        holder.tvNome.setText(alimentoAtual.getNome());

        String alergenos = alimentoAtual.getContem_alergenos();
        holder.tvAlergenos.setText(
                alergenos.isEmpty()
                        ? "Alergênicos: Nenhum"
                        : "Alergênicos: " + alergenos
        );

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, alimentoAtual.getNome(), Toast.LENGTH_SHORT).show()
        );

        holder.ivFavoritar.setOnClickListener(v ->
                Toast.makeText(context,
                        alimentoAtual.getNome() + " favoritado!",
                        Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return listaAlimentos.size();
    }

    public static class AlimentoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvAlergenos;
        ImageView ivFavoritar;

        public AlimentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_alimento_nome);
            tvAlergenos = itemView.findViewById(R.id.tv_alimento_alergenos);
            ivFavoritar = itemView.findViewById(R.id.iv_favoritar);
        }
    }
}
