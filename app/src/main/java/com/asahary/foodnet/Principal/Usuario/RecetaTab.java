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
import android.widget.Toast;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.Favoritos.FavoritosAdapter;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.Principal.RecetaActivity;
import com.asahary.foodnet.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 06/06/2017.
 */

public class RecetaTab extends Fragment implements FavoritosAdapter.OnReciclerItemClickListener{

    RecyclerView lista;
    FavoritosAdapter adaptador;
    ArrayList<Receta> listaRecetas=new ArrayList<Receta>();


    public void initVistas(View vista){

        lista= (RecyclerView) vista.findViewById(R.id.rvFavoritos);
        iniciarLista();
    }

    private void iniciarLista() {
        adaptador=new FavoritosAdapter(new ArrayList<Receta>(listaRecetas), RecetaTab.this);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(RecetaTab.this.getContext(),LinearLayoutManager.VERTICAL,false));
    }


    @Override
    public void itemClic(Receta receta) {
        Intent intent=new Intent(RecetaTab.this.getActivity(), RecetaActivity.class);
        intent.putExtra(Constantes.EXTRA_RECETA,receta);
        startActivity(intent);

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

    public static RecetaTab newInstance(ArrayList<Receta> lista){
        RecetaTab fragment = new RecetaTab();
        fragment.listaRecetas=lista;
        return fragment;
    }
}
