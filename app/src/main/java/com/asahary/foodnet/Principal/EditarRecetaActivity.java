package com.asahary.foodnet.Principal;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.Agregar.IngredienteAdapter;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditarRecetaActivity extends AppCompatActivity {

    Receta receta;
    RecyclerView lista;
    IngredienteAdapter adaptador;
    ArrayList<Ingrediente> ingredientes=new ArrayList<>();
    EditText txtNombre,txtDescripcion,txtPreparacion;
    ImageButton btnAgregar;
    ImageView imgReceta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_receta);

        initVistas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initVistas() {
        receta=getIntent().getParcelableExtra(Constantes.EXTRA_RECETA);
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtDescripcion= (EditText) findViewById(R.id.txtDescripcion);
        txtPreparacion= (EditText) findViewById(R.id.txtPreparacion);
        btnAgregar= (ImageButton) findViewById(R.id.btnAgregar);
        imgReceta=(ImageView) findViewById(R.id.imgReceta);
        lista= (RecyclerView) findViewById(R.id.Ingredientes);

        ingredientes=leerIngredientes();
        adaptador=new IngredienteAdapter(ingredientes);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(EditarRecetaActivity.this,LinearLayoutManager.VERTICAL,false));

        rellenarCampos();


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientes.add(new Ingrediente());
                adaptador.notifyDataSetChanged();
            }
        });
    }
    private void rellenarCampos(){
        txtNombre.setText(receta.getNombre());
        txtDescripcion.setText(receta.getDescripcion());
        txtPreparacion.setText(receta.getPreparacion());
        Picasso.with(this).load("http://steamykitchen.com/wp-content/uploads/2012/07/pork-belly-buns-recipe-8380.jpg").fit().into(imgReceta);
    }
    //Separa la cadena de ingredientes para darle formato
    private ArrayList<Ingrediente> leerIngredientes(){
        ArrayList<Ingrediente> nuevaLista=new ArrayList<>();


        //Obtenemos es array de medidas
        String[] medidas=getResources().getStringArray(R.array.medidas);

        String[] ingredientes =receta.getIngredientes().split("%");

        for (int i=0;i<ingredientes.length;i++){
            String[] componentes= ingredientes[i].split(":");

            double cant=Double.parseDouble(componentes[0]);
            int medida=Integer.parseInt(componentes[1]);
            String nombre=componentes[2];

            nuevaLista.add(new Ingrediente(cant,medida,nombre));

        }
        return  nuevaLista;
    }
}
