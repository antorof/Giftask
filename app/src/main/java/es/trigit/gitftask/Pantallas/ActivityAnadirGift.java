package es.trigit.gitftask.Pantallas;

import android.os.Bundle;

import es.trigit.gitftask.R;

public class ActivityAnadirGift extends ActivitySecondLevel {

    int opcion = -1;

    @Override
    protected void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("opcion", opcion);

        AnadirGiftFragment f = AnadirGiftFragment.newInstance();
        f.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.contenedor, f)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            opcion = extras.getInt("opcion");

        super.onCreate(savedInstanceState);
    }
}
