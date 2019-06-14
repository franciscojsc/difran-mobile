package br.com.franciscochaves.difran.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.franciscochaves.difran.R;
import br.com.franciscochaves.difran.config.ConfiguracaoFirebase;
import br.com.franciscochaves.difran.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = findViewById(R.id.edit_login_email);
        senha = findViewById(R.id.edit_login_senha);
        botaoLogar = findViewById(R.id.button_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailDigitado = email.getText().toString();
                String senhaDigitada = senha.getText().toString();

                if(emailDigitado.trim().length() > 0 && senhaDigitada.trim().length() > 0){
                    usuario = new Usuario();
                    usuario.setEmail(emailDigitado);
                    usuario.setSenha(senhaDigitada);
                    validarLogin();
                } else  {
                    Toast.makeText(LoginActivity.this, "Preencher todos os campos", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();

                } else {

                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "E-mail não existe ou foi desativado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha está errada";
                    } catch (Exception e) {
                        erroExcecao = "Ao logar usuário";
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void abrirTelaPrincipal() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }
}
