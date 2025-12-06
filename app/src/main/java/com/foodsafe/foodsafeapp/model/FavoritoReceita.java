package com.foodsafe.foodsafeapp.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorito_receita",
        indices = {@Index(value = {"idUsuario", "idReceita"}, unique = true)},
        foreignKeys = {
                @ForeignKey(entity = Usuario.class,
                        parentColumns = "id",
                        childColumns = "idUsuario",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Receita.class,
                        parentColumns = "id",
                        childColumns = "idReceita",
                        onDelete = ForeignKey.CASCADE)
        })
public class FavoritoReceita {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idUsuario;
    private int idReceita;

    public FavoritoReceita(int idUsuario, int idReceita) {
        this.idUsuario = idUsuario;
        this.idReceita = idReceita;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }
}
