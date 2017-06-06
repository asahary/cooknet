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
    int opcion;
    int idUsuario;

    public static final int OPCION_FAVORITOS=0;
    public static final int OPCION_PROPIAS=1;

    public void initVistas(View vista){

        lista= (RecyclerView) vista.findViewById(R.id.rvFavoritos);

        switch (opcion){
            case OPCION_FAVORITOS:
                iniciarListaFavoritos();
                break;
            case OPCION_PROPIAS:
                iniciarListaPropios();
                break;
        }
    }

    private void iniciarListaFavoritos() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();

        CookNetService servicio =retrofit.create(CookNetService.class);

        Call<List<Receta>> llamada=servicio.favoritosUser(idUsuario);

        llamada.enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> recetas = response.body();

                if(recetas!=null){
                    adaptador=new FavoritosAdapter(new ArrayList<Receta>(recetas), RecetaTab.this);
                    lista.setAdapter(adaptador);
                    lista.setLayoutManager(new LinearLayoutManager(RecetaTab.this.getContext(),LinearLayoutManager.VERTICAL,false));

                }else{
                    Toast.makeText(RecetaTab.this.getContext(),"No hay recetas",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
                Toast.makeText(RecetaTab.this.getContext(),"respuesta fallida",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarListaPropios() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();

        CookNetService servicio =retrofit.create(CookNetService.class);

        Call<List<Receta>> llamada=servicio.recetasUser(idUsuario);

        llamada.enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> recetas = response.body();

                if(recetas!=null){
                    adaptador=new FavoritosAdapter(new ArrayList<Receta>(recetas), RecetaTab.this);
                    lista.setAdapter(adaptador);
                    lista.setLayoutManager(new LinearLayoutManager(RecetaTab.this.getContext(),LinearLayoutManager.VERTICAL,false));

                }else{
                    Toast.makeText(RecetaTab.this.getContext(),"No hay recetas",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
                Toast.makeText(RecetaTab.this.getContext(),"respuesta fallida",Toast.LENGTH_SHORT).show();
            }
        });
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

    public static RecetaTab newInstance(int idUsuario,int opcion){
        RecetaTab fragment = new RecetaTab();
        fragment.idUsuario=idUsuario;
        fragment.opcion=opcion;
        return fragment;
    }
}
