package com.asahary.foodnet.Principal.Timeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asahary.foodnet.Actividades.MainActivity;
import com.asahary.foodnet.Adaptadores.EventoAdapter;
import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Evento;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Comentarios.ComentariosDialog;
import com.asahary.foodnet.Actividades.RecetaActivity;
import com.asahary.foodnet.Actividades.UsuarioActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 09/06/2017.
 */

public class EventoFragment extends Fragment implements EventoAdapter.OnReciclerItemClickListener {
    RecyclerView lista;
    ArrayList<Evento> eventos=new ArrayList<>();
    EventoAdapter adaptador;
    Echador mEchador;
    View emptyView;

    public interface Echador{
        void echar(Evento evento);
    }

    @Override
    public void itemClic(Evento evento) {
        mEchador.echar(evento);
    }


    public void initVistas(View vista){
        emptyView=vista.findViewById(R.id.emptyView);
        lista= (RecyclerView) vista.findViewById(R.id.lista);
        adaptador=new EventoAdapter(eventos,this);
        lista.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        lista.setAdapter(adaptador);
        adaptador.swapDatos(adaptador.lista);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SwipeRefreshLayout vista= (SwipeRefreshLayout) getView();
        vista.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Libreria.obtenerServicioApi().eventosUser(CacheApp.user.getId()).enqueue(new Callback<List<Evento>>() {
                    @Override
                    public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                        List<Evento> cuerpo=response.body();
                        Collections.sort(cuerpo,Evento.FechaComparator);
                        vista.setRefreshing(false);
                        if(cuerpo!=null){
                            CacheApp.misEventos=new ArrayList<Evento>(cuerpo);
                            adaptador.swapDatos(CacheApp.misEventos);
                        }else{
                            Libreria.mostrarMensjeCorto(getContext(),Constantes.RESPUESTA_NULA);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Evento>> call, Throwable t) {
                        vista.setRefreshing(false);
                        Libreria.mostrarMensjeCorto(getContext(),Constantes.RESPUESTA_FALLIDA);
                    }
                });
            }
        });
        initVistas(vista);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        return inflater.inflate(R.layout.timeline_fragment_layout, container, false);
    }
    public static EventoFragment newInstance(ArrayList<Evento> lista,Echador context){
        EventoFragment fragment=new EventoFragment();
        fragment.mEchador=context;
        fragment.eventos=lista;
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
