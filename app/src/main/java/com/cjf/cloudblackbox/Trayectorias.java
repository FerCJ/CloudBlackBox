package com.cjf.cloudblackbox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Trayectorias extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView listaTrayectorias;
    private MapView mapa;
    private  ArrayList<String> coordenadasLista = new ArrayList<>();

    private List<Trayectoria> trayectorias = new ArrayList<>();
    private FirebaseViewModel firebaseViewModel;
    private  String userID;
    private String NombreTrayectoriaSeleccionada;
    private ProgressBar progressBar;
    private static final String TAG2 = "VerTrayectoria";
    private String FILE_NAME = "";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trayectorias);

        userID = getIntent().getExtras().get("ID").toString();
        progressBar = (ProgressBar) findViewById((R.id.progressBarTrayectorias));
        listaTrayectorias = (RecyclerView) findViewById(R.id.rvListaTrayectorias);
        mapa = (MapView) findViewById(R.id.mvTrayectorias);
        Log.d("Crash solictar:","Antes de solicitar");



        firebaseViewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);
        firebaseViewModel.getTrayectorias().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                if (strings != null)
                {

                    //Obtener(strings);
                    progressBar.setVisibility(View.GONE);
                    for (String trayecto: strings)
                    {
                        trayectorias.add(new Trayectoria(R.drawable.trayectoicon,trayecto));

                    }
                    listaTrayectorias.setLayoutManager( new LinearLayoutManager(getBaseContext()));

                    TrayectoriasAdaptador adaptador = new TrayectoriasAdaptador(trayectorias);

                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NombreTrayectoriaSeleccionada = trayectorias.get(listaTrayectorias.getChildAdapterPosition(view)).getFecha();
                            Log.d("Trayectoria: ", NombreTrayectoriaSeleccionada);
                            ObtenerLinkTrayectoriaSeleccionada(userID,NombreTrayectoriaSeleccionada);
                        }
                    });
                    listaTrayectorias.setAdapter(adaptador);
                }


            }
        });

        firebaseViewModel.getTrayectoriaSeleccionada().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null)
                {
                    Log.i(TAG2, "Si entra a reproducir el video" );
                    Log.i(TAG2, "El link recibido es:  " + uri.toString() );
                    FILE_NAME = uri.toString();
                    leeArchivo();
                }
            }
        });

        Button closeButton = (Button) findViewById(R.id.btnReturnTrayectirias);
        SolicitarTrayectorias(userID);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });


        mapa = (MapView) findViewById(R.id.mvTrayectorias);
        mapa.onCreate(savedInstanceState);
        mapa.getMapAsync(this);
       // crearArchivo();
        //leeArchivo();
    }

    @Override
    public void onMapReady(GoogleMap mapa) {

        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(19.457558810020842, -99.20795649290088);
        mapa.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        float zoomLevel = 16.0f; //This goes up to 21
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));*/



        Polyline polyline1 = mapa.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309)));

                mapa.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(new LatLng(-32.491, 147.309)));

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

       /* // Set listeners for click events.
        mapa.setOnPolylineClickListener(this);
        mapa.setOnPolygonClickListener(this);*/

    }

    protected void onResume() {
        super.onResume();
        mapa.onResume();
    }


    @Override
    protected void onPause() {
        mapa.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapa.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapa.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapa.onSaveInstanceState(outState);
    }

    private void crearArchivo()
    {

        ArrayList<String> instrucciones = new ArrayList<>();
        instrucciones.add("Start");
        instrucciones.add("-35.016,143.321");
        instrucciones.add("-34.747,145.592");
        instrucciones.add("-34.364,147.891");
        instrucciones.add("-33.501,150.217");
        instrucciones.add("-32.306,149.248");
        instrucciones.add("-32.491,147.309");
        instrucciones.add("End");


        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_NAME,MODE_PRIVATE);
            for (int i = 0; i < 8; i++)
            {
                fileOutputStream.write(instrucciones.get(i).getBytes());
                Log.d("Guardar:","Archivo guardado en: " + getFilesDir() + "/" + FILE_NAME);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null)
            {
                try {
                    fileOutputStream.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void leeArchivo()
    {
        Log.d("LeerArchivo: ", "Entra al metodo");
        FileReader fileReader = null;
        BufferedReader bufferedReader;
        String buffer;
        StringBuilder stringBuilder = new StringBuilder();



       // FileInputStream fileInputStream = null;
        //String lineaTexto;
        //StringBuilder stringBuilder = new StringBuilder();
        try {
            fileReader = new FileReader(FILE_NAME);
            bufferedReader = new BufferedReader(fileReader);
            //fileInputStream = openFileInput(FILE_NAME);

            while ((buffer = bufferedReader.readLine()) != null) {
                stringBuilder.append(buffer);
            }
            /*InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((lineaTexto = bufferedReader.readLine())  != null)
            {
                stringBuilder.append(lineaTexto);
                Log.d("LeerArchivo: ", "Se queda en el while");
            }*/
            Log.d("LeerArchivo: ", stringBuilder.toString());
        }catch (Exception e)
        {
            Log.d("LeerArchivo: ", "Error leyendo archivo");
        }
        finally {
            if (fileReader != null)
            {
                try {
                    fileReader.close();
                   // obtenerCoordenadas(stringBuilder.toString());
                }
                catch (Exception e)
                {

                }

            }
        }
    }

    public void obtenerCoordenadas(String coordenadas)
    {
        int startInd, stopInd, aux1, aux2;
        startInd = coordenadas.indexOf("Start");
        stopInd = 0;
        aux1 = 0;
        aux2 = 0;

        while(stopInd != -1)
        {
            if (startInd < 0)
            {
                stopInd = -1;
            }
            else
            {
                stopInd = coordenadas.indexOf("End",startInd);
                coordenadasLista.add(coordenadas.substring(startInd+5,stopInd-1));
        }
        }

        for (String coordenada : coordenadasLista)
        {
            Log.d("CorrdenadasObtenidas: ", coordenada);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void SolicitarTrayectorias(String UserID){
        progressBar.setVisibility(View.VISIBLE);
        firebaseViewModel.ObtenerTrayectorias(UserID);

    }
    /*public void ObtenerListaVideos(ArrayList<String> VideosFirebase){
        List<ListaVideos> ListaVideos=new ArrayList<>();
        for(int i=0;i<VideosFirebase.size();i++){
            for (int j=0;j<VideosFirebase.get(i).length();j++){
                if(VideosFirebase.get(i).charAt(j)=='@')
                    videos.add(new ListaVideos(VideosFirebase.get(i).substring(0,j),VideosFirebase.get(i).substring(j+1,VideosFirebase.get(i).length()), R.drawable.icons8_video_100));
            }
        }
    }*/
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerLinkTrayectoriaSeleccionada(String UserID, String NombreTrayectoria){
        firebaseViewModel.ObtenerTrayectoriaSeleccionada(UserID,NombreTrayectoria);
    }
}