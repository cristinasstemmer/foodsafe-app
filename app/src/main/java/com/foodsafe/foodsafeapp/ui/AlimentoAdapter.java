package com.foodsafe.foodsafeapp.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable; // Importação essencial
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.ArrayList;
import java.util.List;

// Implementando a interface Filterable
public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.ViewHolder> implements Filterable {

    private final List<Alimento> listOriginal; // Contém todos os alimentos
    private List<Alimento> list; // Contém os alimentos atualmente exibidos (filtrados)

    private final AlimentoViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private final FoodListFragment fragment;

    public AlimentoAdapter(AlimentoViewModel vm, LifecycleOwner owner, FoodListFragment fragment) {
        this.viewModel = vm;
        this.lifecycleOwner = owner;
        this.fragment = fragment;
        this.listOriginal = new ArrayList<>();
        this.list = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Alimento> newList) {
        // Atualiza a lista original (fonte de verdade)
        this.listOriginal.clear();
        this.listOriginal.addAll(newList);

        // Reseta a lista de exibição para mostrar tudo
        this.list.clear();
        this.list.addAll(newList);

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

        Alimento alimento = list.get(position); // Usando a lista filtrada

        holder.txtNome.setText(alimento.getNome());
        holder.txtDesc.setText(alimento.getDescricao());

        viewModel.isFavorite(alimento.getId())
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
                viewModel.unfavorite(alimento);
            } else {
                viewModel.favorite(alimento);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            fragment.confirmDelete(alimento);
        });

        holder.btnEdit.setOnClickListener(v -> {
            openEditDialog(alimento);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // --- Implementação do Filterable ---

    @Override
    public Filter getFilter() {
        return alimentoFilter;
    }

    private final Filter alimentoFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Alimento> filteredList = new ArrayList<>();
            String charString = constraint.toString().toLowerCase().trim();

            if (charString.isEmpty()) {
                // Se a busca estiver vazia, retorna a lista completa
                filteredList.addAll(listOriginal);
            } else {
                for (Alimento alimento : listOriginal) {
                    // Lógica de busca: Verifica se o nome ou a descrição contém o texto
                    if (alimento.getNome().toLowerCase().contains(charString) ||
                            alimento.getDescricao().toLowerCase().contains(charString)) {

                        filteredList.add(alimento);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Limpa a lista atual (lista filtrada) e adiciona os resultados
            list.clear();
            list.addAll((List<Alimento>) results.values);
            // Notifica o RecyclerView para redesenhar a lista com os novos dados
            notifyDataSetChanged();
        }
    };

    // --- Funções Originais ---

    private void openEditDialog(Alimento alimento) {

        EditAlimentoDialog dialog = new EditAlimentoDialog(
                fragment.requireContext(),
                alimento,
                editedAlimento -> {
                    viewModel.update(editedAlimento);
                    Toast.makeText(fragment.requireContext(),
                            "Updated '" + editedAlimento.getNome() + "' food!",
                            Toast.LENGTH_SHORT).show();
                }
        );

        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome, txtDesc;
        ImageView btnFav;
        ImageView btnDelete;
        ImageView btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.tv_alimento_nome);
            txtDesc = itemView.findViewById(R.id.tv_alimento_alergenos);
            btnFav = itemView.findViewById(R.id.iv_favoritar);

            btnEdit = itemView.findViewById(R.id.iv_editar);
            btnDelete = itemView.findViewById(R.id.iv_deletar);
        }
    }
}