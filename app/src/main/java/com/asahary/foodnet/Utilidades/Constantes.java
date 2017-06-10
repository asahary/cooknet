package com.asahary.foodnet.Utilidades;

import com.asahary.foodnet.CookNetService;

/**
 * Created by Saha on 22/05/2017.
 */

 public class Constantes {
    public final static String ID_USUARIO="idUsuario";
    public static final String EXTRA_RECETA ="extraAlimno";
    public static final String EXTRA_USUARIO ="extraUser";
    public static final String EXTRA_PREPARACION ="extraPreparacion";
    public static final String EXTRA_ID_USUARIO ="extraIdUsuario";
    public static final String EXTRA_ID_RECETA ="extraIdReceta";

    public static final String EXTRA_OPCION_LISTA="tipoDeLista";
   public static final String EXTRA_LISTA_MIS_RECETA="listaMisRecetas";
   public static final String EXTRA_LISTA_MIS_SEGUIDORES="listaMisSeguidores";
   public static final String EXTRA_LISTA_MIS_SEGUIDOS="listaMisSeguidos";
   public static final String EXTRA_LISTA_MIS_FAVORITOS="listaMisFavoritos";

    public static  final String URL_COMIDA= CookNetService.URL_BASE+"users/foodGeneric.png";
    public static  final String URL_USUARIO=CookNetService.URL_BASE+"users/userGeneric.png";

    public static  final String EVENTO_SEGUIR="SIGUE";
    public static  final String EVENTO_FAVORITO="FAVORITO";
    public static  final String EVENTO_COMENTAR="COMENTARIO";


    public static final String RESPUESTA_NULA="La lista esta vacia";
    public static final String RESPUESTA_FALLIDA="No se pudo obtener los datos";
}
