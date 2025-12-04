package com.foodsafe.foodsafeapp.controller;

import android.content.Context;

public class SistemaFoodSafe {

    public UsuarioController usuarios;
    public AlimentoController alimentos;

    public SistemaFoodSafe(Context ctx) {
        usuarios  = new UsuarioController(ctx);
        alimentos = new AlimentoController(ctx);
    }
}
