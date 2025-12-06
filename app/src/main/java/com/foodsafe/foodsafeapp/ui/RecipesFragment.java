package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;

public class RecipesFragment extends Fragment {

    private RecyclerView rvRecipesList;
    private RecipeViewModel recipeViewModel;
    private RecipeAdapter recipeAdapter;
    private SearchView svSearch;
    private TextView tvFavoritesTitle;
    private boolean showingFavorites = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipes, container, false);

        rvRecipesList = view.findViewById(R.id.rv_recipes_list);
        svSearch = view.findViewById(R.id.sv_search_recipes);
        tvFavoritesTitle = view.findViewById(R.id.tv_favorites_title_recipes);
        Toolbar toolbar = view.findViewById(R.id.toolbar_recipes);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        setupRecipesRecyclerView();
        setupToolbar(toolbar);
        setupSearch();

        observeRecipes();

        return view;
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_favorites) {
                showingFavorites = !showingFavorites;
                if (showingFavorites) {
                    item.setIcon(R.drawable.ic_favorite_filled);
                    tvFavoritesTitle.setVisibility(View.VISIBLE);
                    recipeViewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), recipeAdapter::setRecipes);
                } else {
                    item.setIcon(R.drawable.ic_favorite);
                    tvFavoritesTitle.setVisibility(View.GONE);
                    observeRecipes();
                }
                return true;
            } else if (itemId == R.id.action_filter) {
                // Implement filter logic here
                return true;
            }
            return false;
        });
    }

    private void setupRecipesRecyclerView() {
        if (rvRecipesList != null) {
            rvRecipesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recipeAdapter = new RecipeAdapter(recipeViewModel, getViewLifecycleOwner());
            rvRecipesList.setAdapter(recipeAdapter);
        }
    }

    private void setupSearch() {
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recipeAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void observeRecipes() {
        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), recipeAdapter::setRecipes);
    }
}
