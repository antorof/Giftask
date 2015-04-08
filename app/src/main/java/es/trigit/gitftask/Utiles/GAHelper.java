package es.trigit.gitftask.Utiles;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import es.trigit.gitftask.R;

/**
 * Clase que simplifica el uso de un unico tracker para la aplicacion.
 */
public class GAHelper {
    public static Tracker tracker = null;

    public GAHelper(Context context) {
        if (tracker == null)
            tracker = GoogleAnalytics.getInstance(context).newTracker(R.xml.app_tracker);
    }
}
