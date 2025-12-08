package com.foodsafe.foodsafeapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.foodsafe.foodsafeapp.R;
import com.foodsafe.foodsafeapp.data.AppDatabase;
import com.foodsafe.foodsafeapp.data.UsuarioDAO;
import com.foodsafe.foodsafeapp.model.Usuario;
import com.foodsafe.foodsafeapp.util.Restrictions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText etNome, etEmail, etSenha;
    private AutoCompleteTextView etRestricoes;
    private CheckBox cbTerms;
    private TextView tvTermsLink;
    private Button btnCadastrar;
    private UsuarioDAO usuarioDAO;
    private TextView tvLoginLink;
    private TextInputLayout tilRestricoes;
    private final List<String> selectedRestrictions = new ArrayList<>();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).{6,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        usuarioDAO = AppDatabase.getInstance(this).usuarioDAO();

        etNome = findViewById(R.id.et_nome);
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        etRestricoes = findViewById(R.id.et_restricoes);
        tilRestricoes = findViewById(R.id.til_restricoes);
        cbTerms = findViewById(R.id.cb_terms);
        tvTermsLink = findViewById(R.id.tv_terms_link);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
        tvLoginLink = findViewById(R.id.tv_login_link);

        etRestricoes.setOnClickListener(v -> showRestrictionsDialog());
        tilRestricoes.setEndIconOnClickListener(v -> showRestrictionsDialog());
        tvTermsLink.setOnClickListener(v -> showTermsDialog());

        btnCadastrar.setOnClickListener(v -> sendRegistrationData());
        tvLoginLink.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_terms_and_conditions, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        final Button btnAgree = dialogView.findViewById(R.id.btn_agree);
        final ScrollView svTerms = dialogView.findViewById(R.id.sv_terms_scroll);

        svTerms.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (svTerms.getChildAt(0).getBottom() <= (svTerms.getHeight() + svTerms.getScrollY())) {
                btnAgree.setEnabled(true);
            }
        });

        btnAgree.setOnClickListener(v -> {
            cbTerms.setChecked(true);
            cbTerms.setEnabled(true);
            dialog.dismiss();
        });

        dialog.show();
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

        new AlertDialog.Builder(this)
                .setTitle("Select Dietary Restrictions")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    String selected = items[which];
                    if (selected.equals("Other")) {
                        ((AlertDialog) dialog).getListView().setItemChecked(which, false); // Uncheck "Other"
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
                     if (selectedRestrictions.contains("None") && selectedRestrictions.size() > 1) {
                        selectedRestrictions.remove("None");
                    }
                    updateSelectedRestrictionsText();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showOtherRestrictionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Custom Restriction");

        final EditText input = new EditText(this);
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

        if (cleanedList.isEmpty() || cleanedList.contains("None")) {
            etRestricoes.setText("None");
        } else {
            etRestricoes.setText(TextUtils.join(", ", cleanedList));
        }
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private void sendRegistrationData() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        
        List<String> finalRestrictions = selectedRestrictions.stream()
                            .filter(s -> !s.isEmpty() && !s.equalsIgnoreCase("None") && !s.equalsIgnoreCase("Other"))
                            .map(String::trim)
                            .distinct()
                            .collect(Collectors.toList());

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Name, Email, and Password are required.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(senha)) {
            Toast.makeText(this, "Password must be at least 6 characters and contain one letter and one number.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "You must agree to the Terms and Conditions.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            if (usuarioDAO.getUserByEmail(email) != null) {
                runOnUiThread(() -> Toast.makeText(CadastroActivity.this, "Email already registered.", Toast.LENGTH_SHORT).show());
                return;
            }

            Usuario novoUsuario = new Usuario(nome, email, senha, finalRestrictions);
            usuarioDAO.insertUsuario(novoUsuario);

            runOnUiThread(() -> {
                Toast.makeText(CadastroActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}