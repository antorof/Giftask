package es.trigit.gitftask.Pantallas;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.analytics.HitBuilders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.GAHelper;
import es.trigit.gitftask.Utiles.Globales;


public class ActivityPrincipal extends ActionBarActivity {
    private final String TAG = "Activity Principal";
    private final int SELECCION_CAMERA = 0;
    private final int SELECCION_GALERIA = 1;
    private final int ANIMATION_TIME = 200;

    //file donde se guarda temporalmente la imagen recortada de galeria
    private File file;

    @InjectView(R.id.boton_flotante)
    FloatingActionsMenu mBotonFlotante;

    @InjectView(R.id.nav_drawer_user_name)
    TextView mUserName;

    @InjectView(R.id.nav_drawer_user_email)
    TextView mUserEmail;

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
            R.drawable.ic_settings_grey600_24dp, // AJUSTES
            R.drawable.ic_info_grey600_24dp, // ABOUT
            R.drawable.ic_launcher, // CERRAR
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ButterKnife.inject(this);

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
    protected void onResume()
    {
        super.onResume();
        mUserName.setText(Globales.getUsuarioLogueado().getUsername());
        mUserEmail.setText(Globales.getUsuarioLogueado().getEmail());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (mBotonFlotante.isExpanded()) {
            mBotonFlotante.collapse();
        }
        // Si no estamos en el Timeline se vuelve al Timeline
        else if(navDrawerItemActivo != NAVDRAWER_ITEM.TIMELINE){
            sustituirFragment(NAVDRAWER_ITEM.TIMELINE);
            navDrawerItemActivo = NAVDRAWER_ITEM.TIMELINE;
            getSupportActionBar().setTitle(NAVDRAWER_TITLE_RES_ID[NAVDRAWER_ITEM.TIMELINE.ordinal()]);
            formatearColorItemsNavDrawer(mDrawerItemListContainer);
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
        else
            super.onBackPressed();
    }

    //-----------------------------------------------
    //----------------- ON CLICK --------------------
    //-----------------------------------------------

    @OnClick(R.id.rlNavDrawer_cabecera)
    public void pulsarCabecera() {
        navDrawerItemActivo = NAVDRAWER_ITEM.CABECERA;
//        getSupportActionBar().setTitle("Editar perfil"); // ToDo Esto no debe hacerse asi
//        formatearColorItemsNavDrawer(mDrawerItemListContainer);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        sustituirFragment(NAVDRAWER_ITEM.CABECERA);
    }

    @OnClick(R.id.boton_flotante_sin_imagen)
    public void pulsarSinImagen() {
        startActivity(new Intent(this, ActivityAnadirGift.class));
        mBotonFlotante.collapse();

    }

    @OnClick(R.id.boton_flotante_galeria)
    public void pulsarGaleria() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("crop", "true");
        pickImageIntent.putExtra("outputX", 500);
        pickImageIntent.putExtra("outputY", 500);
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        pickImageIntent.putExtra("scale", true);
        startActivityForResult(pickImageIntent, SELECCION_GALERIA);
        mBotonFlotante.collapse();
    }

    @OnClick(R.id.boton_flotante_camara)
    public void pulsarCamara() {
        startActivityForResult(new Intent(this, ActivityCamera.class), SELECCION_CAMERA);
        mBotonFlotante.collapse();
    }

    //-----------------------------------------------
    //----------------- NAV DRAWER --------------------
    //-----------------------------------------------

    /**
     * Crea el NavDrawer apropiadamente
     */
    private void crearNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null)
            return;

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navdrawer_item_ajustes, R.string.navdrawer_item_discover){
            /** Se llama cuando el drawer se ha cerrado completamente. */
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mBotonFlotante.setVisibility(View.VISIBLE);
                invalidateOptionsMenu(); // recrear el menú de opciones
            }

            /** Se llama cuando el drawer se desliza. */
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView,slideOffset);
                mBotonFlotante.setVisibility(View.GONE);
            }

            /** Se llama cuando el drawer se ha abierto completamente. */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // recrear el menú de opciones
                // Oculto el teclado
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mBotonFlotante.getWindowToken(), 0);
                // Retraigo el boton flotante
                mBotonFlotante.collapse();
            }
        };
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

        TextView tvNombre = (TextView) findViewById(R.id.nav_drawer_user_name);
        TextView tvEmail = (TextView) findViewById(R.id.nav_drawer_user_email);

        tvNombre.setText(Globales.getUsuarioLogueado().getNombre());
        tvEmail.setText(Globales.getUsuarioLogueado().getEmail());

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
                    case MIS_REGALOS:
                        sustituirFragment(NAVDRAWER_ITEM.MIS_REGALOS);
                        Log.v("Navdrawer", "Seleccionado MIS REGALOS");
                        break;
                    case LO_TENGO:
                    case ABOUT:
                    case AJUSTES:
                        Toast.makeText(ActivityPrincipal.this,"Funcionalidad no desarrollada." ,Toast.LENGTH_SHORT ).show();
                        // BORRAR EN UN FUTURO; ES SOLO PARA PRUEBAS
//                        Globales.setFotoObtenida(BitmapFactory.decodeResource(ActivityPrincipal.this.getResources(), R.drawable.sloth));
//                        Intent intent = new Intent(ActivityPrincipal.this, ActivityAnadirGift.class);
//                        intent.putExtra(AnadirGiftFragment.EXTRA_OPTION, AnadirGiftFragment.EXTRA_CAMERA);
//                        startActivity(intent);
                        Log.v("Navdrawer", "Seleccionado AJUSTES");
                        break;
                    case CERRAR:
                        Log.v("Navdrawer", "Seleccionado CERRAR");
                        cerrarSesion();
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
        if(fragmentTarget == NAVDRAWER_ITEM.CABECERA)
            GAHelper.tracker.send(new HitBuilders.EventBuilder()
                .setCategory("SUSTITUIR-FRAGMENT")
                .setAction("CABECERA")
                .build());
        else
            GAHelper.tracker.send(new HitBuilders.EventBuilder()
                .setCategory("SUSTITUIR-FRAGMENT")
                .setAction(getString(NAVDRAWER_TITLE_RES_ID[fragmentTarget.ordinal()]))
                .build());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.animator_fade_in, R.anim.animator_fade_out);

        switch (fragmentTarget) {
            case TIMELINE:
                ft.replace(R.id.contenedor, TimelineFragment.newInstance());
                break;

            case MIS_REGALOS:
                ft.replace(R.id.contenedor, MisRegalosFragment.newInstance());
                break;

            case DISCOVER:
                ft.replace(R.id.contenedor, DiscoverFragment.newInstance());
                break;

            case CABECERA:
                startActivity(new Intent(this, ActivityEditarPerfil.class));
                break;
        }

        fragmentActivo = fragmentTarget;

        ft.commit();
    }

    /**
     * Se encarga de realizar los procesos necesarios en el cierre de sesion
     */
    private void cerrarSesion() {
        // ToDo Quizas se deban borrar datos, por ahora solo cierra el activity
        startActivity(new Intent(this, ActivityLogin.class));
        finish();
    }

    /**
     * Deshabilita el botón flotante (para clases que hereden de esta)
     */
    protected void deshabilitarBotonFlotante()
    {
        mBotonFlotante.setVisibility(View.GONE);
        mBotonFlotante.setClickable(false);
        mBotonFlotante.setEnabled(false);
    }

    //-----------------------------------------------
    //----------------- ACTIVITY RESULT -------------
    //-----------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent;

        if (requestCode == SELECCION_CAMERA && resultCode == RESULT_OK) {
            intent = new Intent(this, ActivityAnadirGift.class);
            intent.putExtra(AnadirGiftFragment.EXTRA_OPTION, AnadirGiftFragment.EXTRA_CAMERA);
            startActivity(intent);

        }else if(requestCode == SELECCION_GALERIA && resultCode == RESULT_OK){
            Bitmap decodeded = BitmapFactory.decodeFile(file.getAbsolutePath());
            Globales.setFotoObtenida(decodeded);
            file.delete();

            intent = new Intent(this, ActivityAnadirGift.class);
            intent.putExtra(AnadirGiftFragment.EXTRA_OPTION, AnadirGiftFragment.EXTRA_GALERIA);
            startActivity(intent);

        }


    }

    //--------------------------------------------------------------------
    //----------------- FUNCIONES PARA EL RECORTE GALERIA ----------------
    //--------------------------------------------------------------------

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

             file = new File(Environment.getExternalStorageDirectory(),"temporary_holder.jpg");
            try {
                file.createNewFile();
            } catch (IOException e) {}

            return file;
        } else {

            return null;
        }
    }
}
