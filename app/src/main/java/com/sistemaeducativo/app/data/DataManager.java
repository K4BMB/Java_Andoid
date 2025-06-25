package com.sistemaeducativo.app.data;

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
    private List<Usuario> usuarios;
    private List<Curso> cursos;
    private List<Matricula> matriculas;
    private int nextUsuarioId = 4;
    private int nextCursoId = 1;
    private int nextMatriculaId = 1;

    private DataManager() {
        initializeData();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initializeData() {
        // Inicializar usuarios
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1, "estudiante", "123", "Juan Pérez", Usuario.TipoUsuario.ESTUDIANTE));
        usuarios.add(new Usuario(2, "profesor", "123", "María García", Usuario.TipoUsuario.PROFESOR));
        usuarios.add(new Usuario(3, "admin", "123", "Carlos Admin", Usuario.TipoUsuario.ADMIN));

        // Inicializar cursos
        cursos = new ArrayList<>();

        // Inicializar matrículas
        matriculas = new ArrayList<>();
    }

    // Métodos para Usuario
    public Usuario authenticateUser(String username, String password) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> getAllUsuarios() {
        return new ArrayList<>(usuarios);
    }

    // Métodos para Curso
    public List<Curso> getAllCursos() {
        return new ArrayList<>(cursos);
    }

    public List<Curso> getCursosByProfesor(int profesorId) {
        List<Curso> cursosProfesor = new ArrayList<>();
        for (Curso curso : cursos) {
            if (curso.getProfesorId() == profesorId) {
                cursosProfesor.add(curso);
            }
        }
        return cursosProfesor;
    }

    public boolean addCurso(String nombre, String descripcion, int profesorId) {
        Usuario profesor = getUserById(profesorId);
        if (profesor != null && profesor.getTipo() == Usuario.TipoUsuario.PROFESOR) {
            Curso nuevoCurso = new Curso(nextCursoId++, nombre, descripcion, profesorId, profesor.getNombre());
            cursos.add(nuevoCurso);
            return true;
        }
        return false;
    }

    public boolean deleteCurso(int cursoId) {
        cursos.removeIf(curso -> curso.getId() == cursoId);
        // También eliminar matrículas relacionadas
        matriculas.removeIf(matricula -> matricula.getCursoId() == cursoId);
        return true;
    }

    // Métodos para Matrícula
    public List<Matricula> getMatriculasByEstudiante(int estudianteId) {
        List<Matricula> matriculasEstudiante = new ArrayList<>();
        for (Matricula matricula : matriculas) {
            if (matricula.getEstudianteId() == estudianteId) {
                // Completar información del curso
                Curso curso = getCursoById(matricula.getCursoId());
                if (curso != null) {
                    matricula.setNombreCurso(curso.getNombre());
                }
                matriculasEstudiante.add(matricula);
            }
        }
        return matriculasEstudiante;
    }

    public List<Curso> getCursosDisponiblesParaEstudiante(int estudianteId) {
        List<Curso> cursosDisponibles = new ArrayList<>();
        List<Integer> cursosMatriculados = new ArrayList<>();
        
        // Obtener IDs de cursos ya matriculados
        for (Matricula matricula : matriculas) {
            if (matricula.getEstudianteId() == estudianteId) {
                cursosMatriculados.add(matricula.getCursoId());
            }
        }
        
        // Agregar cursos no matriculados
        for (Curso curso : cursos) {
            if (!cursosMatriculados.contains(curso.getId())) {
                cursosDisponibles.add(curso);
            }
        }
        
        return cursosDisponibles;
    }

    public boolean matricularEstudiante(int estudianteId, int cursoId) {
        // Verificar si ya está matriculado
        for (Matricula matricula : matriculas) {
            if (matricula.getEstudianteId() == estudianteId && matricula.getCursoId() == cursoId) {
                return false; // Ya está matriculado
            }
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaActual = sdf.format(new Date());
        
        Matricula nuevaMatricula = new Matricula(nextMatriculaId++, estudianteId, cursoId, fechaActual);
        matriculas.add(nuevaMatricula);
        return true;
    }

    public List<Matricula> getEstudiantesByCurso(int cursoId) {
        List<Matricula> estudiantesCurso = new ArrayList<>();
        for (Matricula matricula : matriculas) {
            if (matricula.getCursoId() == cursoId) {
                // Completar información del estudiante
                Usuario estudiante = getUserById(matricula.getEstudianteId());
                if (estudiante != null) {
                    matricula.setNombreEstudiante(estudiante.getNombre());
                }
                estudiantesCurso.add(matricula);
            }
        }
        return estudiantesCurso;
    }

    // Métodos auxiliares
    private Usuario getUserById(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }
        return null;
    }

    private Curso getCursoById(int id) {
        for (Curso curso : cursos) {
            if (curso.getId() == id) {
                return curso;
            }
        }
        return null;
    }

    // Métodos para estadísticas (Admin)
    public int getTotalUsuarios() {
        return usuarios.size();
    }

    public int getTotalCursos() {
        return cursos.size();
    }

    public int getTotalMatriculas() {
        return matriculas.size();
    }
}
