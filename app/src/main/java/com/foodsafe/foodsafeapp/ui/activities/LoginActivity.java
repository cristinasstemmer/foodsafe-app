package com.foodsafe.foodsafeapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.foodsafe.foodsafeapp.MainActivity;
import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.UsuarioDAO;
import com.foodsafe.foodsafeapp.model.Usuario;
import com.foodsafe.foodsafeapp.util.PasswordManager;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etSenha;
    private Button btnLogin;
    private TextView tvSignUpLink, tvForgotPassword;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUpLink = findViewById(R.id.tv_sign_up_link);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        usuarioDAO = AppDatabase.getInstance(this).usuarioDAO();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Usuario usuario = usuarioDAO.getUserByEmail(email);
                runOnUiThread(() -> {
                    if (usuario != null && PasswordManager.checkPassword(senha, usuario.getSenha())) {
                        SharedPreferences prefs = getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("USER_ID", usuario.getId());
                        editor.apply();

                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        tvSignUpLink.setOnClickListener(v ->
                startActivity(new Intent(this, CadastroActivity.class))
        );

        tvForgotPassword.setOnClickListener(v -> openUrl());
    }

    private void openUrl() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://forms.gle/mU3M2gum37EwPkHz7"));
        startActivity(intent);
    }
}
