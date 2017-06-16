package com.asahary.foodnet.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Evento;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Tecnica;
import com.asahary.foodnet.Principal.Comentarios.ComentariosDialog;
import com.asahary.foodnet.Principal.GlosarioFragment;
import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Timeline.EventoFragment;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,EventoFragment.Echador,RecetaTab.Echador{
    public BottomNavigationView bottomView;
    private CircleImageView imgNav;
    private TextView nav_user,nav_userTitle;
    public FragmentManager gestor;
    ImageView imgCarga;


    private void initVistas(){
        bottomView= (BottomNavigationView) findViewById(R.id.bottom_navigation);
        gestor=getSupportFragmentManager();
        imgCarga= (ImageView) findViewById(R.id.imgCarga);

        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mnuFavoritos:
                       cargarFragmentoMisFavoritos();
                        break;
                    case R.id.mnuHome:
                        cargarFragmentoMisEventos();
                        break;
                    case R.id.mnuAgregar:
                        Intent intent=new Intent(MainActivity.this, AgregarRecetaActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        //Los llamamos aqui en lugar de iniciar listas
        crearGlosario();
        bottomView.setSelectedItemId(R.id.mnuHome);
        cargarFragmentoMisEventos();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(bottomView.getSelectedItemId()==R.id.mnuFavoritos){
            cargarFragmentoMisFavoritos();
        }else {
            cargarFragmentoMisEventos();
        }
    }
    //Obtencion de listas por defecto
    public void obtenerListas(){
        CookNetService service= Libreria.obtenerServicioApi();

        Call<List<Usuario>> callSeguidores=service.seguidoresUser(CacheApp.user.getId());
        Call<List<Usuario>> callSeguidos=service.seguidosUser(CacheApp.user.getId());
        Call<List<Receta>> callFavoritos=service.favoritosUser(CacheApp.user.getId());
        Call<List<Receta>> callRecetas=service.recetasUser(CacheApp.user.getId());

        callSeguidores.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> lista=response.body();

                if(lista!=null){
                    CacheApp.misSeguidores=new ArrayList<Usuario>(lista);
                }else{
                   // Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_NULA);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                //Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_FALLIDA);
            }
        });

        callSeguidos.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> lista=response.body();

                if(lista!=null){
                    CacheApp.misSiguiendo=new ArrayList<Usuario>(lista);
                }else{
                    //Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_NULA);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                //Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_FALLIDA);
            }
        });

        callFavoritos.enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> lista=response.body();

                if(lista!=null){
                    CacheApp.misFavoritos=new ArrayList<Receta>(lista);
                }else{
                    //Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_NULA);
                }
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
                //Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_FALLIDA);
            }
        });

        callRecetas.enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> lista=response.body();

                if(lista!=null){
                    CacheApp.misRecetas=new ArrayList<Receta>(lista);
                }else{
                   //Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_NULA);
                }
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
                //Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_FALLIDA);
            }
        });

    }



    //Carga de fragmentos
    public void cargarFragmento(int id, Fragment frag) {
        if(!isFinishing()){
            if (getFragmentManager().findFragmentById(R.id.fragment) != null) {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment)).commit();
            }
            FragmentTransaction transaccion = gestor.beginTransaction();
            transaccion.replace(id, frag);
            transaccion.commitAllowingStateLoss();
        }

    }
    public void cargarFragmentoMisFavoritos(){
        mostrarCarga();
        setTitle(Constantes.TITULO_FAVORITOS);
        Libreria.obtenerServicioApi().favoritosUser(CacheApp.user.getId()).enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                List<Receta> lista=response.body();

                if(lista!=null){
                    CacheApp.misFavoritos=new ArrayList<Receta>(lista);
                }else{
                    CacheApp.misFavoritos=new ArrayList<Receta>();
                }
                cargarFragmento(R.id.fragment, RecetaTab.newInstance(CacheApp.misFavoritos,MainActivity.this));
                ocultarCarga();
            }

            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, RecetaTab.newInstance(CacheApp.misFavoritos,MainActivity.this));
                ocultarCarga();
            }
        });
    }
    public void cargarFragmentoMisEventos(){
        mostrarCarga();
        setTitle(Constantes.TITULO_HOME);
        Libreria.obtenerServicioApi().eventosUser(CacheApp.user.getId()).enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                List<Evento> lista=response.body();

                if(lista!=null){
                    Collections.sort(lista,Evento.FechaComparator);
                    CacheApp.misEventos=new ArrayList<Evento>(lista);
                }else{
                    CacheApp.misEventos=new ArrayList<Evento>();
                }
                cargarFragmento(R.id.fragment, EventoFragment.newInstance(CacheApp.misEventos,MainActivity.this));
                ocultarCarga();
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Libreria.mostrarMensjeCorto(MainActivity.this,Constantes.RESPUESTA_FALLIDA);
                cargarFragmento(R.id.fragment, EventoFragment.newInstance(CacheApp.misEventos,MainActivity.this));
                ocultarCarga();
            }
        });
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
        RelativeLayout hView = (RelativeLayout) navigationView.getHeaderView(0);

        //De la cabecera sacamos la vista contenedora, sobre esta vista que sacamos no podemos sacar los hijos
        // vista los debemos sacar el hView
        RelativeLayout a= (RelativeLayout) hView.findViewById(R.id.navigation_header_container);


        //Obtenemos el texto de la cabecera
        nav_userTitle= (TextView) hView.findViewById(R.id.textViewTitle);
        nav_user = (TextView)hView.findViewById(R.id.textView);

        //Obtenemos la imagen
        imgNav = (CircleImageView) hView.findViewById(R.id.imageView);

        //Rellenamos

        rellenarCabecera();


       a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PerfilActivity.class);
                startActivity(intent);
            }
        });






        navigationView.setNavigationItemSelectedListener(this);

        //Iniciamos el resto de vistas
        initVistas();

        //Obtenemos las listas por defecto
        obtenerListas();
    }
    private void rellenarCabecera() {
        nav_userTitle.setText(CacheApp.user.getNick());
        nav_user.setText("Toca para ver tu perfil");
        Picasso.with(this).load(CacheApp.user.getImagen()).placeholder(R.drawable.user_generic).error(R.drawable.user_generic).fit().into(imgNav);
    }
    private void crearGlosario(){
        String[] lista =Constantes.GLOSARIO.split(":");
        ArrayList<Tecnica> tecnicas=new ArrayList<>();


        for(int i=0;i<lista.length;i++){
            String[] item=lista[i].split("=");
            String nombre=item[0];
            String descripcion="";
            if(item.length>1){
                descripcion=item[1];
            }
            tecnicas.add(new Tecnica(nombre,descripcion));
        }
        CacheApp.glosario=tecnicas;
    }

    //Muestra o no una imagen de carga
    public void mostrarCarga(){
        findViewById(R.id.fragment).setVisibility(View.GONE);
        imgCarga.setVisibility(View.VISIBLE);
        Glide.with(this).load(R.drawable.loading).fitCenter().fitCenter().into(imgCarga);
    }
    public void ocultarCarga(){
        findViewById(R.id.fragment).setVisibility(View.VISIBLE);
        imgCarga.setVisibility(View.GONE);
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

        Intent intentNavigation=new Intent(MainActivity.this,NavigatorActivity.class);


        int id = item.getItemId();

        if (id == R.id.nav_recetas) {
            intentNavigation.putExtra(Constantes.EXTRA_OPCION_LISTA,Constantes.EXTRA_LISTA_MIS_RECETA);
            startActivity(intentNavigation);
        } else if (id == R.id.nav_seguidores) {
            intentNavigation.putExtra(Constantes.EXTRA_OPCION_LISTA,Constantes.EXTRA_LISTA_MIS_SEGUIDORES);
            startActivity(intentNavigation);
        } else if (id == R.id.nav_siguiendo) {
            intentNavigation.putExtra(Constantes.EXTRA_OPCION_LISTA,Constantes.EXTRA_LISTA_MIS_SEGUIDOS);
            startActivity(intentNavigation);
        } else if (id == R.id.nav_log_out) {
            CacheApp.limpiarListas();
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        }else if(id==R.id.nav_glosario){
            intentNavigation.putExtra(Constantes.EXTRA_OPCION_LISTA,Constantes.EXTRA_LISTA_GLOSARIO);
            startActivity(intentNavigation);
        }else if(id==R.id.nav_about){
            new AlertDialog.Builder(MainActivity.this).setTitle("Sobre...").setMessage("CookNet \nCreado por Omar A.Ayala 2017").setPositiveButton("Ok",null).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void echar(Evento evento) {
        Retrofit retrofit=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();
        CookNetService service = retrofit.create(CookNetService.class);

        mostrarCarga();
        switch (evento.getTipo()){
            case Constantes.EVENTO_FAVORITO:
                Call<Receta> call = service.getReceta(evento.getIdReceta());
                call.enqueue(new Callback<Receta>() {
                    @Override
                    public void onResponse(Call<Receta> call, Response<Receta> response) {
                        Receta cuerpo=response.body();

                        if(cuerpo!=null){
                            Intent intent=new Intent(MainActivity.this, RecetaActivity.class);
                            intent.putExtra(Constantes.EXTRA_RECETA,cuerpo);
                            ocultarCarga();
                            startActivity(intent);

                        }
                        ocultarCarga();
                    }

                    @Override
                    public void onFailure(Call<Receta> call, Throwable t) {
ocultarCarga();
                    }
                });
                break;
            case Constantes.EVENTO_SEGUIR:
                Call<Usuario> call3 = service.getUsuario(evento.getIdUser());
                call3.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        Usuario usuario=response.body();

                        if(usuario!=null){
                            Intent intentUser=new Intent(MainActivity.this,UsuarioActivity.class);
                            intentUser.putExtra(Constantes.EXTRA_USUARIO,usuario);
                            startActivity(intentUser);
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {

                    }
                });
                break;

            case Constantes.EVENTO_COMENTAR:
                Call<Receta> call2 = service.getReceta(evento.getIdReceta());
                call2.enqueue(new Callback<Receta>() {
                    @Override
                    public void onResponse(Call<Receta> call, Response<Receta> response) {
                        Receta cuerpo=response.body();

                        if(cuerpo!=null){
                            ComentariosDialog dialog=new ComentariosDialog();
                            Bundle extra = new Bundle();
                            extra.putParcelable(Constantes.EXTRA_RECETA,cuerpo);
                            dialog.setArguments(extra);
                            dialog.show(getSupportFragmentManager(),"Comentarios");
                        }
                    }

                    @Override
                    public void onFailure(Call<Receta> call, Throwable t) {

                    }
                });
                break;
        }
    }

    @Override
    public void echar(Receta receta) {
        mostrarCarga();
        Intent intent=new Intent(MainActivity.this, RecetaActivity.class);
        intent.putExtra(Constantes.EXTRA_RECETA,receta);
        ocultarCarga();
        startActivity(intent);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }
}
