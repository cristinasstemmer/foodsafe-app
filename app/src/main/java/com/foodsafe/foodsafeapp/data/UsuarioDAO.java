package com.foodsafe.foodsafeapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.foodsafe.foodsafeapp.model.Usuario;

@Dao
public interface UsuarioDAO {

    @Insert
    void insertUsuario(Usuario usuario);

    @Query("SELECT * FROM usuario WHERE email = :email AND senha = :senha LIMIT 1")
    Usuario login(String email, String senha);

    @Query("SELECT restricoes FROM usuario WHERE id = :idUsuario")
    String obterRestricoes(int idUsuario);

    @Update
    void updateUsuario(Usuario usuario);
}