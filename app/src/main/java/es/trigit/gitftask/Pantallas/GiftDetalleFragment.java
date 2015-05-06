package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.markushi.ui.RevealColorView;
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

    @InjectView(R.id.gift_detalle_layout_me_gusta)
    RelativeLayout mBotonMeGusta;

    @InjectView(R.id.gift_detalle_boton_lo_tengo)
    ImageButton mBotonLoTengo;

    @InjectView(R.id.gift_detalle_boton_compartir)
    ImageButton mBotonCompartir;

    @InjectView(R.id.gift_detalle_usuario_imagen)
    ImageView mUsuarioImagen;

    @InjectView(R.id.gift_detalle_usuario_nombre)
    TextView mUsuarioNombre;

    @InjectView(R.id.reveal_1)
    RevealColorView revealColorView_1;

    @InjectView(R.id.reveal_2)
    RevealColorView revealColorView_2;

    @InjectView(R.id.reveal_3)
    RevealColorView revealColorView_3;

    @InjectView(R.id.reveal_4)
    RevealColorView revealColorView_4;


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
        Regalo r = new Regalo();
        r.setId(Globales.getNextIdRegalo());
        r.setTitulo(mRegalo.getTitulo());
        r.setImagen(mRegalo.getImagen());
        r.setPrecio(mRegalo.getPrecio());
        r.setNumLikes(0);
        r.setUsuarioPropietario(Globales.getUsuarioLogueado());

        Globales.addRegalo(r);
        animarBoton(button, revealColorView_1);

        Log.v(TAG, "Repost");
    }

    @OnClick(R.id.gift_detalle_layout_me_gusta)
    public void pulsarMeGusta(RelativeLayout button){
        mRegalo.addMeGusta(Globales.getUsuarioLogueado().getId());
        Globales.addRegalo(mRegalo);
        mNumeroMeGusta.setText(mRegalo.getNumLikes()+"");

        animarBoton(button, revealColorView_2);
        Log.v(TAG, "Me gusta");
    }

    @OnClick(R.id.gift_detalle_boton_lo_tengo)
    public void pulsarLoTengo(ImageButton button){
        animarBoton(button, revealColorView_3);
        Log.v(TAG, "Lo tengo");
    }

    @OnClick(R.id.gift_detalle_boton_compartir)
    public void pulsarCompatrir(ImageButton button)
    {
        animarBoton(button, revealColorView_4);
        Log.v(TAG, "Compartir");
    }

    private void animarBoton(final View view, final RevealColorView revealColor)
    {
        reavealColor(view, revealColor);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideColor(view, revealColor);
                    }
                });
            }
        };
        thread.start();
    }

    private void reavealColor(View view, RevealColorView revealColor)
    {
        final Point p = getLocationInView(revealColor, view);
        final int color = getResources().getColor(R.color.LIME_500);
        revealColor.reveal( p.x, p.y, color, view.getHeight() / 2, 340, null);
    }

    private void hideColor(View view, RevealColorView revealColor)
    {
        final Point p = getLocationInView(revealColor, view);
        final int color = getResources().getColor(R.color.transparente);
        revealColor.hide(p.x, p.y, color, 0, 300, null);
    }

    private Point getLocationInView(View src, View target) {
        final int[] l0 = new int[2];
        src.getLocationOnScreen(l0);

        final int[] l1 = new int[2];
        target.getLocationOnScreen(l1);

        l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
        l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

        return new Point(l1[0], l1[1]);
    }
}