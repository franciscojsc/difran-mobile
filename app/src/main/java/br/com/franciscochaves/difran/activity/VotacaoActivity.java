package br.com.franciscochaves.difran.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

import br.com.franciscochaves.difran.R;
import br.com.franciscochaves.difran.config.ConfiguracaoFirebase;

public class VotacaoActivity extends AppCompatActivity {

    private ImageView emojiExcelente;
    private ImageView emojiBom;
    private ImageView emojiMedio;
    private ImageView emojiRuim;
    private ImageView emojiPessimo;

    private static final int VOTO_EXCELENTE = 1;
    private static final int VOTO_BOM = 2;
    private static final int VOTO_MEDIO = 3;
    private static final int VOTO_RUIM = 4;
    private static final int VOTO_PESSIMO = 5;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votacao);

        emojiExcelente = findViewById(R.id.image_emoji_excelente);
        emojiBom = findViewById(R.id.image_emoji_bom);
        emojiMedio = findViewById(R.id.image_emoji_medio);
        emojiRuim = findViewById(R.id.image_emoji_ruim);
        emojiPessimo = findViewById(R.id.image_emoji_pessimo);


        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUsuarioLogado = firebaseAuth.getUid();

        databaseReference = ConfiguracaoFirebase.getFirebase();


        emojiExcelente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votar(VOTO_EXCELENTE);
                abrirTelaAgradecimeto();
            }
        });

        emojiBom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votar(VOTO_BOM);
                abrirTelaAgradecimeto();
            }
        });

        emojiMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votar(VOTO_MEDIO);
                abrirTelaAgradecimeto();
            }
        });

        emojiRuim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votar(VOTO_RUIM);
                abrirTelaAgradecimeto();
            }
        });

        emojiPessimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                votar(VOTO_PESSIMO);
                abrirTelaAgradecimeto();
            }
        });

    }

    private void abrirTelaAgradecimeto() {
        Intent intent = new Intent(VotacaoActivity.this, AgradecimentoActivity.class);
        startActivity(intent);
    }

    private void votar(int voto) {

        Calendar calendar = Calendar.getInstance();
        long dataVoto = calendar.getTimeInMillis();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String dataIdVoto = day + "-" + month + "-" + year;

        databaseReference.child("usuarios")
                .child(idUsuarioLogado)
                .child("votos")
                .child(dataIdVoto)
                .child(String.valueOf(dataVoto))
                .setValue(voto);
    }
}
