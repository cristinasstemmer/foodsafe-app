package com.foodsafe.foodsafeapp.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.FavoritoReceitaDAO;
import com.foodsafe.foodsafeapp.data.ReceitaDAO;
import com.foodsafe.foodsafeapp.model.FavoritoReceita;
import com.foodsafe.foodsafeapp.model.Receita;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeViewModel extends AndroidViewModel {

    private final ReceitaDAO receitaDAO;
    private final FavoritoReceitaDAO favoritoReceitaDAO;
    private final int userId;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        receitaDAO = db.receitaDAO();
        favoritoReceitaDAO = db.favoritoReceitaDAO();

        SharedPreferences prefs =
                application.getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);
    }

    public LiveData<List<Receita>> getAllRecipes() {
        MutableLiveData<List<Receita>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            liveData.postValue(receitaDAO.getAll());
        });
        return liveData;
    }

    public void favoriteRecipe(Receita recipe) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            favoritoReceitaDAO.insert(new FavoritoReceita(userId, recipe.getId()));
        });
    }

    public void unfavoriteRecipe(Receita recipe) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            favoritoReceitaDAO.delete(userId, recipe.getId());
        });
    }

    public LiveData<List<Receita>> getFavoriteRecipes() {
        MutableLiveData<List<Receita>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Integer> ids = favoritoReceitaDAO.getFavoriteRecipeIdsByUserId(userId);
            if (ids.isEmpty()) {
                liveData.postValue(List.of());
            } else {
                liveData.postValue(receitaDAO.getByIds(ids));
            }
        });
        return liveData;
    }

    public LiveData<FavoritoReceita> isFavorite(int recipeId) {
        return favoritoReceitaDAO.isFavorite(userId, recipeId);
    }

    public LiveData<List<Receita>> searchRecipes(String query) {
        MutableLiveData<List<Receita>> liveData = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Receita> allRecipes = receitaDAO.getAll();
            if (query == null || query.isEmpty()) {
                liveData.postValue(allRecipes);
            } else {
                String lowerCaseQuery = query.toLowerCase();
                List<Receita> filteredList = allRecipes.stream()
                        .filter(r -> r.getNome().toLowerCase().contains(lowerCaseQuery) ||
                                     r.getDescricao().toLowerCase().contains(lowerCaseQuery) ||
                                     r.getIngredientes().toLowerCase().contains(lowerCaseQuery))
                        .collect(Collectors.toList());
                liveData.postValue(filteredList);
            }
        });
        return liveData;
    }
    public int getUserId() {
        return userId;
    }
}
