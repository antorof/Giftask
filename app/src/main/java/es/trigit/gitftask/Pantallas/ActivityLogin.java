package es.trigit.gitftask.Pantallas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

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

import es.trigit.gitftask.Objetos.Usuario;
import es.trigit.gitftask.R;
import es.trigit.gitftask.Utiles.Globales;

public class ActivityLogin extends FragmentActivity {

    private enum TipoLogin{login, registro, facebook}

    private ProgressDialog mProgressDialog;
    private MaterialEditText etEmail, etPassword, etNickname;
    private View btConectar, btRegistrar;
    private boolean mModoRegistrar;
    private Usuario mUsuario;
    private Activity mActivity;


    /**
     * Variables para el login de Facebook
     */
    private LoginButton btLoginFacebook;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        btLoginFacebook = (LoginButton) this.findViewById(R.id.btActivityLogin_facebook);
        btLoginFacebook.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_about_me"));
        etEmail = (MaterialEditText) this.findViewById(R.id.etActivityLogin_email);
        etPassword = (MaterialEditText) this.findViewById(R.id.etActivityLogin_password);
        etNickname = (MaterialEditText) this.findViewById(R.id.etActivityLogin_nickname);
        btConectar = findViewById(R.id.btActivityLogin_conectar);
        btRegistrar = findViewById(R.id.btActivityLogin_registrar);

        mModoRegistrar = false;
        mUsuario = new Usuario();
        mProgressDialog = new ProgressDialog(this);
        etNickname.setVisibility(View.INVISIBLE);

        btRegistrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etNickname.setVisibility(View.VISIBLE);
                btRegistrar.setVisibility(View.INVISIBLE);
                mModoRegistrar = true;
            }
        });

        btConectar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModoRegistrar) {
                    new ThreadRegistro().execute();

                } else {
                    new ThreadLogin().execute();

                }
            }
        });

    }

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
            /**
             * Para obtener una imagen mas grande
             */
            /*Bundle params = new Bundle();
            params.putBoolean("redirect", false);
            params.putInt("height", 500);
            params.putInt("width", 500);
            params.putString("type", "large");

            new Request(
                    session,
                    "/me/picture",
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            GraphObject graphObject = response.getGraphObject();
                            try {
                                JSONObject jsonObject = graphObject.getInnerJSONObject();
                                String url = jsonObject.getJSONObject("data").getString("url");
                                Picasso.with(ActivityLogin.this).load(url).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();*/



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



