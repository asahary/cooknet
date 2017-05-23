package com.asahary.foodnet.Principal.Agregar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.Favoritos.FavoritosAdapter;
import com.asahary.foodnet.Principal.Favoritos.FavoritosFragment;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 23/05/2017.
 */

public class AgregarFragment extends Fragment {

    RecyclerView lista;
    AgregarAdapter adaptador;
    ImageButton btnAgregar;
    ArrayList<Ingrediente> ingredientes=new ArrayList<>();


    public AgregarFragment(){
    }

    public void initVistas(View vista){

        lista= (RecyclerView) vista.findViewById(R.id.lista);
        btnAgregar= (ImageButton) vista.findViewById(R.id.btnAgregar);
        adaptador=new AgregarAdapter(ingredientes);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(AgregarFragment.this.getContext(), LinearLayoutManager.VERTICAL, false));

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarLista();
            }
        });
    }

    private void iniciarLista() {
        ingredientes.add(new Ingrediente());
        adaptador.notifyDataSetChanged();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.agregar_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View vista= getView();
        initVistas(vista);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.agregar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static AgregarFragment newInstance(){
        return new AgregarFragment();
    }

}
