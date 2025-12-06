package com.foodsafe.foodsafeapp.ui;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.foodsafe.foodsafeapp.data.AlimentoDAO;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.model.Favorito;

import java.util.List;

public class AlimentoViewModel extends AndroidViewModel {

    private final LiveData<List<Alimento>> alimentos;
    private final AppDatabase db;
    private final int usuarioId;

    public AlimentoViewModel(@NonNull Application application) {
        super(application);

        db = AppDatabase.getInstance(application);

        SharedPreferences prefs =
                application.getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);

        usuarioId = prefs.getInt("USER_ID", -1);

        alimentos = db.alimentoDAO().getAllAlimentos();
    }

    public void update(Alimento alimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.alimentoDAO().update(alimento);
        });
    }

    public LiveData<List<Alimento>> getAll() {
        return alimentos;
    }

    public void delete(Alimento alimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            db.alimentoDAO().delete(alimento);
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

    public LiveData<List<Alimento>> getFavorites(int userId) {
        MutableLiveData<List<Alimento>> favoritos = new MutableLiveData<>();

        AppDatabase.databaseWriteExecutor.execute(() -> {

            List<Favorito> listaFavoritos = db.favoritoDAO().getByUserId(userId);

            List<Integer> ids = listaFavoritos.stream()
                    .map(Favorito::getIdAlimento)
                    .toList();

            if (ids.isEmpty()) {
                favoritos.postValue(List.of());
                return;
            }

            List<Alimento> alimentosFav = db.alimentoDAO().getByIds(ids);

            favoritos.postValue(alimentosFav);
        });

        return favoritos;
    }

    public LiveData<Favorito> isFavorite(int alimentoId) {
        return db.favoritoDAO().isFavorite(usuarioId, alimentoId);
    }
}