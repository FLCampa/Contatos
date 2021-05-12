package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatoBinding;


public class ContatoActivity extends AppCompatActivity {

    private ActivityContatoBinding activityContatoBinding;
    private Contato contato;

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
            case R.id.exportarPdfBt:
                break;
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

}