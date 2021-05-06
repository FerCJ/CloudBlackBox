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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Trayectorias extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView listaTrayectorias;
    private MapView mapa;
    private GoogleMap rutas;
    Polyline polyline1;

    private List<Trayectoria> trayectorias = new ArrayList<>();
    private FirebaseViewModel firebaseViewModel;
    private  String userID;
    private String NombreTrayectoriaSeleccionada;
    private ProgressBar progressBar;
    private static final String TAG2 = "VerTrayectoria";
    private String FILE_NAME = "";

    private int nTrayectorias = 0;
    //private  ArrayList<Coordenadas> coordenadasLista = new ArrayList<>();
    private List<LatLng> coordenadasLista = new ArrayList<>();
    private String linea;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trayectorias);

        userID = getIntent().getExtras().get("ID").toString();
        progressBar = (ProgressBar) findViewById((R.id.progressBarTrayectorias));
        listaTrayectorias = (RecyclerView) findViewById(R.id.rvListaTrayectorias);
        mapa = (MapView) findViewById(R.id.mvTrayectorias);


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

        firebaseViewModel.getTrayectoriaSeleccionada().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG2, "Si entra a reproducir el video" );
                Log.i(TAG2, "El path recibido es:  " + s );
                FILE_NAME = s;
                leeArchivo();

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
        rutas = mapa;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(19.4302681,-99.134059);
        float zoomLevel = 12.0f; //This goes up to 21
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

        polyline1 = rutas.addPolyline(new PolylineOptions()
                .clickable(true));


        if (coordenadasLista.size() > 0)
        {
            for (LatLng coordenada: coordenadasLista)
            {
                Polyline polyline1 = mapa.addPolyline(new PolylineOptions()
                        .add(coordenada));
            }
        }





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


    public void leeArchivo()
    {
        Log.d("LeerArchivo: ", "Entra al metodo");
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (FILE_NAME);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero

            while((linea=br.readLine())!=null)
                stringBuilder.append(linea).append("");
            System.out.println(linea);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {


            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr )
                {
                    fr.close();

                }
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }

        if(stringBuilder != null)
        {
            obtenerCoordenadas(stringBuilder.toString());
        }
        else
        {
            System.out.println("No se encontraron trayectorias en el archivo");
        }

    }

    public void obtenerCoordenadas(String trayectorias)
    {

        int startTrayecto, stopTrayecto,startCoor, stopCoor, aux1, aux2, aux3;
        startTrayecto = trayectorias.indexOf("#");

        aux1 = 0;
        String trayecto = "";
        System.out.println(trayectorias);
        aux1 = trayectorias.indexOf("#",startTrayecto+1);

        while(aux1 != -1)
        {
            coordenadasLista.clear();
            trayecto = trayectorias.substring(startTrayecto+1,aux1);

            startCoor = trayecto.indexOf("@");
            aux2 = 0;
            String coordenada = "";
            aux2 = trayecto.indexOf("@",startCoor+1);

            while(aux2 != -1)
            {
                coordenada = trayecto.substring(startCoor+1,aux2);

                aux3 = coordenada.indexOf("/");

                coordenadasLista.add(new LatLng(Double.parseDouble(coordenada.substring(0,aux3)),Double.parseDouble(coordenada.substring(aux3+1,coordenada.length()))));
                startCoor = trayecto.indexOf("@",aux2-1);
                aux2 = trayecto.indexOf("@",startCoor+1);
            }
            float zoomlevel = 16.0f;
            LatLng sydney = coordenadasLista.get(0);
            rutas.addMarker(new MarkerOptions().position(sydney).title("Inicio"));
            rutas.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));



           /* Polyline polyline2 = rutas.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(19.457271,-99.20797),
                            new LatLng(19.457547,-99.207848)));*/
            polyline1.setPoints(coordenadasLista);
            sydney = coordenadasLista.get(coordenadasLista.size()-1);
            rutas.addMarker(new MarkerOptions().position(sydney).title("Fin"));
            System.out.println("");
            System.out.println("Fin trayecto.........");
            System.out.println("");
            startTrayecto = trayectorias.indexOf("#",aux1-1);
            aux1 = trayectorias.indexOf("#",startTrayecto+1);
        }

    }

    public void  dibujarTrayectorias()
    {

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