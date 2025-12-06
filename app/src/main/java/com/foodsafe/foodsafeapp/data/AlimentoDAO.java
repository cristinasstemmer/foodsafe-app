package com.foodsafe.foodsafeapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.List;

@Dao
public interface AlimentoDAO {

    @Insert
    void inserir(Alimento alimento);

    @Query("SELECT * FROM Alimento ORDER BY nome ASC")
    LiveData<List<Alimento>> listarTodosAlimentos();

    @Query("SELECT * FROM Alimento WHERE id = :id")
    Alimento buscarAlimentoPorId(int id);

    @Query("SELECT * FROM Alimento WHERE contem_alergenos LIKE '%' || :restricao || '%'")
    List<Alimento> buscarAlimentosPorRestricao(String restricao);

    @Delete
    void deletar(Alimento alimento);

    @Query("SELECT * FROM alimento WHERE id IN (:ids)")
    List<Alimento> buscarPorIds(List<Integer> ids);
}
