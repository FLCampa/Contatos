package br.edu.ifsp.scl.ads.pdm.contatos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ViewContatoBinding;

public class ContatosAdapter extends ArrayAdapter<Contato> {

    public ContatosAdapter(Context contexto, int layout, ArrayList<Contato> contatosList) {
        super(contexto, layout, contatosList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // ListView chama este metodo
        // é chamado sempre q uma nova celula for aparecer na tela

        ViewContatoBinding viewContatoBinding;
        ContatoViewHolder contatoViewHolder;

        // verifica se é necessario inflar, criar uma nova celula
        // celula n existe
        if (convertView == null) { // inflar celula
            viewContatoBinding = ViewContatoBinding.inflate(LayoutInflater.from(getContext()));

            // atribui a nova celular a view q vai ser devolvida, preenchida para ListView
            convertView = viewContatoBinding.getRoot();

            // pega as referencias e guarda para as views internas da celula usando um holder
            contatoViewHolder = new ContatoViewHolder();
            contatoViewHolder.nomeContatoTv = viewContatoBinding.nomeContatoTv;
            contatoViewHolder.emailContatoTv = viewContatoBinding.emailContatoTv;
            contatoViewHolder.telefonteTv = viewContatoBinding.telefoneTv;

            // associa view da celula ao holder q referencia suas views internas
            convertView.setTag(contatoViewHolder);
        }
        // celula nova ou reciclada
        // pegar holder associado a celula
        contatoViewHolder = (ContatoViewHolder) convertView.getTag();

        // atulizar ou setar valores dos TextView
        Contato contato = getItem(position);
        contatoViewHolder.nomeContatoTv.setText(contato.getNome());
        contatoViewHolder.emailContatoTv.setText(contato.getEmail());
        contatoViewHolder.telefonteTv.setText(contato.getTelefone());

        return convertView;
    }

    // melhorar desempenho (apontam para os componentes internos)
    private class ContatoViewHolder {
        public TextView nomeContatoTv;
        public TextView emailContatoTv;
        public TextView telefonteTv;
    }
}
