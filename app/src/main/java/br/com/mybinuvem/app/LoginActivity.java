package br.com.mybinuvem.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import br.com.mybinuvem.app.helper.HttpRequest;
import br.com.mybinuvem.app.helper.WebHttpRequest;
import br.com.mybinuvem.app.model.DadosCliente;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView, mEmpresa;
    private View mProgressView;
    private View mLoginFormView;
    private Context context;
    SharedPreferences preferences;
    private CheckBox checkbox_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mEmpresa = (EditText) findViewById(R.id.empresa);
        checkbox_store = (CheckBox) findViewById(R.id.checkbox_store);

        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String loginStored = preferences.getString("loginStored", "");
        String senhaStored = preferences.getString("senhaStored", "");
        String empresaStored = preferences.getString("empresaStored", "");
        String logoEmpresa =preferences.getString("logoEmpresa", "");
        boolean savelogin = preferences.getBoolean("saveLogin", false);

        checkbox_store.setChecked(savelogin);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mEmailView.setText(loginStored);
        mEmpresa.setText(empresaStored);
        mPasswordView.setText(senhaStored);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        //http://52.11.74.228/img/analise-de-resultados-inicio.jpg

        final View mainLayout = findViewById(R.id.mainLayout);



        if(!logoEmpresa.isEmpty()){

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    mainLayout.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.d("HIYA", "onBitmapFailed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d("HIYA", "onPrepareLoad");
                }
            };

            Picasso.with(this)
                    .load(logoEmpresa)
                    .into(target);

        }
    }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mEmpresa.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String empresa = mEmpresa.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid empresa
        if (TextUtils.isEmpty(empresa)) {
            mEmpresa.setError(getString(R.string.error_field_required));
            focusView = mEmpresa;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, empresa);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        private final String mEmpresa;

        UserLoginTask(String email, String password, String empresa) {
            mEmail = email;
            mPassword = password;
            mEmpresa = empresa;
        }

        @Override
        protected String doInBackground(Void... params) {

            /*
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return "";
            }*/

            StringBuilder link = new StringBuilder("http://www.intranetsistemas.com.br/biweb/serialMyBI.aspx?");
            link.append("param1=");
            link.append(mEmail);
            link.append("&param2=");
            link.append(mPassword);
            link.append("&param3=");
            link.append(mEmpresa);
            link.append("&param4=app");

            HttpRequest http = new WebHttpRequest();
            String retorno = http.getJson(link.toString());

            //return "http://www.mybi-nuvem.com.br";
            return retorno;
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
            showProgress(false);


            if (!success.isEmpty()) {

                Gson g = new Gson();
                DadosCliente dados = g.fromJson(success, DadosCliente.class);

                if(checkbox_store.isChecked()) {

                    preferences.edit().putString("loginStored", mEmail).commit();
                    preferences.edit().putString("senhaStored", mPassword).commit();
                    preferences.edit().putString("empresaStored", mEmpresa).commit();

                }else{
                    preferences.edit().remove("loginStored").commit();
                    preferences.edit().remove("senhaStored").commit();
                    preferences.edit().remove("empresaStored").commit();
                }

                preferences.edit().putBoolean("saveLogin", checkbox_store.isChecked()).commit();
                preferences.edit().putString("logoEmpresa", dados.getImagem()).commit();

                Intent intent = new Intent(context, PrincipalActivity.class);
                intent.putExtra("dados", dados);
                intent.putExtra("login", mEmail);
                intent.putExtra("senha", mPassword);
                startActivity(intent);

            } else {

                Toast.makeText(context,"Dados de acesso inválidos", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

