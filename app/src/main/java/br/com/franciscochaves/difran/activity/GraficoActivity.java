package br.com.franciscochaves.difran.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import br.com.franciscochaves.difran.R;

public class GraficoActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        calendarView = findViewById(R.id.calendar_grafico);
        myDate = findViewById(R.id.text_my_date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int ano, int mes, int dia) {
                String date = dia + "/" + (mes + 1) + "/" + ano;
                myDate.setText(date);

            }
        });

    }

}
