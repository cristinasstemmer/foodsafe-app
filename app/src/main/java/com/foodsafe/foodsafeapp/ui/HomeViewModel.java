package com.foodsafe.foodsafeapp.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.model.Receita;
import com.foodsafe.foodsafeapp.model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HomeViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final MutableLiveData<Usuario> loggedInUser = new MutableLiveData<>();
    private final MutableLiveData<List<Alimento>> foodPreview = new MutableLiveData<>();
    private final MutableLiveData<List<Receita>> recipePreview = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        refreshData();
    }

    public LiveData<Usuario> getLoggedInUser() {
        return loggedInUser;
    }

    public LiveData<List<Alimento>> getFoodPreview() {
        return foodPreview;
    }

    public LiveData<List<Receita>> getRecipePreview() {
        return recipePreview;
    }

    public void refreshData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            SharedPreferences prefs = getApplication().getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);
            int userId = prefs.getInt("USER_ID", -1);
            Usuario user = db.usuarioDAO().getById(userId);
            loggedInUser.postValue(user);

            if (user != null) {
                // Load Food Preview
                List<Alimento> allFoods = db.alimentoDAO().getAllAlimentosSync();
                List<Integer> favoriteFoodIds = db.favoritoDAO().getByUserId(userId).stream()
                        .map(fav -> fav.getIdAlimento()).collect(Collectors.toList());

                List<Alimento> prioritizedFoods = prioritizeAlimentos(allFoods, favoriteFoodIds, user.getRestricoes());
                foodPreview.postValue(prioritizedFoods.stream().limit(3).collect(Collectors.toList()));

                // Load Recipe Preview
                List<Receita> allRecipes = db.receitaDAO().getAll();
                List<Integer> favoriteRecipeIds = db.favoritoReceitaDAO().getFavoriteRecipeIdsByUserId(userId);
                
                List<Receita> prioritizedRecipes = prioritizeReceitas(allRecipes, favoriteRecipeIds, user.getRestricoes());
                recipePreview.postValue(prioritizedRecipes.stream().limit(5).collect(Collectors.toList()));
            }
        });
    }

    private List<Alimento> prioritizeAlimentos(List<Alimento> all, List<Integer> favorites, List<String> restrictions) {
        List<Alimento> result = new ArrayList<>();
        // 1. Add favorites
        for (Integer favId : favorites) {
            all.stream().filter(a -> a.getId() == favId).findFirst().ifPresent(result::add);
        }

        // 2. Add compatible (if needed)
        if (result.size() < 3) {
            for (Alimento food : all) {
                if (!favorites.contains(food.getId()) && isAlimentoCompatible(food.getContem_alergenos(), restrictions)) {
                    result.add(food);
                    if (result.size() >= 3) break;
                }
            }
        }

        // 3. Add random (if still needed)
        if (result.size() < 3) {
            Collections.shuffle(all);
            for (Alimento food : all) {
                if (!result.contains(food)) {
                    result.add(food);
                    if (result.size() >= 3) break;
                }
            }
        }
        return result;
    }
    
    private List<Receita> prioritizeReceitas(List<Receita> all, List<Integer> favorites, List<String> restrictions) {
        List<Receita> result = new ArrayList<>();
        // 1. Add favorites
        for (Integer favId : favorites) {
            all.stream().filter(r -> r.getId() == favId).findFirst().ifPresent(result::add);
        }
        
        // 2. Add compatible
        if (result.size() < 5) {
            for (Receita recipe : all) {
                if (!favorites.contains(recipe.getId()) && isReceitaCompatible(recipe.getRestricoes(), restrictions)) {
                    result.add(recipe);
                    if (result.size() >= 5) break;
                }
            }
        }
        
        // 3. Add random
        if (result.size() < 5) {
            Collections.shuffle(all);
            for (Receita recipe : all) {
                if (!result.contains(recipe)) {
                    result.add(recipe);
                    if (result.size() >= 5) break;
                }
            }
        }
        return result;
    }

    private boolean isReceitaCompatible(String itemAllergens, List<String> userRestrictions) {
        if (userRestrictions == null || userRestrictions.isEmpty()) return true;
        if (itemAllergens == null || itemAllergens.isEmpty()) return true;

        for (String restriction : userRestrictions) {
            if (itemAllergens.toLowerCase().contains(restriction.trim().toLowerCase())) {
                return false; // Found a matching restriction
            }
        }
        return true;
    }

    private boolean isAlimentoCompatible(List<String> itemAllergens, List<String> userRestrictions) {
        if (userRestrictions == null || userRestrictions.isEmpty()) return true;
        if (itemAllergens == null || itemAllergens.isEmpty()) return true;

        for (String userRestriction : userRestrictions) {
            for (String itemAllergen : itemAllergens) {
                if (userRestriction.trim().equalsIgnoreCase(itemAllergen.trim())) {
                    return false; // Found a matching restriction
                }
            }
        }
        return true;
    }
}