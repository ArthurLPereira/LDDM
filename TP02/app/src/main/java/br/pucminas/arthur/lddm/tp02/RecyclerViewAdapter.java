package br.pucminas.arthur.lddm.tp02;

/**
 * Created by glori on 12/06/2018.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by glori on 17/04/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.LinkViewHolder> {

    public List<MyNode> dados;

    @Override
    public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        LinkViewHolder lvh = new LinkViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(LinkViewHolder holder, int position) {
        holder.title.setText(dados.get(position).title);

    }

    @Override
    public int getItemCount() {
        return dados.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView view){
        super.onAttachedToRecyclerView(view);
    }

    public RecyclerViewAdapter(List<MyNode> dados){
        this.dados = dados;
    }

    public static class LinkViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public TextView title;


        public LinkViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.cardTitle);
        }
    }
}
