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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.franciscochaves.difran.R;
import br.com.franciscochaves.difran.config.ConfiguracaoFirebase;
import br.com.franciscochaves.difran.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.edit_cadastro_nome);
        email = findViewById(R.id.edit_cadastro_email);
        senha = findViewById(R.id.edit_cadastro_senha);
        botaoCadastrar = findViewById(R.id.button_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeDigitado = nome.getText().toString();
                String emailDigitado = email.getText().toString();
                String senhaDigitada = senha.getText().toString();

                if (nomeDigitado.trim().length() > 0 && emailDigitado.trim().length() > 0 && senhaDigitada.trim().length() > 0) {

                    usuario = new Usuario();
                    usuario.setNome(nomeDigitado);
                    usuario.setEmail(emailDigitado);
                    usuario.setSenha(senhaDigitada);
                    cadastrarUsuario();
                } else  {
                    Toast.makeText(CadastroUsuarioActivity.this, getApplicationContext().getString(R.string.preecher_todos_campos), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroUsuarioActivity.this, getApplicationContext().getString(R.string.sucesso_ao_cadastrar), Toast.LENGTH_LONG).show();

                    String identificadorUsuario = autenticacao.getCurrentUser().getUid();
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();

                    abrirLoginUsuario();

                } else {

                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = getApplicationContext().getString(R.string.excecao_senha_forte);
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = getApplicationContext().getString(R.string.excecao_email_invalido);
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = getApplicationContext().getString(R.string.excecao_email_em_uso);
                    } catch (Exception e) {
                        erroExcecao = getApplicationContext().getString(R.string.excecao_ao_cadastrar);
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, getApplicationContext().getString(R.string.erro) + erroExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void abrirLoginUsuario() {

        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
