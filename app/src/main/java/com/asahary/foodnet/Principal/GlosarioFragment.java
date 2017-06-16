package com.asahary.foodnet.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asahary.foodnet.Actividades.TecnicaActivity;
import com.asahary.foodnet.Adaptadores.TecnicasAdapter;
import com.asahary.foodnet.POJO.Tecnica;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by Saha on 13/06/2017.
 */

public class GlosarioFragment extends Fragment implements TecnicasAdapter.OnReciclerItemClickListener{
    ArrayList<Tecnica> tecnicas;
    TecnicasAdapter adapter;
    RecyclerView lista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.tecnicas_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View vista= getView();
        initVistas(vista);
        initLista();
    }

    private void initVistas(View vista){
        lista= (RecyclerView) vista.findViewById(R.id.lista);
    }

    private void initLista(){
        adapter=new TecnicasAdapter(tecnicas,this);
        lista.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        lista.setAdapter(adapter);
    }


    public static GlosarioFragment newInstance(ArrayList<Tecnica> tecnicas){
        GlosarioFragment fragment=new GlosarioFragment();
        fragment.tecnicas=tecnicas;
        return fragment;
    }

    @Override
    public void itemClic(Tecnica tecnica) {
        Intent intentTecnica=new Intent(GlosarioFragment.this.getActivity(), TecnicaActivity.class);
        intentTecnica.putExtra(Constantes.EXTRA_TECNICA,tecnica);
        startActivity(intentTecnica);
    }
}
