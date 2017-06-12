package com.asahary.foodnet.Actividades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.Utilidades.Libreria;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity {




    EditText txtNick,txtPass;
    Button btnAccess;
    TextView lblRegistrar;
    ImageView imgCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initVistas();
    }

    private void mostrarCarga(){
        txtPass.setEnabled(false);
        txtNick.setEnabled(false);
        imgCarga.setVisibility(View.VISIBLE);
        btnAccess.setVisibility(View.GONE);
        lblRegistrar.setVisibility(View.GONE);
        Glide.with(LogInActivity.this).load(R.drawable.loading).asGif().fitCenter().into(imgCarga);
    }
    private void ocultarCarga(){
        txtPass.setEnabled(true);
        txtNick.setEnabled(true);
        imgCarga.setVisibility(View.GONE);
        btnAccess.setVisibility(View.VISIBLE);
        lblRegistrar.setVisibility(View.VISIBLE);
    }
    public void initVistas(){
        imgCarga= (ImageView) findViewById(R.id.imgCarga);
        txtNick= (EditText) findViewById(R.id.txtNick);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnAccess = (Button) findViewById(R.id.btnAccess);
        lblRegistrar= (TextView) findViewById(R.id.lblRegistro);
        txtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                btnAccess.performClick();
                return true;
            }
        });

        //Link al formulario de registro
        lblRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        //Boton de LogIn
        btnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarCarga();
                Libreria.obtenerServicioApi().login(txtNick.getText().toString(),Libreria.crearPass(txtPass.getText().toString())).enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, final Response<Usuario> response) {
                        final Usuario user=response.body();
                        ocultarCarga();
                        if(user!=null){
                            if(Integer.parseInt(user.getBaja())==1){
                                //Si esta dado de baja mostrar un dialogo que le diga que debe darse de alta para acceder
                                new AlertDialog.Builder(LogInActivity.this)
                                        .setTitle("Activar usuario")
                                        .setMessage("Este usuario esta desactivado. ¿Quieres reactivarlo?")
                                        .setPositiveButton("Si ", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Libreria.obtenerServicioApi().activarUser(Integer.parseInt(user.getId())).enqueue(new Callback<Boolean>() {
                                                    @Override
                                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                        Boolean cuerpo=response.body();
                                                        if(cuerpo!=null){
                                                            user.setBaja("0");
                                                            CacheApp.user=user;
                                                            Intent intent =  new Intent(LogInActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }else{
                                                            Libreria.mostrarMensjeCorto(LogInActivity.this,Constantes.RESPUESTA_NULA);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                                        Libreria.mostrarMensjeCorto(LogInActivity.this, Constantes.RESPUESTA_FALLIDA);
                                                    }
                                                });
                                            }})
                                        .setNegativeButton("No",null).show();
                            }else{
                                //Accedemos a la aplicacion
                                CacheApp.user=response.body();
                                Intent intent =  new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            //Login fallido
                            Libreria.mostrarMensjeCorto(LogInActivity.this,"Alguno de los datos estan mal");
                        }

                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Libreria.mostrarMensjeCorto(LogInActivity.this, Constantes.RESPUESTA_FALLIDA);
                    }
                });

                ocultarTecladoVirtual(txtPass);

            }
        });
    }
    public void ocultarTecladoVirtual(View view){
        InputMethodManager imm =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
