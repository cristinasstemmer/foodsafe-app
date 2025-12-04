package com.foodsafe.foodsafeapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.List;

@Dao
public interface AlimentoDAO {

    @Insert
    void insertAlimento(Alimento alimento);

    @Query("SELECT * FROM Alimento")
    List<Alimento> listarTodosAlimentos();

    @Query("SELECT * FROM Alimento WHERE id = :id")
    Alimento buscarAlimentoPorId(int id);

    @Query("SELECT * FROM Alimento WHERE contem_alergenos LIKE '%' || :restricao || '%'")
    List<Alimento> buscarAlimentosPorRestricao(String restricao);
}
