package com.foodsafe.foodsafeapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AddAlimentoDialog {

    private final Context context;
    private final BottomSheetDialog dialog;

    public interface AddAlimentoCallback {
        void onAlimentoCriado(Alimento alimento);
    }

    public AddAlimentoDialog(Context context, AddAlimentoCallback callback) {
        this.context = context;
        this.dialog = new BottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.modal_add_food, null);
        dialog.setContentView(view);

        EditText etNome = view.findViewById(R.id.et_food_name);
        EditText etAlergenos = view.findViewById(R.id.et_food_alergenos);
        EditText etDescricao = view.findViewById(R.id.et_food_desc);
        EditText etImagem = view.findViewById(R.id.et_food_image);
        Button btnAdd = view.findViewById(R.id.btn_continue);

        btnAdd.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            String alerg = etAlergenos.getText().toString().trim();
            String desc = etDescricao.getText().toString().trim();
            String imagem = etImagem.getText().toString().trim();

            if (nome.isEmpty()) {
                Toast.makeText(context, "Please enter the food name!", Toast.LENGTH_SHORT).show();
                return;
            }

            Alimento alimento = new Alimento(
                    nome,
                    alerg,
                    desc,
                    imagem
            );

            callback.onAlimentoCriado(alimento);
            dialog.dismiss();
        });
    }

    public void show() {
        dialog.show();
    }
}
