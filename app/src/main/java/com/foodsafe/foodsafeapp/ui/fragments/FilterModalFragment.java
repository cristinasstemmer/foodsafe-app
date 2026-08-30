package com.foodsafe.foodsafeapp.ui.fragments;

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

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.ui.FilterListener;
import com.foodsafe.foodsafeapp.util.Restrictions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterModalFragment extends BottomSheetDialogFragment {

    private FilterListener filterListener;
    private SwitchMaterial switchSafe;
    private AutoCompleteTextView etExcludeAllergens;
    private final List<String> selectedExclusions = new ArrayList<>();

    private static final String ARG_INITIAL_SAFE_ONLY = "initial_safe_only";
    private static final String ARG_INITIAL_EXCLUSIONS = "initial_exclusions";

    public static FilterModalFragment newInstance(boolean initialSafeOnly, List<String> initialExclusions) {
        FilterModalFragment fragment = new FilterModalFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_INITIAL_SAFE_ONLY, initialSafeOnly);
        args.putSerializable(ARG_INITIAL_EXCLUSIONS, (Serializable) initialExclusions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FilterListener) {
            filterListener = (FilterListener) context;
        } else if (getParentFragment() instanceof FilterListener) {
            filterListener = (FilterListener) getParentFragment();
        } else {
            throw new RuntimeException(context.toString() + " must implement FilterListener");
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

        switchSafe = view.findViewById(R.id.switch_only_safe);
        etExcludeAllergens = view.findViewById(R.id.et_exclude_allergens);
        TextInputLayout tilExcludeAllergens = view.findViewById(R.id.til_exclude_allergens);
        Button btnApply = view.findViewById(R.id.btn_apply_filters);
        Button btnClear = view.findViewById(R.id.btn_clear_filters);
        ImageView ivClose = view.findViewById(R.id.iv_close_modal);

        if (getArguments() != null) {
            boolean initialSafeOnly = getArguments().getBoolean(ARG_INITIAL_SAFE_ONLY, false);
            List<String> initialExclusions = (List<String>) getArguments().getSerializable(ARG_INITIAL_EXCLUSIONS);
            switchSafe.setChecked(initialSafeOnly);
            if (initialExclusions != null) {
                selectedExclusions.addAll(initialExclusions);
                updateExcludeAllergensText();
            }
        }

        etExcludeAllergens.setOnClickListener(v -> showExcludeDialog());
        tilExcludeAllergens.setEndIconOnClickListener(v -> showExcludeDialog());

        ivClose.setOnClickListener(v -> dismiss());

        btnClear.setOnClickListener(v -> {
            selectedExclusions.clear();
            updateExcludeAllergensText();
            switchSafe.setChecked(false);
            if (filterListener != null) {
                filterListener.onFiltersCleared();
            }
            dismiss();
        });

        btnApply.setOnClickListener(v -> {
            if (filterListener != null) {
                Map<String, Object> currentFilters = collectCurrentFilters();
                filterListener.onFiltersApplied(currentFilters);
            }
            dismiss();
        });
    }

    private void showExcludeDialog() {
        final String[] items = Restrictions.ALL_RESTRICTIONS;
        boolean[] checkedItems = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            checkedItems[i] = selectedExclusions.contains(items[i]);
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Select items to exclude")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    String selected = items[which];
                    if (isChecked) {
                        if (!selectedExclusions.contains(selected)) {
                            selectedExclusions.add(selected);
                        }
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
        filters.put("exclude_allergens", new ArrayList<>(selectedExclusions));
        Log.d("FilterModal", "Collected filters: " + filters);
        return filters;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
