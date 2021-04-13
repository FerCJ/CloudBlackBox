package com.cjf.cloudblackbox;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    EditText NombreE,CorreoE,ContraseñaE,Numero1E,Numero2E,Numero3E,ConfirmarContra;
    Button BRegistrarse;
    ProgressBar progressBar;
    private FirebaseViewModel firebaseViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        NombreE=(EditText)findViewById(R.id.registroNombre);
        CorreoE=(EditText)findViewById(R.id.emailRegistro);
        ContraseñaE=(EditText) findViewById(R.id.registroPass);
        Numero1E=(EditText) findViewById(R.id.registroTel1);
        Numero2E=(EditText) findViewById(R.id.registroTel2);
        Numero3E=(EditText) findViewById(R.id.registroTel3);
        ConfirmarContra=(EditText) findViewById(R.id.registroConfPass);
        progressBar=(ProgressBar) findViewById(R.id.progressBarRegistro);

        BRegistrarse=(Button)findViewById(R.id.btnRegistrar);
        BRegistrarse.setOnClickListener(this);

        firebaseViewModel= ViewModelProviders.of(this).get(FirebaseViewModel.class);
        firebaseViewModel.getUserregister().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean s) {
                if(s==true){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(),"Usuario creado correctamente",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(),"Ya existe un usuario registrado con estos datos",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onClick(View v) {

        String Nombre=NombreE.getText().toString();
        String Correo=CorreoE.getText().toString();
        String Contraseña=ContraseñaE.getText().toString();
        String ConfirmarCon=ConfirmarContra.getText().toString();
        String Numero1=Numero1E.getText().toString();
        String Numero2=Numero2E.getText().toString();
        String Numero3=Numero3E.getText().toString();

        //String UserID=CrearID(Correo);

        if(ValidarCamposllenos(Nombre,Correo,Contraseña,ConfirmarCon,Numero1,Numero2,Numero3)== true){
            if(ValidarContraseña(Contraseña)== true && Contraseña.equals(ConfirmarCon)==true) {
                if(ValidarEmail(Correo)==true) {
                    if(ValidarNumeros(Numero1,Numero2,Numero3)) {
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseViewModel.registrarse(Nombre, Correo, Contraseña, Numero1, Numero2, Numero3);
                    }else
                        Toast.makeText(this,"El numero telefonico debe tener solo numeros y una longitud de 10 digitos", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this,"Ingrese un correo valido", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this,"Las contraseñas deben coincidir y tener al menos una mayuscula,minuscula y tener lontigud minima de 5 y maxima 10", Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();

    }

  /* public String CrearID(String Correo){
        Calendar Fecha= Calendar.getInstance();
        int Mes=Fecha.get(Calendar.MONTH +1);
        String ID="";
        for(int i=0;i<Correo.length();i++){
            if(Correo.charAt(i)=='@'){
                ID=Correo.substring(0,i)+ Integer.toString(Mes);
            }
        }
        return ID;
    }*/

    public boolean ValidarEmail(String Correo){
        // Patron para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(Correo);
        return mather.find();
    }

    public boolean ValidarCamposllenos(String Name, String Correo,String Contraseña,String ConfirmarPass,String Num1,String Num2,String Num3) {
        if (Name.length() != 0 && Correo.length() != 0 && Contraseña.length() != 0 && ConfirmarPass.length() != 0 && Num1.length() != 0 && Num2.length() != 0 && Num3.length() != 0) {
            return true;
        }
        else
            return false;
    }

    public boolean ValidarContraseña(String Contraseña){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");

        Matcher mather = pattern.matcher(Contraseña);

        if(mather.find()==true && Contraseña.length()>5 && Contraseña.length()<12)
            return true;
        else
            return false;
    }

    public boolean ValidarNumeros(String Numero1,String Numero2, String Numero3){
        int bandera=-1;
        Pattern pattern = Pattern.compile("[0-9]*");


        if(Numero1.matches("[0-9]*") && Numero2.matches("[0-9]*")  && Numero3.matches("[0-9]*") ){
            if(Numero1.length()==10 && Numero2.length()==10 && Numero3.length()==10)
                bandera=0;
        }

        if(bandera>=0)
            return true;
        else
            return false;
    }

}