package com.asahary.foodnet.Principal.Agregar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.asahary.foodnet.R;

/**
 * Created by Saha on 05/06/2017.
 */

public class PreparacionDialog extends DialogFragment{
    View vista;
    EditText txtPreparacion;
    OnPrepDone listener;

    public interface OnPrepDone {
        void onClick(String preparacion);
    }

    @Override
    public void onAttach(Context context) {
        listener= (OnPrepDone) context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Preparacion");
        vista= LayoutInflater.from(getContext()).inflate(R.layout.preparacion_dialog_layout,null);
        txtPreparacion= (EditText) vista.findViewById(R.id.txtPreparacion);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(txtPreparacion.getText().toString());
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        String extras = getTag();
        txtPreparacion.setText(extras);
        builder.setView(vista);
        return builder.create();
    }
}
