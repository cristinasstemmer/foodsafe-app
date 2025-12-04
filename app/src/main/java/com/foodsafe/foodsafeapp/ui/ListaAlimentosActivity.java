package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.ui.FoodListAdapter;
import com.foodsafe.foodsafeapp.model.FoodItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ListaAlimentosActivity extends AppCompatActivity {

    private RecyclerView rvFoodList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        rvFoodList = findViewById(R.id.rv_food_list);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupRecyclerView();
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        List<FoodItem> foodItems = createDummyFoodList();

        FoodListAdapter adapter = new FoodListAdapter(this, foodItems);
        rvFoodList.setLayoutManager(new LinearLayoutManager(this));
        rvFoodList.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_food_list) {return true;
            } else if (id == R.id.nav_home) {return true;
            } else if (id == R.id.nav_recipes) {
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_food_list);
    }


    private List<FoodItem> createDummyFoodList() {
        List<FoodItem> items = new ArrayList<>();


        return items;
    }
}