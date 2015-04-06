package es.trigit.gitftask.Utiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import es.trigit.gitftask.Objetos.Regalo;
import es.trigit.gitftask.Objetos.Usuario;
import es.trigit.gitftask.R;

/**
 * Created by Georgevik on 17/03/2015.
 */
public class Globales {
    private static Usuario usuarioLogueado;
    private static Bitmap fotoObtenida;

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void setUsuarioLogueado(Usuario usuarioLogueado) {
        Globales.usuarioLogueado = usuarioLogueado;
    }

    public static void iniciarDatos(Context context) {
        usuarios.clear();
        regalos.clear();

        Usuario u1 = new Usuario();
        u1.setId(0);
        u1.setNombre("David González Sola");
        u1.setEmail("davgs@correo.ugr.es");
        u1.setSexo(true);
        u1.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.borrar_persona_ejemplo));

        Usuario u2 = new Usuario();
        u2.setId(1);
        u2.setNombre("Marta Salinas Cabeza");
        u2.setEmail("inventado@correo.ugr.es");
        u2.setSexo(false);
        u2.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.borrar_persona_ejemplo));

        usuarios.add(u1);
        usuarios.add(u2);

        Regalo r1 = new Regalo();
        r1.setId(0);
        r1.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.sloth));
        r1.setTitulo("Perezoso Molón");
        r1.setNumLikes(2);
        r1.setUsuarioPropietario(u1);
        r1.setPrecio(50);
        r1.setDireccionTienda("C/ Piruleta 18");

        Regalo r2 = new Regalo();
        r2.setId(1);
        r2.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.nav_drawer_background));
        r2.setTitulo("Wallpaper to guapo");
        r2.setNumLikes(18);
        r2.setUsuarioPropietario(u2);
        r2.setPrecio(32);
        r2.setDireccionTienda("C/ Falsa 123");

        regalos.add(r1);
        regalos.add(r2);
    }

    private static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    public static Usuario getUsuario(int id) {
        for (Usuario u : usuarios)
            if (u.getId() == id)
                return u;

        return null;
    }

    public static ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    private static ArrayList<Regalo> regalos = new ArrayList<Regalo>();

    public static Regalo getRegalo(int id) {
        for (Regalo r : regalos)
            if (r.getId() == id)
                return r;

        return null;
    }

    public static ArrayList<Regalo> getRegalos() {
        return regalos;
    }

    public static Bitmap getFotoObtenida() {
        try {
            return fotoObtenida;
        } finally {
            fotoObtenida = null;
        }

    }

    public static void setFotoObtenida(Bitmap fotoObtenida) {
        Globales.fotoObtenida = fotoObtenida;
    }
}
