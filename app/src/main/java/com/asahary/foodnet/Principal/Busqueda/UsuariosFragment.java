package com.asahary.foodnet.Principal.Busqueda;

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
import android.widget.Toast;

import com.asahary.foodnet.Actividades.BusquedaActivity;
import com.asahary.foodnet.Adaptadores.UsuariosAdapter;
import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Actividades.MainActivity;
import com.asahary.foodnet.Actividades.UsuarioActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saha on 25/05/2017.
 */

public class UsuariosFragment extends Fragment implements UsuariosAdapter.OnReciclerItemClickListener,BusquedaActivity.OnTextToolbarTextChange{

    UsuariosAdapter adaptador;
    RecyclerView lista;
    ArrayList<Usuario> usuarios=new ArrayList<>();
    View emptyView;

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
        lista= (RecyclerView) vista.findViewById(R.id.lista);
        adaptador=new UsuariosAdapter(usuarios,this);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        emptyView=vista.findViewById(R.id.emptyView);
        iniciarLista();

    }

    private void iniciarLista(){
        Libreria.obtenerServicioApi().listUsers(CacheApp.user.getId()).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> respuesta=response.body();

                if(respuesta!=null){
                    usuarios=new ArrayList<Usuario>(respuesta);
                    adaptador.swapDatos(usuarios);
                }else{
                    Toast.makeText(getContext(),"cuerpo nullo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(),"respuesta fallida",Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void itemClic(Usuario usuario) {
        Intent intentUser=new Intent(UsuariosFragment.this.getContext(),UsuarioActivity.class);
        intentUser.putExtra(Constantes.EXTRA_USUARIO,usuario);
        startActivity(intentUser);
    }


    @Override
    public void onTextChanged(String text) {
        String texto=text;
        ArrayList<Usuario> nuevosUsuarios= new ArrayList<Usuario>();

        //Si el campo de texto no esta vacio creamos una nueva lista con todos los usuarios que coincidan con el texto
        if(!texto.isEmpty()){


            for (int i=0;i<usuarios.size();i++){
                Usuario usuario=usuarios.get(i);
                String nick=usuario.getNick();
                String nombre=usuario.getNombre();
                String apellidos=usuario.getApellidos();

                if(nick.contains(texto)||nombre.contains(texto)||apellidos.contains(texto)){
                    nuevosUsuarios.add(usuario);
                }
            }

            adaptador.swapDatos(nuevosUsuarios);

        }else{//Si el texto esta vacio mostramos a todos los usuarios
            adaptador.swapDatos(usuarios);
        }
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
