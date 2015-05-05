package es.trigit.gitftask.Pantallas;

import es.trigit.gitftask.R;

/**
 * Created by Georgevik on 28/04/15.
 */
public class ActivityPerfilAjeno extends ActivitySecondLevel {
    @Override
    protected void setFragment() {
        FragmentPerfilAjeno f = FragmentPerfilAjeno.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.contenedor, f)
                .commit();
    }

}
