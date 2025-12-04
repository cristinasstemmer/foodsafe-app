package com.foodsafe.foodsafeapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private String email;
    private String senha;
    private String restricoes;

    public Usuario(String nome, String email, String senha, String restricoes) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.restricoes = restricoes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getRestricoes() { return restricoes; }
    public void setRestricoes(String restricoes) { this.restricoes = restricoes; }
}
