package com.foodsafe.foodsafeapp.ui.dialogs;

import android.content.Context;
import android.net.Uri;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.util.Restrictions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAlimentoDialog {

    private final Context context;
    private final BottomSheetDialog dialog;
    private final List<String> selectedRestrictions = new ArrayList<>();
    private AutoCompleteTextView etAlergenos;
    private CircleImageView ivFoodImage;
    private Uri imageUri;

    public interface AddAlimentoCallback {
        void onAlimentoCriado(Alimento alimento);
    }

    public AddAlimentoDialog(Context context, ActivityResultLauncher<String[]> imagePickerLauncher, AddAlimentoCallback callback) {
        this.context = context;
        this.dialog = new BottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.modal_add_food, null);
        dialog.setContentView(view);

        ivFoodImage = view.findViewById(R.id.iv_food_image);
        ImageView ivAddPhoto = view.findViewById(R.id.iv_add_photo);
        EditText etNome = view.findViewById(R.id.et_food_name);
        etAlergenos = view.findViewById(R.id.et_food_alergenos);
        TextInputLayout tilAlergenos = view.findViewById(R.id.til_food_alergenos);
        EditText etDescricao = view.findViewById(R.id.et_food_desc);
        Button btnAdd = view.findViewById(R.id.btn_continue);

        ivAddPhoto.setOnClickListener(v -> imagePickerLauncher.launch(new String[]{"image/*"}));

        etAlergenos.setOnClickListener(v -> showRestrictionsDialog());
        tilAlergenos.setEndIconOnClickListener(v -> showRestrictionsDialog());

        btnAdd.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            String desc = etDescricao.getText().toString().trim();
            String imagem = (imageUri != null) ? imageUri.toString() : "";

            if (nome.isEmpty()) {
                Toast.makeText(context, "Please enter the food name!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> finalRestrictions = selectedRestrictions.stream()
                    .filter(s -> !s.isEmpty() && !s.equalsIgnoreCase("None") && !s.equalsIgnoreCase("Other"))
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.toList());

            Alimento alimento = new Alimento(
                    nome,
                    finalRestrictions,
                    desc,
                    imagem
            );

            callback.onAlimentoCriado(alimento);
            dialog.dismiss();
        });
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        ivFoodImage.setImageURI(imageUri);
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    private void showRestrictionsDialog() {
        List<String> displayableRestrictions = new ArrayList<>(Arrays.asList(Restrictions.ALL_RESTRICTIONS));
        for (String restriction : selectedRestrictions) {
            if (!displayableRestrictions.contains(restriction)) {
                displayableRestrictions.add(restriction);
            }
        }
        displayableRestrictions.remove("Other");
        displayableRestrictions.add("Other");

        final String[] items = displayableRestrictions.toArray(new String[0]);
        boolean[] checkedItems = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            checkedItems[i] = selectedRestrictions.contains(items[i]);
        }

        new AlertDialog.Builder(context)
                .setTitle("Select Allergens/Restrictions")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    String selected = items[which];
                    if (selected.equals("Other")) {
                        ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                        dialog.dismiss();
                        showOtherRestrictionDialog();
                    } else {
                        if (isChecked) {
                            selectedRestrictions.add(selected);
                        } else {
                            selectedRestrictions.remove(selected);
                        }
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    updateSelectedRestrictionsText();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showOtherRestrictionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Custom Restriction");

        final EditText input = new EditText(context);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String customRestriction = input.getText().toString().trim();
            if (!customRestriction.isEmpty()) {
                selectedRestrictions.add(customRestriction);
            }
            showRestrictionsDialog();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            showRestrictionsDialog();
        });

        builder.show();
    }

    private void updateSelectedRestrictionsText() {
        List<String> cleanedList = selectedRestrictions.stream()
                .filter(s -> !s.equalsIgnoreCase("Other"))
                .collect(Collectors.toList());

        if (cleanedList.isEmpty()) {
            etAlergenos.setText("");
        } else {
            etAlergenos.setText(TextUtils.join(", ", cleanedList));
        }
    }

    public void show() {
        dialog.show();
    }
}
