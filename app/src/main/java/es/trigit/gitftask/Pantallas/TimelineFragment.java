package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import es.trigit.gitftask.Objetos.Regalo;
import es.trigit.gitftask.Objetos.Usuario;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;

/**
 * Fragment que representa la funcionalidad del Timeline.
 * Muestra todos los regalos de los seguidores del Usuario
 * que tiene la sesión activa mediante un GridView de regalos.
 * Premite refrescar el listado mediante un SwipeRefresh.
 */
public class TimelineFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private final String TAG = "Fragment Timeline";

    private final int DELAY_REFRESH_ITEMS = 5000;

    @InjectView(R.id.timeline_grid) GridView mGridView;
    @InjectView(R.id.timeline_swipe_container) SwipeRefreshLayout mSwipeLayout;

    private CustomGridViewAdapter mAdapter;
    private ArrayList<Regalo> mDatos = new ArrayList<Regalo>();

    public static TimelineFragment newInstance()
    {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        ButterKnife.inject(this, view);

        Globales.iniciarDatos(getActivity());
        mDatos = Globales.getRegalos();

        mAdapter = new CustomGridViewAdapter(getActivity(), mDatos);
        mGridView.setAdapter(mAdapter);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * Se ejecuta trás pulsar sobre un elemento del GridView.
     * Se llama a la actividad {@link es.trigit.gitftask.Pantallas.GiftDetalleActivity}
     * enviándole la ID del regalo para que muestre el Regalo
     * que sobre el que se ha pulsado.
     * @param position Posición del item pulsado en el GridView.
     */
    @OnItemClick(R.id.timeline_grid)
    public void pulsarItemGrid(int position)
    {
        // TODO: enviar ID del regalo para cargarlo en la actividad detalle
        Intent intent = new Intent(getActivity(), GiftDetalleActivity.class);
        intent.putExtra("REGALO_ID", mAdapter.getItem(position).getId());
        startActivity(intent);
    }

    public void onRefresh()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: obtener los datos de algún lugar
                mAdapter.remove(mDatos.get(2));
                mSwipeLayout.setRefreshing(false);
            }
        }, DELAY_REFRESH_ITEMS);
    }

    /**
     * Clase adaptador para el GridView del Timeline
     */
    public class CustomGridViewAdapter extends ArrayAdapter<Regalo>
    {
        private final Context context;

        public CustomGridViewAdapter(Context context, ArrayList<Regalo> values)
        {
            super(context, R.layout.row_timeline, values);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.row_timeline, parent, false);

            TextView numeroMeGustas = (TextView) itemView.findViewById(R.id.gift_numero_me_gustas);
            TextView nombreRegalo = (TextView) itemView.findViewById(R.id.gift_nombre);
            TextView nombreUsuario = (TextView) itemView.findViewById(R.id.gift_usuario_nombre);
            ImageView imagenRegalo = (ImageView) itemView.findViewById(R.id.gift_image);

            numeroMeGustas.setText(getItem(position).getNumLikes()+" ");
            nombreRegalo.setText(getItem(position).getTitulo());
            nombreUsuario.setText(getItem(position).getUsuarioPropietario().getNombre());
            imagenRegalo.setImageBitmap(getItem(position).getImagen());

            return itemView;
        }
    }

    /**
     * Crea unos datos de testing para el gridView
     * @param numeroEntradas
     * @return
     */
    private ArrayList<Regalo> crearDatosTesting(int numeroEntradas)
    {
        ArrayList<Regalo> datos = new ArrayList<Regalo>();
        Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.sloth);
        Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.nav_drawer_background);

        for(int i=0; i<numeroEntradas; i++)
        {
            Regalo r = new Regalo();
            Usuario u = new Usuario();
            u.setNombre("David");
            r.setTitulo("Perezoso XR");
            r.setUsuarioPropietario(u);
            Random random = new Random();
            r.setNumLikes(random.nextInt(50));

            Bitmap bMap = random.nextInt(2) == 0 ? bMap1 : bMap2;
            r.setImagen(bMap);

            datos.add(r);
        }
        return datos;
    }
}
