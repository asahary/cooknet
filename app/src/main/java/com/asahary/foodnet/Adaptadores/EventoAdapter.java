package com.asahary.foodnet.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.POJO.Comentario;
import com.asahary.foodnet.POJO.Evento;
import com.asahary.foodnet.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saha on 09/06/2017.
 */

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.Contenedor> implements View.OnClickListener{
    ArrayList<Evento> lista =new ArrayList<>();

    @Override
    public void onClick(View view) {

    }

    public interface OnReciclerItemClickListener{
        void itemClic(Evento evento);
    }


    EventoAdapter.OnReciclerItemClickListener listener;

    public EventoAdapter(ArrayList<Evento> lista, EventoAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.lista =lista;

    }
    public EventoAdapter(ArrayList<Evento> ingredientes){
        super();
        this.lista=ingredientes;

    }

    public void swapDatos(ArrayList<Evento> lista){
        this.lista =lista;
        this.notifyDataSetChanged();
    }

    @Override
    public EventoAdapter.Contenedor onCreateViewHolder(ViewGroup parent, int viewType) {//Poner el layout del item
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento,parent,false);

        final EventoAdapter.Contenedor tvh = new EventoAdapter.Contenedor(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClic(lista.get(tvh.getAdapterPosition()));
            }
        });

        return tvh;
    }

    @Override
    public void onBindViewHolder(EventoAdapter.Contenedor holder, final int position) {
        holder.onBin(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public class Contenedor extends RecyclerView.ViewHolder{

        CircleImageView imgUser;
        TextView lblNickUser,lblAccionUser,lblReferencia;

        public Contenedor(View itemView) {
            super(itemView);
            imgUser= (CircleImageView) itemView.findViewById(R.id.imgUser);
            lblNickUser= (TextView) itemView.findViewById(R.id.lblNickUser);
            lblAccionUser= (TextView) itemView.findViewById(R.id.lblAccionUser);
            lblReferencia= (TextView) itemView.findViewById(R.id.lblEventoReferente);
        }

        public void onBin(final Evento evento){
            //Obtenemos la posicion del ingrediente en el array
            final int position=EventoAdapter.this.lista.indexOf(evento);
            Picasso.with(imgUser.getContext()).load(evento.getImagenUser()).placeholder(R.drawable.user_generic).fit().error(R.drawable.user_generic).into(imgUser);
            lblNickUser.setText(evento.getNick());

            switch (evento.getTipo()){
                case Constantes.EVENTO_SEGUIR:
                    lblAccionUser.setText("te siguio");
                    break;
                case Constantes.EVENTO_COMENTAR:
                    lblAccionUser.setText("comento tu receta");
                    break;
                case Constantes.EVENTO_FAVORITO:
                    lblAccionUser.setText("hizo favorito tu ");
                    lblReferencia.setText(evento.getNombreReceta());
                    break;
            }
        }


    }
}
