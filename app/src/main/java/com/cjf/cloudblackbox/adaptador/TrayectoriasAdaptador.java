package com.cjf.cloudblackbox.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cjf.cloudblackbox.R;
import com.cjf.cloudblackbox.pojo.Trayectoria;

import java.util.List;

public class TrayectoriasAdaptador extends RecyclerView.Adapter<TrayectoriasAdaptador.ViewHolder> implements View.OnClickListener {

    List<Trayectoria> trayectorias;
    private  View.OnClickListener listener;


    public TrayectoriasAdaptador(List<Trayectoria> trayectorias)
    {
        this.trayectorias = trayectorias;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trayectorias_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(this);
        return viewHolder;

    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trayectoria trayectoria = trayectorias.get(position);

        holder.imgTrayectoria.setImageResource(R.drawable.trayectoriasicon);
        holder.tvFecha.setText(trayectoria.getFecha());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imgTrayectoria;
        private TextView tvFecha;

        public ViewHolder (View itemView)
        {
            super(itemView);
            imgTrayectoria = (ImageView) itemView.findViewById(R.id.imgTrayectorias);
            tvFecha = (TextView) itemView.findViewById(R.id.tvFechaT);
        }
    }



    @Override
    public int getItemCount() { //cantiad de elementos que contiene mi lista
        return trayectorias.size();
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public  void  setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
        {
            listener.onClick(view);
        }
    }
}
