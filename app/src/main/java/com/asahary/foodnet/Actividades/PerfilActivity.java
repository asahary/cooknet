package com.asahary.foodnet.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    TextView txtNick,txtEmail,txtNombre,txtApellidos;
    CircleImageView imgPerfil;

    public static final int RQ_EDITAR_USER=7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle(Constantes.TITULO_MI_PERFIL);
        initVistas();

    }

    private void initVistas(){
        imgPerfil= (CircleImageView) findViewById(R.id.imgUsuario);
        txtNick= (TextView) findViewById(R.id.txtNick);
        txtNombre= (TextView) findViewById(R.id.txtNombre);
        txtEmail= (TextView) findViewById(R.id.txtEmail);
        txtApellidos= (TextView) findViewById(R.id.txtApellidos);

        rellenarDatos();
    }
    private void rellenarDatos(){
        Usuario user= CacheApp.user;
        Picasso.with(this).load(user.getImagen()).placeholder(R.drawable.user_generic).fit().error(R.drawable.user_generic).into(imgPerfil);
        txtNick.setText(user.getNick());
        txtNombre.setText(user.getNombre());
        txtEmail.setText(user.getEmail());
        txtApellidos.setText(user.getApellidos());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==RQ_EDITAR_USER){
                CacheApp.user=data.getParcelableExtra(Constantes.EXTRA_USUARIO);
                //Si le ha dado de baja al usuario le hacemos salir
                if(CacheApp.user.getBaja()==1){
                    Intent salir=new Intent(PerfilActivity.this,LogInActivity.class);
                    startActivity(salir);
                    finish();
                }else{
                    rellenarDatos();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.perfil_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editar:
                Intent intentPerfil=new Intent(PerfilActivity.this,EditarUsuarioActivity.class);
                startActivityForResult(intentPerfil,RQ_EDITAR_USER);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
