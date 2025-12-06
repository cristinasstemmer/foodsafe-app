package com.foodsafe.foodsafeapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.List;

@Dao
public interface AlimentoDAO {

    @Insert
    void insert(Alimento alimento);

    @Insert
    void insertAll(List<Alimento> alimentos);

    @Update
    void update(Alimento alimento);

    @Query("SELECT * FROM Alimento ORDER BY nome ASC")
    LiveData<List<Alimento>> getAllAlimentos();

    @Query("SELECT * FROM Alimento ORDER BY nome ASC")
    List<Alimento> getAllAlimentosSync();

    @Query("SELECT * FROM Alimento WHERE id = :id")
    Alimento getAlimentoById(int id);

    @Query("SELECT * FROM Alimento WHERE contem_alergenos LIKE '%' || :restriction || '%'")
    List<Alimento> getAlimentosByRestriction(String restriction);

    @Delete
    void delete(Alimento alimento);

    @Query("SELECT * FROM alimento WHERE id IN (:ids)")
    List<Alimento> getByIds(List<Integer> ids);
}
