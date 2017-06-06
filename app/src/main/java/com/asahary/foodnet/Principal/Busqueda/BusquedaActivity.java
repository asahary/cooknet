package com.asahary.foodnet.Principal.Busqueda;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;


import com.asahary.foodnet.Principal.Favoritos.FavoritosFragment;
import com.asahary.foodnet.Principal.Usuario.RecetaTab;
import com.asahary.foodnet.Principal.Usuario.UsuarioActivity;
import com.asahary.foodnet.Principal.Usuario.UsuariosTab;
import com.asahary.foodnet.R;

import java.util.ArrayList;
import java.util.List;

public class BusquedaActivity extends AppCompatActivity {
    ViewPager viewPager;
    FragmentManager gestor;
    ViewPagerAdapter viewPagerAdapter;
    EditText txtText;
    OnTextToolbarTextChange listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        //Para que no se habra el teclado por la cara al entrar
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initVistas();
        configViewPager();
    }

    private void initVistas() {
        gestor=getSupportFragmentManager();
        txtText= (EditText) findViewById(R.id.myEditText);
        viewPager = (ViewPager) findViewById(R.id.bottom_navigation);
        txtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onTextChanged(txtText.getText().toString());
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
    private void configViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(RecetasFragment.newInstance(), "Recetas", R.drawable.ic_camera);
        viewPagerAdapter.addFragment(UsuariosFragment.newInstance(), "Usuarios", R.drawable.ic_camera);



        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                listener= (OnTextToolbarTextChange) viewPagerAdapter.getItem(position);
                listener.onTextChanged(txtText.getText().toString());
            }

            public void onPageSelected(int position) {
                listener= (OnTextToolbarTextChange) viewPagerAdapter.getItem(position);

            }

            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0);
        listener= (OnTextToolbarTextChange) viewPagerAdapter.getItem(0);
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

    interface OnTextToolbarTextChange{
        void onTextChanged(String Text);
    }
}
