package com.foodsafe.foodsafeapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;

public class EditAlimentoDialog extends Dialog {

    public interface OnAlimentoEditedListener {
        void onAlimentoEdited(Alimento alimento);
    }

    private final Alimento alimentoParaEditar;
    private final OnAlimentoEditedListener listener;

    private EditText etFoodName, etFoodAlergenos, etFoodDesc, etFoodImage;
    private Button btnSave;
    private TextView tvTitle;

    public EditAlimentoDialog(@NonNull Context context, Alimento alimento, OnAlimentoEditedListener listener) {
        super(context);
        this.alimentoParaEditar = alimento;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.modal_add_food);

        initializeViews();
        setupFields();
        setupClickListener();
    }

    private void initializeViews() {
        tvTitle = findViewById(R.id.tv_modal_title);
        etFoodName = findViewById(R.id.et_food_name);
        etFoodAlergenos = findViewById(R.id.et_food_alergenos);
        etFoodDesc = findViewById(R.id.et_food_desc);
        etFoodImage = findViewById(R.id.et_food_image);
        btnSave = findViewById(R.id.btn_continue);
    }

    private void setupFields() {
        tvTitle.setText("Edit Food");
        btnSave.setText("Save Changes");

        if (alimentoParaEditar != null) {
            etFoodName.setText(alimentoParaEditar.getNome());
            etFoodAlergenos.setText(alimentoParaEditar.getContem_alergenos());
            etFoodDesc.setText(alimentoParaEditar.getDescricao());
            etFoodImage.setText(alimentoParaEditar.getImagemUri());
        }
    }

    private void setupClickListener() {
        btnSave.setOnClickListener(v -> {
            String nome = etFoodName.getText().toString().trim();
            String alergenos = etFoodAlergenos.getText().toString().trim();
            String desc = etFoodDesc.getText().toString().trim();
            String image = etFoodImage.getText().toString().trim();

            if (nome.isEmpty()) {
                Toast.makeText(getContext(), "Food name cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            alimentoParaEditar.setNome(nome);
            alimentoParaEditar.setContem_alergenos(alergenos);
            alimentoParaEditar.setDescricao(desc);
            alimentoParaEditar.setImagemUri(image);

            listener.onAlimentoEdited(alimentoParaEditar);
            dismiss();
        });
    }
}