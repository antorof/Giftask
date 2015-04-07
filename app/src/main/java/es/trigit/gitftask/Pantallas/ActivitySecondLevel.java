package es.trigit.gitftask.Pantallas;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import es.trigit.gitftask.R;

/**
 * Created by DavidGSola on 07/04/2015.
 */
public abstract class ActivitySecondLevel extends ActivityPrincipal{

    /**
     * Método donde se setea el fragment y se le hace
     * el commit.
     */
    protected abstract void setFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Desactivar el gesto para abrir el navdrawer
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        deshabilitarBotonFlotante();

        // Seteamos el Fragment
        setFragment();
    }

}
