package com.foodsafe.foodsafeapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Usuario;
import com.foodsafe.foodsafeapp.util.Restrictions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterModalFragment extends BottomSheetDialogFragment {

    private FilterListener filterListener;
    private UsuarioViewModel usuarioViewModel;
    private ChipGroup chipGroup;
    private SwitchMaterial switchSafe;
    private AutoCompleteTextView etExcludeAllergens;
    private Usuario currentUser;
    private final List<String> selectedExclusions = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FilterListener) {
            filterListener = (FilterListener) context;
        } else if (getParentFragment() instanceof FilterListener) {
            filterListener = (FilterListener) getParentFragment();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_modal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipGroup = view.findViewById(R.id.chip_group_dietary);
        switchSafe = view.findViewById(R.id.switch_only_safe);
        etExcludeAllergens = view.findViewById(R.id.et_exclude_allergens);
        TextInputLayout tilExcludeAllergens = view.findViewById(R.id.til_exclude_allergens);
        Button btnApply = view.findViewById(R.id.btn_apply_filters);
        Button btnClear = view.findViewById(R.id.btn_clear_filters);
        ImageView ivClose = view.findViewById(R.id.iv_close_modal);

        usuarioViewModel = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class);

        usuarioViewModel.getLoggedUser().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                currentUser = usuario;
                setupInitialState(usuario.getRestricoes());
            }
        });

        etExcludeAllergens.setOnClickListener(v -> showExcludeDialog());
        tilExcludeAllergens.setEndIconOnClickListener(v -> showExcludeDialog());

        ivClose.setOnClickListener(v -> dismiss());

        btnClear.setOnClickListener(v -> {
            chipGroup.clearCheck();
            selectedExclusions.clear();
            updateExcludeAllergensText();
            if (filterListener != null) {
                filterListener.onFiltersCleared();
            }
            dismiss();
        });

        btnApply.setOnClickListener(v -> {
            if (currentUser != null) {
                // Note: We don't save the temporary exclusions to the user's profile
                if (filterListener != null) {
                    Map<String, Object> currentFilters = collectCurrentFilters();
                    filterListener.onFiltersApplied(currentFilters);
                }
            }
            dismiss();
        });
    }

    private void setupInitialState(List<String> restrictions) {
        if (restrictions == null) return;
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (restrictions.contains(chip.getText().toString())) {
                chip.setChecked(true);
            }
        }
    }

    private List<String> getSelectedChipTexts() {
        List<String> selectedTexts = new ArrayList<>();
        for (int id : chipGroup.getCheckedChipIds()) {
            Chip chip = chipGroup.findViewById(id);
            if (chip != null) {
                selectedTexts.add(chip.getText().toString());
            }
        }
        return selectedTexts;
    }

    private void showExcludeDialog() {
        final String[] items = Restrictions.ALL_RESTRICTIONS;
        boolean[] checkedItems = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            checkedItems[i] = selectedExclusions.contains(items[i]);
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Select allergens to exclude")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    String selected = items[which];
                    if (isChecked) {
                        selectedExclusions.add(selected);
                    } else {
                        selectedExclusions.remove(selected);
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> updateExcludeAllergensText())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateExcludeAllergensText() {
        if (selectedExclusions.isEmpty()) {
            etExcludeAllergens.setText("");
        } else {
            etExcludeAllergens.setText(TextUtils.join(", ", selectedExclusions));
        }
    }

    private Map<String, Object> collectCurrentFilters() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("safe_only", switchSafe.isChecked());
        filters.put("dietary_preferences", getSelectedChipTexts());
        filters.put("exclude_allergens", new ArrayList<>(selectedExclusions)); // Pass a copy
        Log.d("FilterModal", "Filtros coletados: " + filters);
        return filters;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}