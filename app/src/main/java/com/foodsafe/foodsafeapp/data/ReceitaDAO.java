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
    void insert(Receita receita);

    @Insert
    void insertAll(List<Receita> receitas);

    @Update
    void update(Receita receita);

    @Query("SELECT * FROM receitas")
    List<Receita> getAll();

    @Query("SELECT * FROM receitas WHERE id = :id")
    Receita getById(int id);

    @Query("DELETE FROM receitas WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM receitas WHERE restricoes LIKE '%' || :restriction || '%'")
    List<Receita> getByRestriction(String restriction);

    @Query("SELECT * FROM receitas WHERE id IN (:ids)")
    List<Receita> getByIds(List<Integer> ids);
}
