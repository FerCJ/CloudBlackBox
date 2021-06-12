package com.cjf.cloudblackbox.model;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepositorioAPP {

    private Application application;
    private MutableLiveData<Boolean> Userregister;
    private MutableLiveData<String> Userlogin;
    private MutableLiveData<ArrayList<String>> Videos;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private MutableLiveData<Uri> VideoSeleccionado;
    private MutableLiveData<String> TrayectoriaSeleccionada;
    private MutableLiveData<String> ValidarUsuario;
    private MutableLiveData<Boolean> ContraseñaCambiada;
    private MutableLiveData<ArrayList<String>> Trayectorias;
    private MutableLiveData<ArrayList<String>> ValEstadistica;
    private ArrayList<String> ValoresEsta= new ArrayList<>();
    int contador=0;
    private static final String TAG2 = "Repositorio";
    private ArrayList<String> UbicacionReal=new ArrayList<>();
    private MutableLiveData<ArrayList<String>> ubicacionN= new MutableLiveData<>();

   private Map<String, Object> user = new HashMap<>();
   private Map<String,Object> numerosTel= new HashMap<>();
   private ArrayList<String> ListVideos= new ArrayList<>();
   private ArrayList<String> ListaTrayectorias = new ArrayList<>();
   private String nombreVideo,IDuser,IDdocument,nombreTrayectoria;
   private File localfile;

    public RepositorioAPP(Application application){
        this.application=application;
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        Userregister=new MutableLiveData<>();
        Userlogin=new MutableLiveData<>();
        Videos=new MutableLiveData<>();
        VideoSeleccionado=new MutableLiveData<>();
        TrayectoriaSeleccionada=new MutableLiveData<String>();
        ValidarUsuario=new MutableLiveData<>();
        ContraseñaCambiada= new MutableLiveData<>();
        Trayectorias = new MutableLiveData<>();
        ValEstadistica = new MutableLiveData<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void registrar(String Nombre, String Correo, String Contraseña,String Numero1,String Numero2,String Numero3){


        user.put("Id",Numero1);
        user.put("Nombre", Nombre);
        user.put("Correo", Correo);
        user.put("Contraseña", Contraseña);
        user.put("Token","");


        numerosTel.put("UserId",Numero1);
        numerosTel.put("Telefono 1",Numero1);
        numerosTel.put("Telefono 2",Numero2);
        numerosTel.put("Telefono 3",Numero3);


        firebaseFirestore.collection("Usuarios").whereEqualTo("Id",Numero1).whereEqualTo("Correo",Correo).get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty()) {
                    firebaseFirestore.collection("Usuarios").add(user).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                firebaseFirestore.collection("Numeros Telefonicos").add(numerosTel).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Userregister.postValue(true);
                                        }
                                        else{
                                            Toast.makeText(application, "Fallo el registro de numeros telefonicos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(application, "Fallo el registro del usuario", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Userregister.postValue(false);
                }
            }

        });

    }

    public MutableLiveData<Boolean> getUserregister() {
        return Userregister;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void IniciarSesion(String Correo, String Contraseña, final String Token){

        firebaseFirestore.collection("Usuarios")
                .whereEqualTo("Correo",Correo)
                .whereEqualTo("Contraseña",Contraseña)
                .get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            IDuser=document.get("Id").toString();
                            IDdocument=document.getId().toString();
                        }

                        firebaseFirestore.collection("Usuarios").document(IDdocument).update("Token",Token).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Userlogin.postValue(IDuser);
                                }
                            }
                        });
                    }
                    else
                        Userlogin.postValue("");
                }
                else{
                    Toast.makeText(application, "Fallo el inicio de sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public  MutableLiveData<String> getUserlogin(){
        return Userlogin;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerVideos(String UserID){
        firebaseFirestore.collection("Videos").whereEqualTo("UserId",UserID).get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult())
                        ListVideos.add(document.get("Hora").toString()+"@"+document.get("Fecha").toString());

                    Videos.postValue(ListVideos);
                }
                else{
                    Videos.postValue(null);
                }
            }
        });
    }

    public MutableLiveData<ArrayList<String>> getVideos(){return Videos;}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerVideoSeleccionado(String UserID, String NombreVideo){
        nombreVideo = NombreVideo;
        Log.d("DESCARGARVIDEO", "LINK " + UserID);
        Log.d("DESCARGARVIDEO", "LINK " + NombreVideo);
        firebaseFirestore.collection("Videos")
                .whereEqualTo("UserId",UserID)
                .whereEqualTo("Hora",NombreVideo)
                .get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String LinkVideo="";
                if(task.isSuccessful()){
                    Log.d("DESCARGARVIDEO", task.getResult().toString());
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        Log.d("DESCARGARVIDEO", document.get("Link").toString());
                        nombreVideo=document.get("Nombre").toString();
                        LinkVideo = document.get("Link").toString();
                    }

                    Log.d("DESCARGARVIDEO", "LINK " + LinkVideo);
                    Log.d("DESCARGARVIDEO", "NOMBREVIDEO " + nombreVideo);
                    StorageReference VidReference= firebaseStorage.getReferenceFromUrl(LinkVideo);
                    try {
                        localfile= File.createTempFile(nombreVideo,"mp4");
                        VidReference.getFile(localfile).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()) {
                                    VideoSeleccionado.postValue(Uri.fromFile(localfile));
                                }
                                else
                                    VideoSeleccionado.postValue(null);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    VideoSeleccionado.postValue(null);
                }

            }
        });


    }

    public MutableLiveData<Uri> getVideoSeleccionado() { return VideoSeleccionado; }

    //AGREGAR ESTO INICIO
    public void ObtenerUbicacion(String UserID, final MutableLiveData<ArrayList<String>> Ubicacion){

        DocumentReference docRef= firebaseFirestore.collection("Ubicacion").document(UserID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<String> UbicacionReal=new ArrayList<>();

                if (error != null) {
                    Log.w("Ubicacion", "Listen failed.", error);
                    return;
                }

                if (value != null && value.exists()) {
                    Log.d("Ubicacion", "Current data: " + value.getData());
                    UbicacionReal.add(value.get("Latitud").toString());
                    UbicacionReal.add(value.get("Longitud").toString());
                    /// Ubicacion.postValue(null);
                    Ubicacion.postValue(UbicacionReal);

                } else {
                    Log.d("Ubicacion", "Current data: null");
                }
            }
        });

    }
    //FINAL

    //ESTA ES LA LOGICA PARA EL CAMBIO DE CONTRASEÑA, IGUAL SE DEBE AGREGAR INICIO
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ValidarUsuario(String Correo, String NumeroTelefonico){
        firebaseFirestore.collection("Usuarios")
                .whereEqualTo("Correo",Correo)
                .whereEqualTo("Id",NumeroTelefonico)
                .get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot document : task.getResult())
                            ValidarUsuario.postValue(document.getId().toString());
                    }else{
                        ValidarUsuario.postValue(null);
                    }
                }
            }
        });

    }

    public MutableLiveData<String> getValidarUsuario(){return ValidarUsuario;}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void CambiarContraseña(String ContraseñaNueva, String IdDocument){
        firebaseFirestore.collection("Usuarios")
                .document(IdDocument)
                .update("Contraseña",ContraseñaNueva)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ContraseñaCambiada.postValue(true);
                        }
                    }
                });
    }

    public MutableLiveData<Boolean> getContraseñaCambiada(){return ContraseñaCambiada; }
    //FINAL


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerTrayectorias(String UserID){
        firebaseFirestore.collection("Trayectorias").whereEqualTo("UserId",UserID).get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult())
                        ListaTrayectorias.add(document.get("Fecha").toString());

                    Trayectorias.postValue(ListaTrayectorias);
                }
                else{
                    Trayectorias.postValue(null);
                }
            }
        });
    }

    public MutableLiveData<ArrayList<String>> getTrayectorias(){ return Trayectorias;}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerTrayectoriaSeleccionada(String UserID, String Fecha) {
        nombreTrayectoria = Fecha;

        firebaseFirestore.collection("Trayectorias")
                .whereEqualTo("UserId", UserID)
                .whereEqualTo("Fecha", Fecha)
                .get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String LinkTrayectoria = "";
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                        LinkTrayectoria = document.get("Link").toString();


                    StorageReference TrayectReference = firebaseStorage.getReferenceFromUrl(LinkTrayectoria);
                    try {
                        localfile = File.createTempFile(nombreTrayectoria, ".txt");

                        Log.d("Nombre archivo",localfile.getName());
                        Log.d("Ruta absoluta",localfile.getAbsolutePath());
                        Log.d("Ruta",localfile.getPath());
                        TrayectReference.getFile(localfile).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    TrayectoriaSeleccionada.postValue(localfile.getPath());
                                } else
                                    TrayectoriaSeleccionada.postValue(null);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    TrayectoriaSeleccionada.postValue(null);
                }

            }
        });
    }

    public MutableLiveData<String> getTrayectoriaSeleccionada() { return TrayectoriaSeleccionada; }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerValEstadistica(final String UserID,final String TEstadistica, final ArrayList<String> Fechas) {

            firebaseFirestore.collection("Estadisticas")
                    .get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.get("UserId").toString().equals(UserID) && document.get("Tipo").toString().equals(TEstadistica) ){
                                if(Fechas.contains(document.get("Fecha").toString())) {
                                    ValoresEsta.add(document.get("Vpromedio").toString() + "@" + document.get("Fecha").toString());
                                }
                            }
                        }
                        ValEstadistica.postValue(ValoresEsta);

                    }
                }
            });

    }

    public MutableLiveData<ArrayList<String>> getValEstadistica() { return ValEstadistica; }
}

