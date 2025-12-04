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

    public void adicionar(int idUsuario, int idAlimento) {
        favoritoDAO.adicionarFavorito(new Favorito(idUsuario, idAlimento));
    }

    public void remover(int idUsuario, int idAlimento) {
        favoritoDAO.removerFavorito(idUsuario, idAlimento);
    }

    public List<Favorito> listarFavoritos(int idUsuario) {
        return favoritoDAO.listarFavoritos(idUsuario);
    }
}
