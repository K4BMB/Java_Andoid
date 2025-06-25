package com.sistemaeducativo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sistemaeducativo.app.R;
import com.sistemaeducativo.app.model.Matricula;

import java.util.List;

public class MiCursoAdapter extends RecyclerView.Adapter<MiCursoAdapter.ViewHolder> {

    private List<Matricula> matriculas;

    public MiCursoAdapter(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mi_curso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Matricula matricula = matriculas.get(position);
        holder.bind(matricula);
    }

    @Override
    public int getItemCount() {
        return matriculas.size();
    }

    public void updateMatriculas(List<Matricula> newMatriculas) {
        this.matriculas = newMatriculas;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCurso, tvDescripcionCurso, tvProfesor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tvNombreCurso);
            tvDescripcionCurso = itemView.findViewById(R.id.tvDescripcionCurso);
            tvProfesor = itemView.findViewById(R.id.tvProfesor);
        }

        void bind(Matricula matricula) {
            tvNombreCurso.setText(matricula.getNombreCurso());
            tvDescripcionCurso.setText("Matriculado el: " + matricula.getFechaMatricula());
            tvProfesor.setText("Fecha: " + matricula.getFechaMatricula());
        }
    }
}
