package com.cjf.cloudblackbox.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cjf.cloudblackbox.pojo.OpcionesPrincipal;
import com.cjf.cloudblackbox.R;
import com.cjf.cloudblackbox.onOpcionListener;

import java.util.ArrayList;

public class OpcionesPrincialAdaptador extends RecyclerView.Adapter<OpcionesPrincialAdaptador.OpcionesPrincipalViewHolder>{

    ArrayList<OpcionesPrincipal> opciones;
    private onOpcionListener mListener;

    public OpcionesPrincialAdaptador(ArrayList<OpcionesPrincipal> opciones, onOpcionListener listener)
    {
        this.opciones = opciones;
        this.mListener = listener;
    }

    @NonNull
    @Override


    public OpcionesPrincialAdaptador.OpcionesPrincipalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.principal_cardview,parent,false);

        return new OpcionesPrincialAdaptador.OpcionesPrincipalViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OpcionesPrincipalViewHolder holder, int position) {
        OpcionesPrincipal opcion = opciones.get(position);

        holder.imgOpcion.setImageResource(opcion.getIcono());
        holder.tvOpcion.setText(opcion.getOpcion());

    }

    @Override
    public int getItemCount() { //cantiad de elementos que contiene mi lista
        return opciones.size();
    }

    public static class OpcionesPrincipalViewHolder extends RecyclerView.ViewHolder implements onOpcionListener, View.OnClickListener {
       private ImageView imgOpcion;
       private TextView tvOpcion;

       onOpcionListener listener;

       public OpcionesPrincipalViewHolder (View itemView, onOpcionListener listener)
       {
           super(itemView);
           imgOpcion = (ImageView) itemView.findViewById(R.id.imgOpcion);
           tvOpcion = (TextView) itemView.findViewById(R.id.tvOpcion);

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
