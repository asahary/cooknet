package com.asahary.foodnet.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.asahary.foodnet.Actividades.LogInActivity;
import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Agregar.AgregarFragment;
import com.asahary.foodnet.Principal.Agregar.AgregarRecetaActivity;
import com.asahary.foodnet.Principal.Busqueda.BusquedaActivity;
import com.asahary.foodnet.Principal.Favoritos.FavoritosFragment;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Integer idUsuario;
    private BottomNavigationView bottomView;
    private FragmentManager gestor;

    private void initVistas(){


        bottomView= (BottomNavigationView) findViewById(R.id.bottom_navigation);

        gestor=getSupportFragmentManager();


       bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnuFavoritos:
                       cargarFragmento(R.id.fragment, FavoritosFragment.newInstance());
                        break;
                    case R.id.mnuHome:
                        break;
                    case R.id.mnuAgregar:
                        Intent intent=new Intent(MainActivity.this, AgregarRecetaActivity.class);
                        startActivity(intent);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Para que no se habra el teclado por la cara al entrar
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //Aqui se obtiene el navigation View
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Sacamos la cabecera del mismo
        View hView =  navigationView.getHeaderView(0);

        //Obtenemos el texto de la cabecera
        TextView nav_user = (TextView)hView.findViewById(R.id.textView);

        nav_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,EditarUsuarioActivity.class);
                //Hay que meter al usuario
                startActivityForResult(intent,0);
            }
        });

        //Obtenemos la imagen
        CircleImageView imgMav= (CircleImageView) hView.findViewById(R.id.imageView);

        Picasso.with(this).load(R.drawable.user_generic).fit().into(imgMav);
        nav_user.setText("holaaaa");
        navigationView.setNavigationItemSelectedListener(this);

        //Obtenemos el intent y obtenemos el id de usuario que tenemos guardado
        Bundle extras = getIntent().getExtras();
        idUsuario=extras.getInt(Constantes.ID_USUARIO);
        initVistas();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, BusquedaActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recetas) {

        } else if (id == R.id.nav_seguidores) {

        } else if (id == R.id.nav_siguiendo) {

        } else if (id == R.id.nav_log_out) {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
