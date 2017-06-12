package com.asahary.foodnet.Principal.Busqueda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asahary.foodnet.Actividades.BusquedaActivity;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Adaptadores.RecetasAdapter;
import com.asahary.foodnet.Actividades.RecetaActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saha on 26/05/2017.
 */

public class RecetasFragment extends Fragment implements RecetasAdapter.OnReciclerItemClickListener,BusquedaActivity.OnTextToolbarTextChange{
    RecetasAdapter adaptador;
    RecyclerView lista;
    ArrayList<Receta> recetas=new ArrayList<>();
    View emptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);

        //Le decimos que tiene una toolbar
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.favoritos_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View vista= getView();
        initVistas(vista);
    }

    private void initVistas(View vista) {
        lista = (RecyclerView) vista.findViewById(R.id.rvFavoritos);
        adaptador = new RecetasAdapter(recetas, this);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        emptyView=vista.findViewById(R.id.emptyView);
        iniciarLista();
    }

    private void iniciarLista(){

        Libreria.obtenerServicioApi().listRecetas().enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> respuesta=response.body();

                if(respuesta!=null){
                    recetas=new ArrayList<Receta>(respuesta);
                    adaptador.swapDatos(recetas);
                }else{
                    Toast.makeText(getContext(),"cuerpo nullo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
               // Toast.makeText(getContext(),"respuesta fallida",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //Quitamos los botones innecesarios
        super.onCreateOptionsMenu(menu, inflater);
    }


    public static RecetasFragment newInstance(){
        return new RecetasFragment();
    }


    @Override
    public void itemClic(Receta receta) {
        Intent intent =new Intent(getActivity(), RecetaActivity.class);
        intent.putExtra(Constantes.EXTRA_RECETA,receta);
        startActivity(intent);
    }

    @Override
    public void onTextChanged(String text) {
        String texto=text;
        ArrayList<Receta> nuevasRecetas= new ArrayList<>();

        //Si el campo de texto no esta vacio creamos una nueva lista con todos los usuarios que coincidan con el texto
        if(!texto.isEmpty()){


            for (int i=0;i<recetas.size();i++){
                Receta receta=recetas.get(i);
                String nombre=receta.getNombre();
                String descripcion=receta.getDescripcion();

                if(nombre.contains(texto)||descripcion.contains(texto)){
                    nuevasRecetas.add(receta);
                }
            }

            adaptador.swapDatos(nuevasRecetas);

        }else{//Si el texto esta vacio mostramos a todos los usuarios
            adaptador.swapDatos(recetas);
        }
    }

    public void checkVacio(){
        if (adaptador.getItemCount()==0){
            lista.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            lista.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
