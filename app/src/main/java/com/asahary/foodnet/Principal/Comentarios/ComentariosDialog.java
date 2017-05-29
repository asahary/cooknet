package com.asahary.foodnet.Principal.Comentarios;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 29/05/2017.
 */

public class ComentariosDialog extends DialogFragment {

    View vista;
    RecyclerView lista;
    EditText txtComentario;
    ImageButton btnOk;





    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Comentarios");

        vista= LayoutInflater.from(getContext()).inflate(R.layout.comentarios_dialog_layout,null);
        txtComentario= (EditText) vista.findViewById(R.id.txtComentario);
        btnOk= (ImageButton) vista.findViewById(R.id.btnAceptar);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
                CookNetService service =retrofit.create(CookNetService.class);
                Call<String>call=service.subirComentario(txtComentario.getText().toString(),1,2,3);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getContext(),"Response", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(),"Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setView(vista);

        return builder.create();
    }

}
