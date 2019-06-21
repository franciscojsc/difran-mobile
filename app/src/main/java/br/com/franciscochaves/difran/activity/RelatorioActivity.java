package br.com.franciscochaves.difran.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.franciscochaves.difran.R;
import br.com.franciscochaves.difran.config.ConfiguracaoFirebase;

public class RelatorioActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceVotos;
    private DatabaseReference databaseReferenceVotosDetalhes;
    private FirebaseAuth firebaseAuth;
    private String idUsuarioLogado;

    private List<String> dataVotos;
    private List<Integer> votosDetalhes;

    private AlertDialog.Builder dialog;

    private ListView mListaItens;
    private String[] itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        itens = new String[]{};
        dataVotos = new ArrayList<>();
        votosDetalhes = new ArrayList<>();

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUsuarioLogado = firebaseAuth.getUid();
        databaseReference = ConfiguracaoFirebase.getFirebase();

        databaseReferenceVotos = databaseReference.child("usuarios")
                .child(idUsuarioLogado)
                .child("votos");

        databaseReferenceVotos.addValueEventListener(getListenerFirebaseRelatorio());

        mListaItens = findViewById(R.id.list_relatorio);

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                itens);

        mListaItens.setAdapter(adaptador);

        mListaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int codigoPosicao = i;
                String valorClicado = mListaItens.getItemAtPosition(codigoPosicao).toString();

                valorClicado = valorClicado.replace("/", "-");
                databaseReferenceVotosDetalhes = databaseReferenceVotos.child(valorClicado);

                databaseReferenceVotosDetalhes.addListenerForSingleValueEvent(getListenerFirebaseRelatorioDetalhes());

            }
        });

    }

    private ValueEventListener getListenerFirebaseRelatorio() {

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpa a lista
                dataVotos.clear();

                // Listar datas
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    String d = dados.getKey();
                    d = d.replace("-", "/");
                    dataVotos.add(d);

                }

                itens = dataVotos.toArray(new String[dataVotos.size()]);

                ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        itens);

                mListaItens.setAdapter(adaptador);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.erro_acesso_database), Toast.LENGTH_LONG).show();

            }
        };
    }

    private ValueEventListener getListenerFirebaseRelatorioDetalhes() {

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpa a lista
                votosDetalhes.clear();

                //Listar votos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Integer v = Integer.parseInt(String.valueOf(dados.getValue()));
                    votosDetalhes.add(v);

                }

                String titulo = dataSnapshot.getKey();
                titulo = titulo.replace("-", "/");
                String mensagem = contabilizarVotos(votosDetalhes);

                mostrarDialog(titulo, mensagem);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.erro_acesso_database), Toast.LENGTH_LONG).show();

            }
        };
    }

    private String contabilizarVotos(List<Integer> votos) {

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

        return getApplicationContext().getString(R.string.excelente) + " " + excelente + "\n" +
                getApplicationContext().getString(R.string.bom) + " " + bom + "\n" +
                getApplicationContext().getString(R.string.medio) + " " + medio + "\n" +
                getApplicationContext().getString(R.string.ruim) + " " + ruim + "\n" +
                getApplicationContext().getString(R.string.pessimo) + " " + pessimo;

    }

    private void mostrarDialog(String titulo, String mensagem) {

        //Criar alert dialog
        dialog = new AlertDialog.Builder(RelatorioActivity.this);

        //Configurar o título
        dialog.setTitle(titulo);

        //Configurar a mensagem
        dialog.setMessage(mensagem);

        //Cancelar a dialog, ao clicar fora do dialog
        dialog.setCancelable(false);

        //Definir ícone
        dialog.setIcon(android.R.drawable.ic_menu_day);

        //Botão positivo
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //Criar o dialog
        dialog.create();

        //Exibindo a dialog
        dialog.show();
    }

}