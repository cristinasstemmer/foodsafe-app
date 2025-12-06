package com.foodsafe.foodsafeapp.controller;

import android.content.Context;

import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.FavoritoDAO;
import com.foodsafe.foodsafeapp.model.Favorito;

import java.util.List;

public class FavoritoController {

    private final FavoritoDAO favoritoDAO;

    public FavoritoController(Context ctx) {
        AppDatabase db = AppDatabase.getInstance(ctx);
        favoritoDAO = db.favoritoDAO();
    }

    public void insert(int idUsuario, int idAlimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            favoritoDAO.insert(new Favorito(idUsuario, idAlimento));
        });
    }

    public void delete(int idUsuario, int idAlimento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            favoritoDAO.delete(idUsuario, idAlimento);
        });
    }

    public List<Favorito> getByUserId(int idUsuario) {
        return favoritoDAO.getByUserId(idUsuario);
    }
}
