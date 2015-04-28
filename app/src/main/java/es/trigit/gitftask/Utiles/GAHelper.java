package es.trigit.gitftask.Utiles;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import es.trigit.gitftask.R;

/**
 * Clase que simplifica el uso de un unico tracker para la aplicacion.
 * @author Antonio Toro
 */
public class GAHelper extends Application{
    public static Tracker tracker = null;

    public GAHelper() {
        super();
    }

    public synchronized void crearTracker() {
        if (tracker == null)
            tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.app_tracker);

    }
}
