package com.asahary.foodnet.Principal.Busqueda;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Agregar.AgregarFragment;
import com.asahary.foodnet.R;

import java.util.ArrayList;

/**
 * Created by Saha on 25/05/2017.
 */

public class UsuariosFragment extends Fragment {

    EditText txtTexto;
    Spinner spCategorias;
    ArrayList<Usuario> usuarios=new ArrayList<>();

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
        View vista= getView();
        initVistas(vista);
    }

    private void initVistas(View vista)  {
        txtTexto= (EditText) vista.findViewById(R.id.txtText);
        spCategorias= (Spinner) vista.findViewById(R.id.spCategoria);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //Quitamos los botones innecesarios
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


    public static UsuariosFragment newInstance(){
        return new UsuariosFragment();
    }
}
