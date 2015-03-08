package es.trigit.gitftask.Pantallas;

import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.trigit.gitftask.R;


public class ActivityPrincipal extends ActionBarActivity
{
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
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    /**
     * ID del item del NavDrawer activo (por defecto entramos en la pantalla TIMELINE)
     */
    private int navDrawerItemActivo = 0;

    // Todos los posibles items que se pueden añadir al NavDrawer
    protected static final int NAVDRAWER_ITEM_TIMELINE = 0;
    protected static final int NAVDRAWER_ITEM_DISCOVER = 1;
    protected static final int NAVDRAWER_ITEM_AJUSTES = 2;
    protected static final int NAVDRAWER_ITEM_SEPARADOR = -1;

    // Títulos de los posibles items del NavDrawer,
    // los índices deben corresponder con los de la lista superior
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.navdrawer_item_timeline,
            R.string.navdrawer_item_discover,
            R.string.navdrawer_item_ajustes,
    };

    // Iconos de los posibles items del NavDrawer,
    // los índices deben corresponder con los de la lista superior
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_launcher, // TIMELINE
            R.drawable.ic_launcher, // DISCOVER
            R.drawable.ic_launcher, // AJUSTES
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

         mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

    /**
     * Crea el NavDrawer apropiadamente
     */
    private void crearNavDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null)
            return;

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navdrawer_item_ajustes, R.string.navdrawer_item_discover);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.contenedor, TimelineFragment.newInstance())
                .commit();

        rellenarNavDrawer();
    }

    /**
     * Rellena el NavDrawer con la lista de items correspondientes
     */
    private void rellenarNavDrawer()
    {
        mNavDrawerItems.clear();

        mNavDrawerItems.add(NAVDRAWER_ITEM_TIMELINE);
        mNavDrawerItems.add(NAVDRAWER_ITEM_DISCOVER);
        mNavDrawerItems.add(NAVDRAWER_ITEM_AJUSTES);

        crearItemsNavDrawer();
    }

    /**
     * Crea los items del NavDrawer
     */
    private void crearItemsNavDrawer()
    {
        mDrawerItemListContainer = (ViewGroup) findViewById(R.id.navdrawer);
        if (mDrawerItemListContainer == null)
            return;

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemListContainer.removeAllViews();

        int i = 0;
        for (int idItem : mNavDrawerItems)
        {
            mNavDrawerItemViews[i] = crearNavDrawerItem(idItem, mDrawerItemListContainer);
            mDrawerItemListContainer.addView(mNavDrawerItemViews[i]);
            i++;
        }
        formatearColorItemsNavDrawer(mDrawerItemListContainer);
    }

    /**
     * Crea un item del NavDrawer
     * @param idItem ID del item correspondiente
     * @param container Contenedor de los NavDrawer
     * @return View del item creado
     */
    private View crearNavDrawerItem(final int idItem, final ViewGroup container)
    {
        int layoutToInflate = 0;

        layoutToInflate = idItem == NAVDRAWER_ITEM_SEPARADOR ? R.layout.navdrawer_separador : R.layout.navdrawer_item;

        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if(idItem == NAVDRAWER_ITEM_SEPARADOR)
        {
            // TODO: añadir lo de accesibilidad
            return view;
        }

        ImageView iconoView = (ImageView) view.findViewById(R.id.icono);
        TextView tituloView = (TextView) view.findViewById(R.id.titulo);

        iconoView.setImageResource(NAVDRAWER_ICON_RES_ID[idItem]);
        tituloView.setText(getString(NAVDRAWER_TITLE_RES_ID[idItem]));

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navDrawerItemActivo = idItem;
                formatearColorItemsNavDrawer(container);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                switch(idItem)
                {
                    case NAVDRAWER_ITEM_TIMELINE:
                        getFragmentManager().beginTransaction()
                                .replace(R.id.contenedor, TimelineFragment.newInstance())
                                .commit();
                        Log.v("Navdrawer", "Seleccionado TIMELINE");
                        break;
                    case NAVDRAWER_ITEM_DISCOVER:
                        getFragmentManager().beginTransaction()
                            .replace(R.id.contenedor, DiscoverFragment.newInstance())
                            .commit();
                        Log.v("Navdrawer", "Seleccionado DISCOVER");
                        break;
                    case NAVDRAWER_ITEM_AJUSTES:
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
    private void formatearColorItemsNavDrawer(ViewGroup container)
    {
        int i = 0;
        for (int idItem : mNavDrawerItems)
        {
            View view = container.getChildAt(i);
            ImageView iconoView = (ImageView) view.findViewById(R.id.icono);
            TextView tituloView = (TextView) view.findViewById(R.id.titulo);

            iconoView.setColorFilter(idItem == navDrawerItemActivo ?
                    getResources().getColor(R.color.navdrawer_item_seleccionado) :
                    getResources().getColor(R.color.navdrawer_item_no_seleccionado));

            tituloView.setTextColor(idItem == navDrawerItemActivo ?
                    getResources().getColor(R.color.navdrawer_item_seleccionado) :
                    getResources().getColor(R.color.navdrawer_item_no_seleccionado));

            i++;
        }
    }
}
