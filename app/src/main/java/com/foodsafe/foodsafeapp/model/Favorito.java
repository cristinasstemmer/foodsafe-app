package com.foodsafe.foodsafeapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favorito {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idUsuario;
    private int idAlimento;

    public Favorito(int idUsuario, int idAlimento) {
        this.idUsuario = idUsuario;
        this.idAlimento = idAlimento;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setId(int id) {
        this.id = id;
    }
}
