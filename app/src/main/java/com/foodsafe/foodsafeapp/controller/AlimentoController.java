package com.foodsafe.foodsafeapp.controller;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.AlimentoDAO;
import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.List;

public class AlimentoController {

    private AlimentoDAO dao;

    public AlimentoController(Context ctx) {
        dao = AppDatabase.getInstance(ctx).alimentoDAO();
    }

    public void insert(Alimento a) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(a);
        });
    }

    public LiveData<List<Alimento>> getAll() {
        return dao.getAllAlimentos();
    }

    public Alimento getById(int id) {
        return dao.getAlimentoById(id);
    }
}
