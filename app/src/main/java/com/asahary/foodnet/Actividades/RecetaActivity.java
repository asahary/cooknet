package com.asahary.foodnet.Actividades;

import android.content.Intent;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Comentarios.ComentariosDialog;
import com.asahary.foodnet.Principal.Rating.RatingDialog;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;
import com.bumptech.glide.Glide;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecetaActivity extends AppCompatActivity implements RatingDialog.OnDismissListener{

    TextView txtNombre,txtDescripcion,txtIngredientes,txtPreparacion, lblNombreUsuario;
    ImageView imgReceta;
    Receta receta;
    Menu menu;
    RatingBar ratingBar;
    boolean favorito=false;
    public Float puntuacion=0.0f;

    public static final int MODIFICAR_RECETA=9;




    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(Constantes.EXTRA_RECETA,receta);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        receta=savedInstanceState.getParcelable(Constantes.EXTRA_RECETA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(savedInstanceState==null&&getIntent().hasExtra(Constantes.EXTRA_RECETA)){
            receta=getIntent().getParcelableExtra(Constantes.EXTRA_RECETA);
            setTitle(receta.getNombre());

            initVistas();
        }else{
            receta=savedInstanceState.getParcelable(Constantes.EXTRA_RECETA);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.receta_menu,menu);
        this.menu=menu;

        favorito=comprobarFav();

        if(CacheApp.user.getId()!=Integer.valueOf(receta.getIdUsuario())){
            menu.getItem(2).setVisible(false);
        }
        if(!comprobarFav()){
            menu.getItem(0).setIcon(R.drawable.ic_favorite);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_fav:
                actulizarFavorito();
                break;
            case R.id.action_comment:
                ComentariosDialog dialog=new ComentariosDialog();
                Bundle extra = new Bundle();
                extra.putParcelable(Constantes.EXTRA_RECETA,receta);
                dialog.setArguments(extra);
                dialog.show(getSupportFragmentManager(),"Comentarios");
                break;
            case R.id.action_edit:
                Intent intent=new Intent(RecetaActivity.this,EditarRecetaActivity.class);
                intent.putExtra(Constantes.EXTRA_RECETA,receta);
                startActivityForResult(intent,MODIFICAR_RECETA);
                break;
        }
        return true;
    }

    private void actulizarFavorito(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService service = retrofit.create(CookNetService.class);


        //Por algun motivo si pasamos un bool retrofit no hace bien la conversion
        //por eso le pasamos un int
        Call<String> call = service.actulizarFavorito(favorito?1:0,receta.getIdUsuario(),receta.getIdReceta(),CacheApp.user.getId());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String mensaje=response.body();

                if(mensaje!=null){
                    if(favorito){
                        favorito=false;
                        CacheApp.misFavoritos.remove(receta);
                        menu.getItem(0).setIcon(R.drawable.ic_favorite);
                    }else{
                        favorito=true;
                        CacheApp.misFavoritos.add(receta);
                        menu.getItem(0).setIcon(R.drawable.ic_fav_black);
                    }
                }else{
                    Libreria.mostrarMensjeCorto(RecetaActivity.this,Constantes.RESPUESTA_NULA);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RecetaActivity.this,"Respuesta fallida",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean comprobarFav(){
        boolean fav=false;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService service = retrofit.create(CookNetService.class);

        Call<Boolean> call = service.comprobarFavorito(receta.getIdReceta(),CacheApp.user.getId());

        try {
            Response<Boolean> response = call.execute();

            if(response.isSuccessful()){
                if(response.body()!=null){
                    fav=response.body();
                }else{
                }
            }else {
                Toast.makeText(this,"No se pudo obtener si es favorito",Toast.LENGTH_SHORT).show();;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fav;
    }
    private void initVistas() {
        lblNombreUsuario = (TextView) findViewById(R.id.lblNombreUsuario);
        txtNombre= (TextView) findViewById(R.id.txtNombre);
        txtDescripcion= (TextView) findViewById(R.id.txtDescripcion);
        txtIngredientes= (TextView) findViewById(R.id.Ingredientes);
        txtPreparacion= (TextView) findViewById(R.id.txtPreparacion);
        imgReceta= (ImageView) findViewById(R.id.imgReceta);
        ratingBar= (RatingBar) findViewById(R.id.rating);


        //No se hace onclick porque onTouch lo sobrescribe
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    RatingDialog dialog=new RatingDialog();
                    Bundle extras=new Bundle();
                    extras.putParcelable(Constantes.EXTRA_RECETA,receta);
                    extras.putDouble("puntuacion",puntuacion);
                    dialog.setArguments(extras);
                    dialog.show(getSupportFragmentManager(),"Rating");
                }
                return true;
            }
        });
        lblNombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Libreria.obtenerServicioApi().getUsuario(receta.getIdUsuario()).enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        Usuario cuerpo=response.body();

                        if(cuerpo!=null){
                            Intent intentUser=new Intent(RecetaActivity.this,UsuarioActivity.class);
                            intentUser.putExtra(Constantes.EXTRA_USUARIO,cuerpo);
                            startActivity(intentUser);
                        }else{
                            Libreria.mostrarMensjeCorto(RecetaActivity.this,Constantes.RESPUESTA_NULA);
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Libreria.mostrarMensjeCorto(RecetaActivity.this,Constantes.RESPUESTA_FALLIDA);
                    }
                });
            }
        });
        rellenarCampos();
    }

    private void rellenarCampos(){
        int idUsuario=receta.getIdUsuario();
        txtNombre.setText(receta.getNombre());
        txtDescripcion.setText(receta.getDescripcion());
        txtIngredientes.setText(leerIngredientes());
        txtPreparacion.setText(receta.getPreparacion());
        Glide.with(this).load(receta.getImagen()).thumbnail(Glide.with(this).load(R.drawable.loading)).fitCenter().error(R.drawable.food_generic).into(imgReceta);
        rellanarPuntuacion();

        Libreria.obtenerServicioApi().getUsuario(idUsuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario user=response.body();

                if(user!=null){
                    lblNombreUsuario.setText(user.getNick());
                }else{
                    Libreria.mostrarMensjeCorto(RecetaActivity.this,"El nombre es nulo");
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(RecetaActivity.this,"Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void rellanarPuntuacion() {
        Libreria.obtenerServicioApi().obtenerPuntuacion(receta.getIdReceta()).enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {


                if(response!=null){
                    puntuacion=response.body();
                    ratingBar.setRating(puntuacion);
                }else{
                    Libreria.mostrarMensjeCorto(RecetaActivity.this,"No hay informacion sobre puntuacion");
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Libreria.mostrarMensjeCorto(RecetaActivity.this,"No se puede obtener la puntuacion");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent responseIntent=data;
        if(resultCode==RESULT_OK){
            if(requestCode==MODIFICAR_RECETA){
                receta=responseIntent.getParcelableExtra(Constantes.EXTRA_RECETA);
                rellenarCampos();
            }
        }

    }

    //Separa la cadena de ingredientes para darle formato
    private String leerIngredientes(){
        String formato="";


        //Obtenemos es array de medidas
        String[] medidas=getResources().getStringArray(R.array.medidas);

        String[] ingredientes =receta.getIngredientes().split("%");

        for (int i=0;i<ingredientes.length;i++){
            String[] componentes= ingredientes[i].split(":");
            String miniFormato="";

            for(int x=0;x<componentes.length;x++){

                //Si x==1 quiere decir que ese es el campo de la medida por lo que la traducimos
                if(x==1){
                    String medida=componentes[x];
                    int medidaN=Integer.parseInt(medida);
                    miniFormato+=medidas[medidaN]+"  ";
                }else{
                    miniFormato+=componentes[x]+"  ";
                }
            }
            formato+=miniFormato+"\n";
        }
        return  formato;
    }

    @Override
    public void onDismiss() {
        rellanarPuntuacion();
    }
}
