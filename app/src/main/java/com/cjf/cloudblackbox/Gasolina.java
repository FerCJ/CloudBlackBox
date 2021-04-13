package com.cjf.cloudblackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Gasolina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolina);

        BarChart barChart = findViewById(R.id.ctGasolina);

        ArrayList<BarEntry> gasolina  = new ArrayList<>();

        gasolina.add(new BarEntry(1,5));
        gasolina.add(new BarEntry(2,10));
        gasolina.add(new BarEntry(3,2));
        gasolina.add(new BarEntry(4,18));
        gasolina.add(new BarEntry(5,9));

        BarDataSet barDataSet = new BarDataSet(gasolina,"Consumo de gasolina");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Ejemplo grafica de barras");
        barChart.animateY(2000);

        Button closeButton = (Button) findViewById(R.id.btnReturnGasolina);
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