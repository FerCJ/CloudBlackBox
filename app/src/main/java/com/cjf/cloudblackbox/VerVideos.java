package com.cjf.cloudblackbox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

public class VerVideos extends AppCompatActivity {

    private List<ListaVideos> videos = new ArrayList<>();;
    private RecyclerView listaVideos;
    private  String userID;
    private FirebaseViewModel firebaseViewModel;
    private  String NombreVideoSeleccionado;
    private ProgressBar progressBar;
    private static final String TAG2 = "VerVideo";

    private ProgressDialog progressDialog;

    VideoView videoView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_videos);
        userID = getIntent().getExtras().get("ID").toString();


        listaVideos = (RecyclerView) findViewById(R.id.rvListaVideos);
        progressBar = (ProgressBar) findViewById((R.id.progressBarVideos));



        firebaseViewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);
        firebaseViewModel.getVideos().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                if (strings != null)
                {
                    ObtenerListaVideos(strings);
                    progressBar.setVisibility(View.GONE);
                    listaVideos.setLayoutManager( new LinearLayoutManager(getBaseContext()));

                    VerVideosAdaptador adaptador = new VerVideosAdaptador(videos);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NombreVideoSeleccionado = videos.get(listaVideos.getChildAdapterPosition(view)).getHora();
                            ObtenerLinkVideoSeleccionado(userID,NombreVideoSeleccionado);
                        }
                    });
                    listaVideos.setAdapter(adaptador);
                }
            }
        });

        SolicitarVideos(userID);
        videoView = (VideoView) findViewById(R.id.vvVideo);

        firebaseViewModel.getVideoSeleccionado().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null)
                {
                    Log.i(TAG2, "Si entra a reproducir el video" );
                    Log.i(TAG2, "El link recibido es:  " + uri.toString() );
                    videoView.setVideoURI(uri);
                    MediaController mediaController = new MediaController(VerVideos.this);
                    videoView.setMediaController(mediaController);
                    videoView.requestFocus();
                    videoView.start();

                }
            }
        });

        Button closeButton = (Button) findViewById(R.id.btnReturnVideo);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void SolicitarVideos(String UserID){
        progressBar.setVisibility(View.VISIBLE);
        firebaseViewModel.ObtenerVideos(UserID);

    }
    public void ObtenerListaVideos(ArrayList<String> VideosFirebase){
        List<ListaVideos> ListaVideos=new ArrayList<>();
        for(int i=0;i<VideosFirebase.size();i++){
            for (int j=0;j<VideosFirebase.get(i).length();j++){
                if(VideosFirebase.get(i).charAt(j)=='@')
                    videos.add(new ListaVideos(VideosFirebase.get(i).substring(0,j),VideosFirebase.get(i).substring(j+1,VideosFirebase.get(i).length()), R.drawable.icons8_video_100));
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerLinkVideoSeleccionado(String UserID, String NombreVideo){
        firebaseViewModel.ObtenerVideoSeleccionado(UserID,NombreVideo);
    }
}