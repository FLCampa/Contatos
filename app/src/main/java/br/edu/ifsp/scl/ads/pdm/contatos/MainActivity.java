package br.edu.ifsp.scl.ads.pdm.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }

    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.salvarBt:
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

    private void enviarEmail() {
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO);
        enviarEmailIntent.setData(Uri.parse("mailto:"));
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, activityMainBinding.emailEt.getText().toString());
        startActivity(enviarEmailIntent);
    }

    private void ligarTelefone() {
        Intent ligarTelefoneIntent = new Intent(Intent.ACTION_CALL);
        ligarTelefoneIntent.setData(Uri.parse("tel:" + activityMainBinding.telefoneEt.getText().toString()));
        startActivity(ligarTelefoneIntent);
    }

    private void acessarSitePessoal() {
        Intent acessarSitePessoalIntent = new Intent(Intent.ACTION_VIEW);
        acessarSitePessoalIntent.setData(Uri.parse("https://" + activityMainBinding.sitePessoalEt.getText().toString()));
        startActivity(acessarSitePessoalIntent);
    }

    private void addCellphoneChecked() {
        if (activityMainBinding.celularCk.isChecked()) {
            activityMainBinding.celularEt.setVisibility(View.VISIBLE);
        } else if (!activityMainBinding.celularCk.isChecked()) {
            activityMainBinding.celularEt.setVisibility(View.GONE);
            activityMainBinding.celularEt.setText("");
        }
    }
}