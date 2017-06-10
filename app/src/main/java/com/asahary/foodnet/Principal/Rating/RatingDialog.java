package com.asahary.foodnet.Principal.Rating;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 30/05/2017.
 */

public class RatingDialog extends DialogFragment {

    View vista;
    RatingBar ratingBar;
    Receta receta;
    OnDismissListener listener;

    public interface OnDismissListener{
         void onDismiss();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        receta=getArguments().getParcelable(Constantes.EXTRA_RECETA);

        builder.setTitle("Rating");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Retrofit retrofit =new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
                CookNetService servicio = retrofit.create(CookNetService.class);

                Call<String> call =servicio.valorarReceta(ratingBar.getRating(),Integer.parseInt(receta.getIdUsuario()),Integer.parseInt(receta.getIdReceta()), MainActivity.idUsuario);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        listener.onDismiss();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        vista= LayoutInflater.from(getContext()).inflate(R.layout.rating_dialog_layout,null);


        ratingBar= (RatingBar) vista.findViewById(R.id.rating);


        builder.setView(vista);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        listener= (OnDismissListener) getActivity();
        super.onAttach(context);
    }
}
