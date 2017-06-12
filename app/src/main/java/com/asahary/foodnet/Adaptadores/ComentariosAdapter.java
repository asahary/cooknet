package com.asahary.foodnet.Adaptadores;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Comentario;
import com.asahary.foodnet.Principal.Comentarios.ComentariosDialog;
import com.asahary.foodnet.Principal.Comentarios.EditarComentarioDialog;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.Utilidades.Libreria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                listener.itemClic(lista.get(tvh.getAdapterPosition()));
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
        CircleImageView imgUser;
        TextView txtNombre, txtComentario,txtFecha;
        ImageView btnEditar,btnEliminar;

        public Contenedor(View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtComentario = (TextView) itemView.findViewById(R.id.txtComentario);
            txtFecha= (TextView) itemView.findViewById(R.id.txtFecha);
            imgUser= (CircleImageView) itemView.findViewById(R.id.imgUser);
            btnEditar= (ImageView) itemView.findViewById(R.id.btnEditar);
            btnEliminar= (ImageView) itemView.findViewById(R.id.btnEliminar);

        }

        public void onBin(final Comentario comentario){
            //Obtenemos la posicion del ingrediente en el array
            final int position=ComentariosAdapter.this.lista.indexOf(comentario);

            final int idUser=Integer.parseInt(comentario.getIdUsuario());
            final int idReceta= Integer.parseInt(comentario.getIdReceta());

            txtNombre.setText(comentario.getNombre());
            txtComentario.setText(comentario.getComentario());
            txtFecha.setText(comentario.getFecha());
            Picasso.with(imgUser.getContext()).load(comentario.getImagen()).fit().error(R.drawable.user_generic).placeholder(R.drawable.user_generic).into(imgUser);

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Libreria.obtenerServicioApi().eliminarComentario(idUser,idReceta).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            Boolean cuerpo=response.body();
                            if(cuerpo!=null){
                                if(cuerpo){
                                    Libreria.mostrarMensjeCorto(itemView.getContext(),"Se borro el comentario correctamente");
                                    ((Activity)itemView.getContext()).finish();
                                }
                            }else{
                                Libreria.mostrarMensjeCorto(itemView.getContext(),"No ha devuelto nada :'( ");
                                ((Activity)itemView.getContext()).finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Libreria.mostrarMensjeCorto(itemView.getContext(), Constantes.RESPUESTA_FALLIDA);
                            ((Activity)itemView.getContext()).finish();
                        }
                    });
                }
            });

            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditarComentarioDialog dialog=new EditarComentarioDialog();
                    Bundle extra = new Bundle();
                    extra.putParcelable(Constantes.EXTRA_COMENTARIO,comentario);
                    dialog.setArguments(extra);
                    dialog.show(((AppCompatActivity)itemView.getContext()).getSupportFragmentManager(),"Comentarios");
                }
            });
        }


    }
}
