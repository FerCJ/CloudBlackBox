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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SeleccionarModo extends AppCompatActivity {

    private ArrayList<Modos> modos;
    private RecyclerView listaModos;
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private BTUtils btUtils;

    private final int LOCATION_PERMISSION_REQUEST = 101;
    private final int SELECT_DEVICE = 102;

    public static final int MESSAGE_STATE_CHANGED = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_TOAST = 4;

    public static final String DEVICE_NAME = "deviceName";
    public static final String TOAST = "toast";
    private String connectedDevice;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            switch (message.what)
            {
                case MESSAGE_STATE_CHANGED:
                    switch (message.arg1)
                    {
                        case BTUtils.STATE_NONE:
                            btUtils.setState(0);
                            break;
                        case BTUtils.STATE_LISTEN:
                            btUtils.setState(1);
                            break;
                        case BTUtils.STATE_CONNECTING:
                            btUtils.setState(2);
                            break;
                        case BTUtils.STATE_CONNECTED:
                            btUtils.setState(3);
                             break;
                        default:
                            break;
                    }
                    break;

                case MESSAGE_READ:
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_DEVICE_NAME:
                    connectedDevice = message.getData().getString(DEVICE_NAME);
                    Toast.makeText(context, connectedDevice,Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context, message.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            return false;
        }
    });

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
        SeleccionarModoAdaptador adaptador = new SeleccionarModoAdaptador(modos);
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

        inicialziarBT();

        btUtils = new BTUtils(context, handler);

        Button bluetooth = (Button) findViewById(R.id.btnConectarBT);
        bluetooth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               encenderBT();
               revisarPermisos();

            }
        });

        btUtils = new BTUtils(context,handler);
    }

    private void inicialziarBT()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null)
        {
            /*Snackbar.make(view, "Click detectado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
            Toast.makeText(context, "Bluetooth no encontrado",Toast.LENGTH_SHORT).show();
        }
    }

    private  void  encenderBT()
    {
        if (bluetoothAdapter.isEnabled())
        {
            Toast.makeText(context, "Bluetooth ya esta encendido",Toast.LENGTH_SHORT).show();
        }
        else
        {
            bluetoothAdapter.enable();
        }

        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
            startActivity(discoveryIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btUtils != null)
        {
            btUtils.stop();
        }
    }

    private void  revisarPermisos()
    {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);

        }
        else
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 3000);
            Intent intent = new Intent(this,ListaDispositivos.class);
            startActivityForResult(intent,SELECT_DEVICE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == SELECT_DEVICE &&  resultCode == RESULT_OK)
    {
        String address = data.getStringExtra("deviceAddress");
        btUtils.connect2(bluetoothAdapter.getRemoteDevice(address));
    }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("Otorgar los permisos es requerido")
                        .setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                revisarPermisos();
                            }
                        })
                        .setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SeleccionarModo.this.finish();
                            }
                        })
                        .create();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}