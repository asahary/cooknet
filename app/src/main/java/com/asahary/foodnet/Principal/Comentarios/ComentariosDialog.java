package com.asahary.foodnet.Principal.Comentarios;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.Cache;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.asahary.foodnet.Adaptadores.ComentariosAdapter;
import com.asahary.foodnet.Utilidades.CacheApp;
import com.asahary.foodnet.Utilidades.Constantes;
import com.asahary.foodnet.CookNetService;
import com.asahary.foodnet.POJO.Comentario;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;
import com.asahary.foodnet.Principal.MainActivity;
import com.asahary.foodnet.Principal.Usuario.UsuarioActivity;
import com.asahary.foodnet.R;
import com.asahary.foodnet.Utilidades.Libreria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saha on 29/05/2017.
 */

public class ComentariosDialog extends DialogFragment implements ComentariosAdapter.OnReciclerItemClickListener {

    View vista;
    RecyclerView lista;
    EditText txtComentario;
    ImageView btnOk;
    Receta receta;
    ComentariosAdapter adaptador;
    ArrayList<Comentario> comentarios=new ArrayList<>();


public void iniciarLista(){
    Libreria.obtenerServicioApi().comentariosReceta(Integer.parseInt(receta.getIdReceta())).enqueue(new Callback<List<Comentario>>() {
        @Override
        public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
            List<Comentario> respuesta=response.body();

            if(respuesta!=null){
                comentarios=new ArrayList<>(respuesta);
                adaptador.swapDatos(comentarios);
            }else{
                Libreria.mostrarMensjeCorto(getContext(),Constantes.RESPUESTA_NULA);
            }
        }

        @Override
        public void onFailure(Call<List<Comentario>> call, Throwable t) {
            Libreria.mostrarMensjeCorto(getContext(),Constantes.RESPUESTA_FALLIDA);
        }
    });
}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Comentarios");

        vista= LayoutInflater.from(getContext()).inflate(R.layout.comentarios_dialog_layout,null);
        receta=getArguments().getParcelable(Constantes.EXTRA_RECETA);

        adaptador=new ComentariosAdapter(comentarios,this);

        txtComentario= (EditText) vista.findViewById(R.id.txtComentario);
        btnOk= (ImageView) vista.findViewById(R.id.btnAceptar);
        lista= (RecyclerView) vista.findViewById(R.id.lista);
        lista.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        lista.setAdapter(adaptador);

        iniciarLista();


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Libreria.obtenerServicioApi().
                         subirComentario(txtComentario.getText().toString(),Integer.parseInt(receta.getIdUsuario()),Integer.parseInt(receta.getIdReceta()), Integer.parseInt(CacheApp.user.getId()))
                         .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        if(response.body()==null)
                            Toast.makeText(getContext(),"nullo", Toast.LENGTH_SHORT).show();
                        else{
                            Toast.makeText(getContext(),response.body(), Toast.LENGTH_SHORT).show();
                        }
                            dismiss();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(),"Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setView(vista);

        return builder.create();
    }



        @Override
        public void itemClic(Comentario comentario) {
            Libreria.obtenerServicioApi().getUsuario(Integer.parseInt(comentario.getIdUsuario())).enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    Usuario usuario=response.body();

                    if(usuario!=null){
                        Intent intentUser=new Intent(ComentariosDialog.this.getContext(),UsuarioActivity.class);
                        intentUser.putExtra(Constantes.EXTRA_USUARIO,usuario);
                        startActivity(intentUser);
                    }else{
                        Libreria.mostrarMensjeCorto(getContext(),Constantes.RESPUESTA_NULA);
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Libreria.mostrarMensjeCorto(getContext(),Constantes.RESPUESTA_FALLIDA);
                }
            });
        }

}
