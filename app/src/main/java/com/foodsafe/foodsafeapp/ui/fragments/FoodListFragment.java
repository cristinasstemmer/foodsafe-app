package com.foodsafe.foodsafeapp.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.ui.adapters.AlimentoAdapter;
import com.foodsafe.foodsafeapp.ui.views.AlimentoViewModel;
import com.foodsafe.foodsafeapp.ui.FilterListener;
import com.foodsafe.foodsafeapp.ui.views.UsuarioViewModel;
import com.foodsafe.foodsafeapp.ui.dialogs.AddAlimentoDialog;
import com.foodsafe.foodsafeapp.ui.dialogs.EditAlimentoDialog;

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
    private boolean currentSafeOnly = false;
    private List<String> currentExcludeAllergens = new ArrayList<>();
    private List<String> currentUserRestrictions = new ArrayList<>();

    private LiveData<List<Alimento>> currentDataSource;
    private final Observer<List<Alimento>> listObserver = alimentos -> {
        if (adapter != null) {
            adapter.setList(alimentos);
            applyAllFilters();
        }
    };

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
        setupSearch();
        setupToolbar(toolbar);
        observeUser();

        switchDataSource(false);

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
                switchDataSource(showingFavorites);
                if (showingFavorites) {
                    item.setIcon(R.drawable.ic_favorite_filled);
                    tvFavoritesTitle.setVisibility(View.VISIBLE);
                } else {
                    item.setIcon(R.drawable.ic_favorite);
                    tvFavoritesTitle.setVisibility(View.GONE);
                }
                return true;
            } else if (itemId == R.id.action_filter) {
                openFilterModal();
                return true;
            }
            return false;
        });
    }

    private void switchDataSource(boolean showFavorites) {
        if (currentDataSource != null) {
            currentDataSource.removeObserver(listObserver);
        }

        if (showFavorites) {
            currentDataSource = alimentoViewModel.getFavoriteAlimentos();
        } else {
            currentDataSource = alimentoViewModel.getAll();
        }

        currentDataSource.observe(getViewLifecycleOwner(), listObserver);
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
            adapter.filter(currentQuery, currentSafeOnly, currentUserRestrictions, currentExcludeAllergens);
        }
    }

    private void setupRecycler() {
        int columnCount = getResources().getInteger(R.integer.grid_column_count);
        rvFoodItems.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        adapter = new AlimentoAdapter(alimentoViewModel, getViewLifecycleOwner(), this, tvNoResults);
        rvFoodItems.setAdapter(adapter);
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
        FilterModalFragment filterModal = FilterModalFragment.newInstance(currentSafeOnly, currentExcludeAllergens);
        filterModal.show(getChildFragmentManager(), filterModal.getTag());
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
        currentExcludeAllergens = (List<String>) filters.getOrDefault("exclude_allergens", new ArrayList<>());
        applyAllFilters();
    }

    @Override
    public void onFiltersCleared() {
        currentSafeOnly = false;
        currentExcludeAllergens.clear();
        if (svSearchBar != null) {
            svSearchBar.setQuery("", false);
        }
        applyAllFilters();
    }
}
