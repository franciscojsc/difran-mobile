package br.com.franciscochaves.difran.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.franciscochaves.difran.R;

public class AgradecimentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agradecimento);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        thread.start();
    }
}
