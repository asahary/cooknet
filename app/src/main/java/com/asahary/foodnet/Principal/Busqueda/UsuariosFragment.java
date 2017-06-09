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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asahary.foodnet.Adaptadores.UsuariosAdapter;
import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.Principal.Timeline.EventoFragment;
import com.asahary.foodnet.Principal.Usuario.UsuarioActivity;
import com.asahary.foodnet.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 25/05/2017.
 */

public class UsuariosFragment extends Fragment implements UsuariosAdapter.OnReciclerItemClickListener,BusquedaActivity.OnTextToolbarTextChange{

    UsuariosAdapter adaptador;
    RecyclerView lista;
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
        lista= (RecyclerView) vista.findViewById(R.id.lista);
        adaptador=new UsuariosAdapter(usuarios,this);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        iniciarLista();

    }

    private void iniciarLista(){
        Retrofit retrofit=new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService servicio = retrofit.create(CookNetService.class);
        Call<List<Usuario>> call=servicio.listUsers(MainActivity.idUsuario);

        call.enqueue(new Callback<List<Usuario>>() {
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

        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();
        CookNetService service = retrofit.create(CookNetService.class);

        Call<Usuario> call3 = service.getUsuario(Integer.parseInt(usuario.getId()));
        call3.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario usuario=response.body();

                if(usuario!=null){
                    Intent intentUser=new Intent(UsuariosFragment.this.getContext(),UsuarioActivity.class);
                    intentUser.putExtra(Constantes.EXTRA_USUARIO,usuario);
                    startActivity(intentUser);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });

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
}
