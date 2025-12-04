package com.foodsafe.foodsafeapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.foodsafe.foodsafeapp.model.Receita;

import java.util.List;

@Dao
public interface ReceitaDAO {

    @Insert
    void insertReceita(Receita receita);

    @Update
    void updateReceita(Receita receita);

    @Query("SELECT * FROM receitas")
    List<Receita> getAllReceitas();

    @Query("SELECT * FROM receitas WHERE id = :id")
    Receita getReceitaById(int id);

    @Query("DELETE FROM receitas WHERE id = :id")
    void deleteReceita(int id);

    @Query("SELECT * FROM receitas WHERE restricoes LIKE '%' || :restricao || '%'")
    List<Receita> getReceitasPorRestricao(String restricao);
}
