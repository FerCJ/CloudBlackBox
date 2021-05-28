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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.model.Marker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RPM extends AppCompatActivity {

    private FirebaseViewModel firebaseViewModel;
    String UserID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpm);
        UserID=getIntent().getExtras().get("ID").toString();

       /* LocalDate fechaActual = LocalDate.now();
        System.out.println("Fecha actual es: " + fechaActual);
        LocalDate fechaSemana = LocalDate.now().minusDays(7);
        System.out.println("Fecha semanaD es: " + fechaSemana);
        fechaSemana = LocalDate.now().minusWeeks(1);
        System.out.println("Fecha semanaW es: " + fechaSemana);
        LocalDate fechaMes = LocalDate.now().minusMonths(1);
        System.out.println("Fecha mes es: " + fechaMes);
*/
        ArrayList<String> fechasSemana = new ArrayList<>();

        for (int i = 0;i<7;  i++)
        {
            fechasSemana.add(LocalDate.now().minusDays(7-i).toString());
        }

        for (String fecha : fechasSemana)
        {

            System.out.println("Fechas semana: " + LocalDate.parse(fecha,DateTimeFormatter.ofPattern("dd-MM-YYYY")));
        }



       // LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        BarChart barChart = findViewById(R.id.ctRPM);

        ArrayList<BarEntry> gasolina  = new ArrayList<>();

        gasolina.add(new BarEntry(1,5));
        gasolina.add(new BarEntry(2,10));
        gasolina.add(new BarEntry(3,2));
        gasolina.add(new BarEntry(4,18));
        gasolina.add(new BarEntry(5,9));

        BarDataSet barDataSet = new BarDataSet(gasolina,"Cantidad de RPM");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Gráfica de barras");
        barChart.animateY(2000);

        Button closeButton = (Button) findViewById(R.id.btnReturnRPM);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });
    }
}