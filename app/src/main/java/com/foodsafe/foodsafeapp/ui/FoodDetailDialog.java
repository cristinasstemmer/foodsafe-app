package com.foodsafe.foodsafeapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;

public class FoodDetailDialog {

    public static void show(Context context, Alimento alimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_food_details, null);
        builder.setView(view);

        TextView tvName = view.findViewById(R.id.tv_food_detail_name);
        TextView tvDescription = view.findViewById(R.id.tv_food_detail_description);
        TextView tvAllergens = view.findViewById(R.id.tv_food_detail_allergens);

        tvName.setText(alimento.getNome());
        tvDescription.setText(alimento.getDescricao());
        tvAllergens.setText("Allergens: " + (alimento.getContem_alergenos().isEmpty() ? "None" : alimento.getContem_alergenos()));

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
    }
}
