package com.asahary.foodnet.Principal.Busqueda;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.asahary.foodnet.R;

public class BusquedaActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    FragmentManager gestor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        //Para que no se habra el teclado por la cara al entrar
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initVistas();
    }

    private void initVistas() {
        gestor=getSupportFragmentManager();
        navigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);

        setTitle("jajaja");
        //Configuracion del bottom_menu_view
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    //Busqueda de usuarios
                    case R.id.mnuUsuarios:
                        cargarFragmento(R.id.fragment, UsuariosFragment.newInstance());
                        break;
                    //Busqueda de usuarios
                    case R.id.mnuRecetas:
                        cargarFragmento(R.id.fragment, RecetasFragment.newInstance());
                        break;
                }
                return true;
            }
        });
    }

    public void cargarFragmento(int id, Fragment frag) {
        if (getFragmentManager().findFragmentById(R.id.fragment) != null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment)).commit();
        }
        FragmentTransaction transaccion = gestor.beginTransaction();
        transaccion.replace(id, frag);
        transaccion.commit();

    }


}
