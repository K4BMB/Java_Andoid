package com.sistemaeducativo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sistemaeducativo.app.data.DataManager;
import com.sistemaeducativo.app.model.Curso;
import com.sistemaeducativo.app.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTotalUsuarios, tvTotalCursos, tvTotalMatriculas;
    private RecyclerView rvUsuarios, rvCursos;
    private MaterialButton btnCerrarSesion;

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
            usuarioAdapter = new UsuarioAdapter(usuarios);
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

    // Adapter para la lista de usuarios
    private static class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {
        private List<Usuario> usuarios;

        UsuarioAdapter(List<Usuario> usuarios) {
            this.usuarios = usuarios;
        }

        @NonNull
        @Override
        public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new UsuarioViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
            Usuario usuario = usuarios.get(position);
            holder.bind(usuario);
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
            TextView text1, text2;

            UsuarioViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }

            void bind(Usuario usuario) {
                if (usuario != null) {
                    String nombre = usuario.getNombre() != null ? usuario.getNombre() : "Sin nombre";
                    String username = usuario.getUsername() != null ? usuario.getUsername() : "Sin usuario";
                    String tipo = usuario.getTipo() != null ? usuario.getTipo().name() : "Sin tipo";

                    text1.setText(nombre + " (" + username + ")");
                    text2.setText("Tipo: " + tipo);
                } else {
                    text1.setText("Usuario no válido");
                    text2.setText("");
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
