package com.foodsafe.foodsafeapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Alimento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private String contem_alergenos;

    public Alimento(String nome, String contem_alergenos) {
        this.nome = nome;
        this.contem_alergenos = contem_alergenos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getContem_alergenos() { return contem_alergenos; }
    public void setContem_alergenos(String contem_alergenos) { this.contem_alergenos = contem_alergenos; }
}
