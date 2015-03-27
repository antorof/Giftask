package es.trigit.gitftask.Pantallas;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.trigit.gitftask.R;


public class ActivityPrincipal extends ActionBarActivity {
    private final String TAG = "Activity Principal";

    private final int ANIMATION_TIME = 200;

    @InjectView(R.id.boton_flotante)
    ImageButton mBotonFlotante;

    @InjectView(R.id.boton_flotante_galeria)
    ImageButton mBotonFlotanteGaleria;

    @InjectView(R.id.capa_transparente)
    View mCapaTransparente;

    /**
     * NavDrawer
     */
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Toolbar mToolbar;

    /**
     * ViewGroup con los elementos del NavDrawer
     */
    private ViewGroup mDrawerItemListContainer;

    /**
     * View que corresponden con cada item del NavDrawer
     */
    private View[] mNavDrawerItemViews = null;

    /**
     * Listado en orden de los item que se han añadido al NavDrawer
     */
    private ArrayList<NAVDRAWER_ITEM> mNavDrawerItems = new ArrayList<NAVDRAWER_ITEM>();

    /**
     * Item del NavDrawer activo (por defecto entramos en la pantalla TIMELINE)
     */
    private NAVDRAWER_ITEM navDrawerItemActivo = NAVDRAWER_ITEM.TIMELINE;

    /**
     * Enumerado con los items del Navdrawer
     */
    private static enum NAVDRAWER_ITEM {
        TIMELINE, MIS_REGALOS, LO_TENGO, DISCOVER, AJUSTES, ABOUT, CERRAR, SEPARADOR, CABECERA
    };

    /**
     * Indica que fragmen esta activo
     */

    private NAVDRAWER_ITEM fragmentActivo;
    /**
     * Títulos de los posibles items del NavDrawer,
     * los índices deben corresponder con los de la lista superior
     */
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_timeline,
            R.string.navdrawer_item_mis_regalos,
            R.string.navdrawer_item_lo_tengo,
            R.string.navdrawer_item_discover,
            R.string.navdrawer_item_ajustes,
            R.string.navdrawer_item_about,
            R.string.navdrawer_item_cerrar,
    };

    /**
     * Iconos de los posibles items del NavDrawer,
     * los índices deben corresponder con los de la lista superior
     */
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_launcher, // TIMELINE
            R.drawable.ic_launcher, // MIS REGALOS
            R.drawable.ic_launcher, // LO TENGO
            R.drawable.ic_launcher, // DISCOVER
            R.drawable.ic_launcher, // AJUSTES
            R.drawable.ic_launcher, // ABOUT
            R.drawable.ic_launcher, // CERRAR
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ButterKnife.inject(this);

        mCapaTransparente.setClickable(false);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.contenedor, TimelineFragment.newInstance())
                .commit();

        crearNavDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-----------------------------------------------
    //----------------- ON CLICK --------------------
    //-----------------------------------------------

    @OnClick(R.id.rlNavDrawer_cabecera)
    public void pulsarCabecera() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        sustituirFragment(NAVDRAWER_ITEM.CABECERA);
    }

    @OnClick(R.id.boton_flotante_galeria)
    public void pulsarGaleria(){
        startActivity(new Intent(this, ActivityCamera.class));
    }

    @OnClick(R.id.boton_flotante)
    public void pulsarFlotante(ImageButton button){

        if(mBotonFlotanteGaleria.getVisibility() == View.VISIBLE)
        {
            //TODO: animar desaparecer
            animarDesaparecerFlotante();
        }else {
            //TODO: animar aparecer
            animarAparecerFlotante();
        }
    }

    @OnClick(R.id.capa_transparente)
    public void pulsarCapaTransparente()
    {
        mCapaTransparente.setClickable(true);
        animarDesaparecerFlotante();
    }

    private void animarDesaparecerFlotante()
    {
        Animation fade_out = AnimationUtils.loadAnimation(this, R.anim.anim_desappear);
        Animation rotate_in = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_right);
        rotate_in.setDuration(ANIMATION_TIME);
        fade_out.setDuration(ANIMATION_TIME);

        mBotonFlotante.startAnimation(rotate_in);
        mBotonFlotanteGaleria.startAnimation(fade_out);
        mBotonFlotanteGaleria.setVisibility(View.INVISIBLE);
        mCapaTransparente.setBackgroundColor(getResources().getColor(R.color.transparente));
        mCapaTransparente.setClickable(false);
    }

    private void animarAparecerFlotante()
    {
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.anim_appear);
        Animation rotate_in = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_right);
        rotate_in.setDuration(ANIMATION_TIME);
        fade_in.setDuration(ANIMATION_TIME);

        mBotonFlotante.startAnimation(rotate_in);
        mBotonFlotanteGaleria.startAnimation(fade_in);
        mBotonFlotanteGaleria.setVisibility(View.VISIBLE);
        mCapaTransparente.setBackgroundColor(getResources().getColor(R.color.transparenteCapa));
        mCapaTransparente.setClickable(true);
    }

    /**
     * Crea el NavDrawer apropiadamente
     */
    private void crearNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null)
            return;

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navdrawer_item_ajustes, R.string.navdrawer_item_discover);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(NAVDRAWER_TITLE_RES_ID[navDrawerItemActivo.ordinal()]);

        rellenarNavDrawer();
    }

    /**
     * Rellena el NavDrawer con la lista de items correspondientes
     */
    private void rellenarNavDrawer() {
        mNavDrawerItems.clear();

        mNavDrawerItems.add(NAVDRAWER_ITEM.TIMELINE);
        mNavDrawerItems.add(NAVDRAWER_ITEM.MIS_REGALOS);
        mNavDrawerItems.add(NAVDRAWER_ITEM.LO_TENGO);
        mNavDrawerItems.add(NAVDRAWER_ITEM.DISCOVER);
        mNavDrawerItems.add(NAVDRAWER_ITEM.SEPARADOR);
        mNavDrawerItems.add(NAVDRAWER_ITEM.AJUSTES);
        mNavDrawerItems.add(NAVDRAWER_ITEM.ABOUT);
        mNavDrawerItems.add(NAVDRAWER_ITEM.SEPARADOR);
        mNavDrawerItems.add(NAVDRAWER_ITEM.CERRAR);

        crearItemsNavDrawer();
    }

    /**
     * Crea los items del NavDrawer
     */
    private void crearItemsNavDrawer() {
        mDrawerItemListContainer = (ViewGroup) findViewById(R.id.navdrawer);
        if (mDrawerItemListContainer == null)
            return;

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemListContainer.removeAllViews();

        int i = 0;
        for (NAVDRAWER_ITEM navItem : mNavDrawerItems) {
            mNavDrawerItemViews[i] = crearNavDrawerItem(navItem, mDrawerItemListContainer);
            mDrawerItemListContainer.addView(mNavDrawerItemViews[i]);
            i++;
        }
        formatearColorItemsNavDrawer(mDrawerItemListContainer);
    }

    /**
     * Crea un item del NavDrawer
     *
     * @param navItem   Item a crear
     * @param container Contenedor de los NavDrawer
     * @return View del item creado
     */
    private View crearNavDrawerItem(final NAVDRAWER_ITEM navItem, final ViewGroup container) {
        int layoutToInflate = 0;

        layoutToInflate = navItem == NAVDRAWER_ITEM.SEPARADOR ? R.layout.navdrawer_separador : R.layout.navdrawer_item;

        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (navItem == NAVDRAWER_ITEM.SEPARADOR) {
            // TODO: añadir lo de accesibilidad
            return view;
        } else {
            view.setBackgroundResource(R.drawable.drawer_item_selector);
        }

        ImageView iconoView = (ImageView) view.findViewById(R.id.icono);
        TextView tituloView = (TextView) view.findViewById(R.id.titulo);

        iconoView.setImageResource(NAVDRAWER_ICON_RES_ID[navItem.ordinal()]);
        tituloView.setText(getString(NAVDRAWER_TITLE_RES_ID[navItem.ordinal()]));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawerItemActivo = navItem;
                getSupportActionBar().setTitle(NAVDRAWER_TITLE_RES_ID[navItem.ordinal()]);
                formatearColorItemsNavDrawer(container);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                switch (navItem) {
                    case TIMELINE:
                        sustituirFragment(NAVDRAWER_ITEM.TIMELINE);
                        Log.v("Navdrawer", "Seleccionado TIMELINE");
                        break;
                    case DISCOVER:
                        sustituirFragment(NAVDRAWER_ITEM.DISCOVER);
                        Log.v("Navdrawer", "Seleccionado DISCOVER");
                        break;
                    case AJUSTES:
                        Log.v("Navdrawer", "Seleccionado AJUSTES");
                        break;
                }
            }
        });

        return view;
    }

    /**
     * Formatea el color de todos los items del NavDrawer
     * dependiendo de si están seleccionados o no
     */
    private void formatearColorItemsNavDrawer(ViewGroup container) {
        int i = 0;
        for (NAVDRAWER_ITEM navItem : mNavDrawerItems) {
            if (navItem != NAVDRAWER_ITEM.SEPARADOR) {
                View view = container.getChildAt(i);
                ImageView iconoView = (ImageView) view.findViewById(R.id.icono);
                TextView tituloView = (TextView) view.findViewById(R.id.titulo);

                iconoView.setColorFilter(navItem == navDrawerItemActivo ?
                        getResources().getColor(R.color.navdrawer_item_seleccionado) :
                        getResources().getColor(R.color.navdrawer_item_no_seleccionado));

                tituloView.setTextColor(navItem == navDrawerItemActivo ?
                        getResources().getColor(R.color.navdrawer_item_seleccionado) :
                        getResources().getColor(R.color.navdrawer_item_no_seleccionado));
            }

            i++;
        }
    }

    /**
     * Sustituye el fragment actual por uno nuevo mediante una animación
     *
     * @param fragmentTarget Fragment al que navegar
     */
    private void sustituirFragment(NAVDRAWER_ITEM fragmentTarget) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.animator_fade_in, R.anim.animator_fade_out);

        switch (fragmentTarget) {
            case TIMELINE:
                ft.replace(R.id.contenedor, TimelineFragment.newInstance());
                break;

            case DISCOVER:
                ft.replace(R.id.contenedor, DiscoverFragment.newInstance());
                break;

            case CABECERA:
                ft.replace(R.id.contenedor, EditarPerfilFragment.newInstance());
                break;
        }

        fragmentActivo = fragmentTarget;

        ft.commit();
    }
}
