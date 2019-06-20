package br.com.franciscochaves.difran.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.franciscochaves.difran.R;
import br.com.franciscochaves.difran.config.ConfiguracaoFirebase;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class GraficoActivity extends AppCompatActivity {

    private TextView myDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private PieChartView mChart;
    private PieChartData mPieChartData;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceVotos;
    private FirebaseAuth firebaseAuth;
    private String idUsuarioLogado;

    private String date;
    private String dataIdVoto;

    private List<Integer> votos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        myDate = findViewById(R.id.text_my_date);
        mChart = findViewById(R.id.chart);

        votos = new ArrayList<>();

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUsuarioLogado = firebaseAuth.getUid();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        month = month + 1;

        date = day + "/" + month + "/" + year;
        myDate.setText(date);

        dataIdVoto = day + "-" + month + "-" + year;

        databaseReference = ConfiguracaoFirebase.getFirebase();

        databaseReferenceVotos = databaseReference.child("usuarios")
                .child(idUsuarioLogado)
                .child("votos")
                .child(dataIdVoto);

        databaseReferenceVotos.addValueEventListener(getListenerFirebaseChart());

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

                String dataIdVoto = day + "-" + month + "-" + year;

                databaseReferenceVotos = databaseReference.child("usuarios")
                        .child(idUsuarioLogado)
                        .child("votos")
                        .child(dataIdVoto);
                databaseReferenceVotos.addValueEventListener(getListenerFirebaseChart());
            }
        };

    }

    private ValueEventListener getListenerFirebaseChart() {

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpa a lista
                votos.clear();

                //Listar votos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Integer v = Integer.parseInt(String.valueOf(dados.getValue()));
                    votos.add(v);

                }

                contabilizarVotos(votos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(GraficoActivity.this, getApplicationContext().getString(R.string.erro_acesso_database), Toast.LENGTH_LONG).show();

            }
        };
    }

    private void contabilizarVotos(List<Integer> votos) {

        int excelente = 0;
        int bom = 0;
        int medio = 0;
        int ruim = 0;
        int pessimo = 0;

        for (Integer voto : votos) {

            if (voto == 1) {
                excelente++;
            } else if (voto == 2) {
                bom++;
            } else if (voto == 3) {
                medio++;
            } else if (voto == 4) {
                ruim++;
            } else if (voto == 5) {
                pessimo++;
            }

        }

        addChart(excelente, bom, medio, ruim, pessimo);
    }

    private void addChart(int excelente, int bom, int medio, int ruim, int pessimo) {

        List<SliceValue> pieData = new ArrayList<>();

        if (excelente > 0) {
            pieData.add(new SliceValue(excelente, Color.YELLOW).setLabel(getApplicationContext().getString(R.string.excelente) + excelente));
        }
        if (bom > 0) {
            pieData.add(new SliceValue(bom, Color.BLUE).setLabel(getApplicationContext().getString(R.string.bom) + bom));
        }
        if (ruim > 0) {
            pieData.add(new SliceValue(ruim, Color.GRAY).setLabel(getApplicationContext().getString(R.string.ruim) + ruim));
        }
        if (medio > 0) {
            pieData.add(new SliceValue(medio, Color.GREEN).setLabel(getApplicationContext().getString(R.string.medio) + medio));
        }
        if (pessimo > 0) {
            pieData.add(new SliceValue(pessimo, Color.RED).setLabel(getApplicationContext().getString(R.string.pessimo) + pessimo));
        }

        mPieChartData = new PieChartData(pieData);
        mPieChartData.setHasLabels(true);
        mPieChartData.setHasLabels(true).setValueLabelTextSize(14);

        int somaVotos = excelente + bom + medio + ruim + pessimo;

        if (somaVotos == 0) {
            mPieChartData.setHasCenterCircle(true).
                    setCenterText1(getApplicationContext().getString(R.string.nao_possui_dados)).
                    setCenterText1FontSize(20).
                    setCenterText1Color(Color.parseColor("#0097A7"));
        } else {
            mPieChartData.setHasCenterCircle(true).
                    setCenterText1(getApplicationContext().getString(R.string.votos)).
                    setCenterText1FontSize(20).
                    setCenterText1Color(Color.parseColor("#0097A7"));
        }

        mChart.setPieChartData(mPieChartData);
    }
}
