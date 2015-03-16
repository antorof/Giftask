package es.trigit.gitftask.Pantallas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Arrays;

import es.trigit.gitftask.R;

/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class ActivityLogin extends FragmentActivity  implements ConnectionCallbacks, OnConnectionFailedListener{


    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mEmailLoginFormView;
    ImageView imagen;
    private View mSignOutButtons;
    private View mLoginFormView;


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

    /**
     * Variables para el login de Google+
     */
    private ProgressDialog mConnectionProgressDialog;
    private SignInButton btLoginGooglePlus;
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private PlusClient mPlusClient;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        imagen = (ImageView) this.findViewById(R.id.imageView);
        btLoginFacebook = (LoginButton) this.findViewById(R.id.authButton);
        btLoginFacebook.setReadPermissions(Arrays.asList("public_profile","email"));
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmailLoginFormView = findViewById(R.id.email_login_form);
        mSignOutButtons = findViewById(R.id.plus_sign_out_buttons);

        btLoginFacebook.setOnErrorListener(new LoginButton.OnErrorListener() {
            @Override
            public void onError(FacebookException error) {
                Log.i("", "Error " + error.getMessage());
            }
        });


        // Find the Google+ sign in button.
        btLoginGooglePlus = (SignInButton) findViewById(R.id.plus_sign_in_button);
        btLoginGooglePlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

               googlePlusLogin();
            }
        });


        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
            }
        });


        //---------------------GOOGLE PLUS---------------------------
        mPlusClient = new PlusClient.Builder(this, this, this)
               // .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();
        // Se tiene que mostrar esta barra de progreso si no se resuelve el fallo de conexión.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");


    }


    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //uiHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            Bundle params = new Bundle();
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

                                Picasso.with(ActivityLogin.this).load(url).into(imagen);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();

            Log.d("DEBUG", "facebook session is open ");
            // make request to the /me API
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                // callback after Graph API response with user object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        JSONObject asdf = user.getInnerJSONObject();
                        Log.d("DEBUG", "email: " + user.asMap().get("email").toString());
                        Log.d("DEBUG", "id: " + user.asMap().get("id").toString());
                        Log.d("DEBUG", "first_name: " + user.asMap().get("first_name").toString());
                        Log.d("DEBUG", "last_name: " + user.asMap().get("last_name").toString());
                        Log.d("DEBUG", "gender: " + user.asMap().get("gender").toString());
                        Log.d("DEBUG", "locale: " + user.asMap().get("locale").toString());
                    }
                }
            }).executeAsync();
        }
    }


    //-------------------------------------METODOS GOOGLE PLUS----------------------------------
    @Override
    public void onConnected(Bundle bundle) {
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected() {
        Log.d("", "disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                mPlusClient.connect();
            }
        }
        // Guarda el resultado y resuelve el fallo de conexión con el clic de un usuario.
        mConnectionResult = result;
    }
    private void googlePlusLogin() {

        if (!mPlusClient.isConnecting()) {

            signedInUser = true;

            resolveSignInError();

        }

    }

    private void resolveSignInError() {
        /*if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }*/
    }



}



