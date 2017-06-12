package com.asahary.foodnet.Adaptadores;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.Busqueda.RecetasFragment;
import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Saha on 22/05/2017.
 */

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.Contenedor>{
    ArrayList<Receta> recetas=new ArrayList<>();
    private View emptyView;

    public interface OnReciclerItemClickListener{
        void itemClic(Receta receta);
    }


    OnReciclerItemClickListener listener;

    public RecetasAdapter(ArrayList<Receta> recetas, RecetasAdapter.OnReciclerItemClickListener listener){
        super();
        this.listener=listener;
        this.recetas=recetas;
    }
    public RecetasAdapter(ArrayList<Receta> recetas){
        this.recetas=recetas;
    }

    public void swapDatos(ArrayList<Receta> recetas){
        this.recetas=recetas;
        if(listener instanceof RecetaTab){
            ((RecetaTab)listener).checkVacio();
        }else if(listener instanceof RecetasFragment){
            ((RecetasFragment)listener).checkVacio();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public RecetasAdapter.Contenedor onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta,parent,false);

        final RecetasAdapter.Contenedor tvh = new RecetasAdapter.Contenedor(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClic(recetas.get(tvh.getAdapterPosition()));
            }
        });

        return tvh;
    }

    @Override
    public void onBindViewHolder(RecetasAdapter.Contenedor holder, final int position) {
        holder.onBin(recetas.get(position));
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }


    public static class Contenedor extends RecyclerView.ViewHolder{

        TextView lblNombreReceta,lblDescripcionReceta;
        CircleImageView imgReceta;


        public Contenedor(View itemView) {
            super(itemView);
            lblNombreReceta= (TextView) itemView.findViewById(R.id.lblNombreReceta);
            lblDescripcionReceta= (TextView) itemView.findViewById(R.id.lblDescripcionReceta);
            imgReceta= (CircleImageView) itemView.findViewById(R.id.imgReceta);
        }

        public void onBin(Receta receta){
            //Rellenamos los datos
            lblNombreReceta.setText(receta.getNombre());
            lblDescripcionReceta.setText(receta.getDescripcion());
            Picasso.with(imgReceta.getContext()).load(receta.getImagen()).error(R.drawable.food_generic).placeholder(R.drawable.user_generic).fit().into(imgReceta);
        }


    }


}
