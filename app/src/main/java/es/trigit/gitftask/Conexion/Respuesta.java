package es.trigit.gitftask.Conexion;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Antonio on 06/05/2015.
 */
public class Respuesta {
    public class Lista {
        public Integer error;
        public List response;
        public String message;
    }
    public class Objeto<T> {
        public Integer error;
        public T response;
        public String message;
    }
}
