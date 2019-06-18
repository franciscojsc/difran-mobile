package br.com.franciscochaves.difran.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import br.com.franciscochaves.difran.R;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class GraficoActivity extends AppCompatActivity {

    private TextView myDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private PieChartView mChart;
    private PieChartData mPieChartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        myDate = findViewById(R.id.text_my_date);
        mChart = findViewById(R.id.chart);

        myDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        GraficoActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                myDate.setText(date);
                addChart();
            }
        };

        // Graph
        addChart();

    }

    private void addChart() {

        List<SliceValue> pieData = new ArrayList<>();
        Random r = new Random();
        int value1 = r.nextInt(100);
        int value2 = r.nextInt(100);
        int value3 = r.nextInt(100);
        int value4 = r.nextInt(100);
        int value5 = r.nextInt(100);


        pieData.add(new SliceValue(value5, Color.YELLOW).setLabel("Excelente:" + value5));
        pieData.add(new SliceValue(value4, Color.BLUE).setLabel("Bom:" + value4));
        pieData.add(new SliceValue(value2, Color.GRAY).setLabel("Ruim:" + value2));
        pieData.add(new SliceValue(value3, Color.GREEN).setLabel("Médio:" + value3));
        pieData.add(new SliceValue(value1, Color.RED).setLabel("Péssimo:" + value1));


        mPieChartData = new PieChartData(pieData);
        mPieChartData.setHasLabels(true);
        mPieChartData.setHasLabels(true).setValueLabelTextSize(14);
        mPieChartData.setHasCenterCircle(true).setCenterText1("Votos").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));

        mChart.setPieChartData(mPieChartData);
    }
}
