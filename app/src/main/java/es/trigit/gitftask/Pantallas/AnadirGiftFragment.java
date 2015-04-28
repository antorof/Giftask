package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.trigit.gitftask.Objetos.Regalo;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;

/**
 * Created by DavidGSola on 07/04/2015.
 */
public class AnadirGiftFragment extends Fragment {

    private final String TAG = "Fragment Añadir Gift";

    private Bitmap mImagenRegalo;

    public final static int EXTRA_CAMERA = 0;
    public final static int EXTRA_GALERIA = 1;
    public final static String EXTRA_OPTION = "opcion";

    @InjectView(R.id.etFragmentAnadirRegalo_titulo)
    MaterialEditText etTitulo;
    @InjectView(R.id.etFragmentAnadirRegalo_direccion)
    MaterialEditText etDireccion;
    @InjectView(R.id.etFragmentAnadirRegalo_precio)
    MaterialEditText etPrecio;
    @InjectView(R.id.ivActivityAnadirGift_imagen)
    ImageView ivImagen;

    int opcion = -1;

    public static AnadirGiftFragment newInstance() {
        return new AnadirGiftFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        opcion = this.getArguments().getInt(EXTRA_OPTION, -1);

        return inflater.inflate(R.layout.fragment_anadir_regalo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);

        switch (opcion) {
            case EXTRA_CAMERA:
                mImagenRegalo = Globales.getFotoObtenida();
                ivImagen.setImageBitmap(mImagenRegalo);
                break;

            case EXTRA_GALERIA:
                mImagenRegalo = Globales.getFotoObtenida();
                ivImagen.setImageBitmap(mImagenRegalo);
                break;

            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editar_perfil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iMenuEditarPerfil_ok:
                saveRegalo();
                break;
        }

        return true;

    }

    public void saveRegalo() {
        Regalo regalo = new Regalo();
        regalo.setId(Globales.getNextIdRegalo());
        regalo.setTitulo(etTitulo.getText().toString());
        regalo.setDireccionTienda(etDireccion.getText().toString());
        regalo.setPrecio(Integer.parseInt(etPrecio.getText().toString()));
        regalo.setImagen(mImagenRegalo);
        regalo.setUsuarioPropietario(Globales.getUsuarioLogueado());

        Globales.addRegalo(regalo);
        Toast.makeText(this.getActivity(), "Regalo añadido", Toast.LENGTH_LONG).show();
    }

}
