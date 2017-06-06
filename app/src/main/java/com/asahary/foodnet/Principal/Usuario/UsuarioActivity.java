package com.asahary.foodnet.Principal.Usuario;

import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.TextView;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
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

    private static final String ARG_RECETAS = "recetas";
    private static final String ARG_SEGUIDORES = "seguidores";
    private static final String ARG_SIGUIENDO = "siguiendo";

    ViewPager viewPager;
    CircleImageView img;
    TextView lblNick,lblNombre,lblEmail;
    Button btnSigue;
    Usuario usuario;
    boolean sigue;
    int idUsuario;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        idUsuario= (int) getIntent().getExtras().get(Constantes.EXTRA_ID_USUARIO);

        initVistas();
        obtenerUsuario();
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

                if(sigue){
                    btnSigue.setText("Unfollow");
                }else{
                    btnSigue.setText("Follow");
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
    private void obtenerUsuario(){
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();
        CookNetService service = retrofit.create(CookNetService.class);
        Call<Usuario> call = service.getUsuario(idUsuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                usuario=response.body();

                if(usuario!=null){
                    rellenarCampos();
                    comprobarSigue();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });

    }

    private void rellenarCampos() {
        lblNick.setText(usuario.getNick());
        lblNombre.setText(usuario.getNombre());
        lblEmail.setText(usuario.getEmail());
        Picasso.with(this).load(usuario.getImagen()).error(R.drawable.user_generic).into(img);
    }

    private void initVistas() {
        img= (CircleImageView) findViewById(R.id.imgUsuario);
        lblNick= (TextView) findViewById(R.id.lblNick);
        lblNombre= (TextView) findViewById(R.id.lblNombre);
        lblEmail= (TextView) findViewById(R.id.lblEmail);
        btnSigue= (Button) findViewById(R.id.btnfollow);
    }

    private void configViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(RecetaTab.newInstance(idUsuario,RecetaTab.OPCION_FAVORITOS), "Favoritos", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(RecetaTab.newInstance(idUsuario,RecetaTab.OPCION_PROPIAS), "Recetas", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(UsuariosTab.newInstance(idUsuario,UsuariosTab.OPCION_SIGUIENDO), "Siguiendo", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(UsuariosTab.newInstance(idUsuario,UsuariosTab.OPCION_SEGUIDORES), "Seguidores", R.drawable.ic_camera);



        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {

            }

            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0);



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
