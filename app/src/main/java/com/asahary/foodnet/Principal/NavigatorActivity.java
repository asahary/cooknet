package com.asahary.foodnet.Principal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.Principal.Usuario.UsuariosTab;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Cache;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.Utilidades.Libreria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigatorActivity extends AppCompatActivity {

    FragmentManager gestor;
    String opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        opcion=getIntent().getStringExtra(Constantes.EXTRA_OPCION_LISTA);

        initVistas();


    }

    private void initVistas(){
        gestor=getSupportFragmentManager();

        switch (opcion){
            case Constantes.EXTRA_LISTA_MIS_RECETA:
                cargarFragmentoMisRecetas();
                break;
            case  Constantes.EXTRA_LISTA_MIS_SEGUIDORES:
                cargarFragmentoMisSeguidores();
                break;
            case Constantes.EXTRA_LISTA_MIS_SEGUIDOS:
                cargarFragmentoMisSeguidos();
                break;
        }
    }


    public void cargarFragmento(int id, Fragment frag) {
        if (getFragmentManager().findFragmentById(R.id.fragment) != null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment)).commit();
        }
        FragmentTransaction transaccion = gestor.beginTransaction();
        transaccion.replace(id, frag);
        transaccion.commit();

    }

    public void cargarFragmentoMisRecetas(){
        Libreria.obtenerServicioApi().recetasPropiasUser(MainActivity.idUsuario).enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> lista=response.body();

                if(lista!=null){
                    Cache.misRecetas=new ArrayList<Receta>(lista);
                }else{
                    Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_NULA);
                    Cache.misRecetas=new ArrayList<Receta>();
                }

                cargarFragmento(R.id.fragment,RecetaTab.newInstance(Cache.misRecetas));
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, RecetaTab.newInstance(Cache.misRecetas));
            }
        });
    }

    public void cargarFragmentoMisSeguidores(){
        Libreria.obtenerServicioApi().seguidoresUser(MainActivity.idUsuario).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> lista=response.body();

                if(lista!=null){
                    Cache.misSeguidores=new ArrayList<Usuario>(lista);
                }else{
                    Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_NULA);
                    Cache.misSeguidores=new ArrayList<Usuario>();
                }

                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(Cache.misSeguidores));
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(Cache.misSeguidores));
            }
        });
    }

    public void cargarFragmentoMisSeguidos(){
        Libreria.obtenerServicioApi().seguidosUser(MainActivity.idUsuario).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> lista=response.body();

                if(lista!=null){
                    Cache.misSiguiendo=new ArrayList<Usuario>(lista);
                }else{
                    Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_NULA);
                    Cache.misSiguiendo=new ArrayList<Usuario>();
                }

                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(Cache.misSiguiendo));
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(Cache.misSiguiendo));
            }
        });
    }

}
