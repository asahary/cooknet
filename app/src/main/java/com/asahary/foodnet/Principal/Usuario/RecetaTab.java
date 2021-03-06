package com.asahary.foodnet.Principal.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asahary.foodnet.Principal.Timeline.EventoFragment;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Adaptadores.RecetasAdapter;
import com.asahary.foodnet.Actividades.RecetaActivity;
import com.asahary.foodnet.R;

import java.util.ArrayList;

/**
 * Created by Saha on 06/06/2017.
 */

public class RecetaTab extends Fragment implements RecetasAdapter.OnReciclerItemClickListener{

    RecyclerView lista;
    public RecetasAdapter adaptador;
    ArrayList<Receta> listaRecetas=new ArrayList<Receta>();
    View emptyView;
    ImageView imgCarga;
    Echador mEchador;

    public interface Echador{
        void echar(Receta receta);
    }

    public void initVistas(View vista){

        lista= (RecyclerView) vista.findViewById(R.id.rvFavoritos);
        emptyView=vista.findViewById(R.id.emptyView);
        imgCarga= (ImageView) vista.findViewById(R.id.imgCarga);
        iniciarLista();
    }

    private void iniciarLista() {
        adaptador=new RecetasAdapter(new ArrayList<Receta>(listaRecetas), RecetaTab.this);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(RecetaTab.this.getContext(),LinearLayoutManager.VERTICAL,false));
        checkVacio();
    }


    @Override
    public void itemClic(Receta receta) {
        mEchador.echar(receta);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        return inflater.inflate(R.layout.favoritos_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View vista= getView();
        initVistas(vista);
    }

    public static RecetaTab newInstance(ArrayList<Receta> lista, Echador echador){
        RecetaTab fragment = new RecetaTab();
        fragment.mEchador=echador;
        fragment.listaRecetas=lista;
        return fragment;
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
