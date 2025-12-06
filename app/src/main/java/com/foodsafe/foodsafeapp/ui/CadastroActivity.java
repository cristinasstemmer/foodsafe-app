package com.foodsafe.foodsafeapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.UsuarioDAO;
import com.foodsafe.foodsafeapp.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText etNome, etEmail, etSenha;
    private TextView etRestricoes;
    private Button btnCadastrar;
    private UsuarioDAO usuarioDAO;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        usuarioDAO = AppDatabase.getInstance(this).usuarioDAO();

        etNome = findViewById(R.id.et_nome); 
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        etRestricoes = findViewById(R.id.et_restricoes);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegistrationData();
            }
        });

        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
        });
    }

    private void sendRegistrationData() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String restricoes = etRestricoes.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Name, Email, and Password are required.", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(() -> {
            if (usuarioDAO.getUserByEmail(email) != null) {
                runOnUiThread(() -> Toast.makeText(CadastroActivity.this, "Email already registered.", Toast.LENGTH_SHORT).show());
                return;
            }

            Usuario novoUsuario = new Usuario(nome, email, senha, restricoes);
            usuarioDAO.insertUsuario(novoUsuario);

            runOnUiThread(() -> {
                Toast.makeText(CadastroActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}