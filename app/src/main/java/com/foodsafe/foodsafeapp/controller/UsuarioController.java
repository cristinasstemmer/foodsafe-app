package com.foodsafe.foodsafeapp.controller;


import android.content.Context;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.UsuarioDAO;
import com.foodsafe.foodsafeapp.model.Usuario;
import com.foodsafe.foodsafeapp.util.PasswordManager;

public class UsuarioController {
    private final UsuarioDAO usuarioDAO;

    public interface CadastroCallback {
        void onSuccess();
        void onFailure();
    }

    public interface LoginCallback {
        void onSuccess(Usuario usuario);
        void onFailure();
    }

    public UsuarioController(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.usuarioDAO = db.usuarioDAO();
    }

    public void salvarUsuario(Usuario usuario, CadastroCallback callback) {
        if (usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty()) {
            callback.onFailure();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                String hashedPassword = PasswordManager.hashPassword(usuario.getSenha());
                usuario.setSenha(hashedPassword);
                usuarioDAO.insertUsuario(usuario);
                callback.onSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void realizarLogin(String email, String senha, LoginCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Usuario usuario = usuarioDAO.getUserByEmail(email);

            if (usuario != null && PasswordManager.checkPassword(senha, usuario.getSenha())) {
                callback.onSuccess(usuario);
            } else {
                callback.onFailure();
            }
        });
    }

    public void editarPerfil(Usuario usuario) {
        usuarioDAO.update(usuario);
    }

    public Usuario getUsuarioLogado(Usuario usuario) {
        return usuarioDAO.getLoggedInUser();
    }

    public String obterRestricoesDoUsuario(int idUsuario) {
        return usuarioDAO.obterRestricoes(idUsuario);
    }
}
