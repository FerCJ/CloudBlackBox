package com.cjf.cloudblackbox;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

public class Velocidad extends AppCompatActivity {

    private FirebaseViewModel firebaseViewModel;
    String UserID,TEstadistica;
    private BarDataSet barDataSet;
    ArrayList<String> fechasSemana = new ArrayList<>();
    ArrayList<String> fechasMes = new ArrayList<>();
    ArrayList<BarEntry> rpmsem  = new ArrayList<>();
    ArrayList<BarEntry> rpmmen  = new ArrayList<>();
    BarChart barChart;
    String periodoestadistica;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_velocidad);

        UserID=getIntent().getExtras().get("ID").toString();
        TEstadistica=getIntent().getExtras().get("Tipo").toString();

        Button semanas = (Button) findViewById(R.id.btnSemanalVelocidad);
        Button mes = (Button) findViewById(R.id.btnMensualVelocidad);
        barChart = findViewById(R.id.ctVelocidad);

        firebaseViewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);
        firebaseViewModel.getValEstadistica().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                ArrayList<String> EstadisticasFinales=OrdenarValores(strings);

                // for(int i=0;i<fechasSemana.size();i++)
                //   Log.d("ESTADISTICAS","EstadisticasFinales: "+EstadisticasFinales.get(i)+Integer.toString(i));

                if(periodoestadistica.equals("Semanal")){

                    int z = 1;

                    for (int i=0;i<fechasSemana.size();i++)
                    {
                        rpmsem.add(new BarEntry(z,(int)Float.parseFloat(EstadisticasFinales.get(i))));
                        z += 1;
                    }

                    barDataSet = new BarDataSet(rpmsem,"Velocidad semanal");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);

                    BarData barData = new BarData(barDataSet);
                    barChart.setFitBars(true);
                    barChart.setData(barData);
                    barChart.getDescription().setText("Gráfica de barras");
                    barChart.animateY(2000);
                }
                else{

                    int z = 1;

                    for (int i=0;i<fechasMes.size();i++)
                    {
                        rpmmen.add(new BarEntry(z,(int)Float.parseFloat(EstadisticasFinales.get(i))));
                        z += 1;
                    }

                    barDataSet = new BarDataSet(rpmmen,"Velocidad mensual");
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);

                    BarData barData = new BarData(barDataSet);
                    barChart.setFitBars(true);
                    barChart.setData(barData);
                    barChart.getDescription().setText("Gráfica de barras");
                    barChart.animateY(2000);
                }

            }
        });

        semanas.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                periodoestadistica="Semanal";
                for (int i = 0;i<7;  i++)
                {
                    String fechas = LocalDate.now().minusDays(7-i).toString();
                    String fechaF = fechas.substring(8,10) + "-" + fechas.substring(5,7) + "-" + fechas.substring(0,4);
                    fechasSemana.add(fechaF);
                }

                firebaseViewModel.ObtenerValEstadistica(UserID,TEstadistica,fechasSemana);
            }
        });

        mes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                periodoestadistica="Mensual";
                int j = 1;

                for (int i = 0;i<30;  i++)
                {
                    String fechasm = LocalDate.now().minusDays(30-i).toString();
                    String fechamF = fechasm.substring(8,10) + "-" + fechasm.substring(5,7) + "-" + fechasm.substring(0,4);
                    fechasMes.add(fechamF);
                }

                firebaseViewModel.ObtenerValEstadistica(UserID,TEstadistica,fechasMes);

            }
        });


        Button closeButton = (Button) findViewById(R.id.btnReturnVelocidad);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public ArrayList<String> OrdenarValores(ArrayList<String> strings){
        ArrayList Valoresfinales= new ArrayList();

        if(periodoestadistica.equals("Semanal")){
            ArrayList ValoresfinalesSema= new ArrayList();
            for(int i=0;i<fechasSemana.size();i++)
                ValoresfinalesSema.add("0");



            for(String valores : strings){
                int indice=fechasSemana.indexOf(valores.split("@")[1]);
                if( indice !=-1) {
                    ValoresfinalesSema.set(indice, valores.split("@")[0]);
                }
            }



            Valoresfinales=ValoresfinalesSema;
        }
        else{

            ArrayList ValoresfinalesMensu= new ArrayList();
            for(int i=0;i<fechasMes.size();i++)
                ValoresfinalesMensu.add("0");



            for(String valores : strings){
                int indice=fechasMes.indexOf(valores.split("@")[1]);
                if( indice !=-1) {
                    // Log.d("STRINGS","Indice: "+indice);
                    // Log.d("STRINGS","Valor: "+valores.split("@")[0]);
                    ValoresfinalesMensu.set(indice, valores.split("@")[0]);
                }
            }

            Valoresfinales=ValoresfinalesMensu;

        }


        return Valoresfinales;
    }
}
