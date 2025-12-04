package com.foodsafe.foodsafeapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "receitas")
public class Receita {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private String descricao;
    private String ingredientes;
    private String modoPreparo;
    private String restricoes;

    public Receita(String nome, String descricao, String ingredientes, String modoPreparo, String restricoes) {
        this.nome = nome;
        this.descricao = descricao;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
        this.restricoes = restricoes;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public String getRestricoes() {
        return restricoes;
    }

    public void setRestricoes(String restricoes) {
        this.restricoes = restricoes;
    }
}
