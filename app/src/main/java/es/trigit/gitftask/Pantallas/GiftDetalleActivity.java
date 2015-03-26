package es.trigit.gitftask.Pantallas;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import es.trigit.gitftask.R;

/**
 * Created by DavidGSola on 22/03/2015.
 */
public class GiftDetalleActivity extends ActivityPrincipal {

    private final String TAG = "ACTIVITY DETALLE REGALO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int idRegalo = -1;

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            idRegalo = extras.getInt("REGALO_ID");

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

        Bundle bundle = new Bundle();
        bundle.putInt("REGALO_ID", idRegalo);
        GiftDetalleFragment f = GiftDetalleFragment.newInstance();
        f.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.contenedor, f)
                .commit();
    }

}
