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
import android.widget.TextView;
import android.widget.Toast;

import com.asahary.foodnet.Utilidades.Cache;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity {




    EditText txtNick,txtPass;
    Button btnAccess;
    TextView lblRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initVistas();
    }

    public void initVistas(){
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

                //Creamos el retrofit poniendole la url base y el convertidor de Gson
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(CookNetService.URL_BASE)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                //Creamos la interfaz que contiene los metodos que llaman a la api
                CookNetService apiLogin= retrofit.create(CookNetService.class);

                //Ejecutamos el metodo que hace log in de la interfaz
                Call<Usuario> call = apiLogin.login(txtNick.getText().toString(),txtPass.getText().toString());

                //Hacemos una llamada asincrona
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, final Response<Usuario> response) {
final Usuario user=response.body();
                        if(user!=null){
                            if(Integer.parseInt(user.getBaja())==1){
                                //Si esta dado de baja mostrar un dialogo que le diga que debe darse de alta para acceder
                                new AlertDialog.Builder(LogInActivity.this)
                                        .setTitle("Activar usuario")
                                        .setMessage("Este usuario esta desactivado. ¿Quieres reactivarlo?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("Si ", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Libreria.obtenerServicioApi().activarUser(Integer.parseInt(user.getId())).enqueue(new Callback<Boolean>() {
                                                    @Override
                                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                        Boolean cuerpo=response.body();
                                                        if(cuerpo!=null){
                                                            user.setBaja("0");
                                                            Cache.user=user;
                                                            Intent intent =  new Intent(LogInActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Boolean> call, Throwable t) {

                                                    }
                                                });
                                            }})
                                        .setNegativeButton("No",null).show();
                            }else{
                                //Accedemos a la aplicacion
                                Cache.user=response.body();
                                Intent intent =  new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            //Login fallido
                            Toast.makeText(LogInActivity.this, "Cuerpo nullo", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(LogInActivity.this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
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
