package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatoBinding;


public class ContatoActivity extends AppCompatActivity {

    private ActivityContatoBinding activityContatoBinding;
    private Contato contato;
    //private final int PERMISSAO_LIGACAO_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContatoBinding = ActivityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());
    }

    public void onClickButton(View view) {

        contato = new Contato(
                activityContatoBinding.nomeCompletoEt.getText().toString(),
                activityContatoBinding.emailEt.getText().toString(),
                activityContatoBinding.telefoneEt.getText().toString(),
                activityContatoBinding.comercialSw.isChecked(),
                activityContatoBinding.celularEt.getText().toString(),
                activityContatoBinding.sitePessoalEt.getText().toString()
        );

        switch (view.getId()) {
            case R.id.salvarBt:
                salvar();
                break;
            case R.id.enviarEmailBt:
                enviarEmail();
                break;
            case R.id.ligarTelefoneBt:
                ligarTelefone();
                break;
            case R.id.acessarSitePessoalBt:
                acessarSitePessoal();
                break;
            case R.id.exportarPdfBt:
                break;
            case R.id.celularCk:
                addCellphoneChecked();
            default:
                break;
        }
    }

    private void salvar() {
        Intent retornoIntent = new Intent();
        retornoIntent.putExtra(Intent.EXTRA_USER, contato); // msm chave
        setResult(RESULT_OK, retornoIntent);
        finish(); // fechar tela
    }

    private void enviarEmail() {
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO); // msg sem anexo
        enviarEmailIntent.setData(Uri.parse("mailto:"));
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contato.getEmail()});
        enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "contato");
        enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, contato.toString());
        startActivity(enviarEmailIntent);
    }

    private void ligarTelefone() {
        //verificar permissao e versao 18M
        Intent ligarTelefoneIntent = new Intent(Intent.ACTION_CALL);
        ligarTelefoneIntent.setData(Uri.parse("tel:" + activityContatoBinding.telefoneEt.getText().toString()));
        startActivity(ligarTelefoneIntent);
    }

    private void acessarSitePessoal() {
        Intent acessarSitePessoalIntent = new Intent(Intent.ACTION_VIEW);
        acessarSitePessoalIntent.setData(Uri.parse("https://" + activityContatoBinding.sitePessoalEt.getText().toString()));
        startActivity(acessarSitePessoalIntent);
    }

    private void addCellphoneChecked() {
        if (activityContatoBinding.celularCk.isChecked()) {
            activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
        }
        else {
            activityContatoBinding.celularEt.setVisibility(View.GONE);
            activityContatoBinding.celularEt.setText("");
        }
    }
}