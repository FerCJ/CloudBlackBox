package com.cjf.cloudblackbox;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class RPM extends AppCompatActivity {

    private FirebaseViewModel firebaseViewModel;
    String UserID;
    private BarDataSet barDataSet;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpm);
        UserID=getIntent().getExtras().get("ID").toString();

        Button semanas = (Button) findViewById(R.id.btnSemanalRPM);
        Button mes = (Button) findViewById(R.id.btnMensualRPM);

        ArrayList<String> fechasSemana = new ArrayList<>();
        ArrayList<String> fechasMes = new ArrayList<>();

        BarChart barChart = findViewById(R.id.ctRPM);
        ArrayList<BarEntry> rpmsem  = new ArrayList<>();
        ArrayList<BarEntry> rpmmen  = new ArrayList<>();


        semanas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<7;  i++)
                {
                    String fechas = LocalDate.now().minusDays(7-i).toString();
                    String fechaF = fechas.substring(8,10) + "-" + fechas.substring(5,7) + "-" + fechas.substring(0,4);
                    fechasSemana.add(fechaF);
                }

                int z = 1;

                for (String fecha : fechasSemana)
                {

                    System.out.println("Fechas semana: " + fecha);
                    rpmsem.add(new BarEntry(z,5));
                    z += 1;
                }



                barDataSet = new BarDataSet(rpmsem,"Cantidad de RPM Semanal");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.getDescription().setText("Gráfica de barras");
                barChart.animateY(2000);
            }
        });

        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = 1;

                for (int i = 0;i<30;  i++)
                {
                    String fechasm = LocalDate.now().minusDays(30-i).toString();
                    String fechamF = fechasm.substring(8,10) + "-" + fechasm.substring(5,7) + "-" + fechasm.substring(0,4);
                    fechasMes.add(fechamF);
                }

                for (String fechaM : fechasMes)
                {

                    System.out.println("Fechas semana: " + fechaM);
                    rpmmen.add(new BarEntry(j,10));
                    j += 1;
                }

                barDataSet = new BarDataSet(rpmmen,"Cantidad de RPM Mensual");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.getDescription().setText("Gráfica de barras");
                barChart.animateY(2000);

            }
        });



        Button closeButton = (Button) findViewById(R.id.btnReturnRPM);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }



}