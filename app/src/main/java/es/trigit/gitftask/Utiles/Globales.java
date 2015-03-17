package es.trigit.gitftask.Utiles;

import es.trigit.gitftask.Objetos.Usuario;

/**
 * Created by Georgevik on 17/03/2015.
 */
public class Globales {
    private static Usuario usuarioLogueado;

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void setUsuarioLogueado(Usuario usuarioLogueado) {
        Globales.usuarioLogueado = usuarioLogueado;
    }
}
