package es.trigit.gitftask.Pantallas;

import es.trigit.gitftask.R;

/**
 * Created by DavidGSola on 07/04/2015.
 */
public class ActivityEditarPerfil extends ActivitySecondLevel{
    @Override
    protected void setFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.contenedor, EditarPerfilFragment.newInstance())
                .commit();
    }
}
