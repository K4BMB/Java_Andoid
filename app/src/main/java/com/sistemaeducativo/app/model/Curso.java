package com.sistemaeducativo.app.model;

public class Curso {
    private int id;
    private String nombre;
    private String descripcion;
    private int profesorId;
    private String nombreProfesor;

    public Curso() {}

    public Curso(int id, String nombre, String descripcion, int profesorId, String nombreProfesor) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesorId = profesorId;
        this.nombreProfesor = nombreProfesor;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(int profesorId) {
        this.profesorId = profesorId;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", profesorId=" + profesorId +
                ", nombreProfesor='" + nombreProfesor + '\'' +
                '}';
    }
}
