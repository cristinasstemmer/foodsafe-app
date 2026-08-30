package com.foodsafe.foodsafeapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.foodsafe.foodsafeapp.ui.fragments.FoodListFragment;
import com.foodsafe.foodsafeapp.ui.fragments.HomeFragment;
import com.foodsafe.foodsafeapp.ui.fragments.RecipesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.nav_food_list) {
                loadFragment(new FoodListFragment());
                return true;
            } else if (id == R.id.nav_recipes) {
                loadFragment(new RecipesFragment());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}