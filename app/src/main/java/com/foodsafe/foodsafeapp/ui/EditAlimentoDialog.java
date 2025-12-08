package com.foodsafe.foodsafeapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
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

public class EditAlimentoDialog {

    public interface OnAlimentoEditedListener {
        void onAlimentoEdited(Alimento alimento);
    }

    private final Alimento alimentoParaEditar;
    private final OnAlimentoEditedListener listener;
    private final Context context;
    private final BottomSheetDialog dialog;

    private EditText etFoodName, etFoodDesc;
    private AutoCompleteTextView etFoodAlergenos;
    private Button btnSave;
    private TextView tvTitle;
    private CircleImageView ivFoodImage;
    private ImageView ivAddPhoto;
    private Uri imageUri;
    private final List<String> selectedRestrictions = new ArrayList<>();

    public EditAlimentoDialog(@NonNull Context context, Alimento alimento, ActivityResultLauncher<String[]> imagePickerLauncher, OnAlimentoEditedListener listener) {
        this.context = context;
        this.alimentoParaEditar = alimento;
        this.listener = listener;

        this.dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.modal_add_food, null);
        dialog.setContentView(view);

        initializeViews(view);
        setupFields();
        setupClickListener(view, imagePickerLauncher);
    }

    private void initializeViews(View view) {
        tvTitle = view.findViewById(R.id.tv_modal_title);
        etFoodName = view.findViewById(R.id.et_food_name);
        etFoodAlergenos = view.findViewById(R.id.et_food_alergenos);
        etFoodDesc = view.findViewById(R.id.et_food_desc);
        btnSave = view.findViewById(R.id.btn_continue);
        ivFoodImage = view.findViewById(R.id.iv_food_image);
        ivAddPhoto = view.findViewById(R.id.iv_add_photo);
    }

    private void setupFields() {
        tvTitle.setText("Edit Food");
        btnSave.setText("Save Changes");

        if (alimentoParaEditar != null) {
            etFoodName.setText(alimentoParaEditar.getNome());
            etFoodDesc.setText(alimentoParaEditar.getDescricao());

            if (alimentoParaEditar.getImagemUri() != null && !alimentoParaEditar.getImagemUri().isEmpty()) {
                imageUri = Uri.parse(alimentoParaEditar.getImagemUri());
                setImageUri(imageUri);
            } else {
                ivFoodImage.setImageResource(R.drawable.ic_food_placeholder);
            }

            if (alimentoParaEditar.getContem_alergenos() != null) {
                selectedRestrictions.addAll(alimentoParaEditar.getContem_alergenos());
            }
            updateSelectedRestrictionsText();
        }
    }

    private void setupClickListener(View view, ActivityResultLauncher<String[]> imagePickerLauncher) {
        TextInputLayout tilFoodAlergenos = view.findViewById(R.id.til_food_alergenos);

        ivAddPhoto.setOnClickListener(v -> imagePickerLauncher.launch(new String[]{"image/*"}));

        etFoodAlergenos.setOnClickListener(v -> showRestrictionsDialog());
        tilFoodAlergenos.setEndIconOnClickListener(v -> showRestrictionsDialog());

        btnSave.setOnClickListener(v -> {
            String nome = etFoodName.getText().toString().trim();
            String desc = etFoodDesc.getText().toString().trim();
            String image = (imageUri != null) ? imageUri.toString() : "";

            if (nome.isEmpty()) {
                Toast.makeText(context, "Food name cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> finalRestrictions = selectedRestrictions.stream()
                    .filter(s -> !s.isEmpty() && !s.equalsIgnoreCase("None") && !s.equalsIgnoreCase("Other"))
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.toList());

            alimentoParaEditar.setNome(nome);
            alimentoParaEditar.setContem_alergenos(finalRestrictions);
            alimentoParaEditar.setDescricao(desc);
            alimentoParaEditar.setImagemUri(image);

            listener.onAlimentoEdited(alimentoParaEditar);
            dialog.dismiss();
        });
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        Glide.with(context)
                .load(imageUri)
                .placeholder(R.drawable.ic_food_placeholder)
                .error(R.drawable.ic_food_placeholder)
                .into(ivFoodImage);
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
                .setPositiveButton("OK", (dialog, which) -> updateSelectedRestrictionsText())
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
            etFoodAlergenos.setText("");
        } else {
            etFoodAlergenos.setText(TextUtils.join(", ", cleanedList));
        }
    }
    
    public void show() {
        dialog.show();
    }
}
