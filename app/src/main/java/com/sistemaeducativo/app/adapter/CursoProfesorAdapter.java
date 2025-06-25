package com.sistemaeducativo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sistemaeducativo.app.R;
import com.sistemaeducativo.app.model.Curso;

import java.util.List;

public class CursoProfesorAdapter extends RecyclerView.Adapter<CursoProfesorAdapter.ViewHolder> {

    private List<Curso> cursos;
    private OnCursoProfesorClickListener listener;

    public interface OnCursoProfesorClickListener {
        void onVerEstudiantes(Curso curso);
        void onEliminarCurso(Curso curso);
    }

    public CursoProfesorAdapter(List<Curso> cursos, OnCursoProfesorClickListener listener) {
        this.cursos = cursos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso_profesor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Curso curso = cursos.get(position);
        holder.bind(curso);
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public void updateCursos(List<Curso> newCursos) {
        this.cursos = newCursos;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCurso, tvDescripcionCurso, tvEstudiantes;
        MaterialButton btnVerEstudiantes, btnEliminarCurso;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tvNombreCurso);
            tvDescripcionCurso = itemView.findViewById(R.id.tvDescripcionCurso);
            tvEstudiantes = itemView.findViewById(R.id.tvEstudiantes);
            btnVerEstudiantes = itemView.findViewById(R.id.btnVerEstudiantes);
            btnEliminarCurso = itemView.findViewById(R.id.btnEliminarCurso);
        }

        void bind(Curso curso) {
            tvNombreCurso.setText(curso.getNombre());
            tvDescripcionCurso.setText(curso.getDescripcion());
            tvEstudiantes.setText("Estudiantes: 0"); // Se actualizará con datos reales

            btnVerEstudiantes.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerEstudiantes(curso);
                }
            });

            btnEliminarCurso.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEliminarCurso(curso);
                }
            });
        }
    }
}
