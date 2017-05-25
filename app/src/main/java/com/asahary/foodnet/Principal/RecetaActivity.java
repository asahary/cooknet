package com.asahary.foodnet.Principal;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

public class RecetaActivity extends AppCompatActivity {

    EditText txtNombre,txtDescripcion,txtIngredientes,txtPreparacion;
    ImageView imgReceta;
    Receta receta;
    public static final String EXTRA_RECETA ="extraAlimno";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);
        initVistas();
    }

    private void initVistas() {
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtDescripcion= (EditText) findViewById(R.id.txtDescripcion);
        txtIngredientes= (EditText) findViewById(R.id.Ingredientes);
        txtPreparacion= (EditText) findViewById(R.id.txtPreparacion);
        imgReceta= (ImageView) findViewById(R.id.imgReceta);
        receta=getIntent().getParcelableExtra(EXTRA_RECETA);

        rellenarCampos();

    }

    private void rellenarCampos(){
        txtNombre.setText(receta.getNombre());
        txtDescripcion.setText(receta.getDescripcion());
        txtIngredientes.setText(leerIngredientes());
        txtPreparacion.setText(receta.getPreparacion());

        Picasso.with(this).load("http://steamykitchen.com/wp-content/uploads/2012/07/pork-belly-buns-recipe-8380.jpg").fit().into(imgReceta);
    }

    //Separa la cadena de ingredientes para darle formato
    private String leerIngredientes(){
        String formato="";


        //Obtenemos es array de medidas
        String[] medidas=getResources().getStringArray(R.array.medidas);

        String[] ingredientes =receta.getIngredientes().split("%");

        for (int i=0;i<ingredientes.length;i++){
            String[] componentes= ingredientes[i].split(":");
            String miniFormato="";

            for(int x=0;x<componentes.length;x++){

                //Si x==1 quiere decir que ese es el campo de la medida por lo que la traducimos
                if(x==1){
                    String medida=componentes[x];
                    int medidaN=Integer.parseInt(medida);
                    miniFormato+=medidas[medidaN]+"  ";
                }else{
                    miniFormato+=componentes[x]+"  ";
                }
            }
            formato+=miniFormato+"\n";
        }
        return  formato;
    }
}
