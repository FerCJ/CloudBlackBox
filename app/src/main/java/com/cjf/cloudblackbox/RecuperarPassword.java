package com.cjf.cloudblackbox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecuperarPassword extends AppCompatActivity implements View.OnClickListener {

    EditText Correo,NumeroTelefonico,ContraseñaNueva,ConfirmarContraseñaNueva;
    Button ValidarDatos,CambiarContraseña;
    FirebaseViewModel firebaseViewModel;
    String DocumentId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        progressBar=(ProgressBar) findViewById(R.id.progressBarRecuperar);
        Correo=(EditText) findViewById(R.id.emailRecuperar);
        NumeroTelefonico=(EditText) findViewById(R.id.txtTelefonoRecuperar);
        ContraseñaNueva=(EditText) findViewById(R.id.passNewPass);
        ConfirmarContraseñaNueva=(EditText) findViewById(R.id.passConfirmNew);

        ValidarDatos=(Button) findViewById(R.id.btnValidar);
        CambiarContraseña=(Button) findViewById(R.id.btnCambiarPass);

        ValidarDatos.setOnClickListener(this);
        CambiarContraseña.setOnClickListener(this);

        firebaseViewModel= ViewModelProviders.of(this).get(FirebaseViewModel.class);
        firebaseViewModel.getValidarUsuario().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String aBoolean) {
                if(aBoolean!=null){
                    progressBar.setVisibility(View.GONE);
                    Correo.setEnabled(false);
                    NumeroTelefonico.setEnabled(false);
                    ValidarDatos.setEnabled(false);
                    ContraseñaNueva.setEnabled(true);
                    ConfirmarContraseñaNueva.setEnabled(true);
                    CambiarContraseña.setEnabled(true);
                    DocumentId=aBoolean;
                    Toast.makeText(getBaseContext(),"Usuario encontrado",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getBaseContext(),"Usuario no encontrado",Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseViewModel.getContraseñaCambiada().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean!=null){
                    if(aBoolean==true){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(),"Contraseña cambiada con exito",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(),"Error al cambiar la contraseña",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnValidar){
            firebaseViewModel.ValidarUsuario(Correo.getText().toString(),NumeroTelefonico.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            if(ContraseñaNueva.getText().toString().equals(ConfirmarContraseñaNueva.getText().toString()) && ValidarContraseña(ContraseñaNueva.getText().toString()) == true) {
                firebaseViewModel.CambiarContraseña(ContraseñaNueva.getText().toString(), DocumentId);
                progressBar.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(this,"Las contraseñas deben coincidir y tener al menos una mayuscula,minuscula y tener lontigud minima de 5 y maxima 10", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean ValidarContraseña(String Contraseña){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]");

        Matcher mather = pattern.matcher(Contraseña);

        if(mather.find()==true && Contraseña.length()>5 && Contraseña.length()<12)
            return true;
        else
            return false;
    }


}
