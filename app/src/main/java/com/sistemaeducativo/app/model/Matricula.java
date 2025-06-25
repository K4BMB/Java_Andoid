package com.sistemaeducativo.app.model;

public class Matricula {
    private int id;
    private int estudianteId;
    private int cursoId;
    private String fechaMatricula;
    private String nombreEstudiante;
    private String nombreCurso;

    public Matricula() {}

    public Matricula(int id, int estudianteId, int cursoId, String fechaMatricula) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.cursoId = cursoId;
        this.fechaMatricula = fechaMatricula;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    public int getCursoId() {
        return cursoId;
    }

    public void setCursoId(int cursoId) {
        this.cursoId = cursoId;
    }

    public String getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(String fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "id=" + id +
                ", estudianteId=" + estudianteId +
                ", cursoId=" + cursoId +
                ", fechaMatricula='" + fechaMatricula + '\'' +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                ", nombreCurso='" + nombreCurso + '\'' +
                '}';
    }
}
