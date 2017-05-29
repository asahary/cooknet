package com.asahary.foodnet.Principal;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.Comentarios.ComentariosDialog;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecetaActivity extends AppCompatActivity {

    EditText txtNombre,txtDescripcion,txtIngredientes,txtPreparacion;
    ImageView imgReceta;
    Receta receta;
    Menu menu;
    boolean favorito=false;



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
                        menu.getItem(0).setIcon(R.drawable.ic_favorite);
                    }else{
                        favorito=true;
                        menu.getItem(0).setIcon(R.drawable.ic_fav_black);
                    }
                    Toast.makeText(RecetaActivity.this,mensaje+String.valueOf(favorito),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RecetaActivity.this,"Cuerpo nullo",Toast.LENGTH_SHORT).show();
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
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtDescripcion= (EditText) findViewById(R.id.txtDescripcion);
        txtIngredientes= (EditText) findViewById(R.id.Ingredientes);
        txtPreparacion= (EditText) findViewById(R.id.txtPreparacion);
        imgReceta= (ImageView) findViewById(R.id.imgReceta);
        receta=getIntent().getParcelableExtra(Constantes.EXTRA_RECETA);

        rellenarCampos();

    }

    private void rellenarCampos(){
        txtNombre.setText(receta.getNombre());
        txtDescripcion.setText(receta.getDescripcion());
        txtIngredientes.setText(leerIngredientes());
        txtPreparacion.setText(receta.getPreparacion());

        Picasso.with(this).load("http://steamykitchen.com/wp-content/uploads/2012/07/pork-belly-buns-recipe-8380.jpg").fit().into(imgReceta);
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
}
