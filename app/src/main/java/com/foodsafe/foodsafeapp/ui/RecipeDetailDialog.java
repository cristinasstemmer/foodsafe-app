package com.foodsafe.foodsafeapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Receita;

public class RecipeDetailDialog {

    public static void show(Context context, Receita receita) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_recipe_details, null);
        builder.setView(view);

        TextView tvName = view.findViewById(R.id.tv_recipe_detail_name);
        TextView tvDescription = view.findViewById(R.id.tv_recipe_detail_description);
        TextView tvIngredients = view.findViewById(R.id.tv_recipe_detail_ingredients);
        TextView tvInstructions = view.findViewById(R.id.tv_recipe_detail_instructions);

        tvName.setText(receita.getNome());
        tvDescription.setText(receita.getDescricao());
        tvIngredients.setText("Ingredients: \n" + receita.getIngredientes());
        tvInstructions.setText("Instructions: \n" + receita.getModoPreparo());

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
    }
}
