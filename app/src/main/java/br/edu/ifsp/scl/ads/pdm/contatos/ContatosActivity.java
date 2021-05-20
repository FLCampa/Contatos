package br.edu.ifsp.scl.ads.pdm.contatos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatosBinding;
import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatosRvBinding;

public class ContatosActivity extends AppCompatActivity implements OnContatoClickListener {

    //private ActivityContatosBinding activityContatosBinding;
    private ActivityContatosRvBinding activityContatosRvBinding;

    private ArrayList<Contato> contatosList;

    //private ContatosAdapter contatosAdapter;
    private ContatosRvAdapter contatosRvAdapter;

    private LinearLayoutManager contatosLinearLayoutManager;

    private final int NOVO_CONTATO_REQUEST_CODE = 0;
    private final int LIGAR_TELEFONE_REQUEST_CODE = 0;      // VER
    private final int EDITAR_CONTATO_REQUEST_CODE = 1;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activityContatosBinding = ActivityContatosBinding.inflate(getLayoutInflater());
        //setContentView(activityContatosBinding.getRoot());
        activityContatosRvBinding = ActivityContatosRvBinding.inflate(getLayoutInflater());
        setContentView(activityContatosRvBinding.getRoot());


        // instanciar data source
        contatosList = new ArrayList<>();
        popularContatosList();

        // instanciar adapter
//        contatosAdapter = new ContatosAdapter(
//                this,
//                R.layout.view_contato,
//                contatosList
//        );
        contatosRvAdapter = new ContatosRvAdapter(contatosList, this, getMenuInflater());

        // associar adapter com listView
        //activityContatosBinding.contatosLv.setAdapter(contatosAdapter);

        // intanciando LinearLayoutManager
        contatosLinearLayoutManager = new LinearLayoutManager(this);

        // associando o Rv com o Adapter e LayoutManager
        activityContatosRvBinding.contatosRv.setAdapter(contatosRvAdapter);
        activityContatosRvBinding.contatosRv.setLayoutManager(contatosLinearLayoutManager);

        // registra ListView para menu de contexto
        //registerForContextMenu(activityContatosBinding.contatosLv);

        // associar um listener de clique para listView
//        activityContatosBinding.contatosLv.setOnItemClickListener((parent, view, position, id) -> {
//            contato = contatosList.get(position);
//            Intent detalhesIntent = new Intent(this, ContatoActivity.class);
//            detalhesIntent.putExtra(Intent.EXTRA_USER, contato);
//            startActivity(detalhesIntent);
//        });
    }

    private void popularContatosList() {
        for (int i = 0; i < 20; i++) {
            contatosList.add(
                    new Contato(
                            "Nome " + i,
                            "E-mail " + i,
                            "Telefone " + i,
                            ( i % 2 == 0 ) ? false : true,
                            "Celular " + i,
                            "www.site" + i + ".com.br"
                    )
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistra ListView para manu de contexto
        unregisterForContextMenu(activityContatosRvBinding.contatosRv);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contatos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.novoContatoMi) {
            Intent novoContatoIntent = new Intent(this, ContatoActivity.class);
            startActivityForResult(novoContatoIntent, NOVO_CONTATO_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NOVO_CONTATO_REQUEST_CODE && resultCode == RESULT_OK) {
            Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER); // modo sem criar constante
            if (contato != null) {
                contatosList.add(contato);
                //contatosAdapter.notifyDataSetChanged(); // notifica o adapter a alteracao no conjunto de dados
                contatosRvAdapter.notifyDataSetChanged();
            }
        }
        else {
            if (requestCode == EDITAR_CONTATO_REQUEST_CODE && resultCode == RESULT_OK) {
                // atualizar contato na lista
                Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER);
                int posicao = data.getIntExtra(Intent.EXTRA_INDEX, -1);
                if (contato != null && posicao != -1) {
                    contatosList.remove(posicao);
                    contatosList.add(posicao, contato);
                   //contatosAdapter.notifyDataSetChanged();
                    contatosRvAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_contato, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // casting para pegar posicao
        //AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // pregando contato pela posicao
        //contato = contatosAdapter.getItem(menuInfo.position);
        contato = contatosList.get(contatosRvAdapter.getPosicao());

        switch (item.getItemId()) {
            case R.id.enviarEmailMi:
                enviarEmail();
                return true;
            case R.id.ligarMi:
                ligarTelefone();
                return true;
            case R.id.acessarSitePessoalMi:
                acessarSitePessoal();
                return true;
            case R.id.editarContatoMi:
                editarContato(contatosRvAdapter.getPosicao());
                return true;
            case R.id.removerContatoMi:
                removeContato();
                return true;
            default:
                return false;
        }
    }

    private void enviarEmail() {
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: "));
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contato.getEmail()});
        enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, contato.getNome());
        enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, contato.toString());
        startActivity(enviarEmailIntent);
    }

    private void acessarSitePessoal() {
        Intent acessarSitePessoalIntent = new Intent(Intent.ACTION_VIEW);
        acessarSitePessoalIntent.setData(Uri.parse("https://" + contato.getSite()));
        startActivity(acessarSitePessoalIntent);
    }

    private void editarContato(int position) {
        Intent editarContatoIntent = new Intent(this, ContatoActivity.class);
        editarContatoIntent.putExtra(Intent.EXTRA_USER, contato); // constante, contato
        editarContatoIntent.putExtra(Intent.EXTRA_INDEX, position);
        startActivityForResult(editarContatoIntent, EDITAR_CONTATO_REQUEST_CODE);
    }

    private void removeContato(){
        Toast.makeText(this, contato.getNome() + " foi removido!", Toast.LENGTH_SHORT).show();
        contatosList.remove(contato);
        contatosRvAdapter.notifyDataSetChanged();
    }

    private void ligarTelefone() {
        Intent ligarTelefoneIntent = new Intent(Intent.ACTION_CALL);
        ligarTelefoneIntent.setData(Uri.parse("tel:" + contato.getTelefone()));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(ligarTelefoneIntent);
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, LIGAR_TELEFONE_REQUEST_CODE);
            }
        } else {
            startActivity(ligarTelefoneIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LIGAR_TELEFONE_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão de ligação é necessaria", Toast.LENGTH_SHORT).show();
            }
            ligarTelefone();
        }
    }

    @Override
    public void onContatoClick(int posicao) {
        // abrir tela para visualizar detalhes do contato
        contato = contatosList.get(posicao);
        Intent detalhesIntent = new Intent(this, ContatoActivity.class);
        detalhesIntent.putExtra(Intent.EXTRA_USER, contato);
        startActivity(detalhesIntent);
    }
}