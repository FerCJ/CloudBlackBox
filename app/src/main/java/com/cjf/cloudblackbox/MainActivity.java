package com.cjf.cloudblackbox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    TextView Registrarse,RecuperarPass;
    Button IniciarSesion;
    EditText Correo,Contraseña;
    ProgressBar progressBar;
    private FirebaseViewModel firebaseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Registrarse=(TextView) findViewById(R.id.tvRegistrarse);
        RecuperarPass=(TextView) findViewById(R.id.tvRecuperar);
        IniciarSesion=(Button) findViewById(R.id.btnIniciarSesion);
        Correo=(EditText)findViewById(R.id.emailIniciar);
        Contraseña=(EditText)findViewById(R.id.passwdIniciar);
        progressBar=(ProgressBar)findViewById(R.id.progressBarIniciar);

        Registrarse.setOnClickListener(this);
        IniciarSesion.setOnClickListener(this);
        RecuperarPass.setOnClickListener(this);

        firebaseViewModel= ViewModelProviders.of(this).get(FirebaseViewModel.class);
        firebaseViewModel.getUserlogin().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.equals("")){
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getBaseContext(), MenuPrincipal.class);
                    intent.putExtra("ID",s);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(),"Debe Registrarse",Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecuperarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),RecuperarPassword.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Correo.getText().clear();
        Contraseña.getText().clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvRegistrarse){
            Intent intent=new Intent(this,Registro.class);
            startActivity(intent);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            firebaseViewModel.IniciarSesion(Correo.getText().toString(),Contraseña.getText().toString());
        }
    }



}