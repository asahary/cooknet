package com.asahary.foodnet.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Tecnica;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Constantes;

public class TecnicaActivity extends AppCompatActivity {

    Tecnica tecnica;

    TextView lblNombreTecnica,lblDescripciontecnica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnica);
        tecnica=getIntent().getParcelableExtra(Constantes.EXTRA_TECNICA);
        setTitle(tecnica.nombre);
        initVistas();
    }

    private void initVistas() {
        lblNombreTecnica= (TextView) findViewById(R.id.lblNombreTecnica);
        lblDescripciontecnica= (TextView) findViewById(R.id.lblDescripcionTecnica);

        lblNombreTecnica.setText(tecnica.nombre);
        lblDescripciontecnica.setText(tecnica.descripcion);
    }

    @Override
    public void onBackPressed() {
        ((NavigatorActivity)getParent()).cargarFragmentoGlosario();
        super.onBackPressed();
    }
}
