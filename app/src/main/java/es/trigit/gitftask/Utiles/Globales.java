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
    public static boolean iniciado = false;
    private static Usuario usuarioLogueado;
    private static Usuario usuarioConsulta;
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
        u1.setNickname("Davilillo");
        u1.setEmail("davgs@correo.ugr.es");
        u1.setSexo(true);
        u1.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.borrar_persona_ejemplo));

        Usuario u2 = new Usuario();
        u2.setId(1);
        u2.setNombre("Marta Salinas Cabeza");
        u2.setEmail("inventado@correo.ugr.es");
        u2.setSexo(false);
        u2.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.borrar_persona_ejemplo));

        Regalo r1 = new Regalo();
        r1.setId(0);
        r1.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.sloth));
        r1.setTitulo("Perezoso Molón");
        r1.setUsuarioPropietario(u1);
        r1.setPrecio(50);
        r1.setDireccionTienda("C/ Piruleta 18");
        u1.addRegalo(r1);

        Regalo r2 = new Regalo();
        r2.setId(1);
        r2.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.nav_drawer_background));
        r2.setTitulo("Wallpaper to guapo");
        r2.setUsuarioPropietario(u2);
        r2.setPrecio(32);
        r2.setDireccionTienda("C/ Falsa 123");
        u2.addRegalo(r2);

        Regalo r3 = new Regalo();
        r3.setId(2);
        r3.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.nav_drawer_background));
        r3.setTitulo("Fondo poyuo");
        r3.addMeGusta(u1.getId());
        r3.setUsuarioPropietario(u2);
        r3.setPrecio(42);
        r3.setDireccionTienda("C/ Aquí mismo 13");
        u2.addRegalo(r3);

        Regalo r4 = new Regalo();
        r4.setId(3);
        r4.setImagen(BitmapFactory.decodeResource(context.getResources(), R.drawable.sloth));
        r4.setTitulo("Otro perezoso mas");
        r4.setUsuarioPropietario(u2);
        r4.setPrecio(3);
        r4.setDireccionTienda("C/ Falsa 123");
        u2.addRegalo(r4);

        usuarios.add(u1);
        usuarios.add(u2);

        regalos.add(r1);
        regalos.add(r2);
        regalos.add(r3);
        regalos.add(r4);

        setUsuarioLogueado(u1);
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

    /**
     * Comprueba si existe ya un regalo con ese ID, entonces lo añade en su posición,
     * sino lo añade al final.
     * @param regalo
     */
    public static void addRegalo(Regalo regalo){
        int index = -1;
        int i=0;
        for(Regalo r : regalos)
        {
            if(r.getId() == regalo.getId())
                index = i;

            i++;
        }

        if(index == -1) {
            regalos.add(regalo);
        }
        else {
            regalos.remove(index);
            regalos.add(index, regalo);
        }
    }

    public static Regalo getRegalo(int id) {
        for (Regalo r : regalos)
            if (r.getId() == id)
                return r;

        return null;
    }

    public static int getNextIdRegalo()
    {
        return regalos.size()+1;
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

    /**
     * Devuelve el usuario que se esta consultando
     * @return
     */
    public static Usuario getUsuarioConsulta() {
        return usuarioConsulta;
    }

    /**
     * Usuario el cual va a ser consultado, es decir, que se va a acceder a su perfil
     * @param usuario
     */
    public static void setUsuarioConsulta(Usuario usuario){
        usuarioConsulta = usuario;
    }
}
