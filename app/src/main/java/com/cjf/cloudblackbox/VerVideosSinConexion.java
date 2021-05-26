package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class VerVideosSinConexion extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;
    BluetoothService mmBluetoothService;
    static TextView RecCPU,PruevaVideo;
    private RecyclerView recyclerViewVsc;
    private VerVideosAdaptador AdaptadorVideos;
    static List<ListaVideos> Videos=new ArrayList();
    static VideoView Videoview;
    static String VideoSeleccionado;


/*String MacRasp="DC:A6:32:A4:41:AD";
    String NameRasp="RaspBerryNava";
    Button EncenderBlue,ConectarCPU,EnviarComando;
    TextInputEditText Comando;*/

    private Handler mHandler= new MyHandler(this);

    private class MyHandler extends Handler{
        private WeakReference<VerVideosSinConexion> mActivity;
        public MyHandler(VerVideosSinConexion activity) {
            mActivity = new WeakReference<VerVideosSinConexion>(activity);
            //context=activity.getApplicationContext();
        }
        @Override
        public void handleMessage(Message msg) {
            byte[] buffer = (byte[]) msg.obj;
            switch (msg.what){
                case 1:
                    String MensajeCPU=new String(buffer,0,msg.arg1);
                    CrearListaVideos(MensajeCPU);
                    break;
                case 2:
                    ReproducirVideo();
                    break;
            }
        }
    }

    int REQUEST_ENABLE_BL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_videos_sin_conexion);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recyclerViewVsc=(RecyclerView)findViewById(R.id.rvListaVsc);
        Videoview=(VideoView)findViewById(R.id.vvVideoVsc);

        EncenderBlue();
        ObtenerDatosRaspBerry();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Entro al if");


            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                    Toast.makeText(this, "Necesitamos agregar permisos de lectura", Toast.LENGTH_SHORT).show();

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);


            }

        }
        UUID uuid=UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

        mmBluetoothService=new BluetoothService(this,mmDevice,uuid, mHandler);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mmBluetoothService.write("Videos");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerViewVsc.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorVideos= new VerVideosAdaptador(Videos);
        AdaptadorVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSeleccionado=Videos.get(recyclerViewVsc.getChildAdapterPosition(v)).getFecha();
                mmBluetoothService.write(VideoSeleccionado);
            }
        });
        recyclerViewVsc.setAdapter(AdaptadorVideos);

    }

    //EnciendeBluetooth
    private void EncenderBlue() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent intentBlEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentBlEnable, REQUEST_ENABLE_BL);
        } else {
            Toast.makeText(this, "El Bluetooth ya esta encendido", Toast.LENGTH_LONG).show();
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

    private static void CrearListaVideos(String lisVideos){
        int auxiliar=-1;


        for(int i=0;i< lisVideos.length();i++){
            if (lisVideos.charAt(i)=='@'){
                Videos.add(new ListaVideos(lisVideos.substring(auxiliar+1,i),"",R.drawable.icons8_video_100));
                auxiliar=i;
            }
            else if(i==lisVideos.length()-1){
                Videos.add(new ListaVideos(lisVideos.substring(auxiliar+1,i+1),"",R.drawable.icons8_video_100));
            }
        }

    }

    private void ReproducirVideo(){//Context context){

        String Path= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"bluetooth"+File.separator+VideoSeleccionado;
        MediaController mc = new MediaController(this);
        Videoview.setMediaController(mc);
        Videoview.setVideoURI(Uri.parse(Path));
        Videoview.start();

    }
}