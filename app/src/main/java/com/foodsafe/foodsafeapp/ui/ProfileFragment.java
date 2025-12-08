package com.foodsafe.foodsafeapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.UsuarioDAO;
import com.foodsafe.foodsafeapp.model.Usuario;
import com.foodsafe.foodsafeapp.util.Restrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BottomSheetDialogFragment {

    private ImageView ivBack;
    private TextView tvName, tvEmail, tvCurrentRestrictions;
    private Button btnEditRestrictions;
    private LinearLayout llChangePassword;
    private TextView tvSignOut;
    private CircleImageView ivProfilePicture;
    private ImageView ivAddPhoto;
    private UsuarioDAO usuarioDAO;
    private Usuario currentUser;
    private List<String> tempSelectedRestrictions = new ArrayList<>();

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        requireActivity().getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                        
                        ivProfilePicture.setImageURI(imageUri);
                        saveProfilePicture(imageUri);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usuarioDAO = AppDatabase.getInstance(getContext()).usuarioDAO();

        initializeViews(view);
        setupClickListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfileData();
    }

    private void initializeViews(View view) {
        ivBack = view.findViewById(R.id.iv_back);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        btnEditRestrictions = view.findViewById(R.id.btn_edit_restrictions);
        tvCurrentRestrictions = view.findViewById(R.id.tv_current_restrictions);
        llChangePassword = view.findViewById(R.id.ll_change_password);
        tvSignOut = view.findViewById(R.id.tv_sign_out);
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);
        ivAddPhoto = view.findViewById(R.id.iv_add_photo);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> dismiss());
        btnEditRestrictions.setOnClickListener(v -> {
            if (currentUser != null) {
                // Initialize a temporary list to hold changes for this editing session
                tempSelectedRestrictions = new ArrayList<>();
                if (currentUser.getRestricoes() != null) {
                    tempSelectedRestrictions.addAll(currentUser.getRestricoes());
                }
                showRestrictionsDialog(); // Start the dialog process
            }
        });
        llChangePassword.setOnClickListener(v -> {
             Intent intent = new Intent(Intent.ACTION_VIEW);
             intent.setData(Uri.parse("https://forms.gle/mU3M2gum37EwPkHz7"));
             startActivity(intent);
        });
        tvSignOut.setOnClickListener(v -> handleSignOut());
        ivAddPhoto.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        galleryLauncher.launch(intent);
    }

    private void saveProfilePicture(Uri imageUri) {
        if (currentUser != null) {
            currentUser.setProfilePictureUri(imageUri.toString());
            new Thread(() -> {
                usuarioDAO.update(currentUser);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Profile picture updated!", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }
    }

    private void loadProfileData() {
        new Thread(() -> {
            SharedPreferences prefs = requireActivity().getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);
            int userId = prefs.getInt("USER_ID", -1);
            currentUser = usuarioDAO.getById(userId);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (currentUser != null) {
                        tvName.setText(currentUser.getNome());
                        tvEmail.setText(currentUser.getEmail());
                        updateRestrictionsDisplay(currentUser.getRestricoes());
                        if (currentUser.getProfilePictureUri() != null) {
                            try {
                                ivProfilePicture.setImageURI(Uri.parse(currentUser.getProfilePictureUri()));
                            } catch (Exception e) {
                                ivProfilePicture.setImageResource(R.drawable.ic_user);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Could not load profile data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void showRestrictionsDialog() {
        // 1. Build the list of items to display
        List<String> displayableRestrictions = new ArrayList<>(Arrays.asList(Restrictions.ALL_RESTRICTIONS));
        for (String restriction : tempSelectedRestrictions) {
            if (!displayableRestrictions.contains(restriction)) {
                displayableRestrictions.add(restriction);
            }
        }
        displayableRestrictions.remove("Other");
        displayableRestrictions.add("Other");

        final String[] items = displayableRestrictions.toArray(new String[0]);
        boolean[] checkedItems = new boolean[items.length];

        // 2. Set the checked state based on tempSelectedRestrictions
        for (int i = 0; i < items.length; i++) {
            checkedItems[i] = tempSelectedRestrictions.contains(items[i]);
        }

        // 3. Create and show the dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Select Dietary Restrictions")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    String selected = items[which];

                    if (selected.equals("Other")) {
                        ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                        dialog.dismiss();
                        showOtherRestrictionDialog();
                    } else {
                        if (isChecked) {
                            tempSelectedRestrictions.add(selected);
                        } else {
                            tempSelectedRestrictions.remove(selected);
                        }
                    }
                })
                .setPositiveButton("Save", (dialog, which) -> {
                    List<String> cleanedRestrictions = tempSelectedRestrictions.stream()
                            .filter(s -> !s.isEmpty() && !s.equalsIgnoreCase("None") && !s.equalsIgnoreCase("Other"))
                            .map(String::trim)
                            .distinct()
                            .collect(Collectors.toList());

                    currentUser.setRestricoes(cleanedRestrictions);

                    new Thread(() -> {
                        usuarioDAO.update(currentUser);
                        if(getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                updateRestrictionsDisplay(currentUser.getRestricoes());
                                Toast.makeText(getContext(), "Restrictions updated!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showOtherRestrictionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Custom Restriction");

        final EditText input = new EditText(requireContext());
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String customRestriction = input.getText().toString().trim();
            if (!customRestriction.isEmpty()) {
                tempSelectedRestrictions.add(customRestriction);
            }
            showRestrictionsDialog();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            showRestrictionsDialog();
        });

        builder.show();
    }

    private void updateRestrictionsDisplay(List<String> restrictions) {
        if (restrictions == null || restrictions.isEmpty() || (restrictions.size() == 1 && restrictions.get(0).equalsIgnoreCase("None"))) {
            tvCurrentRestrictions.setText("None selected");
        } else {
             List<String> displayRestrictions = restrictions.stream()
                    .filter(s -> !s.equalsIgnoreCase("None"))
                    .collect(Collectors.toList());
            tvCurrentRestrictions.setText(TextUtils.join(", ", displayRestrictions));
        }
    }

    private void handleSignOut() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("FoodSafePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("USER_ID");
        editor.apply();

        Toast.makeText(getContext(), "You have been signed out.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        dismiss();
    }
}