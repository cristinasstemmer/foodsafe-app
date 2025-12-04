package com.foodsafe.foodsafeapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.foodsafe.foodsafeapp.R;

public class SplashActivity extends AppCompatActivity {

    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 1. Remova o Handler().postDelayed que fazia a transição automática.
        // O código removido era:
        // new Handler().postDelayed(() -> {
        //     startActivity(new Intent(this, LoginActivity.class));
        //     finish();
        // }, 5000);

        // 2. Encontre o botão pelo ID (de acordo com o seu layout XML: android:id="@+id/btn_get_started")
        btnGetStarted = findViewById(R.id.btn_get_started);

        // 3. Configure um listener para detectar o clique
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 4. Crie a Intent para a próxima tela (LoginActivity)
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                // 5. Inicie a nova Activity
                startActivity(intent);

                // 6. Finalize a SplashActivity para que o usuário não possa voltar
                finish();
            }
        });
    }
}