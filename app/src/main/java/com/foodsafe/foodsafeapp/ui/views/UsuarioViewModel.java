package com.foodsafe.foodsafeapp.ui.views;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.UsuarioDAO;
import com.foodsafe.foodsafeapp.model.Usuario;

public class UsuarioViewModel extends AndroidViewModel {

    private final UsuarioDAO usuarioDAO;
    private final LiveData<Usuario> loggedUser;
    private final int usuarioId;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);
        usuarioDAO = db.usuarioDAO();

        SharedPreferences prefs =
                application.getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);
        usuarioId = prefs.getInt("USER_ID", -1);

        if (usuarioId != -1) {
            loggedUser = usuarioDAO.getUserById(usuarioId);
        } else {
            loggedUser = null;
        }
    }

    public LiveData<Usuario> getLoggedUser() {
        return loggedUser;
    }

    public void update(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDAO.update(usuario);
        });
    }
}