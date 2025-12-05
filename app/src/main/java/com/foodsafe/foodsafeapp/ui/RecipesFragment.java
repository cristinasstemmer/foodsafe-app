package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.ui.ProfileActivity;

public class RecipesFragment extends Fragment {

    private RecyclerView rvRecipesList;
    private Button btnFavorites;
    private Button btnFilter;
    private ImageView ivProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipes, container, false);

        rvRecipesList = view.findViewById(R.id.rv_recipes_list);
        btnFavorites = view.findViewById(R.id.btn_favorites_recipes);
        btnFilter = view.findViewById(R.id.btn_filter_recipes);
        ivProfile = view.findViewById(R.id.iv_profile);

        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        setupRecipesRecyclerView();
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        btnFavorites.setOnClickListener(v -> {
            System.out.println("Mostrar receitas favoritas");
        });

        btnFilter.setOnClickListener(v -> {
            System.out.println("Abrir tela de filtro");
        });
    }

    private void setupRecipesRecyclerView() {
        if (rvRecipesList != null) {
            rvRecipesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
    }
}