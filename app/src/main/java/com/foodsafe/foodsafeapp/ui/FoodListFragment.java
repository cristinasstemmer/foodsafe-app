package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.foodsafe.foodsafeapp.ui.ProfileActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodListFragment extends Fragment {

    private RecyclerView rvFoodItems;
    private ImageView ivSearch;
    private FoodListAdapter adapter;
    private EditText etSearchBar;
    private ImageView ivProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        rvFoodItems = view.findViewById(R.id.rv_food_items);
        etSearchBar = view.findViewById(R.id.et_search_bar);
        ivProfile = view.findViewById(R.id.iv_profile);

        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        setupClickListeners();

        setupFoodRecyclerView();

        return view;
    }

    private void setupClickListeners() {
        etSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar navegação para a tela de pesquisa ou abrir um SearchView
                Toast.makeText(getContext(), "Abrindo Pesquisa de Alimentos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFoodRecyclerView() {
        rvFoodItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // TO DO:
        // 1. Obter a lista de dados (do banco de dados ou API)
        // List<FoodItem> foodList = getDummyFoodItems();

        // 2. Criar e configurar o Adapter
        // adapter = new FoodListAdapter(foodList);
        // rvFoodItems.setAdapter(adapter);
    }

    // Método de exemplo para simular dados

//    private List<FoodItem> getDummyFoodItems() {
//        List<FoodItem> list = new ArrayList<>();
//        list.add(new FoodItem("Restaurante A", "Japonesa", 4.5f, "rating", "Japonesa"));
//        return list;
//    }

}