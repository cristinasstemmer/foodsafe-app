package com.foodsafe.foodsafeapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;

public class FoodListFragment extends Fragment {

    private RecyclerView rvFoodItems;
    private EditText etSearchBar;
    private ImageView ivProfile;
    private Button btnAdd;

    private AlimentoAdapter adapter;
    private AlimentoViewModel alimentoViewModel;
    private Button btn_favorites;
    private boolean mostrandoFavoritos = false;

    public FoodListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        btn_favorites = view.findViewById(R.id.btn_favorites);

        rvFoodItems = view.findViewById(R.id.rv_food_items);
        etSearchBar = view.findViewById(R.id.et_search_bar);
        ivProfile = view.findViewById(R.id.iv_profile);
        btnAdd = view.findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(v -> openAddFoodModal());

        alimentoViewModel = new ViewModelProvider(requireActivity()).get(AlimentoViewModel.class);

        setupRecycler();
        observeData();

        btn_favorites.setOnClickListener(v -> {
            mostrandoFavoritos = !mostrandoFavoritos;

            if (mostrandoFavoritos) {

                btn_favorites.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_favorite_filled, 0, 0, 0
                );

                alimentoViewModel.getFavoritos(alimentoViewModel.getUsuarioId())
                        .observe(getViewLifecycleOwner(), favoritos -> adapter.setLista(favoritos));

            } else {

                btn_favorites.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_favorite, 0, 0, 0
                );

                alimentoViewModel.getAllAlimentos()
                        .observe(getViewLifecycleOwner(), alimentos -> adapter.setLista(alimentos));
            }
        });

        return view;
    }

    private void setupRecycler() {
        rvFoodItems.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlimentoAdapter(
                alimentoViewModel,
                getViewLifecycleOwner()
        );

        rvFoodItems.setAdapter(adapter);
    }

    private void observeData() {
        alimentoViewModel.getAllAlimentos().observe(getViewLifecycleOwner(), alimentos -> {
            adapter.setLista(alimentos);
        });
    }

    private void openAddFoodModal() {

        AddAlimentoDialog dialog = new AddAlimentoDialog(
                requireContext(),
                alimento -> {

                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase.getInstance(requireContext())
                                .alimentoDAO()
                                .inserir(alimento);
                    });

                    Toast.makeText(requireContext(),
                            "Alimento adicionado: " + alimento.getNome(),
                            Toast.LENGTH_SHORT).show();
                }
        );

        dialog.show();
    }
}
