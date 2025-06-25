package com.sistemaeducativo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sistemaeducativo.app.data.DataManager;
import com.sistemaeducativo.app.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etUsuario, etPassword;
    private MaterialButton btnLogin;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
        
        dataManager = DataManager.getInstance();
    }

    private void initializeViews() {
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String username = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = dataManager.authenticateUser(username, password);
        
        if (usuario != null) {
            // Login exitoso
            Intent intent;
            
            switch (usuario.getTipo()) {
                case ESTUDIANTE:
                    intent = new Intent(this, EstudianteActivity.class);
                    break;
                case PROFESOR:
                    intent = new Intent(this, ProfesorActivity.class);
                    break;
                case ADMIN:
                    intent = new Intent(this, AdminActivity.class);
                    break;
                default:
                    Toast.makeText(this, "Tipo de usuario no válido", Toast.LENGTH_SHORT).show();
                    return;
            }
            
            intent.putExtra("usuario_id", usuario.getId());
            intent.putExtra("usuario_nombre", usuario.getNombre());
            intent.putExtra("usuario_tipo", usuario.getTipo().name());
            
            startActivity(intent);
            finish(); // Cerrar la actividad de login
            
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
