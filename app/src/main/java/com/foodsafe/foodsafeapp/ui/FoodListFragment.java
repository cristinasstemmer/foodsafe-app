package com.foodsafe.foodsafeapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodListFragment extends Fragment implements FilterListener {

    private RecyclerView rvFoodItems;
    private SearchView svSearchBar;
    private TextView tvFavoritesTitle;
    private TextView tvNoResults;
    private AlimentoAdapter adapter;
    private AlimentoViewModel alimentoViewModel;
    private UsuarioViewModel usuarioViewModel;
    private boolean showingFavorites = false;

    private AddAlimentoDialog addAlimentoDialog;
    private EditAlimentoDialog editAlimentoDialog;
    private ActivityResultLauncher<String[]> imagePickerLauncher;

    private String currentQuery = "";
    private List<String> currentDietaryPrefs = new ArrayList<>();
    private boolean currentSafeOnly = false;
    private List<String> currentExcludeAllergens = new ArrayList<>();
    private List<String> currentUserRestrictions = new ArrayList<>();

    public FoodListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
            if (uri != null) {
                requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (addAlimentoDialog != null && addAlimentoDialog.isShowing()) {
                    addAlimentoDialog.setImageUri(uri);
                }
                if (editAlimentoDialog != null && editAlimentoDialog.isShowing()) {
                    editAlimentoDialog.setImageUri(uri);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        rvFoodItems = view.findViewById(R.id.rv_food_items);
        svSearchBar = view.findViewById(R.id.sv_search_bar);
        tvFavoritesTitle = view.findViewById(R.id.tv_favorites_title);
        tvNoResults = view.findViewById(R.id.tv_no_results_food);
        Toolbar toolbar = view.findViewById(R.id.toolbar_food_list);

        alimentoViewModel = new ViewModelProvider(requireActivity()).get(AlimentoViewModel.class);
        usuarioViewModel = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class);

        setupRecycler();
        observeData();
        setupSearch();
        setupToolbar(toolbar);
        observeUser();

        return view;
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_add) {
                openAddFoodModal();
                return true;
            } else if (itemId == R.id.action_favorites) {
                showingFavorites = !showingFavorites;
                if (showingFavorites) {
                    item.setIcon(R.drawable.ic_favorite_filled);
                    tvFavoritesTitle.setVisibility(View.VISIBLE);
                    alimentoViewModel.getFavorites(alimentoViewModel.getUserId()).observe(getViewLifecycleOwner(), adapter::setList);
                } else {
                    item.setIcon(R.drawable.ic_favorite);
                    tvFavoritesTitle.setVisibility(View.GONE);
                    observeData();
                }
                return true;
            } else if (itemId == R.id.action_filter) {
                openFilterModal();
                return true;
            }
            return false;
        });
    }

    private void setupSearch() {
        svSearchBar.setIconifiedByDefault(false);
        svSearchBar.clearFocus();
        svSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void applyAllFilters() {
        if (adapter != null) {
            adapter.filter(currentQuery, currentDietaryPrefs, currentSafeOnly, currentUserRestrictions, currentExcludeAllergens);
        }
    }

    private void setupRecycler() {
        int columnCount = getResources().getInteger(R.integer.grid_column_count);
        rvFoodItems.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        adapter = new AlimentoAdapter(alimentoViewModel, getViewLifecycleOwner(), this, tvNoResults);
        rvFoodItems.setAdapter(adapter);
    }

    private void observeData() {
        alimentoViewModel.getAll().observe(getViewLifecycleOwner(), alimentos -> {
            adapter.setList(alimentos);
            applyAllFilters();
        });
    }

    private void observeUser() {
        usuarioViewModel.getLoggedUser().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null && usuario.getRestricoes() != null) {
                currentUserRestrictions = usuario.getRestricoes();
                applyAllFilters();
            }
        });
    }

    private void openAddFoodModal() {
        addAlimentoDialog = new AddAlimentoDialog(
                requireContext(),
                imagePickerLauncher,
                alimento -> {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase.getInstance(requireContext()).alimentoDAO().insert(alimento);
                    });
                    Toast.makeText(requireContext(), "Food added: " + alimento.getNome(), Toast.LENGTH_SHORT).show();
                }
        );
        addAlimentoDialog.show();
    }

    private void openFilterModal() {
        FilterModalFragment filterModal = new FilterModalFragment();
        filterModal.show(getParentFragmentManager(), filterModal.getTag());
    }

    public void openEditFoodModal(Alimento alimento) {
        editAlimentoDialog = new EditAlimentoDialog(
                requireContext(),
                alimento,
                imagePickerLauncher,
                editedAlimento -> {
                    alimentoViewModel.update(editedAlimento);
                    Toast.makeText(requireContext(),
                            "Updated '" + editedAlimento.getNome() + "' food!",
                            Toast.LENGTH_SHORT).show();
                }
        );
        editAlimentoDialog.show();
    }

    public void confirmDelete(Alimento alimento) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete '" + alimento.getNome() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    alimentoViewModel.delete(alimento);
                    Toast.makeText(requireContext(), "Food deleted!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
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
        if (svSearchBar != null) {
            svSearchBar.setQuery("", false);
        }
        applyAllFilters();
    }
}
