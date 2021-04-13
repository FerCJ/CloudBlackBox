package com.cjf.cloudblackbox;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VerVideosAdaptador extends RecyclerView.Adapter<VerVideosAdaptador.ViewHolder> implements  View.OnClickListener{
    private List<ListaVideos> videos;

    private  View.OnClickListener listener;

    public VerVideosAdaptador(List<ListaVideos> videos) { this.videos = videos; }


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListaVideos video = videos.get(position);

        holder.imgVideo.setImageResource(video.getImagen());
        holder.tvFecha.setText(video.getFecha());
        holder.tvHora.setText(video.getHora());
    }


    @Override
    public int getItemCount() { //cantiad de elementos que contiene mi lista
        return videos.size();
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imgVideo;
        private TextView tvFecha;
        private TextView tvHora;



        public ViewHolder (View itemView)
        {
            super(itemView);
            imgVideo = (ImageView) itemView.findViewById(R.id.imgVideo);
            tvFecha = (TextView) itemView.findViewById(R.id.tvFechaV);
            tvHora = (TextView) itemView.findViewById(R.id.tvHoraV);
        }
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

