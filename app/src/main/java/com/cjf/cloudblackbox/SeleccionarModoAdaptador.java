package com.cjf.cloudblackbox;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class SeleccionarModoAdaptador extends RecyclerView.Adapter<SeleccionarModoAdaptador.SeleccionarModoViewHolder>
{
    private ArrayList<Modos> modos;

    public SeleccionarModoAdaptador(ArrayList<Modos> modos)
    {
        this.modos = modos;
    }

    public SeleccionarModoAdaptador.SeleccionarModoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modos_cardview,parent,false);

        return new SeleccionarModoAdaptador.SeleccionarModoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SeleccionarModoViewHolder holder, int position) {
        Modos modo = modos.get(position);

        holder.imgModo.setImageResource(modo.getIcono());
        holder.tvModo.setText(modo.getModo());

    }


    @Override
    public int getItemCount() { //cantiad de elementos que contiene mi lista
        return modos.size();
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }





    public static class SeleccionarModoViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imgModo;
        private TextView tvModo;

        public SeleccionarModoViewHolder (View itemView)
        {
            super(itemView);
            imgModo = (ImageView) itemView.findViewById(R.id.imgModo);
            tvModo = (TextView) itemView.findViewById(R.id.tvModo);
        }
    }


}
