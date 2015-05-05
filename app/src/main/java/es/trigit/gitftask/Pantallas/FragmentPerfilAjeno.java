package es.trigit.gitftask.Pantallas;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.trigit.gitftask.Objetos.Regalo;
import es.trigit.gitftask.Objetos.Usuario;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;


public class FragmentPerfilAjeno extends Fragment {

    private final String TAG = "Fragment PerfilAjeno";
    private Usuario usuario;
    private CustomGridViewAdapter mAdapter;
    @InjectView(R.id.fragmen_perfil_ajeno_image_user)CircleImageView civImagen;
    @InjectView(R.id.fragmen_perfil_ajeno_user_name)TextView tvUsername;
    @InjectView(R.id.fragmen_perfil_ajeno_user_email)TextView tvEmail;
    @InjectView(R.id.fragmen_perfil_ajeno_subtitle_gifts)TextView tvNumeroGift;
    @InjectView(R.id.fragmen_perfil_ajeno_subtitle_following)TextView tvSiguiendo;
    @InjectView(R.id.fragmen_perfil_ajeno_subtitle_followers)TextView tvSeguidores;
    @InjectView(R.id.perfil_ajeno_grid) GridView mGridView;

    public static FragmentPerfilAjeno newInstance() {
        FragmentPerfilAjeno fragment = new FragmentPerfilAjeno();
        return fragment;
    }

    public FragmentPerfilAjeno() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        usuario = Globales.getUsuarioConsulta();
        return inflater.inflate(R.layout.framgent_perfil_ajeno, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.inject(this, view);
        civImagen.setImageBitmap(usuario.getImagen());
        tvUsername.setText(usuario.getNickname());
        tvNumeroGift.setText(usuario.getRegalos().size() + "");
        tvEmail.setText(usuario.getEmail());
        tvSiguiendo.setText(usuario.getSiguiendo().size()+"");
        tvSeguidores.setText(usuario.getSeguidores().size()+"");

        mAdapter = new CustomGridViewAdapter(getActivity(), usuario.getRegalos());
        mGridView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        Log.v(TAG, "On resume");
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Se ejecuta trás pulsar sobre un elemento del GridView.
     * Se llama a la actividad {@link ActivityGiftDetalle}
     * enviándole la ID del regalo para que muestre el Regalo
     * que sobre el que se ha pulsado.
     * @param position Posición del item pulsado en el GridView.
     */
    @OnItemClick(R.id.perfil_ajeno_grid)
    public void pulsarItemGrid(int position)
    {
        // TODO: enviar ID del regalo para cargarlo en la actividad detalle
        Intent intent = new Intent(getActivity(), ActivityGiftDetalle.class);
        intent.putExtra("REGALO_ID", mAdapter.getItem(position).getId());
        startActivity(intent);
    }

    /**
     * Clase adaptador para el GridView del Timeline
     */
    public class CustomGridViewAdapter extends ArrayAdapter<Regalo>
    {
        private final Context context;
        LayoutInflater inflater;
        public CustomGridViewAdapter(Context context, ArrayList<Regalo> values)
        {
            super(context, R.layout.row_timeline, values);
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            if (rowView == null) {
                rowView = inflater.inflate(R.layout.row_timeline,parent, false);

                //reusamos la vista
                TimelineFragment.ViewHolderTimeline viewHolder = new TimelineFragment.ViewHolderTimeline();
                viewHolder.tvNumeroMeGustas = (TextView) rowView.findViewById(R.id.gift_numero_me_gustas);
                viewHolder.tvNombreRegalo = (TextView) rowView.findViewById(R.id.gift_nombre);
                viewHolder.ivImagenRegalo = (ImageView) rowView.findViewById(R.id.gift_image);
                viewHolder.tvNombreUsuario = (TextView) rowView.findViewById(R.id.gift_usuario_nombre);
                viewHolder.rlCampoUsuario = (RelativeLayout) rowView.findViewById(R.id.rlRowTimeline_usuario);
                viewHolder.position = position;
                rowView.setTag(viewHolder);
            }

            Regalo regalo = getItem(position);

            //rellenamos los datos
            TimelineFragment.ViewHolderTimeline holder = (TimelineFragment.ViewHolderTimeline) rowView.getTag();
            holder.tvNumeroMeGustas.setText(regalo.getNumLikes()+" ");
            holder.tvNombreRegalo.setText(regalo.getTitulo());
            holder.tvNombreUsuario.setText(regalo.getUsuarioPropietario().getNombre());
            holder.rlCampoUsuario.setVisibility(View.GONE);

            if(getItem(position).getImagen() == null){
                Picasso.with(context).load(regalo.getUrlImagen()).into(holder.ivImagenRegalo);
            }else {
                holder.ivImagenRegalo.setImageBitmap(regalo.getImagen());
            }

            return rowView;
        }
    }


}
