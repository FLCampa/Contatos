package br.edu.ifsp.scl.ads.pdm.contatos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ActivityContatoBinding;


public class ContatoActivity extends AppCompatActivity {

    private ActivityContatoBinding activityContatoBinding;
    private Contato contato;
    private int posicao = -1; // valor invalido

    private final int PERMISSAO_ESCRITA_ARMAZENAMENTO_EXTERNO_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContatoBinding = ActivityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());

        // verificar se algum contato foi recebido
        contato = (Contato) getIntent().getSerializableExtra(Intent.EXTRA_USER);
        if (contato != null) {
            // recebendo a posicao
            posicao = getIntent().getIntExtra(Intent.EXTRA_INDEX, -1);

            // alterando ativacao das views
            boolean ativo = (posicao != -1)? true : false;
            alterarAtivacaoViews(ativo);
            if (ativo) {
                getSupportActionBar().setSubtitle("Edição de contato");
                activityContatoBinding.exportarPdfBt.setVisibility(View.GONE);
            }
            else {
                getSupportActionBar().setSubtitle("Detalhes do contato");
                activityContatoBinding.salvarBt.setVisibility(View.GONE);
            }

            // abrindo tela para editar contato
            // usando dados do contato para preencher views
            activityContatoBinding.nomeCompletoEt.setText(contato.getNome());
            activityContatoBinding.emailEt.setText(contato.getEmail());
            activityContatoBinding.telefoneEt.setText(contato.getTelefone());
            activityContatoBinding.comercialSw.setChecked(contato.isTelefoneComercial());
            activityContatoBinding.celularEt.setText(contato.getTelefoneCelular());
            activityContatoBinding.sitePessoalEt.setText(contato.getSite());
        }
        else {
            getSupportActionBar().setSubtitle("Novo contato");
            activityContatoBinding.exportarPdfBt.setVisibility(View.GONE);
        }
    }

    private void alterarAtivacaoViews(boolean ativo) {
        activityContatoBinding.nomeCompletoEt.setEnabled(ativo);
        activityContatoBinding.emailEt.setEnabled(ativo);
        activityContatoBinding.telefoneEt.setEnabled(ativo);
        activityContatoBinding.comercialSw.setEnabled(ativo);
        activityContatoBinding.celularCk.setChecked(true);
        activityContatoBinding.celularCk.setEnabled(ativo);
        cellphoneChecked();
        activityContatoBinding.celularEt.setEnabled(ativo);
        activityContatoBinding.sitePessoalEt.setEnabled(ativo);
    }

    public void cellphoneChecked() {
        if (activityContatoBinding.celularCk.isChecked()) {
            activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
        } else {
            activityContatoBinding.celularEt.setVisibility(View.GONE);
            activityContatoBinding.celularEt.setText("");
        }
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
                permissaoEscritaArmazenamentoExterno();
                break;
            default:
                break;
        }
    }

    private void salvar() {
        Intent retornoIntent = new Intent();
        retornoIntent.putExtra(Intent.EXTRA_USER, contato); // msm chave
        retornoIntent.putExtra(Intent.EXTRA_INDEX, posicao);
        setResult(RESULT_OK, retornoIntent);
        finish(); // fechar tela
    }

    private void permissaoEscritaArmazenamentoExterno() {
        Intent ligarTelefoneIntent = new Intent(Intent.ACTION_CALL);
        ligarTelefoneIntent.setData(Uri.parse("tel:" + contato.getTelefone()));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                gerarDocumentoPdf();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_ESCRITA_ARMAZENAMENTO_EXTERNO_REQUEST_CODE);
            }
        } else {
            gerarDocumentoPdf();
        }
    }

    private void gerarDocumentoPdf() {
        // pegar altura e largura da view raiz para gerar imagem q vai no PDF
        View conteudo = activityContatoBinding.getRoot();
        int largura = conteudo.getWidth();
        int altura = conteudo.getHeight();

        // criando PDF
        PdfDocument documentoPDF = new PdfDocument();

        // criando a config de uma pagina e iniciando um pag a partir da config
        PdfDocument.PageInfo configuracaoPagina = new PdfDocument.PageInfo.Builder(largura, altura, 1).create();
        PdfDocument.Page pagina = documentoPDF.startPage(configuracaoPagina);

        // criando um snapshot(copia intantanea) da view na pag PDF
        conteudo.draw(pagina.getCanvas());

        documentoPDF.finishPage(pagina);

        // salvando arquivo
        File diretorioDocumentos = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        try {
            File documento = new File(diretorioDocumentos, contato.getNome().replace("", "_") + ".pdf");
            documento.createNewFile();
            documentoPDF.writeTo(new FileOutputStream(documento));
            documentoPDF.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_ESCRITA_ARMAZENAMENTO_EXTERNO_REQUEST_CODE) {
            permissaoEscritaArmazenamentoExterno();
        }
    }

    public void addCellphoneChecked(View view) {
        if (activityContatoBinding.celularCk.isChecked()) {
            activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
        } else {
            activityContatoBinding.celularEt.setVisibility(View.GONE);
            activityContatoBinding.celularEt.setText("");
        }
    }
}