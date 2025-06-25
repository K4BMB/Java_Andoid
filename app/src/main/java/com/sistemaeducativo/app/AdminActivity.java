package com.sistemaeducativo.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sistemaeducativo.app.data.DataManager;
import com.sistemaeducativo.app.model.Curso;
import com.sistemaeducativo.app.model.Usuario;

import java.util.ArrayList;
import java.util.List;

// Interfaz para manejar acciones de usuarios
interface UsuarioActionListener {
    void onEditarUsuario(Usuario usuario);

    void onEliminarUsuario(Usuario usuario);

    void onResetearPassword(Usuario usuario);
}

public class AdminActivity extends AppCompatActivity implements UsuarioActionListener {

    private TextView tvWelcome, tvTotalUsuarios, tvTotalCursos, tvTotalMatriculas;
    private RecyclerView rvUsuarios, rvCursos;
    private MaterialButton btnCerrarSesion, btnAgregarUsuario;

    private UsuarioAdapter usuarioAdapter;
    private CursoAdminAdapter cursoAdminAdapter;
    private DataManager dataManager;

    private int usuarioId;
    private String usuarioNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_admin);

            getUserData();
            initializeViews();

            dataManager = DataManager.getInstance();
            setupRecyclerViews();
            setupListeners();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, volver al login
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getUserData() {
        usuarioId = getIntent().getIntExtra("usuario_id", -1);
        usuarioNombre = getIntent().getStringExtra("usuario_nombre");
    }

    private void initializeViews() {
        try {
            tvWelcome = findViewById(R.id.tvWelcome);
            tvTotalUsuarios = findViewById(R.id.tvTotalUsuarios);
            tvTotalCursos = findViewById(R.id.tvTotalCursos);
            tvTotalMatriculas = findViewById(R.id.tvTotalMatriculas);
            rvUsuarios = findViewById(R.id.rvUsuarios);
            rvCursos = findViewById(R.id.rvCursos);
            btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
            btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);

            if (tvWelcome != null) {
                String welcomeText = "Bienvenido, " + (usuarioNombre != null ? usuarioNombre : "Administrador");
                tvWelcome.setText(welcomeText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRecyclerViews() {
        try {
            // RecyclerView para usuarios
            rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
            List<Usuario> usuarios = dataManager.getAllUsuarios();
            if (usuarios == null)
                usuarios = new ArrayList<>();
            usuarioAdapter = new UsuarioAdapter(usuarios, this);
            rvUsuarios.setAdapter(usuarioAdapter);

            // RecyclerView para cursos
            rvCursos.setLayoutManager(new LinearLayoutManager(this));
            List<Curso> cursos = dataManager.getAllCursos();
            if (cursos == null)
                cursos = new ArrayList<>();
            cursoAdminAdapter = new CursoAdminAdapter(cursos);
            rvCursos.setAdapter(cursoAdminAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        btnCerrarSesion.setOnClickListener(v -> logout());
        btnAgregarUsuario.setOnClickListener(v -> mostrarDialogoAgregarUsuario());
    }

    private void loadData() {
        try {
            // Actualizar estadísticas
            tvTotalUsuarios.setText(String.valueOf(dataManager.getTotalUsuarios()));
            tvTotalCursos.setText(String.valueOf(dataManager.getTotalCursos()));
            tvTotalMatriculas.setText(String.valueOf(dataManager.getTotalMatriculas()));

            // Actualizar listas
            List<Usuario> usuarios = dataManager.getAllUsuarios();
            if (usuarios != null && usuarioAdapter != null) {
                usuarioAdapter.updateUsuarios(usuarios);
            }

            List<Curso> cursos = dataManager.getAllCursos();
            if (cursos != null && cursoAdminAdapter != null) {
                cursoAdminAdapter.updateCursos(cursos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    // Implementación de UsuarioActionListener
    @Override
    public void onEditarUsuario(Usuario usuario) {
        mostrarDialogoEditarUsuario(usuario);
    }

    @Override
    public void onEliminarUsuario(Usuario usuario) {
        // No permitir eliminar al usuario actual ni al admin principal
        if (usuario.getId() == usuarioId || usuario.getId() == 3) {
            Toast.makeText(this, "No puedes eliminar este usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Está seguro de que desea eliminar al usuario '" + usuario.getNombre()
                        + "'?\n\nEsta acción también eliminará todos sus cursos y matrículas asociadas.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    boolean exito = dataManager.deleteUsuario(usuario.getId());
                    if (exito) {
                        Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onResetearPassword(Usuario usuario) {
        new AlertDialog.Builder(this)
                .setTitle("Resetear Contraseña")
                .setMessage("¿Desea resetear la contraseña del usuario '" + usuario.getNombre() + "' a '123'?")
                .setPositiveButton("Resetear", (dialog, which) -> {
                    boolean exito = dataManager.resetPassword(usuario.getId(), "123");
                    if (exito) {
                        Toast.makeText(this, "Contraseña reseteada a '123'", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al resetear contraseña", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoAgregarUsuario() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_usuario, null);

        TextInputEditText etNombre = dialogView.findViewById(R.id.etNombre);
        TextInputEditText etUsername = dialogView.findViewById(R.id.etUsername);
        TextInputEditText etPassword = dialogView.findViewById(R.id.etPassword);
        Spinner spinnerTipo = dialogView.findViewById(R.id.spinnerTipo);

        // Configurar spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[] { "ESTUDIANTE", "PROFESOR" });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        new AlertDialog.Builder(this)
                .setTitle("Agregar Usuario")
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String username = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    String tipoStr = spinnerTipo.getSelectedItem().toString();

                    if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario.TipoUsuario tipo = Usuario.TipoUsuario.valueOf(tipoStr);
                    boolean exito = dataManager.addUsuario(username, password, nombre, tipo);

                    if (exito) {
                        Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Error: El nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditarUsuario(Usuario usuario) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_usuario, null);

        TextInputEditText etNombre = dialogView.findViewById(R.id.etNombre);
        TextInputEditText etUsername = dialogView.findViewById(R.id.etUsername);
        TextInputEditText etPassword = dialogView.findViewById(R.id.etPassword);
        Spinner spinnerTipo = dialogView.findViewById(R.id.spinnerTipo);

        // Llenar campos con datos actuales
        etNombre.setText(usuario.getNombre());
        etUsername.setText(usuario.getUsername());
        etPassword.setVisibility(View.GONE); // No mostrar campo de contraseña en edición

        // Configurar spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[] { "ESTUDIANTE", "PROFESOR", "ADMIN" });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
        spinnerTipo.setSelection(usuario.getTipo().ordinal());

        new AlertDialog.Builder(this)
                .setTitle("Editar Usuario")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String username = etUsername.getText().toString().trim();
                    String tipoStr = spinnerTipo.getSelectedItem().toString();

                    if (nombre.isEmpty() || username.isEmpty()) {
                        Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Usuario.TipoUsuario tipo = Usuario.TipoUsuario.valueOf(tipoStr);
                    boolean exito = dataManager.updateUsuario(usuario.getId(), username, nombre, tipo);

                    if (exito) {
                        Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Error: El nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Adapter para la lista de usuarios
    private static class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {
        private List<Usuario> usuarios;
        private UsuarioActionListener listener;

        UsuarioAdapter(List<Usuario> usuarios, UsuarioActionListener listener) {
            this.usuarios = usuarios;
            this.listener = listener;
        }

        @NonNull
        @Override
        public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_usuario_admin, parent, false);
            return new UsuarioViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
            Usuario usuario = usuarios.get(position);
            holder.bind(usuario, listener);
        }

        @Override
        public int getItemCount() {
            return usuarios.size();
        }

        void updateUsuarios(List<Usuario> newUsuarios) {
            this.usuarios = newUsuarios;
            notifyDataSetChanged();
        }

        static class UsuarioViewHolder extends RecyclerView.ViewHolder {
            TextView tvNombreUsuario, tvDetalleUsuario;
            MaterialButton btnEditarUsuario, btnResetPassword, btnEliminarUsuario;

            UsuarioViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
                tvDetalleUsuario = itemView.findViewById(R.id.tvDetalleUsuario);
                btnEditarUsuario = itemView.findViewById(R.id.btnEditarUsuario);
                btnResetPassword = itemView.findViewById(R.id.btnResetPassword);
                btnEliminarUsuario = itemView.findViewById(R.id.btnEliminarUsuario);
            }

            void bind(Usuario usuario, UsuarioActionListener listener) {
                if (usuario != null) {
                    String nombre = usuario.getNombre() != null ? usuario.getNombre() : "Sin nombre";
                    String username = usuario.getUsername() != null ? usuario.getUsername() : "Sin usuario";
                    String tipo = usuario.getTipo() != null ? usuario.getTipo().name() : "Sin tipo";

                    tvNombreUsuario.setText(nombre);
                    tvDetalleUsuario.setText(username + " - " + tipo);

                    // Configurar listeners de botones
                    btnEditarUsuario.setOnClickListener(v -> listener.onEditarUsuario(usuario));
                    btnResetPassword.setOnClickListener(v -> listener.onResetearPassword(usuario));
                    btnEliminarUsuario.setOnClickListener(v -> listener.onEliminarUsuario(usuario));

                    // Deshabilitar eliminar para el admin principal
                    if (usuario.getId() == 3) {
                        btnEliminarUsuario.setEnabled(false);
                        btnEliminarUsuario.setAlpha(0.5f);
                    }
                } else {
                    tvNombreUsuario.setText("Usuario no válido");
                    tvDetalleUsuario.setText("");
                }
            }
        }
    }

    // Adapter para la lista de cursos
    private static class CursoAdminAdapter extends RecyclerView.Adapter<CursoAdminAdapter.CursoViewHolder> {
        private List<Curso> cursos;

        CursoAdminAdapter(List<Curso> cursos) {
            this.cursos = cursos;
        }

        @NonNull
        @Override
        public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new CursoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
            Curso curso = cursos.get(position);
            holder.bind(curso);
        }

        @Override
        public int getItemCount() {
            return cursos.size();
        }

        void updateCursos(List<Curso> newCursos) {
            this.cursos = newCursos;
            notifyDataSetChanged();
        }

        static class CursoViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;

            CursoViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }

            void bind(Curso curso) {
                if (curso != null) {
                    String nombre = curso.getNombre() != null ? curso.getNombre() : "Sin nombre";
                    String profesor = curso.getNombreProfesor() != null ? curso.getNombreProfesor() : "Sin profesor";

                    text1.setText(nombre);
                    text2.setText("Profesor: " + profesor);
                } else {
                    text1.setText("Curso no válido");
                    text2.setText("");
                }
            }
        }
    }
}
