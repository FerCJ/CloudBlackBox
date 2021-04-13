package com.cjf.cloudblackbox.model;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    private MutableLiveData<String> ValidarUsuario;
    private MutableLiveData<Boolean> ContraseñaCambiada;
    private static final String TAG2 = "Repositorio";
    private ArrayList<String> UbicacionReal=new ArrayList<>();
    private MutableLiveData<ArrayList<String>> ubicacionN= new MutableLiveData<>();

   private Map<String, Object> user = new HashMap<>();
   private Map<String,Object> numerosTel= new HashMap<>();
   private ArrayList<String> ListVideos= new ArrayList<>();
   private String nombreVideo;
   private File localfile;

    public RepositorioAPP(Application application){
        this.application=application;
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        Userregister=new MutableLiveData<>();
        Userlogin=new MutableLiveData<>();
        Videos=new MutableLiveData<>();
        VideoSeleccionado=new MutableLiveData<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void registrar(String Nombre, String Correo, String Contraseña,String Numero1,String Numero2,String Numero3){


        user.put("Id",Numero1);
        user.put("Nombre", Nombre);
        user.put("Correo", Correo);
        user.put("Contraseña", Contraseña);


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
    public void IniciarSesion(String Correo, String Contraseña){
        firebaseFirestore.collection("Usuarios")
                .whereEqualTo("Correo",Correo)
                .whereEqualTo("Contraseña",Contraseña)
                .get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult())
                            Userlogin.postValue(document.get("Id").toString());
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
                        ListVideos.add(document.get("Nombre").toString()+"@"+document.get("Fecha").toString());

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

        firebaseFirestore.collection("Videos")
                .whereEqualTo("UserId",UserID)
                .whereEqualTo("Nombre",NombreVideo)
                .get().addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String LinkVideo="";
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult())
                        LinkVideo=document.get("Link").toString();



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
    public void ObtenerUbicacion(String UserID, MutableLiveData<ArrayList<String>> Ubicacion){
        ubicacionN = Ubicacion;



        DocumentReference docRef= firebaseFirestore.collection("Ubicacion").document(UserID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Ubicacion", "Listen failed.", error);
                    return;
                }

                if (value != null && value.exists()) {
                    Log.d("Ubicacion", "Current data: " + value.getData());
                    UbicacionReal.add(value.get("Latitud").toString());
                    UbicacionReal.add(value.get("Longitud").toString());
                    /// Ubicacion.postValue(null);
                    ubicacionN.postValue(UbicacionReal);

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
}
