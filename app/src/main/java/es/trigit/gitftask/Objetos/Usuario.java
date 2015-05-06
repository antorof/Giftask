package es.trigit.gitftask.Objetos;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Georgevik on 17/03/2015.
 */
public class Usuario {
    private String _id;
    private String email;
    private String username;
    @SerializedName("name")
    private String nombre;
    @Deprecated private String gender;
    private boolean isMale=true;
    @SerializedName("town")
    private String localidad;
    @SerializedName("birthday")
    private String fechaCumpleanos;
    @SerializedName("image")
    private String imageURL;
    private Bitmap imagen;
    @SerializedName("gifts")
    private ArrayList<Regalo> misRegalos = new ArrayList<>();
    @SerializedName("followers")
    private ArrayList<Usuario> seguidores = new ArrayList<>();
    @SerializedName("following")
    private ArrayList<Usuario> siguiendo = new ArrayList<>();

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getFechaCumpleanos() {
        return fechaCumpleanos;
    }

    public void setFechaCumpleanos(String fechaCumpleanos) {
        this.fechaCumpleanos = fechaCumpleanos;
    }

    public void setIsMale(boolean isMale){
        this.isMale = isMale;
    }

    public boolean isMale(){
        return isMale;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public ArrayList<Regalo> getMisRegalos()
    {
        return misRegalos;
    }

    public void setMisRegalos(ArrayList<Regalo> misRegalos) {
        this.misRegalos = misRegalos;
    }

    public void addRegalo(Regalo regalo)
    {
        misRegalos.add(regalo);
    }

    public ArrayList<Usuario> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(ArrayList<Usuario> seguidores) {
        this.seguidores = seguidores;
    }

    public ArrayList<Usuario> getSiguiendo() {
        return siguiendo;
    }

    public void setSiguiendo(ArrayList<Usuario> siguiendo) {
        this.siguiendo = siguiendo;
    }
}
