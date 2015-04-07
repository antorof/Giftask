package es.trigit.gitftask.Objetos;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Modela un regalo.
 * 
 * Created by Antonio Toro on 17/03/2015.
 */
public class Regalo {
    /** ID del regalo */
    private int id;

    /** Titulo del regalo */
    private String titulo;

    /** Imagen del regalo */
    private Bitmap imagen;

    /** Precio del regalo */
    private double precio;

    /** <tt>true</tt> si el usuario lo tiene */
    private boolean loTengo;

    /** Usuario propietario del regalo */
    private Usuario usuarioPropietario;

    /** Usuario que reserva el regalo */
    private Usuario usuarioReserva;

    /** ID de la tienda para los regalos patrocinados */
    private int idTienda;

    /** Nombre de la tienda para los regalos introducidos por los usuarios */
    private String nombreTienda;

    /** Direccion de la tienda para los regalos introducidos por los usuarios */
    private String direccionTienda;

    /** Fecha de inserción */
    private String fechaInsercion;

    /** ID del regalo original en el caso de que sea un regalo a partir de TENGO o LIKE */
    private int idOrigen;

    /** Número de likes */
    private int numLikes;

    /** Lista de Usuarios con me gusta*/
    private ArrayList<Integer> idUsuariosMeGusta = new ArrayList<Integer>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isLoTengo() {
        return loTengo;
    }

    public void setLoTengo(boolean loTengo) {
        this.loTengo = loTengo;
    }

    public Usuario getUsuarioPropietario() {
        return usuarioPropietario;
    }

    public void setUsuarioPropietario(Usuario usuarioPropietario) {
        this.usuarioPropietario = usuarioPropietario;
    }

    public Usuario getUsuarioReserva() {
        return usuarioReserva;
    }

    public void setUsuarioReserva(Usuario usuarioReserva) {
        this.usuarioReserva = usuarioReserva;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getDireccionTienda() {
        return direccionTienda;
    }

    public void setDireccionTienda(String direccionTienda) {
        this.direccionTienda = direccionTienda;
    }

    public String getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(String fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public int getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    public int getNumLikes() {
        return idUsuariosMeGusta.size();
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public void addMeGusta(int idUsuario)
    {
        idUsuariosMeGusta.add(idUsuario);
    }
}
