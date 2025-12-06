package com.foodsafe.foodsafeapp.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.ArrayList;
import java.util.List;

public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.ViewHolder> {

    private List<Alimento> lista = new ArrayList<>();
    private final AlimentoViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;

    public AlimentoAdapter(AlimentoViewModel vm, LifecycleOwner owner) {
        this.viewModel = vm;
        this.lifecycleOwner = owner;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLista(List<Alimento> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alimento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Alimento alimento = lista.get(position);

        holder.txtNome.setText(alimento.getNome());
        holder.txtDesc.setText(alimento.getDescricao());

        viewModel.isFavorito(alimento.getId())
                .observe(lifecycleOwner, favorito -> {

                    if (favorito != null) {
                        holder.btnFav.setImageResource(R.drawable.ic_favorite_filled);
                        holder.btnFav.setTag("favorited");
                    } else {
                        holder.btnFav.setImageResource(R.drawable.ic_favorite);
                        holder.btnFav.setTag("not");
                    }
                });

        holder.btnFav.setOnClickListener(v -> {

            String state = (String) holder.btnFav.getTag();

            if ("favorited".equals(state)) {
                viewModel.desfavoritar(alimento);
            } else {
                viewModel.favoritar(alimento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome, txtDesc;
        ImageView btnFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.tv_alimento_nome);
            btnFav = itemView.findViewById(R.id.iv_favoritar);
        }
    }
}
