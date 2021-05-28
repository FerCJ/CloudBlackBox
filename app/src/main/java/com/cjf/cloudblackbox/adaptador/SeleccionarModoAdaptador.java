package com.cjf.cloudblackbox.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cjf.cloudblackbox.pojo.Modos;
import com.cjf.cloudblackbox.R;
import com.cjf.cloudblackbox.onOpcionListener;

import java.util.ArrayList;

public class SeleccionarModoAdaptador extends RecyclerView.Adapter<SeleccionarModoAdaptador.SeleccionarModoViewHolder>
{
    private ArrayList<Modos> modos;
    private onOpcionListener mListener;


    public SeleccionarModoAdaptador(ArrayList<Modos> modos, onOpcionListener listener)
    {
        this.modos = modos;
        this.mListener = listener;
    }

    public SeleccionarModoAdaptador.SeleccionarModoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modos_cardview,parent,false);

        return new SeleccionarModoAdaptador.SeleccionarModoViewHolder(v,mListener);
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

    public static class SeleccionarModoViewHolder extends RecyclerView.ViewHolder implements onOpcionListener, View.OnClickListener
    {
        private ImageView imgModo;
        private TextView tvModo;
        onOpcionListener listener;

        public SeleccionarModoViewHolder (View itemView, onOpcionListener listener)
        {
            super(itemView);
            imgModo = (ImageView) itemView.findViewById(R.id.imgModo);
            tvModo = (TextView) itemView.findViewById(R.id.tvModo);

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
