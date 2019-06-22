package br.com.franciscochaves.difran.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.com.franciscochaves.difran.R;
import br.com.franciscochaves.difran.config.ConfiguracaoFirebase;

public class ResetUsuarioActivity extends AppCompatActivity {

    private EditText email;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_usuario);

        email = findViewById(R.id.edit_login_email_recupera);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    }

    public void reset( View view ){
        autenticacao
                .sendPasswordResetEmail( email.getText().toString() )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            email.setText("");
                            Toast.makeText(
                                    ResetUsuarioActivity.this,
                                    "Recuperação de acesso iniciada. Email enviado.",
                                    Toast.LENGTH_LONG
                            ).show();

                            finish();
                        }
                        else{
                            Toast.makeText(
                                    ResetUsuarioActivity.this,
                                    "Falhou! Tente novamente",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }
}
