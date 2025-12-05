package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.ui.ProfileActivity;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.model.Usuario;

public class HomeFragment extends Fragment {

    private ImageView ivProfile;
    private RecyclerView rvFoodPreview;
    private RecyclerView rvRecipesPreview;
    private TextView tvUsername;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, container, false);

        tvUsername = view.findViewById(R.id.tv_username);

        db = AppDatabase.getInstance(getContext());

        observeLoggedInUser();

        ivProfile = view.findViewById(R.id.iv_profile);
        rvFoodPreview = view.findViewById(R.id.rv_home_food_preview);
        rvRecipesPreview = view.findViewById(R.id.rv_home_recipes_preview);

        ivProfile.setOnClickListener(v -> {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        setupFoodPreview();
        setupRecipesPreview();

        return view;
    }

    private void observeLoggedInUser() {
        if (db == null) return;

        db.usuarioDAO().getLoggedInUser().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario != null) {

                    tvUsername.setText(usuario.getNome());
                } else {

                    tvUsername.setText("Guest");
                }
            }
        });
    }

    private void setupFoodPreview() {
        if (rvFoodPreview != null) {
            rvFoodPreview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
    }

    private void setupRecipesPreview() {
        if (rvRecipesPreview != null) {
            rvRecipesPreview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }
}