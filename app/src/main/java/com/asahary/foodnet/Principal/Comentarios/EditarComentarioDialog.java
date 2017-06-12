package com.asahary.foodnet.Principal.Comentarios;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.asahary.foodnet.POJO.Comentario;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.Utilidades.Libreria;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saha on 12/06/2017.
 */

public class EditarComentarioDialog extends DialogFragment {
    View vista;
    EditText txtComentario;
    Comentario comentario;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Editar comentario");

        vista= LayoutInflater.from(getContext()).inflate(R.layout.preparacion_dialog_layout,null);
        comentario=getArguments().getParcelable(Constantes.EXTRA_COMENTARIO);
        final int idUsuerio=Integer.parseInt(comentario.getIdUsuario());
        final int idReceta=Integer.parseInt(comentario.getIdReceta());
        final String cuerpoComentario=comentario.getComentario();

        txtComentario= (EditText) vista.findViewById(R.id.txtPreparacion);
        txtComentario.setText(cuerpoComentario);

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!txtComentario.getText().toString().isEmpty()){
                    Libreria.obtenerServicioApi().editarComentario(idUsuerio,idReceta,txtComentario.getText().toString()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            Boolean cuerpo=response.body();
                            if(cuerpo!=null){
                                if(cuerpo){
                                    Libreria.mostrarMensjeCorto(vista.getContext(),"Se ha editado el comentario correctamente");
                                }
                            }else{
                                Libreria.mostrarMensjeCorto(vista.getContext(),Constantes.RESPUESTA_NULA);
                            }
                            ((Activity)vista.getContext()).finish();
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Libreria.mostrarMensjeCorto(vista.getContext(),Constantes.RESPUESTA_FALLIDA);
                            ((Activity)vista.getContext()).finish();
                        }
                    });
                }
            }
        });
        builder.setView(vista);

        return builder.create();
    }
}
