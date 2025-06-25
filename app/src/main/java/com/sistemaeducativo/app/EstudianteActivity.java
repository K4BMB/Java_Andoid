package com.sistemaeducativo.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sistemaeducativo.app.adapter.CursoDisponibleAdapter;
import com.sistemaeducativo.app.adapter.MiCursoAdapter;
import com.sistemaeducativo.app.data.DataManager;
import com.sistemaeducativo.app.model.Curso;
import com.sistemaeducativo.app.model.Matricula;

import java.util.List;

public class EstudianteActivity extends AppCompatActivity implements CursoDisponibleAdapter.OnCursoClickListener {

    private TextView tvWelcome;
    private RecyclerView rvMisCursos, rvCursosDisponibles;
    private MaterialButton btnCerrarSesion;

    private MiCursoAdapter miCursoAdapter;
    private CursoDisponibleAdapter cursoDisponibleAdapter;
    private DataManager dataManager;

    private int usuarioId;
    private String usuarioNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante);

        dataManager = DataManager.getInstance();
        getUserData();
        initializeViews();
        setupRecyclerViews();
        setupListeners();
        loadData();
    }

    private void getUserData() {
        usuarioId = getIntent().getIntExtra("usuario_id", -1);
        usuarioNombre = getIntent().getStringExtra("usuario_nombre");
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        rvMisCursos = findViewById(R.id.rvMisCursos);
        rvCursosDisponibles = findViewById(R.id.rvCursosDisponibles);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        tvWelcome.setText("Bienvenido, " + usuarioNombre);
    }

    private void setupRecyclerViews() {
        // RecyclerView para mis cursos
        rvMisCursos.setLayoutManager(new LinearLayoutManager(this));
        miCursoAdapter = new MiCursoAdapter(dataManager.getMatriculasByEstudiante(usuarioId));
        rvMisCursos.setAdapter(miCursoAdapter);

        // RecyclerView para cursos disponibles
        rvCursosDisponibles.setLayoutManager(new LinearLayoutManager(this));
        cursoDisponibleAdapter = new CursoDisponibleAdapter(
                dataManager.getCursosDisponiblesParaEstudiante(usuarioId), this);
        rvCursosDisponibles.setAdapter(cursoDisponibleAdapter);
    }

    private void setupListeners() {
        btnCerrarSesion.setOnClickListener(v -> logout());
    }

    private void loadData() {
        // Actualizar mis cursos
        List<Matricula> misCursos = dataManager.getMatriculasByEstudiante(usuarioId);
        miCursoAdapter.updateMatriculas(misCursos);

        // Actualizar cursos disponibles
        List<Curso> cursosDisponibles = dataManager.getCursosDisponiblesParaEstudiante(usuarioId);
        cursoDisponibleAdapter.updateCursos(cursosDisponibles);
    }

    @Override
    public void onMatricularse(Curso curso) {
        boolean exito = dataManager.matricularEstudiante(usuarioId, curso.getId());

        if (exito) {
            Toast.makeText(this, "Te has matriculado en: " + curso.getNombre(), Toast.LENGTH_SHORT).show();
            loadData(); // Recargar datos para actualizar las listas
        } else {
            Toast.makeText(this, "Error al matricularse. Ya estás inscrito en este curso.", Toast.LENGTH_SHORT).show();
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
        loadData(); // Recargar datos cuando regrese a la actividad
    }
}
