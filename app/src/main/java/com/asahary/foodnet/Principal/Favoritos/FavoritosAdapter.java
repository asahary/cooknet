package com.asahary.foodnet.Principal.Favoritos;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.R;

import java.util.ArrayList;

/**
 * Created by Saha on 22/05/2017.
 */

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.Contenedor>{
    ArrayList<Receta> recetas=new ArrayList<>();

    public interface OnReciclerItemClickListener{
        void itemClic(Receta receta);
    }


    OnReciclerItemClickListener listener;

    public FavoritosAdapter(ArrayList<Receta> recetas, FavoritosAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.recetas=recetas;

    }
    public FavoritosAdapter(ArrayList<Receta> recetas){
        this.recetas=recetas;
    }

    public void swapDatos(ArrayList<Receta> recetas){
        this.recetas=recetas;
        this.notifyDataSetChanged();
    }

    @Override
    public FavoritosAdapter.Contenedor onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta,parent,false);

        final FavoritosAdapter.Contenedor tvh = new FavoritosAdapter.Contenedor(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClic(recetas.get(tvh.getAdapterPosition()));
            }
        });

        return tvh;
    }

    @Override
    public void onBindViewHolder(FavoritosAdapter.Contenedor holder, final int position) {
        holder.onBin(recetas.get(position));
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }


    public static class Contenedor extends RecyclerView.ViewHolder{

        TextView lblNombreReceta,lblDescripcionReceta;
        ImageView imgReceta;
        RatingBar rating;
        ImageButton btnFav,btnComments;

        public Contenedor(View itemView) {
            super(itemView);
            lblNombreReceta= (TextView) itemView.findViewById(R.id.lblNombreReceta);
            lblDescripcionReceta= (TextView) itemView.findViewById(R.id.lblDescripcionReceta);
            imgReceta= (ImageView) itemView.findViewById(R.id.imgReceta);
            btnFav= (ImageButton) itemView.findViewById(R.id.btnFav);
            btnComments= (ImageButton) itemView.findViewById(R.id.btnComments);
            rating= (RatingBar) itemView.findViewById(R.id.rating);

        }

        public void onBin(Receta receta){
            //Rellenamos los datos
            lblNombreReceta.setText(receta.getNombre());
            lblDescripcionReceta.setText(receta.getDescripcion());

            //--FALTA LAS FUNCIONALIDADES DE LOS BOTONES Y LLAMAR A RETROFIT PARA INSERTAR LA IMAGEN
        }


    }
}
