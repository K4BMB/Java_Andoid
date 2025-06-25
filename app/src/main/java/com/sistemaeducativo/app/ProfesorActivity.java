package com.sistemaeducativo.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sistemaeducativo.app.adapter.CursoProfesorAdapter;
import com.sistemaeducativo.app.data.DataManager;
import com.sistemaeducativo.app.model.Curso;
import com.sistemaeducativo.app.model.Matricula;

import java.util.List;

public class ProfesorActivity extends AppCompatActivity implements CursoProfesorAdapter.OnCursoProfesorClickListener {

    private TextView tvWelcome;
    private TextInputEditText etNombreCurso, etDescripcion;
    private MaterialButton btnCrearCurso, btnCerrarSesion;
    private RecyclerView rvMisCursos;

    private CursoProfesorAdapter cursoAdapter;
    private DataManager dataManager;

    private int usuarioId;
    private String usuarioNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor);

        dataManager = DataManager.getInstance();
        getUserData();
        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadData();
    }

    private void getUserData() {
        usuarioId = getIntent().getIntExtra("usuario_id", -1);
        usuarioNombre = getIntent().getStringExtra("usuario_nombre");
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        etNombreCurso = findViewById(R.id.etNombreCurso);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnCrearCurso = findViewById(R.id.btnCrearCurso);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        rvMisCursos = findViewById(R.id.rvMisCursos);

        tvWelcome.setText("Bienvenido, " + usuarioNombre);
    }

    private void setupRecyclerView() {
        rvMisCursos.setLayoutManager(new LinearLayoutManager(this));
        cursoAdapter = new CursoProfesorAdapter(dataManager.getCursosByProfesor(usuarioId), this);
        rvMisCursos.setAdapter(cursoAdapter);
    }

    private void setupListeners() {
        btnCrearCurso.setOnClickListener(v -> crearCurso());
        btnCerrarSesion.setOnClickListener(v -> logout());
    }

    private void loadData() {
        List<Curso> misCursos = dataManager.getCursosByProfesor(usuarioId);
        cursoAdapter.updateCursos(misCursos);
    }

    private void crearCurso() {
        String nombre = etNombreCurso.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito = dataManager.addCurso(nombre, descripcion, usuarioId);

        if (exito) {
            Toast.makeText(this, "Curso creado exitosamente", Toast.LENGTH_SHORT).show();
            etNombreCurso.setText("");
            etDescripcion.setText("");
            loadData(); // Recargar la lista de cursos
        } else {
            Toast.makeText(this, "Error al crear el curso", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onVerEstudiantes(Curso curso) {
        List<Matricula> estudiantes = dataManager.getEstudiantesByCurso(curso.getId());

        if (estudiantes.isEmpty()) {
            Toast.makeText(this, "No hay estudiantes matriculados en este curso", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear diálogo para mostrar estudiantes
        StringBuilder estudiantesInfo = new StringBuilder();
        estudiantesInfo.append("Estudiantes matriculados en: ").append(curso.getNombre()).append("\n\n");

        for (Matricula matricula : estudiantes) {
            estudiantesInfo.append("• ").append(matricula.getNombreEstudiante())
                    .append(" (").append(matricula.getFechaMatricula()).append(")\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Lista de Estudiantes")
                .setMessage(estudiantesInfo.toString())
                .setPositiveButton("Cerrar", null)
                .show();
    }

    @Override
    public void onEliminarCurso(Curso curso) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Curso")
                .setMessage("¿Está seguro de que desea eliminar el curso '" + curso.getNombre()
                        + "'?\n\nEsta acción también eliminará todas las matrículas asociadas.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    dataManager.deleteCurso(curso.getId());
                    Toast.makeText(this, "Curso eliminado", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Cancelar", null)
                .show();
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
}
