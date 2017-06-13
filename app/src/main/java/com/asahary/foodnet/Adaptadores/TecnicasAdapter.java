package com.asahary.foodnet.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Tecnica;
import com.asahary.foodnet.Principal.Busqueda.RecetasFragment;
import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saha on 13/06/2017.
 */

public class TecnicasAdapter extends RecyclerView.Adapter<TecnicasAdapter.Contenedor>{
    ArrayList<Tecnica> lista=new ArrayList<>();

    public interface OnReciclerItemClickListener{
        void itemClic(Tecnica tecnica);
    }


    TecnicasAdapter.OnReciclerItemClickListener listener;

    public TecnicasAdapter(ArrayList<Tecnica> lista, TecnicasAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.lista=lista;
    }
    public TecnicasAdapter(ArrayList<Tecnica> lista){
        this.lista=lista;
    }

    public void swapDatos(ArrayList<Tecnica> lista){
        this.lista=lista;
        this.notifyDataSetChanged();
    }

    @Override
    public TecnicasAdapter.Contenedor onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tecnica,parent,false);

        final TecnicasAdapter.Contenedor tvh = new TecnicasAdapter.Contenedor(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClic(lista.get(tvh.getAdapterPosition()));
            }
        });

        return tvh;
    }

    @Override
    public void onBindViewHolder(TecnicasAdapter.Contenedor holder, final int position) {
        holder.onBin(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class Contenedor extends RecyclerView.ViewHolder{

        TextView lblNombreTecnica;

        public Contenedor(View itemView) {
            super(itemView);
            lblNombreTecnica= (TextView) itemView.findViewById(R.id.lblNombreTecnica);
        }

        public void onBin(Tecnica tecnica){
            //Rellenamos los datos
            lblNombreTecnica.setText(tecnica.nombre);
        }

    }

}
