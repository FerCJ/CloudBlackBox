package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cjf.cloudblackbox.adaptador.EstadisticasAdaptador;
import com.cjf.cloudblackbox.pojo.Estadistica;

import java.util.ArrayList;

public class Estadisticas extends AppCompatActivity implements onOpcionListener{

    private ArrayList<Estadistica> estadisticas;
    private RecyclerView listaEstadisticas;
    String UserID;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserID=getIntent().getExtras().get("ID").toString();
        setContentView(R.layout.activity_estadisticas);

        estadisticas = new ArrayList<Estadistica>();

        estadisticas.add(new Estadistica(R.drawable.gasolinaicon,"Consumo de gasolina"));
        estadisticas.add(new Estadistica(R.drawable.velocidadicon,"Velocidad promedio"));
        estadisticas.add(new Estadistica(R.drawable.estadisticasicon,"Estado batería"));
        estadisticas.add(new Estadistica(R.drawable.rpmicon,"RPM Promedio"));
        estadisticas.add(new Estadistica(R.drawable.distanciaicon,"Distancia recorrida"));

        listaEstadisticas = (RecyclerView) findViewById(R.id.rvEstadistica);
        GridLayoutManager glm = new GridLayoutManager(this,2);
        listaEstadisticas.setLayoutManager(glm);
        EstadisticasAdaptador adaptador = new EstadisticasAdaptador(estadisticas,this);
        listaEstadisticas.setAdapter(adaptador);

        Button closeButton = (Button) findViewById(R.id.btnReturnEstadisticas);
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

    private static final String TAG = "MyActivity";

    @Override
    public void onOpcionClick(int position) {

        int posicion = position;
        Log.i(TAG, "Posicion para el switch: " + posicion);

        switch (posicion) {

            case 0:
                Intent intent = new Intent(this, Gasolina.class );
                intent.putExtra("ID",UserID);
                startActivity(intent);
                break;
           /* case 1:
                Log.i(TAG, "Entro a la posicion 1 ");
                Intent intent2 = new Intent(MenuPrincipal.this, VerVideos.class);
                startActivity(intent2);
                break;
            case 2:
                intent = new Intent(this, Trayectorias.class);
                startActivity(intent);
                break;*/
            case 3:
                intent = new Intent(this, RPM.class);
                intent.putExtra("ID",UserID);
                intent.putExtra("Tipo","Rpm");
                startActivity(intent);
                break;
           /* case 4:
                intent = new Intent(this, Estadisticas.class);
                startActivity(intent);
                break;*/
            default:
                break;
        }


    }

}