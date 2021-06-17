package com.cjf.cloudblackbox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cjf.cloudblackbox.adaptador.TrayectoriasAdaptador;
import com.cjf.cloudblackbox.pojo.Trayectoria;
import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Trayectorias extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView listaTrayectorias;
    private MapView mapa;
    private GoogleMap rutas;
    private Polyline polyline1;
    private Marker markerI, markerF;
    private List<Polyline> polylines = new ArrayList<>();

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

        //polyline1 = rutas.addPolyline(new PolylineOptions()
          //      .clickable(true));
        for (int i = 0; i < 10; i++)
        {
            polylines.add(rutas.addPolyline(new PolylineOptions().clickable(true)));
        }

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
            archivo = new File (FILE_NAME);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

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

        if (markerI != null && markerF !=null)
        {
            markerI.remove();
            markerF.remove();
        }
        int startTrayecto, stopTrayecto,startCoor, stopCoor, aux1, aux2, aux3;
        startTrayecto = trayectorias.indexOf("#");

        aux1 = 0;
        String trayecto = "";
        System.out.println(trayectorias);
        aux1 = trayectorias.indexOf("#",startTrayecto+1);

        int n = 0;

        while(aux1 != -1 && startTrayecto != -1)
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
            markerI = rutas.addMarker(new MarkerOptions().position(sydney).title("Inicio"));
            rutas.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));

            polylines.get(n).setPoints(coordenadasLista);
            sydney = coordenadasLista.get(coordenadasLista.size()-1);
            markerF = rutas.addMarker(new MarkerOptions().position(sydney).title("Fin"));
            System.out.println("");
            System.out.println("Fin trayecto.........");
            System.out.println("");
            startTrayecto = trayectorias.indexOf("#",aux1+1);
            aux1 = trayectorias.indexOf("#",startTrayecto+1);
            n += 1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void SolicitarTrayectorias(String UserID){
        progressBar.setVisibility(View.VISIBLE);
        firebaseViewModel.ObtenerTrayectorias(UserID);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerLinkTrayectoriaSeleccionada(String UserID, String NombreTrayectoria){
        firebaseViewModel.ObtenerTrayectoriaSeleccionada(UserID,NombreTrayectoria);
    }
}