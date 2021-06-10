package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cjf.cloudblackbox.adaptador.OpcionesPrincialAdaptador;
import com.cjf.cloudblackbox.pojo.OpcionesPrincipal;

import java.util.ArrayList;

public class MenuPrincipal extends AppCompatActivity implements onOpcionListener {
    private ArrayList<OpcionesPrincipal> opciones;
    private RecyclerView listaOpciones;
    private String userID;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        userID = getIntent().getExtras().get("ID").toString();


        opciones = new ArrayList<OpcionesPrincipal>();

        opciones.add(new OpcionesPrincipal("Seleccionar Modo",R.drawable.modoicon));
        opciones.add(new OpcionesPrincipal("Videos",R.drawable.videoicon));
        opciones.add(new OpcionesPrincipal("Trayectorias",R.drawable.trayectoriasicon));
        opciones.add(new OpcionesPrincipal("Ubicación",R.drawable.ubicacionicon));
        opciones.add(new OpcionesPrincipal("Estadisticas",R.drawable.estadisticasicon));
        opciones.add(new OpcionesPrincipal("Contacto",R.drawable.contactoicon));
        opciones.add(new OpcionesPrincipal("Más Opciones",R.drawable.icons8_opciones_para_ordenar_96));

        listaOpciones = (RecyclerView) findViewById(R.id.rvMenuPrincial);
        GridLayoutManager glm = new GridLayoutManager(this,2);
        listaOpciones.setLayoutManager(glm);
        OpcionesPrincialAdaptador adaptador = new OpcionesPrincialAdaptador(opciones, this);
        listaOpciones.setAdapter(adaptador);

    }

    private static final String TAG = "MyActivity";

    @Override
    public void onOpcionClick(int position) {

        int posicion = position;
        Log.i(TAG, "Posicion para el switch: " + posicion);

        switch (posicion) {

            case 0:
                Log.i(TAG, "Entro a la posicion 0 ");
                Intent intent = new Intent(MenuPrincipal.this, SeleccionarModo.class );
                startActivity(intent);
                break;
            case 1:
                Log.i(TAG, "Entro a la posicion 1 ");
                Intent intent2 = new Intent(MenuPrincipal.this, VerVideos.class);
                intent2.putExtra("ID",userID);
                startActivity(intent2);
                break;
            case 2:
                intent = new Intent(this, Trayectorias.class);
                intent.putExtra("ID",userID);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, Ubicacion.class);
                intent.putExtra("ID",userID);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, Estadisticas.class);
                intent.putExtra("ID",userID);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(this, Contacto.class);
                intent.putExtra("ID",userID);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(this, MasOpciones.class);
                intent.putExtra("ID",userID);
                startActivity(intent);
                break;
            default:
                break;
        }


    }

    public void bckPrincipal(View view) {
        finish();
    }
}