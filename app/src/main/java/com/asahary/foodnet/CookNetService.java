package com.asahary.foodnet;

import com.asahary.foodnet.POJO.Comentario;
import com.asahary.foodnet.POJO.Evento;
import com.asahary.foodnet.POJO.Receta;
import com.asahary.foodnet.POJO.Usuario;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by omniumlab on 15/05/2017.
 */

public interface CookNetService {

    public static String URL_BASE="http://asahary.esy.es/";

    @GET("usuarios/all/{id}")
    Call<List<Usuario>> listUsers(@Path("id") int id);

    @GET("usuarios/{id}")
    Call<Usuario> getUsuario(@Path("id") int id);

    @GET("recetas")
    Call<List<Receta>> listRecetas();

    @GET("usuarios/{id}/recetas")
    Call<List<Receta>> recetasUser(@Path("id") int id);

    @GET("usuarios/{id}/favoritos")
    Call<List<Receta>> favoritosUser(@Path("id") int id);

    @GET("usuarios/{id}/seguidos")
    Call<List<Usuario>> seguidosUser(@Path("id") int id);

    @GET("usuarios/{id}/seguidores")
    Call<List<Usuario>> seguidoresUser(@Path("id") int id);

    @GET("recetas/{id}")
    Call<Receta> getReceta(@Path("id") int id);

    @GET("recetas/{id}/puntuacion")
    Call<Float> obtenerPuntuacion(@Path("id") int id);

    @GET("usuarios/{id}/eventos")
    Call<List<Evento>> eventosUser(@Path("id") int id);

    @GET("recetas/{idReceta}/comentarios")
    Call<List<Comentario>> comentariosReceta(@Path("idReceta") int id);

    @FormUrlEncoded
    @POST("usuarios/login")
    Call<Usuario> login(@Field("user")String user,@Field("pass")String pass);

    @Multipart
    @POST("usuarios/imagenes")
    Call<Boolean>subirFotoUsuario(@Part("descripcion") RequestBody description, @Part MultipartBody.Part foto,@Part("idUser") int idUser);

    @FormUrlEncoded
    @POST("usuarios/index")
    Call<Integer> registrar(@Field("nick")String nick,@Field("pass")String pass,@Field("email")String email,@Field("nombre")String nombre,@Field("apellidos")String apellidos,@Field("imagen")String imagen);

    @FormUrlEncoded
    @PUT("usuarios/index")
    Call<Boolean> actualizarUser(@Field("idUser") int idUser,@Field("nick")String nick,@Field("email")String email,@Field("nombre")String nombre,@Field("apellidos")String apellidos,@Field("imagen")String imagen,@Field("baja")int baja);

    @FormUrlEncoded
    @PUT("usuarios/actualizar")
    Call<Boolean> actualizarUserPass(@Field("idUser") int idUser,@Field("oldPass")String oldPass,@Field("newPass")String newPass);

    @FormUrlEncoded
    @POST("comprobaciones/nick")
    Call<Boolean> comprobarNick(@Field("nick")String nick);

    @FormUrlEncoded
    @POST("comprobaciones/nick")
    Call<Boolean> comprobarNick(@Field("idUser")int id,@Field("nick")String nick);

    @FormUrlEncoded
    @POST("comprobaciones/email")
    Call<Boolean> comprobarEmail(@Field("email")String email);
    @FormUrlEncoded
    @POST("comprobaciones/email")
    Call<Boolean> comprobarEmail(@Field("isUser") int idUser,@Field("email")String email);

    @FormUrlEncoded
    @POST("receta/index")
    Call<String> aregarReceta(@Field("idUsuario")int idUsuario,@Field("nombre")String nombre,
        @Field("descripcion")String descripcion,@Field("preparacion")String preparacion,
        @Field("ingredientes")String ingredientes,@Field("categoria")int categoria,@Field("imagen")String imagen);

    @FormUrlEncoded
    @POST("comprobaciones/favorito")
    Call<Boolean> comprobarFavorito(@Field("idReceta") int idReceta,@Field("idUsuario")int idUsuario);

    @FormUrlEncoded
    @POST("comprobaciones/sigue")
    Call<Boolean> comprobarSigue(@Field("idUser") int idUser,@Field("idSeguido")int idSeguido);

    @FormUrlEncoded
    @POST("sigue/actualizar")
    Call<Boolean> actualizarSigue(@Field("idUser") int idUser,@Field("idSeguido")int idSeguido,@Field("bandera")int bandera);

    @FormUrlEncoded
    @POST("favoritos/actualizar")
    Call<String> actulizarFavorito(@Field("fav") int fav,@Field("idCreador") int idCreador,@Field("idReceta") int idReceta,@Field("idUsuario")int idUsuario);

    @FormUrlEncoded
    @POST("recetas/comentarios")
    Call<String>subirComentario(@Field("comentario") String fav,@Field("idCreador") int idCreador,@Field("idReceta") int idReceta,@Field("idUsuario")int idUsuario);

    @FormUrlEncoded
    @POST("recetas/valorar")
    Call<String>valorarReceta(@Field("puntuacion") float puntuacion,@Field("idCreador") int idCreador,@Field("idReceta") int idReceta,@Field("idUsuario")int idUsuario);


    @Multipart
    @POST("recetas/imagenes")
    Call<Boolean>subirFotoReceta(@Part("descripcion") RequestBody description, @Part MultipartBody.Part foto,@Part("idCreador") int idCreador);

    @FormUrlEncoded
    @PUT("recetas")
    Call<String>actualizarReceta(@Field("idReceta") int idReceta,@Field("nombre")String nombre,
                                 @Field("descripcion")String descripcion,@Field("preparacion")String preparacion,
                                 @Field("ingredientes")String ingredientes,@Field("categoria")int categoria,@Field("imagen")String imagen,@Field("bajaReceta")int bajaReceta);

}
