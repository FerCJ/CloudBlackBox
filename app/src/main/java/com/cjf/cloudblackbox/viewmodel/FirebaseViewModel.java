package com.cjf.cloudblackbox.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.cjf.cloudblackbox.model.RepositorioAPP;

import java.util.ArrayList;

public class FirebaseViewModel extends AndroidViewModel {
    private RepositorioAPP repositorioAPP;
    private MutableLiveData<Boolean> Userregister;
    private MutableLiveData<String> Userlogin;
    private MutableLiveData<ArrayList<String>> Videos;
    private MutableLiveData<ArrayList<String>> Trayectorias;
    private MutableLiveData<Uri> VideoSeleccionado;
    private MutableLiveData<Uri> TrayectoriaSeleccionada;
    private MutableLiveData<String> ValidarUsuario;
    private  MutableLiveData<Boolean> ContraseñaCambiada;
    private MutableLiveData<ArrayList<String>> Ubicacion= new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> UbicacionNew=Ubicacion;

    public FirebaseViewModel(@NonNull Application application) {
        super(application);

        repositorioAPP=new RepositorioAPP(application);
        Userregister =repositorioAPP.getUserregister();
        Userlogin= repositorioAPP.getUserlogin();
        Videos= repositorioAPP.getVideos();
        VideoSeleccionado=repositorioAPP.getVideoSeleccionado();
        TrayectoriaSeleccionada = repositorioAPP.getTrayectoriaSeleccionada();
        ValidarUsuario=repositorioAPP.getValidarUsuario();
        ContraseñaCambiada=repositorioAPP.getContraseñaCambiada();
        Trayectorias = repositorioAPP.getTrayectorias();

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void registrarse(String Nombre, String Correo, String Contraseña,String Numero1,String Numero2,String Numero3){
        repositorioAPP.registrar(Nombre,Correo,Contraseña,Numero1,Numero2,Numero3);
    }

    public MutableLiveData<Boolean> getUserregister() {
        return Userregister;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void IniciarSesion(String Correo, String Contraseña,String Token){
        repositorioAPP.IniciarSesion(Correo,Contraseña,Token);
    }

    public MutableLiveData<String> getUserlogin() { return Userlogin; }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerVideos(String UserID){
        repositorioAPP.ObtenerVideos(UserID);
    }

    public MutableLiveData<ArrayList<String>> getVideos() { return Videos; }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerVideoSeleccionado(String UserID, String NombreVideo){repositorioAPP.ObtenerVideoSeleccionado(UserID,NombreVideo);}

    public MutableLiveData<Uri> getVideoSeleccionado(){return VideoSeleccionado;}

    //AGREGAR ESTA FUNCION
    //DEFINIR VARIABLE Ubicacion como viene hasta arriba
    //DEFINIR VARIABLE UbicacionNew e igualarla a la varibale Ubicacion como viene hasta arriba.
    public void getUbicacion(String UserID){ repositorioAPP.ObtenerUbicacion(UserID,Ubicacion); }

    //AGREGAR ESTO TAMBIEN PARA CAMBIAR LA CONTRASEÑA
    //LA VARIABLE ValidarUsuario es una variable del tipo MutableLiveData<String> y esta definida arriba
    //LA VARIABLE ContraseñaCambiada es del tipo MutableLiveData<Boolean> y esta definida arriba.
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ValidarUsuario(String Correo, String NumeroTelefonico){repositorioAPP.ValidarUsuario(Correo,NumeroTelefonico);}
    public MutableLiveData<String> getValidarUsuario(){return ValidarUsuario;}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void CambiarContraseña(String ContraseñaNueva, String IdDocumento){repositorioAPP.CambiarContraseña(ContraseñaNueva,IdDocumento);}
    public MutableLiveData<Boolean> getContraseñaCambiada(){return ContraseñaCambiada;}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerTrayectorias(String UserID){ repositorioAPP.ObtenerTrayectorias(UserID);
    }

    public MutableLiveData<ArrayList<String>> getTrayectorias() { return Trayectorias; }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void ObtenerTrayectoriaSeleccionada(String UserID, String NombreTrayectoria){repositorioAPP.ObtenerTrayectoriaSeleccionada(UserID,NombreTrayectoria);}

    public MutableLiveData<Uri> getTrayectoriaSeleccionada(){return TrayectoriaSeleccionada;}
}
