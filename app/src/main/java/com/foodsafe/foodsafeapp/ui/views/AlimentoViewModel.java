package com.foodsafe.foodsafeapp.ui.views;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.foodsafe.foodsafeapp.data.AlimentoDAO;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.model.Favorito;

import java.util.Collections;
import java.util.List;

public class AlimentoViewModel extends AndroidViewModel {

    private final AlimentoDAO alimentoDAO;
    private final AppDatabase db;
    private final int usuarioId;

    public AlimentoViewModel(@NonNull Application application) {
        super(application);

        db = AppDatabase.getInstance(application);
        alimentoDAO = db.alimentoDAO();

        SharedPreferences prefs =
                application.getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);

        usuarioId = prefs.getInt("USER_ID", -1);
    }

    public void update(Alimento alimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            alimentoDAO.update(alimento);
        });
    }

    public LiveData<List<Alimento>> getAll() {
        return alimentoDAO.getAllAlimentos();
    }

    public void delete(Alimento alimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            alimentoDAO.delete(alimento);
        });
    }

    public int getUserId() {
        return usuarioId;
    }

    public void favorite(Alimento alimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Favorito fav = new Favorito(usuarioId, alimento.getId());
            db.favoritoDAO().insert(fav);
        });
    }

    public void unfavorite(Alimento alimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.favoritoDAO().delete(usuarioId, alimento.getId());
        });
    }

    public LiveData<List<Alimento>> getFavoriteAlimentos() {
        LiveData<List<Integer>> favoriteFoodIds = db.favoritoDAO().getFavoriteFoodIds(usuarioId);

        return Transformations.switchMap(favoriteFoodIds, ids -> {
            if (ids == null || ids.isEmpty()) {
                return new LiveData<List<Alimento>>(Collections.emptyList()) {};
            }
            return alimentoDAO.getByIdsAsLiveData(ids);
        });
    }

    public LiveData<Favorito> isFavorite(int alimentoId) {
        return db.favoritoDAO().isFavorite(usuarioId, alimentoId);
    }
}