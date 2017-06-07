package com.asahary.foodnet.Principal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.asahary.foodnet.Actividades.RegisterActivity;
import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.Agregar.ImagenOptionDialog;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditarUsuarioActivity extends AppCompatActivity implements ImagenOptionDialog.OnOptionClick{

    Usuario user;
    EditText txtNick,txtEmail,txtNombre,txtApellidos;
    CircleImageView img;
    FloatingActionButton fab;
    Button btnRegistrar;
    Switch sw;

    Intent intent;
    String sOriginal="";
    String nombreArchivo="";
    File file;

    private static final String[] PERMISOS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int RQ_PERMISOS = 1;
    private static final int RQ_GALERIA =2;
    private static final int RQ_CAMARA=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        user=getIntent().getParcelableExtra(Constantes.EXTRA_USUARIO);
        initVistas();
        rellenarCampos();
    }

    private void initVistas() {
        sw= (Switch) findViewById(R.id.sw);
        txtNick= (EditText) findViewById(R.id.txtNick);
        txtEmail= (EditText) findViewById(R.id.txtEmail);
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtApellidos= (EditText) findViewById(R.id.txtApellidos);
        btnRegistrar= (Button) findViewById(R.id.btnRegister);
        img= (CircleImageView) findViewById(R.id.imgUsuario);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImagenOptionDialog().show(getSupportFragmentManager(),"Alo");
            }
        });


        //Cuando pulse el boton se registrará el nuevo usuario
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(comprobarTodo()){
                    String nick,email, nombre, apellidos, imagen;

                    if (file != null) {
                        imagen = CookNetService.URL_BASE + "users/" + MainActivity.idUsuario + "/imgProfile/" + file.getName();
                    } else {
                        imagen = user.getImagen();
                    }

                    nick = txtNick.getText().toString();
                    email = txtEmail.getText().toString();
                    nombre = txtNombre.getText().toString();
                    apellidos = txtApellidos.getText().toString();

                    //Creamos el retrofit y la interfaz de servicio
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();
                    CookNetService service = retrofit.create(CookNetService.class);

                    //Creamos la llamada
                    Call<Boolean> llamada = service.actualizarUser(MainActivity.idUsuario,nick, email, nombre, apellidos, imagen);

                    llamada.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            //Obtenemos el cuerpo y comprobamos que no sea nullo
                            Boolean cuerpo = response.body();

                            if (cuerpo != null) {

                                if(file!=null){
                                    subirFoto();
                                }

                            } else {
                                Toast.makeText(EditarUsuarioActivity.this, "El cuerpo es nullo", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(EditarUsuarioActivity.this, "No ha habido respuesta", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    private void rellenarCampos(){
        Picasso.with(this).load(user.getImagen()).fit().error(R.drawable.user_generic).into(img);

        String nombre,nick,apellidos,email;

        nombre=user.getNombre();
        nick=user.getNick();
        email=user.getEmail();
        apellidos=user.getApellidos();

        txtNick.setText(nick);
        txtNombre.setText(nombre);
        txtEmail.setText(email);
        txtApellidos.setText(apellidos);

    }

    //--COMPROBACIONES DE CAMPOS
    private boolean comprobarTodo(){

        if(comprobarCampos()){
            if(comprobarSyntaxEmail()){
                    if(comprobarNick()){
                        Toast.makeText(EditarUsuarioActivity.this,"El nick ya existe",Toast.LENGTH_LONG).show();
                        return false;
                    }else{
                        if(comprobarEmail()){
                            Toast.makeText(EditarUsuarioActivity.this,"El email ya existe",Toast.LENGTH_LONG).show();
                            return false;
                        }else{
                            return true;
                        }
                    }

            }else{
                Toast.makeText(EditarUsuarioActivity.this,"El formato del email no es valido",Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(EditarUsuarioActivity.this,"Alguno de los campos esta vacio",Toast.LENGTH_LONG).show();
            return false;
        }
    }
    //Comprueba que los campos no esten vacios
    private boolean comprobarCampos(){
        return !txtNick.getText().toString().isEmpty() &&
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



        //Utilizamos el objeto llamada de manera sincrona
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent=data;
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case RQ_GALERIA:
                    if(hasPermission(EditarUsuarioActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        procesarUri();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(PERMISOS,RQ_PERMISOS);
                        }
                    }
                    break;
                case RQ_CAMARA:
                    if(hasPermission(EditarUsuarioActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                        String path= file.getAbsoluteFile().getAbsolutePath();
                        Picasso.with(EditarUsuarioActivity.this).load(file).fit().into(img);


                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(PERMISOS,RQ_PERMISOS);
                        }
                    }
                    break;

            }
        }

    }

    //--OPCIONES DE PERMISOS
    //Comprobar si tiene el permiso
    private boolean hasPermission(Context contexto, String permiso) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(contexto, permiso));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RQ_PERMISOS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    procesarUri();
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISOS,RQ_PERMISOS);
                }
                break;
        }
    }

    //METDODOS DE GESTION DE IMAGENES
    private String getRealPath(Uri uriGaleria) {

        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uriGaleria, proj, null, null,
                    null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void mostrarFoto() {
        File f = null;
        if (!TextUtils.isEmpty(sOriginal)) {
            f = new File(sOriginal);
            file=f;
        }
        Picasso.with(EditarUsuarioActivity.this).load(f).fit().error(R.drawable.ic_check).into(img);
    }

    private void procesarUri(){

        //Debemos comprobar que la uri contenta file: o content: porque si no tiene content nos devuelve un cursos nullo
        Uri targetUri = intent.getData();
        if (intent.toString().contains("content:")) {
            sOriginal = getRealPath(targetUri);
        } else if (intent.toString().contains("file:")) {
            sOriginal = targetUri.getPath();
        } else {
            sOriginal = null;
        }
        mostrarFoto();
    }

    private File crearArchivoFoto(String nombre, boolean publico) {
        // Se obtiene el directorio en el que almacenarlo.
        File directorio;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (publico) {
                // En el directorio público para imágenes del almacenamiento externo.
                directorio = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            } else {
                directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
        } else {
            // En almacenamiento interno.
            directorio = getFilesDir();
        }
        // Su no existe el directorio, se crea.
        if (directorio != null && !directorio.exists()) {
            if (!directorio.mkdirs()) {
                Log.d(getString(R.string.app_name), "error al crear el directorio");
                return null;
            }
        }
        // Se crea un archivo con ese nombre y la extensión jpg en ese
        // directorio.
        File archivo = null;
        if (directorio != null) {
            archivo = new File(directorio.getPath() + File.separator +
                    nombre);
            Log.d(getString(R.string.app_name), archivo.getAbsolutePath());
        }
        Toast.makeText(EditarUsuarioActivity.this,archivo.getAbsolutePath(),Toast.LENGTH_LONG).show();
        this.file=archivo;

        // Se retorna el archivo creado.
        return archivo;
    }

    private void capturarFoto(String nombreArchivoPrivado) {
        // Se guarda el nombre para uso posterior.
        nombreArchivo = nombreArchivoPrivado;
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Si hay alguna actividad que sepa realizar la acción.
        if (i.resolveActivity(getPackageManager()) != null) {
            // Se crea el archivo para la foto en el directorio público (true).
            // Se obtiene la fecha y hora actual.
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            String nombre = "IMG_" + timestamp + "_" + ".jpg";
            File fotoFile = crearArchivoFoto(nombre, true);
            if (fotoFile != null) {
                // Se guarda el path del archivo para cuando se haya hecho la captura.
                sOriginal = fotoFile.getAbsolutePath();
                // Se obtiene la Uri correspondiente al archivo creado a través del FileProvider,
                // cuyo autorithies debe coincidir con lo especificado para el FileProvider en el
                // manifiesto (necesario para API >= 25).

                Uri fotoURI = FileProvider.getUriForFile(this,
                        "com.asahary.cooknet.fileprovider", fotoFile);
                // Se añade como extra del intent la uri donde debe guardarse.
                i.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                startActivityForResult(i, RQ_CAMARA);
            }
        }
    }

    public void subirFoto(){

        RequestBody filePart = RequestBody.create(MediaType.parse("*/*"),file);

        MultipartBody.Part archivo = MultipartBody.Part.createFormData("file",file.getName(),filePart);

        RequestBody descripcion =RequestBody.create(MultipartBody.FORM,file.getName());


        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();

        CookNetService service = retrofit.create(CookNetService.class);

        Call<Boolean> call =service.subirFotoUsuario(descripcion,archivo,MainActivity.idUsuario);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if(response.body()!=null){
                    //Toast.makeText(RegisterActivity.this,response.body().toString(),Toast.LENGTH_SHORT).show();
                }

                else{
                    //Toast.makeText(RegisterActivity.this,"null",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(int option) {
        switch (option){
            case 1:
                Intent i=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i,RQ_GALERIA);
                break;
            case 2:
                capturarFoto("pa");
                break;
        }
    }
}
