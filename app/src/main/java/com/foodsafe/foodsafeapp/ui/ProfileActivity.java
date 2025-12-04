package com.foodsafe.foodsafeapp.ui;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.foodsafe.foodsafeapp.R; // Ajuste o pacote conforme necessário

public class ProfileActivity extends AppCompatActivity {

    // Declaração das Views interativas
    private ImageView ivBack;
    private TextView tvAddAllergy;
    private LinearLayout llChangePassword;
    private TextView tvSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Certifique-se de que o nome do layout corresponde ao seu XML (activity_profile.xml)
        setContentView(R.layout.activity_profile);

        // 1. Inicializa as Views
        initializeViews();

        // 2. Configura os Listeners de Clique
        setupClickListeners();

        // 3. Carrega os dados do perfil (Nome, Email, etc.)
        loadProfileData();
    }

    private void initializeViews() {
        ivBack = findViewById(R.id.iv_back);
        tvAddAllergy = findViewById(R.id.tv_add_allergy);
        llChangePassword = findViewById(R.id.ll_change_password);
        tvSignOut = findViewById(R.id.tv_sign_out);

        // Outras views de dados (opcional, dependendo de como você carrega)
        // TextView tvName = findViewById(R.id.tv_name);
        // TextView tvEmail = findViewById(R.id.tv_email);
        // ...
    }

    private void setupClickListeners() {
        // Ação de Voltar
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega de volta para a Activity anterior
                finish();
            }
        });

        // Ação para Adicionar Alergia
        tvAddAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Abrir modal/tela para adicionar alergia.", Toast.LENGTH_SHORT).show();
                // Exemplo: startActivity(new Intent(ProfileActivity.this, AddAllergyActivity.class));
            }
        });

        // Ação para Mudar Senha
        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Abrir tela de mudança de senha.", Toast.LENGTH_SHORT).show();
                // Exemplo: startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
            }
        });

        // Ação de Logout
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignOut();
            }
        });
    }

    private void loadProfileData() {
        // Implemente a lógica para carregar os dados do perfil (Nome, Email, Contagem de Receitas, etc.)
        // a partir da API, banco de dados local ou SharedPreferences.

        // Exemplo:
        // TextView tvName = findViewById(R.id.tv_name);
        // tvName.setText("Jasmine Silva");

        // TextView tvSavedRecipes = findViewById(R.id.tv_saved_recipes_count);
        // tvSavedRecipes.setText("24");

        // Esta função garante que as informações visuais (além do XML inicial) sejam preenchidas.
    }

    private void handleSignOut() {
        // 1. Lógica de deslogar (limpar token de sessão, etc.)
        // AuthManager.getInstance().signOut();

        // 2. Notificação e redirecionamento para a tela de Login
        Toast.makeText(this, "Usuário deslogado.", Toast.LENGTH_SHORT).show();

        // Exemplo de redirecionamento para LoginActivity (remova os comentários se a Activity existir)
        /*
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        */
    }
}