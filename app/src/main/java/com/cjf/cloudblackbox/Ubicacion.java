package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Ubicacion extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapa;
    private GoogleMap mMap;
    private FirebaseViewModel firebaseViewModel;
    Marker marker;
    private float Latitud;
    private float Longitud;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        Button closeButton = (Button) findViewById(R.id.btnReturnUbicacion);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });

        mapa = (MapView) findViewById(R.id.mvUbicacion);
        mapa.onCreate(savedInstanceState);
        mapa.getMapAsync(this);

        //AGREGAR ESTO INICIO
        firebaseViewModel= ViewModelProviders.of(this).get(FirebaseViewModel.class);

        UserID=getIntent().getExtras().get("ID").toString();
        firebaseViewModel.getUbicacion(UserID);
        firebaseViewModel.UbicacionNew.observe(this, new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {
                        Log.d("MAP", "PROBANDO VARIABLE " + firebaseViewModel.UbicacionNew.getValue());
                        Latitud=Float.parseFloat(strings.get(strings.size()-2));
                        Longitud=Float.parseFloat(strings.get(strings.size()-1));



                        Log.d("Map","cambio Latitud"+Latitud);
                        Log.d("Map","cambio Longitud"+Longitud);

                        if (marker !=null)
                            marker.remove();

                        LatLng newLocation=new LatLng(Latitud,Longitud);
                        marker=mMap.addMarker(new MarkerOptions().position(newLocation).title("hola"));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation,15));
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap mapa) {

       mMap = mapa;

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
}