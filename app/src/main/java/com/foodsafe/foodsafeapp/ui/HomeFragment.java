package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;

public class HomeFragment extends Fragment {

    private ImageView ivProfile;
    private RecyclerView rvFoodPreview;
    private RecyclerView rvRecipesPreview;
    private TextView tvUsername;
    private HomeViewModel homeViewModel;
    private FoodPreviewAdapter foodPreviewAdapter;
    private RecipePreviewAdapter recipePreviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        tvUsername = view.findViewById(R.id.tv_username);
        ivProfile = view.findViewById(R.id.iv_profile);
        rvFoodPreview = view.findViewById(R.id.rv_home_food_preview);
        rvRecipesPreview = view.findViewById(R.id.rv_home_recipes_preview);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        ivProfile.setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.show(getParentFragmentManager(), profileFragment.getTag());
        });

        setupRecyclerViews();
        observeViewModel();

        return view;
    }

    private void setupRecyclerViews() {
        rvFoodPreview.setLayoutManager(new LinearLayoutManager(getContext()));
        foodPreviewAdapter = new FoodPreviewAdapter();
        rvFoodPreview.setAdapter(foodPreviewAdapter);

        rvRecipesPreview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipePreviewAdapter = new RecipePreviewAdapter();
        rvRecipesPreview.setAdapter(recipePreviewAdapter);
    }

    private void observeViewModel() {
        homeViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvUsername.setText(user.getNome());
            } else {
                tvUsername.setText("Guest");
            }
        });

        homeViewModel.getFoodPreview().observe(getViewLifecycleOwner(), foods -> {
            foodPreviewAdapter.setFoodList(foods);
        });

        homeViewModel.getRecipePreview().observe(getViewLifecycleOwner(), recipes -> {
            recipePreviewAdapter.setRecipeList(recipes);
        });
    }
}