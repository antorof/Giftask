package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;

/**
 * Created by DavidGSola on 07/04/2015.
 */
public class AnadirGiftFragment extends Fragment {

    private final String TAG = "Fragment Añadir Gift";

    public static AnadirGiftFragment newInstance() {
        return new AnadirGiftFragment();
    }

    public final static int EXTRA_CAMERA = 0;
    public final static int EXTRA_GALERIA = 1;
    public final static String EXTRA_OPTION = "opcion";

    @InjectView(R.id.ivActivityAnadirGift_imagen)
    ImageView ivImagen;

    int opcion = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        opcion = this.getArguments().getInt(EXTRA_OPTION, -1);

        return inflater.inflate(R.layout.fragment_anadir_regalo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ButterKnife.inject(this, view);

        switch (opcion) {
            case EXTRA_CAMERA:
                ivImagen.setImageBitmap(Globales.getFotoObtenida());
                break;

            case EXTRA_GALERIA:
                break;

            default:
                break;
        }
    }
}
