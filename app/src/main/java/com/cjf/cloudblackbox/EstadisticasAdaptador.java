package com.cjf.cloudblackbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EstadisticasAdaptador extends RecyclerView.Adapter<EstadisticasAdaptador.EstadisticasViewHolder>
{
    ArrayList<Estadistica> estadisticas;
    private onOpcionListener mListener;

    public EstadisticasAdaptador(ArrayList<Estadistica> estadisticas, onOpcionListener listener)
    {
        this.estadisticas = estadisticas;
        this.mListener = listener;
    }

    public EstadisticasAdaptador.EstadisticasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.estadisticas_cardview,parent,false);

        return new EstadisticasAdaptador.EstadisticasViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EstadisticasAdaptador.EstadisticasViewHolder holder, int position) {
        Estadistica estadistica = estadisticas.get(position);

        holder.imgEstadistica.setImageResource(estadistica.getIcono());
        holder.tvEstadistica.setText(estadistica.getEstadistica());

    }


    @Override
    public int getItemCount() { //cantiad de elementos que contiene mi lista
        return estadisticas.size();
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class EstadisticasViewHolder extends RecyclerView.ViewHolder implements onOpcionListener, View.OnClickListener
    {
        private ImageView imgEstadistica;
        private TextView tvEstadistica;

        onOpcionListener listener;

        public EstadisticasViewHolder (View itemView , onOpcionListener listener)
        {
            super(itemView);
            imgEstadistica = (ImageView) itemView.findViewById(R.id.imgEstadistica);
            tvEstadistica = (TextView) itemView.findViewById(R.id.tvEstadistica);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onOpcionClick(int position) {
            listener.onOpcionClick(getAdapterPosition());

        }

        @Override
        public void onClick(View view) {
            listener.onOpcionClick(getAdapterPosition());

        }
    }

}
