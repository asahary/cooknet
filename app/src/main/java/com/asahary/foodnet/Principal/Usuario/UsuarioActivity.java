package com.asahary.foodnet.Principal.Usuario;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Busqueda.RecetasFragment;
import com.asahary.foodnet.Principal.Busqueda.UsuariosFragment;
import com.asahary.foodnet.Principal.Favoritos.FavoritosFragment;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioActivity extends FragmentActivity {

    ViewPager viewPager;
    CircleImageView img;
    TextView lblNick,lblNombre,lblEmail;
    Button btnSigue;
    Usuario usuario;
    boolean sigue;
    int idUsuario;
    private ViewPagerAdapter viewPagerAdapter;


    //Las listas por defecto que estan vacias
    ArrayList<Receta> recetasFavoritos=new ArrayList<>();
    ArrayList<Receta> recetasPropias=new ArrayList<>();
    ArrayList<Usuario> seguidores=new ArrayList<>();
    ArrayList<Usuario> seguidos=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        usuario= (Usuario) getIntent().getExtras().get(Constantes.EXTRA_USUARIO);
        idUsuario= Integer.parseInt(usuario.getId());

        initVistas();
        rellenarCampos();
        comprobarSigue();
        configViewPager();
    }

    private void comprobarSigue(){
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();
        final CookNetService service = retrofit.create(CookNetService.class);
        Call<Boolean> call = service.comprobarSigue(MainActivity.idUsuario,idUsuario);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                sigue=response.body();
                configurarBotonFollow(sigue);
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

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
        lblEmail.setText(usuario.getEmail());
        Picasso.with(this).load(usuario.getImagen()).fit().into(img);
    }

    private void initVistas() {
        img= (CircleImageView) findViewById(R.id.imgUsuario);
        lblNick= (TextView) findViewById(R.id.lblNick);
        lblNombre= (TextView) findViewById(R.id.lblNombre);
        lblEmail= (TextView) findViewById(R.id.lblEmail);
        btnSigue= (Button) findViewById(R.id.btnfollow);
        btnSigue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit=new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
                final CookNetService service= retrofit.create(CookNetService.class);
                Call<Boolean> call=service.actualizarSigue(MainActivity.idUsuario,idUsuario,sigue?1:0);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()!=null){
                            sigue=response.body();
                            configurarBotonFollow(sigue);
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
        viewPagerAdapter.addFragment(RecetaTab.newInstance(recetasFavoritos), "Favoritos", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(RecetaTab.newInstance(recetasPropias), "Recetas", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(UsuariosTab.newInstance(seguidos), "Siguiendo", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(UsuariosTab.newInstance(seguidores), "Seguidores", R.drawable.ic_camera);

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

        Call<List<Receta>> callFavoritos=service.favoritosUser(idUsuario);
        Call<List<Receta>> callPropias=service.recetasUser(idUsuario);
        Call<List<Usuario>> callSiguiendo=service.seguidosUser(idUsuario);
        Call<List<Usuario>> callSeguidores=service.seguidoresUser(idUsuario);

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
