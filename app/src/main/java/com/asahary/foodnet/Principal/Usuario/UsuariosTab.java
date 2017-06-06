package com.asahary.foodnet.Principal.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Busqueda.UsuariosAdapter;
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

public class UsuariosTab extends Fragment implements UsuariosAdapter.OnReciclerItemClickListener {

    UsuariosAdapter adaptador;
    RecyclerView lista;
    EditText txtTexto;
    Spinner spCategorias;
    ArrayList<Usuario> usuarios = new ArrayList<>();
    int opcion;
    int idUsuario;

    public static final int OPCION_SEGUIDORES=0;
    public static final int OPCION_SIGUIENDO=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);

        //Le decimos que tiene una toolbar
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.usuarios_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View vista = getView();
        initVistas(vista);
    }

    private void initVistas(View vista) {
        lista = (RecyclerView) vista.findViewById(R.id.lista);


        adaptador = new UsuariosAdapter(usuarios, this);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        switch (opcion){
            case OPCION_SEGUIDORES:
                iniciarListaSeguidores();
                break;
            case  OPCION_SIGUIENDO:
                iniciarListaSeguidos();
                break;
        }
        iniciarListaSeguidos();


    }

    private void iniciarListaSeguidos() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService servicio = retrofit.create(CookNetService.class);
        Call<List<Usuario>> call = servicio.seguidosUser(idUsuario);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> respuesta = response.body();

                if (respuesta != null) {
                    usuarios = new ArrayList<Usuario>(respuesta);
                    adaptador.swapDatos(usuarios);
                } else {
                    Toast.makeText(getContext(), "cuerpo nullo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(), "respuesta fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void iniciarListaSeguidores() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService servicio = retrofit.create(CookNetService.class);
        Call<List<Usuario>> call = servicio.seguidoresUser(idUsuario);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> respuesta = response.body();

                if (respuesta != null) {
                    usuarios = new ArrayList<Usuario>(respuesta);
                    adaptador.swapDatos(usuarios);
                } else {
                    Toast.makeText(getContext(), "cuerpo nullo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(), "respuesta fallida", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //Quitamos los botones innecesarios
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


    public static UsuariosTab newInstance(int idUsuario,int opcion) {
        UsuariosTab fragment =new UsuariosTab();
        fragment.opcion=opcion;
        fragment.idUsuario=idUsuario;
        return fragment;
    }

    @Override
    public void itemClic(Usuario usuario) {
        Intent intent = new Intent(UsuariosTab.this.getActivity(), UsuarioActivity.class);
        intent.putExtra(Constantes.EXTRA_ID_USUARIO, Integer.parseInt(usuario.getId()));
        startActivity(intent);
    }
}