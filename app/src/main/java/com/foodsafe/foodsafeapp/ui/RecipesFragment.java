package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipesFragment extends Fragment implements FilterListener {

    private RecyclerView rvRecipesList;
    private RecipeViewModel recipeViewModel;
    private UsuarioViewModel usuarioViewModel;
    private RecipeAdapter recipeAdapter;
    private SearchView svSearch;
    private TextView tvFavoritesTitle;
    private TextView tvNoResults;
    private boolean showingFavorites = false;

    private String currentQuery = "";
    private List<String> currentDietaryPrefs = new ArrayList<>();
    private boolean currentSafeOnly = false;
    private List<String> currentExcludeAllergens = new ArrayList<>();
    private List<String> currentUserRestrictions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipes, container, false);

        rvRecipesList = view.findViewById(R.id.rv_recipes_list);
        svSearch = view.findViewById(R.id.sv_search_recipes);
        tvFavoritesTitle = view.findViewById(R.id.tv_favorites_title_recipes);
        tvNoResults = view.findViewById(R.id.tv_no_results_recipes);
        Toolbar toolbar = view.findViewById(R.id.toolbar_recipes);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        usuarioViewModel = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class);

        setupRecipesRecyclerView();
        setupToolbar(toolbar);
        setupSearch();

        observeUser();
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
                    recipeViewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), recipes -> {
                        recipeAdapter.setRecipes(recipes);
                    });
                } else {
                    item.setIcon(R.drawable.ic_favorite);
                    tvFavoritesTitle.setVisibility(View.GONE);
                    observeRecipes();
                }
                return true;
            } else if (itemId == R.id.action_filter) {
                FilterModalFragment filterModal = new FilterModalFragment();
                filterModal.show(getParentFragmentManager(), "FilterModal");
                return true;
            }
            return false;
        });
    }

    private void setupRecipesRecyclerView() {
        if (rvRecipesList != null) {
            int columnCount = getResources().getInteger(R.integer.grid_column_count);
            rvRecipesList.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
            recipeAdapter = new RecipeAdapter(recipeViewModel, getViewLifecycleOwner(), tvNoResults);
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
                currentQuery = newText;
                applyAllFilters();
                return true;
            }
        });
    }

    private void observeRecipes() {
        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), recipes -> {
            recipeAdapter.setRecipes(recipes);
            applyAllFilters();
        });
    }

    private void observeUser() {
        usuarioViewModel.getLoggedUser().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null && usuario.getRestricoes() != null) {
                currentUserRestrictions = usuario.getRestricoes();
                applyAllFilters(); // Re-apply filters if user restrictions change
            }
        });
    }

    private void applyAllFilters() {
        if (recipeAdapter != null) {
            recipeAdapter.filter(currentQuery, currentDietaryPrefs, currentSafeOnly, currentUserRestrictions, currentExcludeAllergens);
        }
    }

    @Override
    public void onFiltersApplied(Map<String, Object> filters) {
        currentSafeOnly = (boolean) filters.getOrDefault("safe_only", false);
        currentDietaryPrefs = (List<String>) filters.getOrDefault("dietary_preferences", new ArrayList<>());
        currentExcludeAllergens = (List<String>) filters.getOrDefault("exclude_allergens", new ArrayList<>());
        applyAllFilters();
    }

    @Override
    public void onFiltersCleared() {
        currentSafeOnly = false;
        currentDietaryPrefs.clear();
        currentExcludeAllergens.clear();
        if (svSearch != null) {
            svSearch.setQuery("", false);
        }
        applyAllFilters();
    }
}
