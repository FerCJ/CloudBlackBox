package com.cjf.cloudblackbox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

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
                    String Token;
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("Error en obtenerToken: ", "Fetching FCM registration token failed", task.getException());
                                        return;
                                    }

                                    // Get new FCM registration token
                                    String token = task.getResult();

                                    // Log and toast
                                    //Log.d("Token registrado: ", token);
                                    enviarTokenRegistro(token);


                                }
                            });

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

    private void enviarTokenRegistro(String Token)
    {
        Log.d("token enviado", "El token es: " + Token);
    }



}

//Token cmdMkI7dRoKVNAP78bfxbg:APA91bEfOO4Sz-yW3AqCeznXEgBvwosYQS3UQPz1W_DvW6hU_7BNoaN1tww93ELI3Vil3IyWM8X7hlcZJXSCoXkADMVpfXSd1t80cqQlKvlTV9nPU4-9d2Wh9fgTabYESspFhtt4aWHX