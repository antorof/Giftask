package es.trigit.gitftask.Objetos;

import android.graphics.Bitmap;

/**
 * Created by Georgevik on 17/03/2015.
 */
public class Usuario {
    private int id;
    private String email;
    private String nickname;
    private String nombre;
    private String sexo;
    private boolean isMale=true;
    private String localidad;
    private String fechaCumpleanos;
    private Bitmap imagen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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

    public void setFechaCumpleano(String fechaCumpleanos) {
        this.fechaCumpleanos = fechaCumpleanos;
    }

    public void setSexo(boolean isMale){
        this.isMale = isMale;
    }

    public boolean isMale(){
        return isMale;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
