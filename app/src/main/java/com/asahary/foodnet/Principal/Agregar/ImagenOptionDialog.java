package com.asahary.foodnet.Principal.Agregar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.asahary.foodnet.R;

/**
 * Created by Saha on 01/06/2017.
 */

public class ImagenOptionDialog extends DialogFragment {
    View vista;

    ImageButton btnFotos,btnCamara;


    public interface OnOptionClick{
        void onClick(int option);
    }

    OnOptionClick listener;

    @Override
    public void onAttach(Context context) {
        listener= (OnOptionClick) context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Imagen");
        vista= LayoutInflater.from(getContext()).inflate(R.layout.imagen_option_dialog_layout,null);

        btnCamara= (ImageButton) vista.findViewById(R.id.btnCamara);
        btnFotos= (ImageButton) vista.findViewById(R.id.btnGaleria);

        btnFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(1);
                dismiss();
            }
        });
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(2);
                dismiss();
            }
        });



        builder.setView(vista);
        return builder.create();
    }
}
