package com.asahary.foodnet.Principal.Comentarios;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Comentario;
import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.R;

import java.util.ArrayList;

/**
 * Created by Saha on 23/05/2017.
 */

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.Contenedor> implements View.OnClickListener{
    ArrayList<Comentario> lista =new ArrayList<>();

    @Override
    public void onClick(View view) {

    }

    public interface OnReciclerItemClickListener{
        void itemClic(Comentario comentario);
    }


    ComentariosAdapter.OnReciclerItemClickListener listener;

    public ComentariosAdapter(ArrayList<Comentario> lista, ComentariosAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.lista =lista;

    }
    public ComentariosAdapter(ArrayList<Comentario> ingredientes){
        super();
        this.lista=ingredientes;

    }

    public void swapDatos(ArrayList<Comentario> lista){
        this.lista =lista;
        this.notifyDataSetChanged();
    }

    @Override
    public ComentariosAdapter.Contenedor onCreateViewHolder(ViewGroup parent, int viewType) {//Poner el layout del item
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario,parent,false);

        final ComentariosAdapter.Contenedor tvh = new ComentariosAdapter.Contenedor(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listener.itemClic(ingr edientes.get(tvh.getAdapterPosition()));
            }
        });

        return tvh;
    }

    @Override
    public void onBindViewHolder(ComentariosAdapter.Contenedor holder, final int position) {
        holder.onBin(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public class Contenedor extends RecyclerView.ViewHolder{

        TextView txtNombre, txtComentario,txtFecha;

        public Contenedor(View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtComentario = (TextView) itemView.findViewById(R.id.txtComentario);
            txtFecha= (TextView) itemView.findViewById(R.id.txtFecha);


        }

        public void onBin(final Comentario comentario){
            //Obtenemos la posicion del ingrediente en el array
            final int position=ComentariosAdapter.this.lista.indexOf(comentario);


            txtNombre.setText(comentario.getNombre());
            txtComentario.setText(comentario.getComentario());
            txtFecha.setText(comentario.getFecha());



        }


    }
}