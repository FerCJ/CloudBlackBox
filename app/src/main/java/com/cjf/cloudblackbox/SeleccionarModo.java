package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cjf.cloudblackbox.adaptador.SeleccionarModoAdaptador;
import com.cjf.cloudblackbox.pojo.Modos;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class SeleccionarModo extends AppCompatActivity implements onOpcionListener {

    private ArrayList<Modos> modos;
    private RecyclerView listaModos;
    private Context context;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;
    BluetoothService mmBluetoothService;


    private Handler mHandler= new MyHandler(this);

    private class MyHandler extends Handler{
        private WeakReference<SeleccionarModo> mActivity;
        public MyHandler(SeleccionarModo activity) {
            mActivity = new WeakReference<SeleccionarModo>(activity);
            //context=activity.getApplicationContext();
        }
        @Override
        public void handleMessage(Message msg) {
            byte[] buffer = (byte[]) msg.obj;
            switch (msg.what){
                case 1:
                    String Estadodeconexion=new String(buffer,0,msg.arg1);
                    if(Estadodeconexion.equals("Conexion creada")) {
                        Toast.makeText(getApplication(), Estadodeconexion, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplication(), Estadodeconexion, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    String MensajeCPU=new String(buffer,0,msg.arg1);
                    Toast.makeText(getApplication(), MensajeCPU, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    int REQUEST_ENABLE_BL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_modo);
        context = this;

        modos = new ArrayList<Modos>();
        modos.add(new Modos("Modo Trayecto",R.drawable.trayectoicon));
        modos.add(new Modos("Modo Parking",R.drawable.parkingicon));

        listaModos = (RecyclerView) findViewById(R.id.rvModo);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaModos.setLayoutManager(llm);
        SeleccionarModoAdaptador adaptador = new SeleccionarModoAdaptador(modos,this);
        listaModos.setAdapter(adaptador);

        EncenderBlue();


        Button closeButton = (Button) findViewById(R.id.btnReturn);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                try {
                    if (mmBluetoothService != null)
                    {
                        mmBluetoothService.cerrar();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        Button bluetooth = (Button) findViewById(R.id.btnConectarBT);

        bluetooth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ObtenerDatosRaspBerry();
                UUID uuid=UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

                mmBluetoothService=new BluetoothService(context,mmDevice,uuid, mHandler);

            }
        });
    }

    @Override
    public void onOpcionClick(int position) {
        int posicion = position;

        switch (posicion) {

            case 0:
                if (mmBluetoothService != null)
                {
                    mmBluetoothService.write("Trayecto");
                }
                else
                {
                    Toast.makeText(this, "Primero necesita conectarse a la raspberry", Toast.LENGTH_SHORT) .show();
                }
                break;
            case 1:
                if (mmBluetoothService != null)
                {
                    mmBluetoothService.write("Parking");
                }
                else
                {
                    Toast.makeText(this, "Primero necesita conectarse a la raspberry", Toast.LENGTH_SHORT) .show();
                }
                break;
            default:
                break;
        }
    }

    private void EncenderBlue() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent intentBlEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBlEnable, REQUEST_ENABLE_BL);
        } else {
            //Toast.makeText(this, "El Bluetooth ya esta encendido", Toast.LENGTH_LONG).show();
        }
    }

    private void ObtenerDatosRaspBerry() {
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



}