package com.asahary.foodnet.Actividades;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.Principal.Usuario.UsuariosTab;
import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioActivity extends FragmentActivity implements RecetaTab.Echador,UsuariosTab.Echador{

    ViewPager viewPager;
    CircleImageView img;
    TextView lblNick,lblNombre,lblApellidos,lblEmail;
    Button btnSigue;
    Usuario usuario;
    Boolean sigue;
    private ViewPagerAdapter viewPagerAdapter;


    //Las listas por defecto que estan vacias
    ArrayList<Receta> recetasFavoritos=new ArrayList<>();
    ArrayList<Receta> recetasPropias=new ArrayList<>();
    ArrayList<Usuario> seguidores=new ArrayList<>();
    ArrayList<Usuario> seguidos=new ArrayList<>();

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(Constantes.EXTRA_USUARIO,usuario);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        usuario=savedInstanceState.getParcelable(Constantes.EXTRA_USUARIO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        if(!isFinishing()){
        if(savedInstanceState==null&&getIntent().hasExtra(Constantes.EXTRA_USUARIO)){
            usuario=getIntent().getParcelableExtra(Constantes.EXTRA_USUARIO);
        }else{
            usuario=savedInstanceState.getParcelable(Constantes.EXTRA_USUARIO);
        }

        setTitle(usuario.getNick());
        if(usuario.getId()==CacheApp.user.getId()){
            startActivity(new Intent(UsuarioActivity.this, PerfilActivity.class));
            finish();
        }else{
            initVistas();
            rellenarCampos();
            comprobarSigue();
            configViewPager();
        }
        }
    }

    private void comprobarSigue(){
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();
        final CookNetService service = retrofit.create(CookNetService.class);
        Call<Boolean> call = service.comprobarSigue(CacheApp.user.getId(),usuario.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                sigue=response.body();
                if(sigue!=null){
                    configurarBotonFollow(sigue);

                }else{
                    Libreria.mostrarMensjeCorto(UsuarioActivity.this,"No se pudo comprobar si se sigue");
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Libreria.mostrarMensjeCorto(UsuarioActivity.this,"No se pudo comprobar si se sigue");
            }
        });
    }

    private void configurarBotonFollow(boolean bandera) {
        if(bandera){
            btnSigue.setText("Unfollow");
        }else{
            btnSigue.setText("Follow");
        }
    }

    private void rellenarCampos() {
        lblNick.setText(usuario.getNick());
        lblNombre.setText(usuario.getNombre());
        lblApellidos.setText(usuario.getApellidos());
        lblEmail.setText(usuario.getEmail());
        Picasso.with(this).load(usuario.getImagen()).error(R.drawable.user_generic).placeholder(R.drawable.user_generic).fit().into(img);
    }

    private void initVistas() {
        img= (CircleImageView) findViewById(R.id.imgUsuario);
        lblNick= (TextView) findViewById(R.id.lblNick);
        lblNombre= (TextView) findViewById(R.id.lblNombre);
        lblApellidos= (TextView) findViewById(R.id.lblApellidos);
        lblEmail= (TextView) findViewById(R.id.lblEmail);
        btnSigue= (Button) findViewById(R.id.btnfollow);
        btnSigue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit=new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
                final CookNetService service= retrofit.create(CookNetService.class);
                Call<Boolean> call=service.actualizarSigue(CacheApp.user.getId(),usuario.getId(),sigue?1:0);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()!=null){
                            sigue=response.body();
                            configurarBotonFollow(sigue);

                            if(sigue){
                                seguidores.add(CacheApp.user);
                            }else{
                                for(int i=0;i<seguidores.size();i++){
                                    int id=seguidores.get(i).getId();
                                    if(id==CacheApp.user.getId()){
                                        seguidores.remove(seguidores.get(i));
                                        break;
                                    }
                                }
                            }
                            ((UsuariosTab)viewPagerAdapter.getItem(3)).adaptador.swapDatos(seguidores);
                        }else{
                            Toast.makeText(UsuarioActivity.this,"Cuerpor nullo",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(UsuarioActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void configViewPager() {

        //Para no hacer una llamada a la api cada vez que se cambia de fragmento descargamos todas las listas al inicio
        //Creamos el view pager y su adaptador
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(RecetaTab.newInstance(recetasFavoritos,this), "Favoritos", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(RecetaTab.newInstance(recetasPropias,this), "Recetas", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(UsuariosTab.newInstance(seguidos,this), "Siguiendo", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(UsuariosTab.newInstance(seguidores,this), "Seguidores", R.drawable.ic_camera);

        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        //Con este metodo le decimos el numero de paginas que debe mantener de otra manera las destruye
        //Tambien debemos hacerlo porque por defecto el viewPager tiene un numero de 2 paginas por lo
        // nos dara nullo al intentar usar alguna mas
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount()-1);

        //Creamos el restrofit y el servicio que hara las llamadas
        Retrofit retrofit= new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService service= retrofit.create(CookNetService.class);

        Call<List<Receta>> callFavoritos=service.favoritosUser(usuario.getId());
        Call<List<Receta>> callPropias=service.recetasUser(usuario.getId());
        Call<List<Usuario>> callSiguiendo=service.seguidosUser(usuario.getId());
        Call<List<Usuario>> callSeguidores=service.seguidoresUser(usuario.getId());

        callFavoritos.enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                if(response.body()!=null){
                    recetasFavoritos=new ArrayList<Receta>(response.body());
                    ((RecetaTab)viewPagerAdapter.getItem(0)).adaptador.swapDatos(recetasFavoritos);
                }
            }
            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {

            }
        });

        callPropias.enqueue(new Callback<List<Receta>>() {
            @Override
            public void onResponse(Call<List<Receta>> call, Response<List<Receta>> response) {
                if(response.body()!=null){
                    recetasPropias=new ArrayList<Receta>(response.body());
                    ((RecetaTab)viewPagerAdapter.getItem(1)).adaptador.swapDatos(recetasPropias);
                }
            }
            @Override
            public void onFailure(Call<List<Receta>> call, Throwable t) {

            }
        });

        callSiguiendo.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.body()!=null){
                    seguidos=new ArrayList<Usuario>(response.body());
                    ((UsuariosTab)viewPagerAdapter.getItem(2)).adaptador.swapDatos(seguidos);
                }
            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });

        callSeguidores.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.body()!=null){
                    seguidores=new ArrayList<Usuario>(response.body());
                    ((UsuariosTab)viewPagerAdapter.getItem(3)).adaptador.swapDatos(seguidores);
                }
            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });

    }

    @Override
    public void echar(Receta receta) {
        Intent intent=new Intent(UsuarioActivity.this, RecetaActivity.class);
        intent.putExtra(Constantes.EXTRA_RECETA,receta);
        startActivity(intent);
    }

    @Override
    public void echar(Usuario usuario) {
        Intent intentUser=new Intent(UsuarioActivity.this,UsuarioActivity.class);
        intentUser.putExtra(Constantes.EXTRA_USUARIO,usuario);
        startActivity(intentUser);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private final List<Integer> mFragmentIcons = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Añade un fragmento al adaptador. Recibe el fragmento, el título
        // para la tab y el icono para la tab.
        public void addFragment(Fragment fragment, String title,
                                int resIdIcon) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
            mFragmentIcons.add(resIdIcon);
        }

        // Retorna el fragmento correspondiente a la posición recibida.
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        // Retorna el número de fragmentos del adaptador.
        @Override
        public int getCount() {
            return mFragments.size();
        }

        // Retorna el título asociado a una determinada página.
        @Override
        public CharSequence getPageTitle(int position) {
            return " " + mFragmentTitles.get(position);
        }

        // Retorna el resId del icono asociado a una determinada página.

        public int getPageIcon(int position) {
            return mFragmentIcons.get(position);
        }

    }
}
