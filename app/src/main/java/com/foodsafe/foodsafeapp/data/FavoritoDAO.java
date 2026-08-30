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
    void insert(Favorito favorito);

    @Query("DELETE FROM Favorito WHERE idUsuario = :idUsuario AND idAlimento = :idAlimento")
    void delete(int idUsuario, int idAlimento);

    @Query("SELECT * FROM Favorito WHERE idUsuario = :idUsuario")
    List<Favorito> getByUserId(int idUsuario);

    @Query("SELECT idAlimento FROM Favorito WHERE idUsuario = :idUsuario")
    LiveData<List<Integer>> getFavoriteFoodIds(int idUsuario);

    @Query("SELECT * FROM Favorito WHERE idUsuario = :idUsuario AND idAlimento = :idAlimento LIMIT 1")
    LiveData<Favorito> isFavorite(int idUsuario, int idAlimento);
}
