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

public class CursoDisponibleAdapter extends RecyclerView.Adapter<CursoDisponibleAdapter.ViewHolder> {

    private List<Curso> cursos;
    private OnCursoClickListener listener;

    public interface OnCursoClickListener {
        void onMatricularse(Curso curso);
    }

    public CursoDisponibleAdapter(List<Curso> cursos, OnCursoClickListener listener) {
        this.cursos = cursos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso_disponible, parent, false);
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
        TextView tvNombreCurso, tvDescripcionCurso, tvProfesor;
        MaterialButton btnMatricularse;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tvNombreCurso);
            tvDescripcionCurso = itemView.findViewById(R.id.tvDescripcionCurso);
            tvProfesor = itemView.findViewById(R.id.tvProfesor);
            btnMatricularse = itemView.findViewById(R.id.btnMatricularse);
        }

        void bind(Curso curso) {
            tvNombreCurso.setText(curso.getNombre());
            tvDescripcionCurso.setText(curso.getDescripcion());
            tvProfesor.setText("Profesor: " + curso.getNombreProfesor());

            btnMatricularse.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMatricularse(curso);
                }
            });
        }
    }
}
