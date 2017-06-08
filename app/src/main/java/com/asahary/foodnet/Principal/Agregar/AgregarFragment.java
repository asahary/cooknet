package com.asahary.foodnet.Principal.Agregar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.asahary.foodnet.Adaptadores.IngredienteAdapter;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 23/05/2017.
 */

public class AgregarFragment extends Fragment {

    RecyclerView lista;
    IngredienteAdapter adaptador;
    ImageButton btnAgregar;
    ArrayList<Ingrediente> ingredientes=new ArrayList<>();
    EditText txtNombre,txtDescripcion,txtPreparacion;
    Spinner spCategoria;


    public AgregarFragment(){
    }

    public void initVistas(View vista){

        spCategoria= (Spinner) vista.findViewById(R.id.spCategoria);
        txtNombre= (EditText) vista.findViewById(R.id.txtNombre);
        txtDescripcion= (EditText) vista.findViewById(R.id.txtDescripcion);
        txtPreparacion= (EditText) vista.findViewById(R.id.txtPreparacion);
        lista= (RecyclerView) vista.findViewById(R.id.lista);
        btnAgregar= (ImageButton) vista.findViewById(R.id.btnAgregar);
        adaptador=new IngredienteAdapter(ingredientes);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(AgregarFragment.this.getContext(), LinearLayoutManager.VERTICAL, false));

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarLista();
            }
        });
    }


    //Añade un nuevo Ingrediente a la la lista
    private void iniciarLista() {
        ingredientes.add(new Ingrediente());
        adaptador.notifyDataSetChanged();
    }

    //Hace la llamada post que sube la receta
    private void subirReceta(){

        String nombre=txtNombre.getText().toString();
        String descripcion=txtNombre.getText().toString();
        String preparacion=txtPreparacion.getText().toString();

        Retrofit retrofit=new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();

        CookNetService servicio=retrofit.create(CookNetService.class);

        Call<String> llamada = servicio.aregarReceta(MainActivity.idUsuario,nombre,descripcion,preparacion,formatearIngredientes(),spCategoria.getSelectedItemPosition(),"");

        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String respuesta=response.body();
                if(respuesta!=null){
                    Toast.makeText(getContext(),respuesta,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"cuerpo nulo",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(),"Respuesta fallida",Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Crea una cadena a base de todos los ingredientes de la lista
    private String formatearIngredientes(){
        String format="";

        String nombre="";
        int medida=0;
        Double cantidad=0.0;
        for(int i=0;i<ingredientes.size();i++){
            Ingrediente ingrediente =ingredientes.get(i);
            nombre=ingrediente.nombre;
            medida=ingrediente.medida;
            cantidad=ingrediente.cantidad;

            String miniFormat=cantidad+":"+String.valueOf(medida)+":"+nombre;
            format+=miniFormat;
            if(i<ingredientes.size()-1){
               format+="%";
            }
        }
        return format;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);

        //Le decimos que tiene una toolbar
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.agregar_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View vista= getView();
        initVistas(vista);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //Quitamos los
        menu.clear();
        inflater.inflate(R.menu.agregar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                subirReceta();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static AgregarFragment newInstance(){
        return new AgregarFragment();
    }

}
