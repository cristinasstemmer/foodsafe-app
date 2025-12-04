package com.foodsafe.foodsafeapp.ui;

import java.util.Map;

public interface FilterListener {
    void onFiltersApplied(Map<String, Object> filters);
    void onFiltersCleared();
}