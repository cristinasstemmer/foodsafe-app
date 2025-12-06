package com.foodsafe.foodsafeapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.foodsafe.foodsafeapp.model.Favorito;

import java.util.List;

@Dao
public interface FavoritoDAO {

    @Insert
    void adicionarFavorito(Favorito favorito);

    @Query("DELETE FROM Favorito WHERE idUsuario = :idUsuario AND idAlimento = :idAlimento")
    void removerFavorito(int idUsuario, int idAlimento);

    @Query("SELECT * FROM Favorito WHERE idUsuario = :idUsuario")
    List<Favorito> listarFavoritos(int idUsuario);

    @Query("SELECT * FROM Favorito WHERE idUsuario = :idUsuario AND idAlimento = :idAlimento LIMIT 1")
    LiveData<Favorito> isFavorito(int idUsuario, int idAlimento);

    @Query("SELECT * FROM favorito WHERE idUsuario = :idUsuario")
    List<Favorito> listarPorUsuario(int idUsuario);
}
