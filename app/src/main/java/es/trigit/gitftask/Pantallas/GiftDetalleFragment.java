package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.trigit.gitftask.Objetos.Regalo;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;

/**
 * Created by DavidGSola on 22/03/2015.
 */
public class GiftDetalleFragment extends Fragment {

    private final String TAG = "Fragment Gift Detalle";

    private Regalo mRegalo;

    @InjectView(R.id.gift_detalle_imagen)
    ImageView mRegaloImagen;

    @InjectView(R.id.gift_detalle_titulo)
    TextView mRegaloTitulo;

    @InjectView(R.id.gift_detalle_direccion)
    TextView mRegaloDireccion;

    @InjectView(R.id.gift_detalle_precio)
    TextView mRegaloPrecio;

    @InjectView(R.id.gift_detalle_numero_me_gusta)
    TextView mNumeroMeGusta;

    @InjectView(R.id.gift_detalle_boton_repost)
    ImageButton mBotonRepost;

    @InjectView(R.id.gift_detalle_boton_me_gusta)
    ImageButton mBotonMeGusta;

    @InjectView(R.id.gift_detalle_boton_lo_tengo)
    ImageButton mBotonLoTengo;

    @InjectView(R.id.gift_detalle_boton_compartir)
    ImageButton mBotonCompartir;

    @InjectView(R.id.gift_detalle_usuario_imagen)
    ImageView mUsuarioImagen;

    @InjectView(R.id.gift_detalle_usuario_nombre)
    TextView mUsuarioNombre;


    public static GiftDetalleFragment newInstance() {
        return new GiftDetalleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int idRegalo = getArguments().getInt("REGALO_ID");
        mRegalo = Globales.getRegalo(idRegalo);

        return inflater.inflate(R.layout.fragment_gift_detalle, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.inject(this, view);

        mRegaloImagen.setImageBitmap(mRegalo.getImagen());
        mNumeroMeGusta.setText(mRegalo.getNumLikes()+"");
        mRegaloTitulo.setText(mRegalo.getTitulo());
        mRegaloDireccion.setText(mRegalo.getDireccionTienda());
        mRegaloPrecio.setText(mRegalo.getPrecio()+"€");

        mUsuarioImagen.setImageBitmap(mRegalo.getUsuarioPropietario().getImagen());
        mUsuarioNombre.setText(mRegalo.getUsuarioPropietario().getNombre());
    }

    @OnClick(R.id.gift_detalle_boton_repost)
    public void pulsarRepost(ImageButton button){
        Log.v(TAG, "Repost");
    }

    @OnClick(R.id.gift_detalle_boton_me_gusta)
    public void pulsarMeGusta(ImageButton button){
        mRegalo.addMeGusta(Globales.getUsuarioLogueado().getId());
        Globales.addRegalo(mRegalo);
        mNumeroMeGusta.setText(mRegalo.getNumLikes()+"");
        Log.v(TAG, "Me gusta");
    }

    @OnClick(R.id.gift_detalle_boton_lo_tengo)
    public void pulsarLoTengo(ImageButton button){
        Log.v(TAG, "Lo tengo");
    }

    @OnClick(R.id.gift_detalle_boton_compartir)
    public void pulsarCompatrir(ImageButton button){
        Log.v(TAG, "Compartir");
    }
}