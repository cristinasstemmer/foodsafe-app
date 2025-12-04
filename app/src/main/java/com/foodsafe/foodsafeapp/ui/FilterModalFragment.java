package com.foodsafe.foodsafeapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.foodsafe.foodsafeapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class FilterModalFragment extends BottomSheetDialogFragment {

    private FilterListener filterListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FilterListener) {
            filterListener = (FilterListener) context;
        } else if (getParentFragment() instanceof FilterListener) {
            filterListener = (FilterListener) getParentFragment();
        } else {

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

        Button btnApply = view.findViewById(R.id.btn_apply_filters);
        Button btnClear = view.findViewById(R.id.btn_clear_filters);
        ImageView ivClose = view.findViewById(R.id.iv_close_modal);

        Switch switchSafe = view.findViewById(R.id.switch_only_safe);

        ivClose.setOnClickListener(v -> dismiss());

        btnClear.setOnClickListener(v -> {
            if (filterListener != null) {
                filterListener.onFiltersCleared();
            }
            dismiss();
        });

        btnApply.setOnClickListener(v -> {
            if (filterListener != null) {
                Map<String, Object> currentFilters = collectCurrentFilters(view);
                filterListener.onFiltersApplied(currentFilters);
            }
            dismiss();
        });
    }

    private Map<String, Object> collectCurrentFilters(View view) {
        Map<String, Object> filters = new HashMap<>();

        Switch switchSafe = view.findViewById(R.id.switch_only_safe);
        filters.put("safe_only", switchSafe.isChecked());

        // TODO: Adicionar lógica para coletar o estado dos Chips (Dietary preferences)
        // e CheckBoxes (Exclude allergens) aqui.

        Log.d("FilterModal", "Filtros coletados: " + filters.toString());
        return filters;
    }

   @Override
    public int getTheme() {

        return R.style.BottomSheetDialogTheme;
    }
}