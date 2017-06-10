package com.asahary.foodnet.Principal.Timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asahary.foodnet.Adaptadores.EventoAdapter;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Evento;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Comentarios.ComentariosDialog;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.Principal.RecetaActivity;
import com.asahary.foodnet.Principal.Usuario.UsuarioActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;

import java.util.ArrayList;
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


    public EventoFragment(){
    }

    public void initVistas(View vista){

        lista= (RecyclerView) vista.findViewById(R.id.lista);
        adaptador=new EventoAdapter(eventos,this);
        lista.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        lista.setAdapter(adaptador);
    }


    @Override
    public void itemClic(Evento evento) {

        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();
        CookNetService service = retrofit.create(CookNetService.class);

        switch (evento.getTipo()){
            case Constantes.EVENTO_FAVORITO:
                Call<Receta> call = service.getReceta(evento.getIdReceta());
                call.enqueue(new Callback<Receta>() {
                    @Override
                    public void onResponse(Call<Receta> call, Response<Receta> response) {
                        Receta cuerpo=response.body();

                        if(cuerpo!=null){
                            Intent intent=new Intent(EventoFragment.this.getActivity(), RecetaActivity.class);
                            intent.putExtra(Constantes.EXTRA_RECETA,cuerpo);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Receta> call, Throwable t) {

                    }
                });
                break;
            case Constantes.EVENTO_SEGUIR:
                Call<Usuario> call3 = service.getUsuario(evento.getIdUser());
                call3.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        Usuario usuario=response.body();

                        if(usuario!=null){
                            Intent intentUser=new Intent(EventoFragment.this.getContext(),UsuarioActivity.class);
                            intentUser.putExtra(Constantes.EXTRA_USUARIO,usuario);
                            startActivity(intentUser);
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {

                    }
                });

                break;
            case Constantes.EVENTO_COMENTAR:
                Call<Receta> call2 = service.getReceta(evento.getIdReceta());
                call2.enqueue(new Callback<Receta>() {
                    @Override
                    public void onResponse(Call<Receta> call, Response<Receta> response) {
                        Receta cuerpo=response.body();

                        if(cuerpo!=null){
                            ComentariosDialog dialog=new ComentariosDialog();
                            Bundle extra = new Bundle();
                            extra.putParcelable(Constantes.EXTRA_RECETA,cuerpo);
                            dialog.setArguments(extra);
                            dialog.show(getFragmentManager(),"Comentarios");
                        }
                    }

                    @Override
                    public void onFailure(Call<Receta> call, Throwable t) {

                    }
                });
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View vista= getView();
        initVistas(vista);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        return inflater.inflate(R.layout.timeline_fragment_layout, container, false);
    }
    public static EventoFragment newInstance(ArrayList<Evento> lista){
        EventoFragment fragment=new EventoFragment();
        fragment.eventos=lista;
        return fragment;
    }


}
