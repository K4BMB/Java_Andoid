package com.sistemaeducativo.app.model;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String nombre;
    private TipoUsuario tipo;

    public enum TipoUsuario {
        ESTUDIANTE, PROFESOR, ADMIN
    }

    public Usuario() {}

    public Usuario(int id, String username, String password, String nombre, TipoUsuario tipo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
