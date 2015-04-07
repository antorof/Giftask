package es.trigit.gitftask.Pantallas;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;

public class ActivityAnadirGift extends ActionBarActivity {

    public final static int EXTRA_CAMERA = 0;
    public final static int EXTRA_GALERIA = 1;
    public final static String EXTRA_OPTION = "opcion";

    @InjectView(R.id.ivActivityAnadirGift_imagen)
    ImageView ivImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_regalo);
        ButterKnife.inject(this);

        int opcion = -1;
        if(this.getIntent().getExtras() != null){
            opcion = this.getIntent().getIntExtra(EXTRA_OPTION, -1);

        }

        switch (opcion){
            case EXTRA_CAMERA:
                ivImagen.setImageBitmap(Globales.getFotoObtenida());
                break;

            case EXTRA_GALERIA:
                ivImagen.setImageBitmap(Globales.getFotoObtenida());
                break;

            default:
                break;
        }

    }



}
