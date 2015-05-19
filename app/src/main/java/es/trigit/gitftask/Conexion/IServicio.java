package es.trigit.gitftask.Conexion;

import java.util.List;

import es.trigit.gitftask.Objetos.Usuario;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Created by Antonio Toro on 06/05/2015.
 */
public interface IServicio {
    String API_URL = "http://giftask-roommatesteam.rhcloud.com";

    @GET("/users/")
    Respuesta.Lista usuarios(@Header("Authorization") String authorization);

    @GET("/users/{id}")
    Respuesta.Objeto<Usuario> getUsuario(@Header("Authorization") String authorization, @Path("id") String id);

    @POST("/users/")
    void crearUsuario(@Body Usuario user, Callback<Usuario> cb);

    @POST("/users/login")
    @FormUrlEncoded
    void login(@Field("username") String username, @Field("password") String password, @Field("email") String email, Callback<Respuesta.Objeto<Usuario>> cb);
}
