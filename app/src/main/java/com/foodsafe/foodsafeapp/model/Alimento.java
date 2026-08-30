package com.foodsafe.foodsafeapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity
public class Alimento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private List<String> contem_alergenos;
    private String descricao;
    private String imagemUri;

    public Alimento(String nome, List<String> contem_alergenos, String descricao, String imagemUri) {
        this.nome = nome;
        this.contem_alergenos = contem_alergenos;
        this.descricao = descricao;
        this.imagemUri = imagemUri;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<String> getContem_alergenos() { return contem_alergenos; }
    public void setContem_alergenos(List<String> contem_alergenos) { this.contem_alergenos = contem_alergenos; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUri() { return imagemUri; }
    public void setImagemUri(String imagemUri) { this.imagemUri = imagemUri; }
}
