package com.asahary.foodnet.Principal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.asahary.foodnet.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Ingrediente;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.Principal.Agregar.AgregarRecetaActivity;
import com.asahary.foodnet.Principal.Agregar.ImagenOptionDialog;
import com.asahary.foodnet.Principal.Agregar.IngredienteAdapter;
import com.asahary.foodnet.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditarRecetaActivity extends AppCompatActivity implements ImagenOptionDialog.OnOptionClick{

    Receta receta;
    Switch sw;
    RecyclerView lista;
    IngredienteAdapter adaptador;
    TextView lblMostrar;
    ArrayList<Ingrediente> ingredientes=new ArrayList<>();
    EditText txtNombre,txtDescripcion,txtPreparacion;
    ImageButton btnAgregar;
    ImageView imgReceta;
    FloatingActionButton fab;
    Intent intent;
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
        setContentView(R.layout.activity_editar_receta);

        initVistas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initVistas() {
        sw= (Switch) findViewById(R.id.sw);
        receta=getIntent().getParcelableExtra(Constantes.EXTRA_RECETA);
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtDescripcion= (EditText) findViewById(R.id.txtDescripcion);
        txtPreparacion= (EditText) findViewById(R.id.txtPreparacion);
        btnAgregar= (ImageButton) findViewById(R.id.btnAgregar);
        imgReceta=(ImageView) findViewById(R.id.imgReceta);
        lista= (RecyclerView) findViewById(R.id.Ingredientes);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        ingredientes=leerIngredientes();
        adaptador=new IngredienteAdapter(ingredientes);
        lista.setAdapter(adaptador);
        lista.setLayoutManager(new LinearLayoutManager(EditarRecetaActivity.this,LinearLayoutManager.VERTICAL,false));

        rellenarCampos();


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientes.add(new Ingrediente());
                adaptador.notifyDataSetChanged();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImagenOptionDialog().show(getSupportFragmentManager(),"Alo");
            }
        });
    }
    private void rellenarCampos(){

        txtNombre.setText(receta.getNombre());
        txtDescripcion.setText(receta.getDescripcion());
        txtPreparacion.setText(receta.getPreparacion());
        Picasso.with(this).load(receta.getImagen()).fit().into(imgReceta);

        if(Boolean.valueOf(receta.getBajaReceta())){
            sw.setChecked(false);
        }else{
            sw.setChecked(true);
        }
    }
    //Separa la cadena de ingredientes para darle formato
    private ArrayList<Ingrediente> leerIngredientes(){
        ArrayList<Ingrediente> nuevaLista=new ArrayList<>();


        //Obtenemos es array de medidas
        String[] medidas=getResources().getStringArray(R.array.medidas);

        String[] ingredientes =receta.getIngredientes().split("%");

        for (int i=0;i<ingredientes.length;i++){
            String[] componentes= ingredientes[i].split(":");

            double cant=Double.parseDouble(componentes[0]);
            int medida=Integer.parseInt(componentes[1]);
            String nombre=componentes[2];

            nuevaLista.add(new Ingrediente(cant,medida,nombre));

        }
        return  nuevaLista;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        intent=data;
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case RQ_GALERIA:
                    if(hasPermission(EditarRecetaActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        procesarUri();
                    }
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(PERMISOS,RQ_PERMISOS);
                        }
                    }
                    break;
                case RQ_CAMARA:
                    if(hasPermission(EditarRecetaActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                        String path= file.getAbsoluteFile().getAbsolutePath();
                        Picasso.with(EditarRecetaActivity.this).load(file).fit().into(imgReceta);


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
        Picasso.with(EditarRecetaActivity.this).load(f).fit().error(R.drawable.ic_check).into(imgReceta);
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
        Toast.makeText(EditarRecetaActivity.this,archivo.getAbsolutePath(),Toast.LENGTH_LONG).show();
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

    private void agregarFotoAGaleria() {
        // Se un intent implícito con la acción de
        // escaneo de un fichero multimedia.
        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File f = new File(sOriginal);
        Uri uri = Uri.fromFile(f);

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


    public void subirFoto(){

        RequestBody filePart = RequestBody.create(MediaType.parse("*/*"),file);

        MultipartBody.Part archivo = MultipartBody.Part.createFormData("file",file.getName(),filePart);

        RequestBody descripcion =RequestBody.create(MultipartBody.FORM,file.getName());

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(CookNetService.URL_BASE).build();

        CookNetService service = retrofit.create(CookNetService.class);

        Call<Boolean> call =service.subirFotoReceta(descripcion,archivo,MainActivity.idUsuario);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if(response.body()!=null){

                }

                else{
                    // Toast.makeText(AgregarRecetaActivity.this,"null",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(EditarRecetaActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
