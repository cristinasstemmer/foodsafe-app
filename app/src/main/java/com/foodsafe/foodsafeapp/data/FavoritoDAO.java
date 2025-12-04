package com.foodsafe.foodsafeapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.foodsafe.foodsafeapp.model.Favorito;

import java.util.List;

@Dao
public interface FavoritoDAO {

    @Insert
    void adicionarFavorito(Favorito favorito);

    @Query("DELETE FROM Favorito WHERE id_usuario = :idUsuario AND id_alimento = :idAlimento")
    void removerFavorito(int idUsuario, int idAlimento);

    @Query("SELECT * FROM Favorito WHERE id_usuario = :idUsuario")
    List<Favorito> listarFavoritos(int idUsuario);
}
