package es.trigit.gitftask.Pantallas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.analytics.HitBuilders;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.trigit.gitftask.Conexion.Respuesta;
import es.trigit.gitftask.Conexion.IServicio;
import es.trigit.gitftask.Objetos.Usuario;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.GAHelper;
import es.trigit.gitftask.Utiles.Globales;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import com.google.gson.Gson;

public class ActivityLogin extends FragmentActivity {

    private enum TipoLogin {login, registro, facebook}

    private ProgressDialog mProgressDialog;
    private boolean mModoRegistrar;
    private Usuario mUsuario;
    private Activity mActivity;

    @InjectView(R.id.panelSuperior)
    View panelSuperior;
    @InjectView(R.id.drwRegalo)
    View drwRegalo;
    @InjectView(R.id.progress)
    CircularProgressView progress;
    @InjectView(R.id.tvGiftask)
    TextView tvGiftask;
    @InjectView(R.id.etActivityLogin_email)
    MaterialEditText etEmail;
    @InjectView(R.id.etActivityLogin_nickname)
    MaterialEditText etNickname;
    @InjectView(R.id.etActivityLogin_password)
    MaterialEditText etPassword;
    @InjectView(R.id.btActivityLogin_facebook)
    LoginButton btLoginFacebook;
    @InjectView(R.id.btActivityLogin_registrar)
    Button btRegistrar;
    @InjectView(R.id.btActivityLogin_conectar)
    Button btConectar;


    /**
     * Variables para el login de Facebook
     */
    private UiLifecycleHelper uiHelper;

    int originalHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mActivity = this;

        uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                onSessionStateChange(session, sessionState, e);
            }
        });
        uiHelper.onCreate(savedInstanceState);

        btLoginFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        mModoRegistrar = false;
        mUsuario = new Usuario();
        mProgressDialog = new ProgressDialog(this);
        etNickname.setVisibility(View.INVISIBLE);

        ((GAHelper)this.getApplication()).crearTracker();
    }

    @OnClick(R.id.btActivityLogin_registrar)
    public void pulsarRegistrar(Button button) {
        GAHelper.tracker.send(new HitBuilders.EventBuilder()
                .setCategory("BTN-LOGIN")
                .setAction("Pulsar Registrar")
                .build());

        etNickname.setVisibility(View.VISIBLE);
        btRegistrar.setVisibility(View.INVISIBLE);
        mModoRegistrar = !mModoRegistrar;
    }

    @OnClick(R.id.btActivityLogin_conectar)
    public void pulsarConectar(Button button) {
        if (mModoRegistrar) {
            GAHelper.tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("ACTION-LOGIN")
                    .setAction("Registro")
                    .build());

            new ThreadRegistro().execute();
        } else {
            GAHelper.tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("ACTION-LOGIN")
                    .setAction("Iniciar Sesión")
                    .build());

//            new ThreadLogin().execute();
            hacerLogin();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mModoRegistrar) {
            super.onBackPressed();
        } else {
            GAHelper.tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("BTN-LOGIN")
                    .setAction("Volver registrar")
                    .build());
            mModoRegistrar = false;
            etNickname.setVisibility(View.INVISIBLE);
            btRegistrar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    private void finActivity(TipoLogin tipo) {
        Globales.setUsuarioLogueado(mUsuario);
        mActivity.startActivity(new Intent(mActivity, ActivityPrincipal.class));
        mProgressDialog.dismiss();
        finish();
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            new ThreadFacebook().execute(session);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void muestraCargando() {
        // Oculto el teclado
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);

        // Dimensiones de la pantalla
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        originalHeight = panelSuperior.getMeasuredHeight();

        // Animación del tamaño de la caja
        ValueAnimator anim = ValueAnimator.ofInt(panelSuperior.getMeasuredHeight(), height + 100);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = panelSuperior.getLayoutParams();
                layoutParams.height = val;
                panelSuperior.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(500);
        anim.start();

        // Mostramos el progreso y el titulo
        progress.setVisibility(View.VISIBLE);
        tvGiftask.setAlpha(0.0f);
        tvGiftask.setVisibility(View.VISIBLE);
        ObjectAnimator animTitle = ObjectAnimator.ofFloat(tvGiftask, "alpha", 0.0f, 1).setDuration(500);
        animTitle.setStartDelay(200);
        animTitle.start();

        // Movemos y escalamos el regalo y el progreso
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(drwRegalo, "scaleX", 1, 2f),
                ObjectAnimator.ofFloat(drwRegalo, "scaleY", 1, 2f),
                ObjectAnimator.ofFloat(tvGiftask, "translationY", 0, height / 14),
                ObjectAnimator.ofFloat(drwRegalo, "translationY", 0, height / 4),
                ObjectAnimator.ofFloat(progress, "translationY", 0, height / 3)
        );
        set.setDuration(1 * 500).start();
    }

    private void ocultaCargando(){

        // Animación del tamaño de la caja
        ValueAnimator anim = ValueAnimator.ofInt(panelSuperior.getMeasuredHeight(), originalHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = panelSuperior.getLayoutParams();
                layoutParams.height = val;
                panelSuperior.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(500);
        anim.start();

        // Mostramos el progreso y el titulo
//        progress.setVisibility(View.GONE);
//        tvGiftask.setAlpha(0.0f);
//        tvGiftask.setVisibility(View.GONE);
        ObjectAnimator animTitle = ObjectAnimator.ofFloat(tvGiftask, "alpha", 1f, 0.0f).setDuration(500);
        animTitle.setStartDelay(200);
        animTitle.start();

        // Movemos y escalamos el regalo y el progreso
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(drwRegalo, "scaleX", drwRegalo.getScaleX(), 1f),
                ObjectAnimator.ofFloat(drwRegalo, "scaleY", drwRegalo.getScaleY(), 1f),
                ObjectAnimator.ofFloat(tvGiftask, "translationY", tvGiftask.getTranslationY(), 0), //originalHeight / 14
                ObjectAnimator.ofFloat(drwRegalo, "translationY", drwRegalo.getTranslationY(), 0), //originalHeight / 4,
                ObjectAnimator.ofFloat(progress, "translationY", progress.getTranslationY(), 0) //originalHeight / 3
        );
        set.setDuration(1 * 500).start();
    }

    private void hacerLogin() {
        RestAdapter ra = new RestAdapter.Builder().setEndpoint(IServicio.API_URL).build();
        IServicio iS = ra.create(IServicio.class);

        muestraCargando();

        iS.login(etNickname.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString(), new Callback<Respuesta.Objeto<Usuario>>() {
            @Override
            public void success(Respuesta.Objeto<Usuario> resp, retrofit.client.Response response) {
                Log.e("LOGIN", "ACIEEEEEEERTO");
                if(resp.response != null) {
                    mUsuario = resp.response;
                    finActivity(TipoLogin.login);
                } else {
                    ocultaCargando();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("LOGIN", error.getMessage());
                Log.e("LOGIN","ERROOOOOOOOOOOOOOOOR");
                ocultaCargando();
            }
        });
    }

    //---------------------------------------------------
    //-------------------- Hebras -----------------------
    //---------------------------------------------------

    public class ThreadRegistro extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            muestraCargando();

            mUsuario.setEmail(etEmail.getText().toString());

            mUsuario.setUsername("NickLogin");
            mProgressDialog.setMessage("Registrando usuario...");

//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //TODO Realizar el registro con el servidor
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            finActivity(TipoLogin.registro);
        }
    }

    public class ThreadLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            muestraCargando();
            mUsuario.setEmail(etEmail.getText().toString());
            mUsuario.setUsername(etNickname.getText().toString());

            mProgressDialog.setMessage("Iniciando sesión...");
//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //TODO Realizar el login con el servidor
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            finActivity(TipoLogin.login);
        }
    }

    public class ThreadFacebook extends AsyncTask<Session, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage("Logueando con Facebook");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Session... session) {

            new Request(
                    session[0],
                    "/me",
                    null,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            try {
                                GraphObject user = response.getGraphObject();
                                mUsuario.setEmail(user.asMap().get("email").toString());
                                mUsuario.setFechaCumpleanos(user.asMap().get("birthday").toString());
                                mUsuario.setNombre(user.asMap().get("first_name").toString() + " " + user.asMap().get("last_name").toString());
                                mUsuario.setGender(user.asMap().get("gender").toString());
                                mUsuario.setLocalidad(user.asMap().get("locale").toString());
                                mUsuario.setImagen(Picasso.with(mActivity).load("https://graph.facebook.com/" + user.asMap().get("id").toString() + "/picture?type=large").get());

                            } catch (IOException e) {
                                //Se puede haber cancelado la sincronizacion con facebook, por lo que user es null
                                e.printStackTrace();
                            }


                            //TODO Registrarse con el correo e id

                        }
                    }
            ).executeAndWait();

            mProgressDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            finActivity(TipoLogin.facebook);
        }
    }


}



