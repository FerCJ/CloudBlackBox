package com.cjf.cloudblackbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class SeleccionarModo extends AppCompatActivity implements onOpcionListener {

    private ArrayList<Modos> modos;
    private RecyclerView listaModos;

    // Declarar las variables que se utilizaran
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;
    BluetoothService mmBluetoothService = null;

    //"Manejador" que ayuda a controlar todos los mensajes enviados por el BT
    private Handler mHandler= new MyHandler(this);

    private class MyHandler extends Handler{
        //crea un contexto para la clase de la cual se recibiran los mensajes
        private WeakReference<SeleccionarModo> mActivity;
        //Constructo de la clase, obtiene como parametro la actividad
        public MyHandler(SeleccionarModo activity) {
            mActivity = new WeakReference<SeleccionarModo>(activity);
            //context=activity.getApplicationContext();
        }
        //SE soobreescribe el metodo para manejar los mensajes, que hacer en caso de que llegue un mensaje nuevo
        @Override
        public void handleMessage(Message msg) {

            byte[] buffer = (byte[]) msg.obj;
            switch (msg.what){
                case 1:
                    String MensajeCPU=new String(buffer,0,msg.arg1);
                    Toast.makeText(SeleccionarModo.this, "El Bluetooth ya esta encendido", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    int REQUEST_ENABLE_BL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_modo);
        EncenderBlue();

        modos = new ArrayList<Modos>();
        modos.add(new Modos("Modo Trayecto",R.drawable.trayectoicon));
        modos.add(new Modos("Modo Parking",R.drawable.parkingicon));

        listaModos = (RecyclerView) findViewById(R.id.rvModo);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaModos.setLayoutManager(llm);
        SeleccionarModoAdaptador adaptador = new SeleccionarModoAdaptador(modos,this);
        listaModos.setAdapter(adaptador);

        Button closeButton = (Button) findViewById(R.id.btnReturn);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });

        Button bluetooth = (Button) findViewById(R.id.btnConectarBT);

        bluetooth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Metodo para confirmar que la app ya esta emparejada con la Raspberry
                ObtenerDatosRaspBerry();
                // Verifica los persimos si la version de android es mayor a la loolilop y si no los tiene los pide
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Entro al if");


                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                            Toast.makeText(getBaseContext(), "Necesitamos agregar permisos de lectura", Toast.LENGTH_SHORT).show();

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);


                    }
                }

                //STring que indica el protocolo de comunicacion y el tipo dispositivo a conectar
                UUID uuid=UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

                //SE intenta establecer la conexion con la Raspberry
                mmBluetoothService=new BluetoothService(SeleccionarModo.this,mmDevice,uuid, mHandler);
                Toast.makeText(SeleccionarModo.this, "Conexion exitosa BT", Toast.LENGTH_SHORT) .show();



            }
        });


    }
    //Verifica si el BT esta encendido, sino lo enciende
    private void EncenderBlue() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent intentBlEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBlEnable, REQUEST_ENABLE_BL);
        } else {
            Toast.makeText(this, "El Bluetooth ya esta encendido", Toast.LENGTH_LONG).show();
        }
    }

    //Metodo que verifica todos los dispositivos emparejados en el smartphone y busca el que llama Raspberry
    private void ObtenerDatosRaspBerry() {

        //Obtiene la lista de todos los dispositivos vinculados
        Set<BluetoothDevice> DispositivosVinculados = bluetoothAdapter.getBondedDevices();
        //obtener la direccion MAC de la raspberry
        if (DispositivosVinculados.size() > 0) {
            for (BluetoothDevice device : DispositivosVinculados) {
                if (device.getName().equals("raspberrypi")) {
                    mmDevice = device;
                }
            }
        }
    }

    @Override
    public void onOpcionClick(int position) {
        int posicion = position;
        Toast.makeText(this, "Se selecciono un elemnto", Toast.LENGTH_SHORT) .show();
        Log.i("MsgBT", "Posicion para el switch: " + posicion);
        switch (posicion) {

            case 0:
                Log.i("MsgBT", "Entro a la posicion 0 ");
                if (mmBluetoothService != null)
                {
                    mmBluetoothService.write("Trayecto");
                }
                else
                {
                    Toast.makeText(this, "No hay conexion BT", Toast.LENGTH_SHORT) .show();
                }
                break;
            case 1:
                Log.i("MsgBT", "Entro a la posicion 1 ");
                if (mmBluetoothService != null)
                {
                    mmBluetoothService.write("Parking");
                }
                else
                {
                    Toast.makeText(this, "No hay conexion BT", Toast.LENGTH_SHORT) .show();
                }
                break;
            default:
                break;
        }
    }



}