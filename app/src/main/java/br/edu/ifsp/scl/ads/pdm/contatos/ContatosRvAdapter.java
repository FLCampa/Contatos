package br.edu.ifsp.scl.ads.pdm.contatos;

import android.text.NoCopySpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.edu.ifsp.scl.ads.pdm.contatos.databinding.ViewContatoBinding;

public class ContatosRvAdapter extends
        RecyclerView.Adapter<ContatosRvAdapter.ContatoViewHolder> {
    public class ContatoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView nomeContatoTv;
        private final TextView emailContatoTv;

        public ContatoViewHolder(View viewContato) {
            super(viewContato);
            nomeContatoTv = viewContato.findViewById(R.id.nomeContatoTv);
            emailContatoTv = viewContato.findViewById(R.id.emailContatoTv);
            viewContato.setOnCreateContextMenuListener(this);
        }

        public TextView getNomeContatoTv() {
            return nomeContatoTv;
        }

        public TextView getEmailContatoTv() {
            return emailContatoTv;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.add(R.id.acoesMg, R.id.enviarEmailMi, 0, R.string.enviar_email);
            menuInflater.inflate(R.menu.context_menu_contato, menu);
        }
    }

    private ArrayList<Contato> contatosList;
    private OnContatoClickListener onContatoClickListener;
    private int posicao;
    private MenuInflater menuInflater;

    public ContatosRvAdapter(ArrayList<Contato> contatosList, OnContatoClickListener onContatoClickListener, MenuInflater menuInflater) {
        this.contatosList = contatosList;
        this.onContatoClickListener = onContatoClickListener;
        this.menuInflater = menuInflater;
    }

    // chamado pelo layoutManager sempre q uma nova celula precisa ser criada
    @NonNull
    @Override
    public ContatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewContatoBinding viewContatoBinding = ViewContatoBinding.inflate(LayoutInflater.from(parent.getContext())); // infla classe binding
        ContatoViewHolder contatoViewHolder = new ContatoViewHolder(viewContatoBinding.getRoot()); // viewHolder celula
        return contatoViewHolder;
    }

    // chamado qnd precisar atualizar o valor da celula
    @Override
    public void onBindViewHolder(@NonNull ContatoViewHolder holder, int position) {
        // busca o contato
        Contato contato = contatosList.get(position);

        // seta os valores no ViewHolder
        holder.getNomeContatoTv().setText(contato.getNome());
        holder.getEmailContatoTv().setText(contato.getEmail());

        // seta onClickListener de cada celula
        holder.itemView.setOnClickListener((v) ->
                onContatoClickListener.onContatoClick(position)
        );

        // seta onLongClickListener
        holder.itemView.setOnLongClickListener((v) -> {
            posicao = position;
            return false;
        });

    }

    // chamado para prever a qtde de celulas necessarias
    @Override
    public int getItemCount() {
        return contatosList.size();
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
