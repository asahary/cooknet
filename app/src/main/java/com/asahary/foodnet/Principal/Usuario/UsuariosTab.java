package com.asahary.foodnet.Principal.Usuario;

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

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Busqueda.UsuariosAdapter;
import com.asahary.foodnet.R;

import java.util.ArrayList;

/**
 * Created by Saha on 06/06/2017.
 */

public class UsuariosTab extends Fragment implements UsuariosAdapter.OnReciclerItemClickListener {

    UsuariosAdapter adaptador;
    RecyclerView lista;
    ArrayList<Usuario> listaUsuarios = new ArrayList<>();


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
        iniciarLista();
    }

    private void iniciarLista()
    {
        adaptador = new UsuariosAdapter(listaUsuarios, this);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //Quitamos los botones innecesarios
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


    public static UsuariosTab newInstance(ArrayList<Usuario> usuarios) {
        UsuariosTab fragment =new UsuariosTab();
        fragment.listaUsuarios =usuarios;
        return fragment;
    }

    @Override
    public void itemClic(Usuario usuario) {
        Intent intent = new Intent(UsuariosTab.this.getActivity(), UsuarioActivity.class);
        intent.putExtra(Constantes.EXTRA_ID_USUARIO, Integer.parseInt(usuario.getId()));
        startActivity(intent);
    }
}