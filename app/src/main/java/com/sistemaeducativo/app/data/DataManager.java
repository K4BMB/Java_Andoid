package com.sistemaeducativo.app.data;

import android.content.Context;

import com.sistemaeducativo.app.database.DatabaseHelper;
import com.sistemaeducativo.app.model.Usuario;
import com.sistemaeducativo.app.model.Curso;
import com.sistemaeducativo.app.model.Matricula;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataManager {
    private static DataManager instance;
    private DatabaseHelper dbHelper;
    private Context context;

    private DataManager(Context context) {
        this.context = context.getApplicationContext();
        this.dbHelper = new DatabaseHelper(this.context);
    }

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public static DataManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DataManager must be initialized with context first");
        }
        return instance;
    }

    // Métodos para Usuario
    public Usuario authenticateUser(String username, String password) {
        return dbHelper.authenticateUser(username, password);
    }

    public List<Usuario> getAllUsuarios() {
        return dbHelper.getAllUsuarios();
    }

    // Métodos para Curso
    public List<Curso> getAllCursos() {
        return dbHelper.getAllCursos();
    }

    public List<Curso> getCursosByProfesor(int profesorId) {
        return dbHelper.getCursosByProfesor(profesorId);
    }

    public boolean addCurso(String nombre, String descripcion, int profesorId) {
        Usuario profesor = dbHelper.getUserById(profesorId);
        if (profesor != null && profesor.getTipo() == Usuario.TipoUsuario.PROFESOR) {
            Curso nuevoCurso = new Curso(0, nombre, descripcion, profesorId, profesor.getNombre());
            long id = dbHelper.addCurso(nuevoCurso);
            return id > 0;
        }
        return false;
    }

    public boolean deleteCurso(int cursoId) {
        return dbHelper.deleteCurso(cursoId);
    }

    // Métodos para Matrícula
    public List<Matricula> getMatriculasByEstudiante(int estudianteId) {
        return dbHelper.getMatriculasByEstudiante(estudianteId);
    }

    public List<Curso> getCursosDisponiblesParaEstudiante(int estudianteId) {
        List<Curso> todosLosCursos = dbHelper.getAllCursos();
        List<Curso> cursosDisponibles = new ArrayList<>();

        for (Curso curso : todosLosCursos) {
            if (!dbHelper.isStudentEnrolled(estudianteId, curso.getId())) {
                cursosDisponibles.add(curso);
            }
        }

        return cursosDisponibles;
    }

    public boolean matricularEstudiante(int estudianteId, int cursoId) {
        if (dbHelper.isStudentEnrolled(estudianteId, cursoId)) {
            return false; // Ya está matriculado
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaActual = sdf.format(new Date());

        Matricula nuevaMatricula = new Matricula(0, estudianteId, cursoId, fechaActual);
        long id = dbHelper.addMatricula(nuevaMatricula);
        return id > 0;
    }

    public List<Matricula> getEstudiantesByCurso(int cursoId) {
        return dbHelper.getEstudiantesByCurso(cursoId);
    }

    // Métodos para estadísticas (Admin)
    public int getTotalUsuarios() {
        return dbHelper.getTotalUsuarios();
    }

    public int getTotalCursos() {
        return dbHelper.getTotalCursos();
    }

    public int getTotalMatriculas() {
        return dbHelper.getTotalMatriculas();
    }

    // Métodos para gestión de usuarios (Admin)
    public boolean addUsuario(String username, String password, String nombre, Usuario.TipoUsuario tipo) {
        // Verificar que el username no exista
        if (dbHelper.isUsernameExists(username, 0)) {
            return false; // Username ya existe
        }

        Usuario nuevoUsuario = new Usuario(0, username, password, nombre, tipo);
        long id = dbHelper.addUsuario(nuevoUsuario);
        return id > 0;
    }

    public boolean updateUsuario(int usuarioId, String username, String nombre, Usuario.TipoUsuario tipo) {
        Usuario usuario = dbHelper.getUserById(usuarioId);
        if (usuario == null) {
            return false;
        }

        // Verificar que el nuevo username no exista en otro usuario
        if (dbHelper.isUsernameExists(username, usuarioId)) {
            return false; // Username ya existe en otro usuario
        }

        usuario.setUsername(username);
        usuario.setNombre(nombre);
        usuario.setTipo(tipo);
        return dbHelper.updateUsuario(usuario);
    }

    public boolean deleteUsuario(int usuarioId) {
        // No permitir eliminar al admin principal (ID 3 o menos)
        if (usuarioId <= 3) {
            return false;
        }

        return dbHelper.deleteUsuario(usuarioId);
    }

    public boolean resetPassword(int usuarioId, String newPassword) {
        Usuario usuario = dbHelper.getUserById(usuarioId);
        if (usuario == null) {
            return false;
        }

        usuario.setPassword(newPassword);
        return dbHelper.updateUsuario(usuario);
    }
}
