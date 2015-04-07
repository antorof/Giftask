package es.trigit.gitftask.Pantallas;

import android.os.Bundle;

import es.trigit.gitftask.R;

/**
 * Created by DavidGSola on 22/03/2015.
 */
public class ActivityGiftDetalle extends ActivitySecondLevel {

    private final String TAG = "ACTIVITY DETALLE REGALO";
    int idRegalo = -1;

    @Override
    protected void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("REGALO_ID", idRegalo);

        GiftDetalleFragment f = GiftDetalleFragment.newInstance();
        f.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.contenedor, f)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            idRegalo = extras.getInt("REGALO_ID");

        super.onCreate(savedInstanceState);
    }

}
