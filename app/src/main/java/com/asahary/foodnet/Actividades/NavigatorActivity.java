package com.asahary.foodnet.Actividades;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.GlosarioFragment;
import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.Principal.Usuario.UsuariosTab;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.Utilidades.Libreria;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigatorActivity extends AppCompatActivity {

    FragmentManager gestor;
    ImageView imgCarga;
    String opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        opcion=getIntent().getStringExtra(Constantes.EXTRA_OPCION_LISTA);

        initVistas();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    private void initVistas(){
        gestor=getSupportFragmentManager();
        imgCarga= (ImageView) findViewById(R.id.imgCarga);

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
            case Constantes.EXTRA_LISTA_GLOSARIO:
                cargarFragmentoGlosario();
                break;

        }
    }

    private void mostrarCarga(){
        findViewById(R.id.fragment).setVisibility(View.GONE);
        imgCarga.setVisibility(View.VISIBLE);
        Glide.with(this).load(R.drawable.loading).fitCenter().fitCenter().into(imgCarga);
    }
    private void ocultarCarga(){
        findViewById(R.id.fragment).setVisibility(View.VISIBLE);
        imgCarga.setVisibility(View.GONE);
    }


    //Carga de fragmentos
    public void cargarFragmento(int id, Fragment frag) {
        if (getFragmentManager().findFragmentById(R.id.fragment) != null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment)).commit();
        }
        FragmentTransaction transaccion = gestor.beginTransaction();
        transaccion.replace(id, frag);
        transaccion.commit();

    }

    public void cargarFragmentoMisRecetas(){
        mostrarCarga();
        setTitle(Constantes.TITULO_MIS_RECETAS);
        Libreria.obtenerServicioApi().recetasPropiasUser(MainActivity.idUsuario).enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> lista=response.body();

                if(lista!=null){
                    CacheApp.misRecetas=new ArrayList<Receta>(lista);
                }else{
                    Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_NULA);
                    CacheApp.misRecetas=new ArrayList<Receta>();
                }

                cargarFragmento(R.id.fragment,RecetaTab.newInstance(CacheApp.misRecetas));
                ocultarCarga();
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, RecetaTab.newInstance(CacheApp.misRecetas));
                ocultarCarga();
            }
        });
    }

    public void cargarFragmentoMisSeguidores(){
        mostrarCarga();
        setTitle(Constantes.TITULO_SEGUIDORES);
        Libreria.obtenerServicioApi().seguidoresUser(MainActivity.idUsuario).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> lista=response.body();

                if(lista!=null){
                    CacheApp.misSeguidores=new ArrayList<Usuario>(lista);
                }else{
                    Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_NULA);
                    CacheApp.misSeguidores=new ArrayList<Usuario>();
                }

                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(CacheApp.misSeguidores));
                ocultarCarga();
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(CacheApp.misSeguidores));
                ocultarCarga();
            }
        });
    }

    public void cargarFragmentoMisSeguidos(){
        mostrarCarga();
        setTitle(Constantes.TITULO_SIGUIENDO);
        Libreria.obtenerServicioApi().seguidosUser(MainActivity.idUsuario).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> lista=response.body();

                if(lista!=null){
                    CacheApp.misSiguiendo=new ArrayList<Usuario>(lista);
                }else{
                    Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_NULA);
                    CacheApp.misSiguiendo=new ArrayList<Usuario>();
                }

                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(CacheApp.misSiguiendo));
                ocultarCarga();
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(NavigatorActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, UsuariosTab.newInstance(CacheApp.misSiguiendo));
                ocultarCarga();
            }
        });
    }

    public void cargarFragmentoGlosario(){
        setTitle(Constantes.TITULO_GLOSARIO);
        cargarFragmento(R.id.fragment, GlosarioFragment.newInstance(CacheApp.glosario));
    }

}