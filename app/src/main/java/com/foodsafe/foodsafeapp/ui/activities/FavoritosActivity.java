package com.foodsafe.foodsafeapp.ui.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.controller.FavoritoController;
import com.foodsafe.foodsafeapp.controller.AlimentoController;
import com.foodsafe.foodsafeapp.model.Favorito;
import com.foodsafe.foodsafeapp.model.Alimento;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private FavoritoController controller;
    private AlimentoController alimentoController;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        controller = new FavoritoController(this);
        alimentoController = new AlimentoController(this);
        listView = findViewById(R.id.list_favoritos_view);

        int userId = getIntent().getIntExtra("userId", -1);

        if (userId != -1) {
            List<Favorito> favs = controller.getByUserId(userId);
            List<String> nomes = new ArrayList<>();

            for (Favorito f : favs) {
                Alimento a = alimentoController.getById(f.getIdAlimento());
                if (a != null) {
                    nomes.add(a.getNome() + " (Alergênicos: " + a.getContem_alergenos() + ")");
                }
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomes);

            listView.setAdapter(adapter);
        }
    }
}
