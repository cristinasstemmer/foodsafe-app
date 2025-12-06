package com.foodsafe.foodsafeapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.foodsafe.foodsafeapp.model.FavoritoReceita;

import java.util.List;

@Dao
public interface FavoritoReceitaDAO {

    @Insert
    void insert(FavoritoReceita favoritoReceita);

    @Query("DELETE FROM favorito_receita WHERE idUsuario = :idUsuario AND idReceita = :idReceita")
    void delete(int idUsuario, int idReceita);

    @Query("SELECT * FROM favorito_receita WHERE idUsuario = :idUsuario")
    List<FavoritoReceita> getByUserId(int idUsuario);

    @Query("SELECT * FROM favorito_receita WHERE idUsuario = :idUsuario AND idReceita = :idReceita LIMIT 1")
    LiveData<FavoritoReceita> isFavorite(int idUsuario, int idReceita);

    @Query("SELECT idReceita FROM favorito_receita WHERE idUsuario = :idUsuario")
    List<Integer> getFavoriteRecipeIdsByUserId(int idUsuario);
}
