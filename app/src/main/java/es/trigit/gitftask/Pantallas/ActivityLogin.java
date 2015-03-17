package es.trigit.gitftask.Pantallas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.trigit.gitftask.Objetos.Usuario;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;

public class ActivityLogin extends FragmentActivity {

    private enum TipoLogin{login, registro, facebook}

    private ProgressDialog mProgressDialog;
    private boolean mModoRegistrar;
    private Usuario mUsuario;
    private Activity mActivity;

    @InjectView(R.id.etActivityLogin_email)  MaterialEditText etEmail;
    @InjectView(R.id.etActivityLogin_nickname)  MaterialEditText etNickname;
    @InjectView(R.id.etActivityLogin_password)  MaterialEditText etPassword;
    @InjectView(R.id.btActivityLogin_facebook)  LoginButton btLoginFacebook;
    @InjectView(R.id.btActivityLogin_registrar)  Button btRegistrar;
    @InjectView(R.id.btActivityLogin_conectar)  Button btConectar;


    /**
     * Variables para el login de Facebook
     */
    private UiLifecycleHelper uiHelper;


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
    }
    @OnClick(R.id.btActivityLogin_registrar)
    public void pulsarRegistrar(Button button){
        etNickname.setVisibility(View.VISIBLE);
        btRegistrar.setVisibility(View.INVISIBLE);
        mModoRegistrar = !mModoRegistrar;
    }

    @OnClick(R.id.btActivityLogin_conectar)
    public void pulsarConectar(Button button){
        if (mModoRegistrar) {
            new ThreadRegistro().execute();

        } else {
            new ThreadLogin().execute();

        }
    }

    @Override
    public void onBackPressed() {
        if (!mModoRegistrar) {
            super.onBackPressed();
        } else {
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

    private void finActivity(TipoLogin tipo){
        Globales.setUsuarioLogueado(mUsuario);
        mActivity.startActivity(new Intent(mActivity, ActivityPrincipal.class));
        mProgressDialog.dismiss();
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

    //---------------------------------------------------
    //-------------------- Hebras -----------------------
    //---------------------------------------------------

    public class ThreadRegistro extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mUsuario.setEmail(etEmail.getText().toString());

            mUsuario.setNickname("NickLogin");
            mProgressDialog.setMessage("Registrando usuario...");

            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //TODO Realizar el registro con el servidor
                Thread.sleep(2000);
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
            mUsuario.setEmail(etEmail.getText().toString());
            mUsuario.setNickname(etNickname.getText().toString());

            mProgressDialog.setMessage("Iniciando sesión...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //TODO Realizar el login con el servidor
                Thread.sleep(2000);
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
                            GraphObject user = response.getGraphObject();;
                            mUsuario.setEmail(user.asMap().get("email").toString());
                            mUsuario.setEdad(user.asMap().get("email").toString());
                            mUsuario.setNombre(user.asMap().get("first_name").toString());
                            mUsuario.setApellidos(user.asMap().get("last_name").toString());
                            mUsuario.setSexo(user.asMap().get("gender").toString());
                            mUsuario.setLocalidad(user.asMap().get("locale").toString());

                            try {
                                mUsuario.setImagen(Picasso.with(mActivity).load("https://graph.facebook.com/"+user.asMap().get("id").toString()+"/picture?type=large").get());
                            } catch (IOException e) {
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



