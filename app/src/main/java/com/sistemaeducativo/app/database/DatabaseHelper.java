package com.sistemaeducativo.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sistemaeducativo.app.model.Usuario;
import com.sistemaeducativo.app.model.Curso;
import com.sistemaeducativo.app.model.Matricula;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sistema_educativo.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla Usuarios
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_USERNAME = "username";
    private static final String COL_USER_PASSWORD = "password";
    private static final String COL_USER_NOMBRE = "nombre";
    private static final String COL_USER_TIPO = "tipo";

    // Tabla Cursos
    private static final String TABLE_CURSOS = "cursos";
    private static final String COL_CURSO_ID = "id";
    private static final String COL_CURSO_NOMBRE = "nombre";
    private static final String COL_CURSO_DESCRIPCION = "descripcion";
    private static final String COL_CURSO_PROFESOR_ID = "profesor_id";
    private static final String COL_CURSO_PROFESOR_NOMBRE = "profesor_nombre";

    // Tabla Matriculas
    private static final String TABLE_MATRICULAS = "matriculas";
    private static final String COL_MATRICULA_ID = "id";
    private static final String COL_MATRICULA_ESTUDIANTE_ID = "estudiante_id";
    private static final String COL_MATRICULA_CURSO_ID = "curso_id";
    private static final String COL_MATRICULA_FECHA = "fecha";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla usuarios
        String createUsersTable = "CREATE TABLE " + TABLE_USUARIOS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_USER_PASSWORD + " TEXT NOT NULL, " +
                COL_USER_NOMBRE + " TEXT NOT NULL, " +
                COL_USER_TIPO + " TEXT NOT NULL)";

        // Crear tabla cursos
        String createCursosTable = "CREATE TABLE " + TABLE_CURSOS + " (" +
                COL_CURSO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CURSO_NOMBRE + " TEXT NOT NULL, " +
                COL_CURSO_DESCRIPCION + " TEXT, " +
                COL_CURSO_PROFESOR_ID + " INTEGER, " +
                COL_CURSO_PROFESOR_NOMBRE + " TEXT, " +
                "FOREIGN KEY(" + COL_CURSO_PROFESOR_ID + ") REFERENCES " + TABLE_USUARIOS + "(" + COL_USER_ID + "))";

        // Crear tabla matriculas
        String createMatriculasTable = "CREATE TABLE " + TABLE_MATRICULAS + " (" +
                COL_MATRICULA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MATRICULA_ESTUDIANTE_ID + " INTEGER, " +
                COL_MATRICULA_CURSO_ID + " INTEGER, " +
                COL_MATRICULA_FECHA + " TEXT, " +
                "FOREIGN KEY(" + COL_MATRICULA_ESTUDIANTE_ID + ") REFERENCES " + TABLE_USUARIOS + "(" + COL_USER_ID
                + "), " +
                "FOREIGN KEY(" + COL_MATRICULA_CURSO_ID + ") REFERENCES " + TABLE_CURSOS + "(" + COL_CURSO_ID + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createCursosTable);
        db.execSQL(createMatriculasTable);

        // Insertar datos iniciales
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATRICULAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURSOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insertar usuarios iniciales
        ContentValues userValues = new ContentValues();

        // Estudiante
        userValues.put(COL_USER_USERNAME, "estudiante");
        userValues.put(COL_USER_PASSWORD, "123");
        userValues.put(COL_USER_NOMBRE, "Juan Pérez");
        userValues.put(COL_USER_TIPO, "ESTUDIANTE");
        long estudianteId = db.insert(TABLE_USUARIOS, null, userValues);

        // Profesor
        userValues.clear();
        userValues.put(COL_USER_USERNAME, "profesor");
        userValues.put(COL_USER_PASSWORD, "123");
        userValues.put(COL_USER_NOMBRE, "María García");
        userValues.put(COL_USER_TIPO, "PROFESOR");
        long profesorId = db.insert(TABLE_USUARIOS, null, userValues);

        // Admin
        userValues.clear();
        userValues.put(COL_USER_USERNAME, "admin");
        userValues.put(COL_USER_PASSWORD, "123");
        userValues.put(COL_USER_NOMBRE, "Carlos Admin");
        userValues.put(COL_USER_TIPO, "ADMIN");
        long adminId = db.insert(TABLE_USUARIOS, null, userValues);

        // Insertar cursos iniciales
        ContentValues cursoValues = new ContentValues();

        cursoValues.put(COL_CURSO_NOMBRE, "Inglés");
        cursoValues.put(COL_CURSO_DESCRIPCION, "Inglés para principiantes");
        cursoValues.put(COL_CURSO_PROFESOR_ID, profesorId);
        cursoValues.put(COL_CURSO_PROFESOR_NOMBRE, "María García");
        long cursoInglesId = db.insert(TABLE_CURSOS, null, cursoValues);

        cursoValues.clear();
        cursoValues.put(COL_CURSO_NOMBRE, "Matemáticas");
        cursoValues.put(COL_CURSO_DESCRIPCION, "Matemáticas básicas");
        cursoValues.put(COL_CURSO_PROFESOR_ID, profesorId);
        cursoValues.put(COL_CURSO_PROFESOR_NOMBRE, "María García");
        long cursoMatematicasId = db.insert(TABLE_CURSOS, null, cursoValues);

        // Insertar matrícula inicial
        ContentValues matriculaValues = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaActual = sdf.format(new Date());

        matriculaValues.put(COL_MATRICULA_ESTUDIANTE_ID, estudianteId);
        matriculaValues.put(COL_MATRICULA_CURSO_ID, cursoInglesId);
        matriculaValues.put(COL_MATRICULA_FECHA, fechaActual);
        db.insert(TABLE_MATRICULAS, null, matriculaValues);
    }

    // ======================== MÉTODOS PARA USUARIOS ========================

    public long addUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_USER_USERNAME, usuario.getUsername());
        values.put(COL_USER_PASSWORD, usuario.getPassword());
        values.put(COL_USER_NOMBRE, usuario.getNombre());
        values.put(COL_USER_TIPO, usuario.getTipo().name());

        long id = db.insert(TABLE_USUARIOS, null, values);
        db.close();
        return id;
    }

    public Usuario authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE " +
                COL_USER_USERNAME + " = ? AND " + COL_USER_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { username, password });
        Usuario usuario = null;

        if (cursor.moveToFirst()) {
            usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NOMBRE)),
                    Usuario.TipoUsuario.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_TIPO))));
        }

        cursor.close();
        db.close();
        return usuario;
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIOS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NOMBRE)),
                        Usuario.TipoUsuario.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_TIPO))));
                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return usuarios;
    }

    public Usuario getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE " + COL_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(id) });
        Usuario usuario = null;

        if (cursor.moveToFirst()) {
            usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NOMBRE)),
                    Usuario.TipoUsuario.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_TIPO))));
        }

        cursor.close();
        db.close();
        return usuario;
    }

    public boolean updateUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_USER_USERNAME, usuario.getUsername());
        values.put(COL_USER_PASSWORD, usuario.getPassword());
        values.put(COL_USER_NOMBRE, usuario.getNombre());
        values.put(COL_USER_TIPO, usuario.getTipo().name());

        int result = db.update(TABLE_USUARIOS, values, COL_USER_ID + " = ?",
                new String[] { String.valueOf(usuario.getId()) });
        db.close();
        return result > 0;
    }

    public boolean deleteUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Eliminar matrículas del usuario
        db.delete(TABLE_MATRICULAS, COL_MATRICULA_ESTUDIANTE_ID + " = ?",
                new String[] { String.valueOf(id) });

        // Eliminar cursos del profesor
        db.delete(TABLE_CURSOS, COL_CURSO_PROFESOR_ID + " = ?",
                new String[] { String.valueOf(id) });

        // Eliminar usuario
        int result = db.delete(TABLE_USUARIOS, COL_USER_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
        return result > 0;
    }

    public boolean isUsernameExists(String username, int excludeUserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USUARIOS + " WHERE " +
                COL_USER_USERNAME + " = ? AND " + COL_USER_ID + " != ?";

        Cursor cursor = db.rawQuery(query, new String[] { username, String.valueOf(excludeUserId) });
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        return count > 0;
    }

    // ======================== MÉTODOS PARA CURSOS ========================

    public long addCurso(Curso curso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_CURSO_NOMBRE, curso.getNombre());
        values.put(COL_CURSO_DESCRIPCION, curso.getDescripcion());
        values.put(COL_CURSO_PROFESOR_ID, curso.getProfesorId());
        values.put(COL_CURSO_PROFESOR_NOMBRE, curso.getNombreProfesor());

        long id = db.insert(TABLE_CURSOS, null, values);
        db.close();
        return id;
    }

    public List<Curso> getAllCursos() {
        List<Curso> cursos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CURSOS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Curso curso = new Curso(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CURSO_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CURSO_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CURSO_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CURSO_PROFESOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CURSO_PROFESOR_NOMBRE)));
                cursos.add(curso);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cursos;
    }

    public List<Curso> getCursosByProfesor(int profesorId) {
        List<Curso> cursos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CURSOS + " WHERE " + COL_CURSO_PROFESOR_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(profesorId) });

        if (cursor.moveToFirst()) {
            do {
                Curso curso = new Curso(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CURSO_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CURSO_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CURSO_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CURSO_PROFESOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CURSO_PROFESOR_NOMBRE)));
                cursos.add(curso);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cursos;
    }

    public boolean deleteCurso(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Eliminar matrículas del curso
        db.delete(TABLE_MATRICULAS, COL_MATRICULA_CURSO_ID + " = ?",
                new String[] { String.valueOf(id) });

        // Eliminar curso
        int result = db.delete(TABLE_CURSOS, COL_CURSO_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
        return result > 0;
    }

    // ======================== MÉTODOS PARA MATRICULAS ========================

    public long addMatricula(Matricula matricula) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_MATRICULA_ESTUDIANTE_ID, matricula.getEstudianteId());
        values.put(COL_MATRICULA_CURSO_ID, matricula.getCursoId());
        values.put(COL_MATRICULA_FECHA, matricula.getFechaMatricula());

        long id = db.insert(TABLE_MATRICULAS, null, values);
        db.close();
        return id;
    }

    public List<Matricula> getMatriculasByEstudiante(int estudianteId) {
        List<Matricula> matriculas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.*, c." + COL_CURSO_NOMBRE + " as curso_nombre " +
                "FROM " + TABLE_MATRICULAS + " m " +
                "INNER JOIN " + TABLE_CURSOS + " c ON m." + COL_MATRICULA_CURSO_ID + " = c." + COL_CURSO_ID + " " +
                "WHERE m." + COL_MATRICULA_ESTUDIANTE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(estudianteId) });

        if (cursor.moveToFirst()) {
            do {
                Matricula matricula = new Matricula(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MATRICULA_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MATRICULA_ESTUDIANTE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MATRICULA_CURSO_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MATRICULA_FECHA)));
                matricula.setNombreCurso(cursor.getString(cursor.getColumnIndexOrThrow("curso_nombre")));
                matriculas.add(matricula);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return matriculas;
    }

    public List<Matricula> getEstudiantesByCurso(int cursoId) {
        List<Matricula> matriculas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m.*, u." + COL_USER_NOMBRE + " as estudiante_nombre " +
                "FROM " + TABLE_MATRICULAS + " m " +
                "INNER JOIN " + TABLE_USUARIOS + " u ON m." + COL_MATRICULA_ESTUDIANTE_ID + " = u." + COL_USER_ID + " "
                +
                "WHERE m." + COL_MATRICULA_CURSO_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(cursoId) });

        if (cursor.moveToFirst()) {
            do {
                Matricula matricula = new Matricula(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MATRICULA_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MATRICULA_ESTUDIANTE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_MATRICULA_CURSO_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MATRICULA_FECHA)));
                matricula.setNombreEstudiante(cursor.getString(cursor.getColumnIndexOrThrow("estudiante_nombre")));
                matriculas.add(matricula);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return matriculas;
    }

    public boolean isStudentEnrolled(int estudianteId, int cursoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_MATRICULAS + " WHERE " +
                COL_MATRICULA_ESTUDIANTE_ID + " = ? AND " + COL_MATRICULA_CURSO_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(estudianteId), String.valueOf(cursoId) });
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();

        return count > 0;
    }

    // ======================== MÉTODOS PARA ESTADÍSTICAS ========================

    public int getTotalUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USUARIOS;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalCursos() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_CURSOS;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalMatriculas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_MATRICULAS;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
