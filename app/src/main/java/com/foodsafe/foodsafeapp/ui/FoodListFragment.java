package com.foodsafe.foodsafeapp.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.model.Alimento;

public class FoodListFragment extends Fragment {

    private RecyclerView rvFoodItems;
    private SearchView svSearchBar;
    private TextView tvFavoritesTitle;
    private AlimentoAdapter adapter;
    private AlimentoViewModel alimentoViewModel;
    private boolean showingFavorites = false;

    public FoodListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        rvFoodItems = view.findViewById(R.id.rv_food_items);
        svSearchBar = view.findViewById(R.id.sv_search_bar);
        tvFavoritesTitle = view.findViewById(R.id.tv_favorites_title);
        Toolbar toolbar = view.findViewById(R.id.toolbar_food_list);

        alimentoViewModel = new ViewModelProvider(requireActivity()).get(AlimentoViewModel.class);

        setupRecycler();
        observeData();
        setupSearch();
        setupToolbar(toolbar);

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
        if (svSearchBar != null) {
            svSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    performSearch(newText);
                    return true;
                }
            });
        }
    }

    private void performSearch(String query) {
        if (adapter != null) {
            adapter.getFilter().filter(query);
        }
    }

    private void setupRecycler() {
        rvFoodItems.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlimentoAdapter(alimentoViewModel, getViewLifecycleOwner(), this);
        rvFoodItems.setAdapter(adapter);
    }

    private void observeData() {
        alimentoViewModel.getAll().observe(getViewLifecycleOwner(), adapter::setList);
    }

    private void openAddFoodModal() {
        AddAlimentoDialog dialog = new AddAlimentoDialog(
                requireContext(),
                alimento -> {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        AppDatabase.getInstance(requireContext()).alimentoDAO().insert(alimento);
                    });
                    Toast.makeText(requireContext(), "Food added: " + alimento.getNome(), Toast.LENGTH_SHORT).show();
                }
        );
        dialog.show();
    }

    private void openFilterModal() {
        FilterModalFragment filterModal = new FilterModalFragment();
        filterModal.show(getParentFragmentManager(), filterModal.getTag());
    }

    public void confirmDelete(Alimento alimento) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete'" + alimento.getNome() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    alimentoViewModel.delete(alimento);
                    Toast.makeText(requireContext(), "Food deleted!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
