package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.UUID;

public class MasOpciones extends AppCompatActivity {

    private Context context;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;
    BluetoothService mmBluetoothService;

    private EditText etNombreWF;
    private EditText etPassWF;
    private EditText etmlMensaje;
    private Button btnAtras;
    private Button btnConectarBT;
    private Button btnEnviar;
    private Button btnSolicitar;
    private String UserID;

    private String destinatario = "cloudblackboxsystem@gmail.com";




    private Handler mHandler= new MasOpciones.MyHandler(this);

    private class MyHandler extends Handler{
        private WeakReference<MasOpciones> mActivity;
        public MyHandler(MasOpciones activity) {
            mActivity = new WeakReference<MasOpciones>(activity);
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
                case 6:
                    String Mensaje6CPU=new String(buffer,0,msg.arg1);
                    Toast.makeText(getApplication(), Mensaje6CPU, Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    String Mensaje7CPU=new String(buffer,0,msg.arg1);
                    Toast.makeText(getApplication(), Mensaje7CPU, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    int REQUEST_ENABLE_BL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_opciones);
        context = this;
        UserID=getIntent().getExtras().get("ID").toString();

        etNombreWF = (EditText) findViewById(R.id.etNombreWF);
        etPassWF = (EditText) findViewById(R.id.etPwdWF);
        btnAtras = (Button) findViewById(R.id.btnReturnOpc);
        btnConectarBT = (Button) findViewById(R.id.btnConectarBTOpc);
        btnEnviar = (Button) findViewById(R.id.btnEnviarConfWF);
        etmlMensaje = (EditText) findViewById(R.id.etmlDatosMP);
        btnSolicitar = (Button) findViewById(R.id.btnSolicitarFoto);

        EncenderBlue();

        btnAtras.setOnClickListener(new View.OnClickListener() {

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

        btnConectarBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ObtenerDatosRaspBerry();
                UUID uuid=UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
                mmBluetoothService=new BluetoothService(context,mmDevice,uuid, mHandler);

                if (mmBluetoothService != null)
                {
                    etNombreWF.setEnabled(true);
                    etPassWF.setEnabled(true);
                    btnEnviar.setEnabled(true);
                }

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mmBluetoothService != null)
                {
                    String Wifi="wifi@"+etNombreWF.getText().toString()+"@"+etPassWF.getText().toString();
                    mmBluetoothService.write(Wifi);

                }
                else
                {
                    Toast.makeText(MasOpciones.this, "Primero necesita conectarse a la raspberry", Toast.LENGTH_SHORT) .show();
                }

            }
        });

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etmlMensaje.getText().toString().matches(""))
                {
                    Toast.makeText(MasOpciones.this, "Seleccione una aplicación de correo electrónico", Toast.LENGTH_SHORT) .show();
                    String enviarcorreo = destinatario;
                    String enviarasunto = "Solicitud de fotogramas";
                    String enviarmensaje = etmlMensaje.getText().toString() + " Usuario: " + UserID;

                    // Defino mi Intent y hago uso del objeto ACTION_SEND
                    Intent intent = new Intent(Intent.ACTION_SEND);

                    // Defino los Strings Email, Asunto y Mensaje con la función putExtra
                    intent.putExtra(Intent.EXTRA_EMAIL,
                            new String[] { enviarcorreo });
                    intent.putExtra(Intent.EXTRA_SUBJECT, enviarasunto);
                    intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);

                    // Establezco el tipo de Intent
                    intent.setType("message/rfc822");

                    // Lanzo el selector de cliente de Correo
                    startActivity(
                            Intent
                                    .createChooser(intent,
                                            "Elije un cliente de Correo:"));

                }
                else
                {
                    Toast.makeText(MasOpciones.this, "Ingrese el mensaje del correo", Toast.LENGTH_SHORT) .show();
                }
            }
        });
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
