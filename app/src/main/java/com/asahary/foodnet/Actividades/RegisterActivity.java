package com.asahary.foodnet.Actividades;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.R;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    EditText txtNick,txtPass,txtPass2,txtEmail,txtNombre,txtApellidos;
    Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initVistas();
    }

    private void initVistas() {
        txtNick= (EditText) findViewById(R.id.txtNick);
        txtPass= (EditText) findViewById(R.id.txtPass);
        txtPass2= (EditText) findViewById(R.id.txtPass2);
        txtEmail= (EditText) findViewById(R.id.txtEmail);
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtApellidos= (EditText) findViewById(R.id.txtApellidos);
        btnRegistrar= (Button) findViewById(R.id.btnRegister);


        txtNick.addTextChangedListener(this);
        txtPass.addTextChangedListener(this);
        txtPass2.addTextChangedListener(this);
        txtEmail.addTextChangedListener(this);
        txtNombre.addTextChangedListener(this);
        txtApellidos.addTextChangedListener(this);



        //Cuando pulse el boton se registrará el nuevo usuario
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nick,pass,email,nombre,apellidos;
                nick=txtNick.getText().toString();
                pass=txtPass.getText().toString();
                email=txtEmail.getText().toString();
                nombre=txtNombre.getText().toString();
                apellidos=txtApellidos.getText().toString();

                //Creamos el retrofit y la interfaz de servicio
                Retrofit retrofit= new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
                CookNetService service = retrofit.create(CookNetService.class);

                //Creamos la llamada
                Call<String> llamada = service.registrar(nick,pass,email,nombre,apellidos);

                llamada.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //Obtenemos el cuerpo y comprobamos que no sea nullo
                        String cuerpo = response.body();
                        if(cuerpo!=null){
                            //En caso de que no sea nullo mostramos el mensaje de exito
                            Toast.makeText(RegisterActivity.this,cuerpo,Toast.LENGTH_SHORT).show();

                            /*--WHAT TO DO NEXT?--
                                Access to the app or go to the login window again?
                                Launch a dialog of confirmation and then access to the app?
                            */
                        }else{
                            Toast.makeText(RegisterActivity.this,"El cuerpo es nullo",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this,"No ha habido respuesta",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //--COMPROBACIONES DE CAMPOS
    //Comprueba que las contraseñas sean iguales
    private boolean comprobarPass(){
        if(txtPass.getText().toString().equals(txtPass2.getText().toString())){
            return true;
        }
        else{
            Toast.makeText(RegisterActivity.this,"Las contraseñas no coinciden, reviselas",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Comprueba que los campos no esten vacios
    private boolean comprobarCampos(){
        return !txtNick.getText().toString().isEmpty() &&
                !txtPass.getText().toString().isEmpty() &&
                !txtPass2.getText().toString().isEmpty() &&
                !txtEmail.getText().toString().isEmpty() &&
                !txtNombre.getText().toString().isEmpty() &&
                !txtApellidos.getText().toString().isEmpty();

    }

    //Comprueba que el formato del email sea correcto
    private boolean comprobarSyntaxEmail(){
        Pattern patron= Patterns.EMAIL_ADDRESS;
        return patron.matcher(txtEmail.getText().toString()).matches();
    }

    //Llaman a la api para comprobar si existen
    private boolean comprobarEmail(){
        Boolean existe=null;

        //Creamos el objeto retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();

        //Creamos la interfaz de la api
        CookNetService service=retrofit.create(CookNetService.class);

        //Creamos el objeto llamada
        Call<Boolean> llamada = service.comprobarEmail(txtEmail.getText().toString());



        //Utilizamos el objeto llamada de manera asincrona
        try {
            Response<Boolean> respuesta=llamada.execute();
            if(respuesta.isSuccessful()){
                existe=respuesta.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existe;
    }
    private boolean comprobarNick(){

        Boolean existe=null;

        //Creamos el objeto retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();

        //Creamos la interfaz de la api
        CookNetService service=retrofit.create(CookNetService.class);

        //Creamos el objeto llamada
        Call<Boolean> llamada = service.comprobarNick(txtNick.getText().toString());



        //Utilizamos el objeto llamada de manera asincrona
        try {
            Response<Boolean> respuesta=llamada.execute();
            if(respuesta.isSuccessful()){
                existe=respuesta.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existe;
    }



    //--LISTENERS EN LOS CAMBIOS DE LOS CAMPOS
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        //En principio deshabilitamos el boton, y si se cumplen todos los requisitos lo habilitamos
        btnRegistrar.setEnabled(false);
        if(comprobarCampos()){
            if(!comprobarPass()){
                Toast.makeText(RegisterActivity.this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            }else if(!comprobarSyntaxEmail()){
                Toast.makeText(RegisterActivity.this,"El formato del email es incorrecto",Toast.LENGTH_SHORT).show();
            }else if(comprobarNick()){
                Toast.makeText(RegisterActivity.this,"El nick introducido ya existe",Toast.LENGTH_SHORT).show();
            }else if(comprobarEmail()){
                Toast.makeText(RegisterActivity.this,"El correo introducido ya existe",Toast.LENGTH_SHORT).show();
            }
            else{
                btnRegistrar.setEnabled(true);
            }
        }
    }
}
