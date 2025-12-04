package com.foodsafe.foodsafeapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favorito {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int id_usuario;
    private int id_alimento;

    public Favorito(int id_usuario, int id_alimento) {
        this.id_usuario = id_usuario;
        this.id_alimento = id_alimento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public int getId_alimento() { return id_alimento; }
    public void setId_alimento(int id_alimento) { this.id_alimento = id_alimento; }
}
