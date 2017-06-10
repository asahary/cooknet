package com.asahary.foodnet.Principal;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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

import com.asahary.foodnet.Utilidades.Cache;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Comentarios.ComentariosDialog;
import com.asahary.foodnet.Principal.Rating.RatingDialog;
import com.asahary.foodnet.Principal.Usuario.UsuarioActivity;
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

    public static final int MODIFICAR_RECETA=9;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receta);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initVistas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.receta_menu,menu);
        this.menu=menu;

        favorito=comprobarFav();

        if(MainActivity.idUsuario!=Integer.valueOf(receta.getIdUsuario())){
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
        Call<String> call = service.actulizarFavorito(favorito?1:0,Integer.parseInt(receta.getIdUsuario()),Integer.parseInt(receta.getIdReceta()),MainActivity.idUsuario);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String mensaje=response.body();

                if(mensaje!=null){
                    if(favorito){
                        favorito=false;
                        Cache.misFavoritos.remove(receta);
                        menu.getItem(0).setIcon(R.drawable.ic_favorite);
                    }else{
                        favorito=true;
                        Cache.misFavoritos.add(receta);
                        menu.getItem(0).setIcon(R.drawable.ic_fav_black);
                    }
                    Libreria.mostrarMensjeCorto(RecetaActivity.this,mensaje);
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

        Call<Boolean> call = service.comprobarFavorito(Integer.parseInt(receta.getIdReceta()),MainActivity.idUsuario);

        try {
            Response<Boolean> response = call.execute();

            if(response.isSuccessful()){
                if(response.body()!=null){
                    fav=response.body();
                }else{
                    Toast.makeText(this,"Cuerpo nullo",Toast.LENGTH_SHORT).show();;
                }
            }else {
                Toast.makeText(this,"Respuesta fallida",Toast.LENGTH_SHORT).show();;
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


        receta=getIntent().getParcelableExtra(Constantes.EXTRA_RECETA);

        //No se hace onclick porque onTouch lo sobrescribe
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    RatingDialog dialog=new RatingDialog();
                    Bundle extras=new Bundle();
                    extras.putParcelable(Constantes.EXTRA_RECETA,receta);
                    dialog.setArguments(extras);
                    dialog.show(getSupportFragmentManager(),"Rating");
                }
                return true;
            }
        });
        lblNombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecetaActivity.this, UsuarioActivity.class);
                intent.putExtra(Constantes.EXTRA_ID_USUARIO,Integer.parseInt(receta.getIdUsuario()));
                startActivity(intent);
            }
        });
        rellenarCampos();
    }

    private void rellenarCampos(){
        int idUsuario=Integer.parseInt(receta.getIdUsuario());
        txtNombre.setText(receta.getNombre());
        txtDescripcion.setText(receta.getDescripcion());
        txtIngredientes.setText(leerIngredientes());
        txtPreparacion.setText(receta.getPreparacion());
        Glide.with(this).load(receta.getImagen()).thumbnail(Glide.with(this).load(R.drawable.loading)).fitCenter().error(R.drawable.food_generic).into(imgReceta);
        rellanarPuntuacion();

        Retrofit retrofit=new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService service = retrofit.create(CookNetService.class);
        Call<Usuario> call = service.getUsuario(idUsuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario user=response.body();

                if(user!=null){
                    lblNombreUsuario.setText(user.getNombre());
                }else{
                    Toast.makeText(RecetaActivity.this,"Cuerpo nullo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(RecetaActivity.this,"Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void rellanarPuntuacion() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
        CookNetService servicio=retrofit.create(CookNetService.class);
        Call<Float> call=servicio.obtenerPuntuacion(Integer.parseInt(receta.getIdReceta()));

        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                Float respuesta=response.body();

                ratingBar.setRating(respuesta);
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {

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
