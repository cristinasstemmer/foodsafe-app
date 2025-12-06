package com.foodsafe.foodsafeapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.UsuarioDAO;
import com.foodsafe.foodsafeapp.model.Usuario;
import com.foodsafe.foodsafeapp.util.Restrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileFragment extends BottomSheetDialogFragment {

    private ImageView ivBack;
    private TextView tvName, tvEmail, tvCurrentRestrictions;
    private Button btnEditRestrictions;
    private LinearLayout llChangePassword;
    private TextView tvSignOut;
    private UsuarioDAO usuarioDAO;
    private Usuario currentUser;
    private final List<String> selectedRestrictions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usuarioDAO = AppDatabase.getInstance(getContext()).usuarioDAO();

        initializeViews(view);
        setupClickListeners();
        loadProfileData();

        return view;
    }

    private void initializeViews(View view) {
        ivBack = view.findViewById(R.id.iv_back);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        btnEditRestrictions = view.findViewById(R.id.btn_edit_restrictions);
        tvCurrentRestrictions = view.findViewById(R.id.tv_current_restrictions);
        llChangePassword = view.findViewById(R.id.ll_change_password);
        tvSignOut = view.findViewById(R.id.tv_sign_out);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> dismiss());
        btnEditRestrictions.setOnClickListener(v -> {
            if (currentUser != null) {
                showRestrictionsDialog();
            }
        });
        llChangePassword.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Open change password screen.", Toast.LENGTH_SHORT).show();
        });
        tvSignOut.setOnClickListener(v -> handleSignOut());
    }

    private void loadProfileData() {
        new Thread(() -> {
            currentUser = usuarioDAO.getLoggedInUser();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (currentUser != null) {
                        tvName.setText(currentUser.getNome());
                        tvEmail.setText(currentUser.getEmail());
                        updateRestrictionsDisplay(currentUser.getRestricoes());
                    } else {
                        Toast.makeText(getContext(), "Could not load profile data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void showRestrictionsDialog() {
        selectedRestrictions.clear();
        String currentRestrictions = currentUser.getRestricoes();
        if (currentRestrictions != null && !currentRestrictions.isEmpty()) {
            // Trim the whitespace from each item to ensure correct matching
            selectedRestrictions.addAll(Arrays.stream(currentRestrictions.split(",")).map(String::trim).collect(Collectors.toList()));
        }

        boolean[] checkedItems = new boolean[Restrictions.ALL_RESTRICTIONS.length];
        for (int i = 0; i < Restrictions.ALL_RESTRICTIONS.length; i++) {
            checkedItems[i] = selectedRestrictions.contains(Restrictions.ALL_RESTRICTIONS[i]);
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Select Dietary Restrictions")
                .setMultiChoiceItems(Restrictions.ALL_RESTRICTIONS, checkedItems, (dialog, which, isChecked) -> {
                    String selected = Restrictions.ALL_RESTRICTIONS[which];
                    if (isChecked) {
                        selectedRestrictions.add(selected);
                    } else {
                        selectedRestrictions.remove(selected);
                    }
                })
                .setPositiveButton("Save", (dialog, which) -> {
                    // Remove any empty strings and trim whitespace before joining
                    List<String> cleanedRestrictions = selectedRestrictions.stream()
                            .filter(s -> !s.isEmpty())
                            .map(String::trim)
                            .collect(Collectors.toList());
                    String newRestrictions = TextUtils.join(", ", cleanedRestrictions);
                    currentUser.setRestricoes(newRestrictions);
                    new Thread(() -> {
                        usuarioDAO.updateUsuario(currentUser);
                        if(getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                updateRestrictionsDisplay(newRestrictions);
                                Toast.makeText(getContext(), "Restrictions updated!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateRestrictionsDisplay(String restrictions) {
        if (TextUtils.isEmpty(restrictions)) {
            tvCurrentRestrictions.setText("None selected");
        } else {
            tvCurrentRestrictions.setText(restrictions);
        }
    }

    private void handleSignOut() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("USER_ID");
        editor.commit();

        Toast.makeText(getContext(), "You have been signed out.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        dismiss();
    }
}
