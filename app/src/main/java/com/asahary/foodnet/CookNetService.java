package com.asahary.foodnet;

import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;

import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by omniumlab on 15/05/2017.
 */

public interface CookNetService {

    public static String URL_BASE="http://asahary.esy.es/";

    @GET("usuarios")
    Call<List<Usuario>> listUsers();

    @GET("recetas")
    Call<List<Receta>> listRecetas();

    @GET("usuarios/{id}/favoritos")
    Call<List<Receta>> favoritosUser(@Path("id") int id);

    @FormUrlEncoded
    @POST("usuarios/login")
    Call<Usuario> login(@Field("user")String user,@Field("pass")String pass);

    @FormUrlEncoded
    @POST("usuarios/index")
    Call<String> registrar(@Field("nick")String nick,@Field("pass")String pass,@Field("email")String email,@Field("nombre")String nombre,@Field("apellidos")String apellidos);

    @FormUrlEncoded
    @POST("comprobaciones/nick")
    Call<Boolean> comprobarNick(@Field("nick")String nick);

    @FormUrlEncoded
    @POST("comprobaciones/email")
    Call<Boolean> comprobarEmail(@Field("email")String email);

    @FormUrlEncoded
    @POST("receta/index")
    Call<String> aregarReceta(@Field("idUsuario")int idUsuario,@Field("nombre")String nombre,
        @Field("descripcion")String descripcion,@Field("preparacion")String preparacion,
        @Field("ingredientes")String ingredientes,@Field("categoria")int categoria,@Field("imagen")String imagen);

    @FormUrlEncoded
    @POST("comprobaciones/favorito")
    Call<Boolean> comprobarFavorito(@Field("idReceta") int idReceta,@Field("idUsuario")int idUsuario);

    @FormUrlEncoded
    @POST("favoritos/actualizar")
    Call<String> actulizarFavorito(@Field("fav") int fav,@Field("idCreador") int idCreador,@Field("idReceta") int idReceta,@Field("idUsuario")int idUsuario);

    @FormUrlEncoded
    @POST("comentarios/subir")
    Call<String>subirComentario(@Field("comentario") String fav,@Field("idCreador") int idCreador,@Field("idReceta") int idReceta,@Field("idUsuario")int idUsuario);

}
