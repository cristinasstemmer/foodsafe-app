package com.foodsafe.foodsafeapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.foodsafe.foodsafeapp.model.Usuario;

@Dao
public interface UsuarioDAO {

    @Insert
    void insertUsuario(Usuario usuario);

    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    Usuario getUserByEmail(String email);

    @Query("SELECT * FROM usuario WHERE id = :userId")
    LiveData<Usuario> getUserById(int userId);

    @Query("SELECT restricoes FROM usuario WHERE id = :idUsuario")
    String obterRestricoes(int idUsuario);

    @Query("SELECT * FROM usuario LIMIT 1")
    Usuario getLoggedInUser();

    @Query("SELECT * FROM usuario WHERE id = :id")
    Usuario getById(int id);

    @Update
    void update(Usuario usuario);
}
