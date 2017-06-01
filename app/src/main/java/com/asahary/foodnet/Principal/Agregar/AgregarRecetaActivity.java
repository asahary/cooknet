package com.asahary.foodnet.Principal.Agregar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.GestorImagenes;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgregarRecetaActivity extends AppCompatActivity implements ImagenOptionDialog.OnOptionClick,GestorImagenes.ImageRequester{
    RecyclerView lista;
    IngredienteAdapter adaptador;
    ImageButton btnAgregar;
    ArrayList<Ingrediente> ingredientes=new ArrayList<>();
    EditText txtNombre,txtDescripcion,txtPreparacion;
    Spinner spCategoria;
    FloatingActionButton fab;
    Intent intent;
    Receta receta;
    String sOriginal="";
    ImageView img;
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
        setContentView(R.layout.activity_agregar_receta);
        initVistas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                subirReceta();
                break;
        }
        return true;
    }

    public void initVistas(){

        img= (ImageView) findViewById(R.id.imgReceta);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        spCategoria= (Spinner)findViewById(R.id.spCategoria);
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtDescripcion= (EditText)findViewById(R.id.txtDescripcion);
        txtPreparacion= (EditText)findViewById(R.id.txtPreparacion);
        lista= (RecyclerView)findViewById(R.id.Ingredientes);
        btnAgregar= (ImageButton)findViewById(R.id.btnAgregar);
        adaptador=new IngredienteAdapter(ingredientes);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(AgregarRecetaActivity.this, LinearLayoutManager.VERTICAL, false));

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anhadirIngrediente();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImagenOptionDialog().show(getSupportFragmentManager(),"Imagen");
            }
        });
    }


    //Añade un nuevo Ingrediente a la la lista
    private void anhadirIngrediente() {
        ingredientes.add(new Ingrediente());
        adaptador.notifyDataSetChanged();
    }

    //Hace la llamada post que sube la receta
    private void subirReceta(){

        String nombre=txtNombre.getText().toString();
        String descripcion=txtNombre.getText().toString();
        String preparacion=txtPreparacion.getText().toString();

        Retrofit retrofit=new Retrofit.Builder().baseUrl(CookNetService.URL_BASE).addConverterFactory(GsonConverterFactory.create()).build();

        CookNetService servicio=retrofit.create(CookNetService.class);

        Call<String> llamada = servicio.aregarReceta(MainActivity.idUsuario,nombre,descripcion,preparacion,formatearIngredientes(),spCategoria.getSelectedItemPosition(),"");

        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String respuesta=response.body();
                if(respuesta!=null){
                    Toast.makeText(AgregarRecetaActivity.this,respuesta,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AgregarRecetaActivity.this,"cuerpo nulo",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AgregarRecetaActivity.this,"Respuesta fallida",Toast.LENGTH_SHORT).show();
            }
        });


    }

    //Crea una cadena a base de todos los ingredientes de la lista
    private String formatearIngredientes(){
        String format="";

        String nombre="";
        int medida=0;
        Double cantidad=0.0;
        for(int i=0;i<ingredientes.size();i++){
            Ingrediente ingrediente =ingredientes.get(i);
            nombre=ingrediente.nombre;
            medida=ingrediente.medida;
            cantidad=ingrediente.cantidad;

            String miniFormat=cantidad+":"+String.valueOf(medida)+":"+nombre;
            format+=miniFormat;
            if(i<ingredientes.size()-1){
                format+="%";
            }
        }
        return format;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent=data;
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case RQ_GALERIA:
                    if(hasPermission(AgregarRecetaActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        procesarUri();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(PERMISOS,RQ_PERMISOS);
                        }
                    }
                    break;
                case RQ_CAMARA:
                    if(hasPermission(AgregarRecetaActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Bundle extras = data.getExtras();
                        Bitmap miniatura = (Bitmap) extras.get("data");
                        // Se muestra en el ImageView.
                        img.setImageBitmap(miniatura);
                        agregarFotoAGaleria();
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

    //Metodos para asignar la imagen
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
        }
        Picasso.with(AgregarRecetaActivity.this).load(f).fit().error(R.drawable.ic_check).into(img);
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
                // Se añade como extra del intent la uri donde debe guardarse.
                startActivityForResult(i, RQ_CAMARA);
            }
        }
    }

    private void agregarFotoAGaleria() {
        // Se un intent implícito con la acción de
        // escaneo de un fichero multimedia.
        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri uri = Uri.fromFile(file);
        // Se establece la uri con datos del intent.
        i.setData(uri);
        // Se envía un broadcast con el intent.
        this.sendBroadcast(i);
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

    @Override
    public ImageView getImage() {
        return img;
    }
}
