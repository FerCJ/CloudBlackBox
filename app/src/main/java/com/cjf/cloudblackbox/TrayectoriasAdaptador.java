package com.cjf.cloudblackbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrayectoriasAdaptador extends RecyclerView.Adapter<TrayectoriasAdaptador.TrayectoriasViewHolder>{

    ArrayList<Trayectoria> trayectorias;

    public TrayectoriasAdaptador(ArrayList<Trayectoria> trayectorias)
    {
        this.trayectorias = trayectorias;
    }

    public TrayectoriasAdaptador.TrayectoriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trayectorias_cardview,parent,false);

        return new TrayectoriasAdaptador.TrayectoriasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrayectoriasAdaptador.TrayectoriasViewHolder holder, int position) {
        Trayectoria trayectoria = trayectorias.get(position);

        holder.imgTrayectoria.setImageResource(R.drawable.trayectoriasicon);
        holder.tvFecha.setText(trayectoria.getFecha());
    }



    @Override
    public int getItemCount() { //cantiad de elementos que contiene mi lista
        return trayectorias.size();
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class TrayectoriasViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imgTrayectoria;
        private TextView tvFecha;

        public TrayectoriasViewHolder (View itemView)
        {
            super(itemView);
            imgTrayectoria = (ImageView) itemView.findViewById(R.id.imgTrayectorias);
            tvFecha = (TextView) itemView.findViewById(R.id.tvFechaT);
        }
    }
}
