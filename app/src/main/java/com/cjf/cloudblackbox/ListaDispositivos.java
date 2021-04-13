package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ListaDispositivos extends AppCompatActivity {

    private ListView listaEmparejados, listaDisponibles;
    private ArrayAdapter<String> adaptadorDisponibles, adaptadorEmparejados;

    private BluetoothAdapter bluetoothAdapter;
    private View vista;
    private ProgressBar progressBar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos);
        context = this;

        inicializar();

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

        Button escanear = (Button) findViewById(R.id.btnEscanearBT);
        escanear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vista = v;
                progressBar.setVisibility(vista.VISIBLE);
                adaptadorDisponibles.clear();
                Toast.makeText(context, "Escaneo iniciado...",Toast.LENGTH_SHORT).show();

                if (bluetoothAdapter.isDiscovering())
                {
                    bluetoothAdapter.cancelDiscovery();
                }

                bluetoothAdapter.startDiscovery();
            }
        });


    }

    private BroadcastReceiver bluetoothListener = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    adaptadorDisponibles.add(device.getName() + "\n" + device.getAddress());
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                progressBar.setVisibility(vista.GONE);
                if (adaptadorDisponibles.getCount() == 0)
                {
                    Toast.makeText(context, "No se encontraron nuevos dispoitivos",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Selecciona el dispositivo",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void inicializar()
    {
        listaDisponibles = findViewById(R.id.lvDisponibles);
        listaEmparejados = findViewById(R.id.lvEmparejados);

        adaptadorDisponibles = new ArrayAdapter<String>(this, R.layout.lista_dispositivos_item);
        adaptadorEmparejados = new ArrayAdapter<String>(this, R.layout.lista_dispositivos_item);

        listaEmparejados.setAdapter(adaptadorEmparejados);
        listaDisponibles.setAdapter(adaptadorDisponibles);

        listaDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length()-17);

                Intent intent = new Intent();
                intent.putExtra("deviceAddress",address);
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        progressBar = (ProgressBar) findViewById(R.id.pbEscanear);
        Set<BluetoothDevice> emparejados = bluetoothAdapter.getBondedDevices();

        if (emparejados != null && emparejados.size()>0)
        {
            for (BluetoothDevice device : emparejados)
            {
                adaptadorEmparejados.add(device.getName() + '\n' + device.getAddress());
            }
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothListener,intentFilter);
        IntentFilter intentFilter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothListener,intentFilter2);

    }
}