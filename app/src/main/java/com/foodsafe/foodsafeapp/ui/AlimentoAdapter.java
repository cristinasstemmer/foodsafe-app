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

import com.bumptech.glide.Glide;
import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.ViewHolder> {

    private List<Alimento> listFull = new ArrayList<>();
    private List<Alimento> listFiltered = new ArrayList<>();
    private final AlimentoViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private final FoodListFragment fragment;
    private final TextView tvNoResults;

    public AlimentoAdapter(AlimentoViewModel vm, LifecycleOwner owner, FoodListFragment fragment, TextView tvNoResults) {
        this.viewModel = vm;
        this.lifecycleOwner = owner;
        this.fragment = fragment;
        this.tvNoResults = tvNoResults;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Alimento> newList) {
        this.listFull = new ArrayList<>(newList);
        this.listFiltered = new ArrayList<>(newList);
        notifyDataSetChanged();
        updateNoResultsView();
    }

    private void updateNoResultsView() {
        if (tvNoResults != null) {
            tvNoResults.setVisibility(listFiltered.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alimento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alimento alimento = listFiltered.get(position);

        holder.txtNome.setText(alimento.getNome());
        holder.txtDesc.setText(alimento.getDescricao());

        Glide.with(holder.itemView.getContext())
                .load(alimento.getImagemUri())
                .placeholder(R.drawable.ic_food_placeholder)
                .error(R.drawable.ic_food_placeholder)
                .into(holder.ivAlimentoImagem);

        viewModel.isFavorite(alimento.getId()).observe(lifecycleOwner, favorito -> {
            if (favorito != null) {
                holder.btnFav.setImageResource(R.drawable.ic_favorite_filled);
                holder.btnFav.setTag("favorited");
            } else {
                holder.btnFav.setImageResource(R.drawable.ic_favorite);
                holder.btnFav.setTag("not");
            }
        });

        holder.btnFav.setOnClickListener(v -> {
            if ("favorited".equals(holder.btnFav.getTag())) {
                viewModel.unfavorite(alimento);
            } else {
                viewModel.favorite(alimento);
            }
        });

        holder.btnDelete.setOnClickListener(v -> fragment.confirmDelete(alimento));
        holder.btnEdit.setOnClickListener(v -> fragment.openEditFoodModal(alimento));
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    public void filter(String query, List<String> dietaryPrefs, boolean safeOnly, List<String> userRestrictions, List<String> excludeAllergens) {
        List<Alimento> filteredList = new ArrayList<>();

        for (Alimento alimento : listFull) {
            // Text search filter
            boolean matchesQuery = query.isEmpty() || alimento.getNome().toLowerCase().contains(query.toLowerCase());

            // Dietary preferences filter
            boolean matchesDiet = true;
            if (dietaryPrefs != null && !dietaryPrefs.isEmpty()) {
                List<String> alimentoAllergens = alimento.getContem_alergenos() != null ? 
                        alimento.getContem_alergenos().stream().map(String::toLowerCase).collect(Collectors.toList()) : new ArrayList<>();
                for (String pref : dietaryPrefs) {
                    if (!alimentoAllergens.contains(pref.toLowerCase())) {
                        matchesDiet = false;
                        break;
                    }
                }
            }

            // "Safe only" filter (based on user's own restrictions)
            boolean isSafe = true;
            if (safeOnly && userRestrictions != null && !userRestrictions.isEmpty()) {
                if (alimento.getContem_alergenos() != null) {
                    for (String userRestriction : userRestrictions) {
                        if (alimento.getContem_alergenos().stream().anyMatch(a -> a.equalsIgnoreCase(userRestriction))) {
                            isSafe = false;
                            break;
                        }
                    }
                }
            }

            // Temporary "Exclude allergens" filter
            boolean isExcluded = false;
            if (excludeAllergens != null && !excludeAllergens.isEmpty()) {
                if (alimento.getContem_alergenos() != null) {
                    for (String exclusion : excludeAllergens) {
                        if (alimento.getContem_alergenos().stream().anyMatch(a -> a.equalsIgnoreCase(exclusion))) {
                            isExcluded = true;
                            break;
                        }
                    }
                }
            }

            if (matchesQuery && matchesDiet && isSafe && !isExcluded) {
                filteredList.add(alimento);
            }
        }

        listFiltered = filteredList;
        notifyDataSetChanged();
        updateNoResultsView();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtDesc;
        ImageView btnFav, btnDelete, btnEdit, ivAlimentoImagem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.tv_alimento_nome);
            txtDesc = itemView.findViewById(R.id.tv_alimento_alergenos);
            btnFav = itemView.findViewById(R.id.iv_favoritar);
            ivAlimentoImagem = itemView.findViewById(R.id.iv_alimento_imagem);
            btnEdit = itemView.findViewById(R.id.iv_editar);
            btnDelete = itemView.findViewById(R.id.iv_deletar);
        }
    }
}
