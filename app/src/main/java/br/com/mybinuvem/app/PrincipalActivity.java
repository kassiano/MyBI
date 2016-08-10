package br.com.mybinuvem.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.util.EncodingUtils;

import br.com.mybinuvem.app.model.DadosCliente;


public class PrincipalActivity extends AppCompatActivity {


    ProgressDialog progressBar;
    WebView webView;
    Context context;
    String LINKAPP;
    final String LOGOUT ="modulos_sair.aspx";

    boolean isLogginOut=false;

    boolean isLogin= true;
    String login="",senha="";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();


        if(intent!=null){

            DadosCliente dados = (DadosCliente)intent.getSerializableExtra("dados");

            setTitle(dados.getTitulo());


            setIconImage(dados);



            LINKAPP = dados.getUrl();
            login = intent.getStringExtra("login");
            senha = intent.getStringExtra("senha");

        }else{
            logout();
        }


        webView =(WebView) findViewById(R.id.webView);

        context = this;


        webView.setWebViewClient(new AppWebViewClients());


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);


       // webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");

        //webView.getSettings().setUserAgentString("Mozilla/5.0");

        String postData = String.format("login=%s&senha=%s", login, senha);


        Log.i("VERSAO",Build.VERSION.SDK_INT +"" );

        webView.postUrl(LINKAPP, EncodingUtils.getBytes(postData, "BASE64"));

    }


    private void logout() {

        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }




    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }


        super.onBackPressed();
    }


    private void setIconImage(final DadosCliente dados){

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("HIYA", "onBitmapLoaded");
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                BitmapDrawable icon = new BitmapDrawable(toolbar.getResources(), b);
                toolbar.setNavigationIcon(icon);
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

        Picasso.with(toolbar.getContext())
                .load(dados.getImagem())
                .into(target);


    }





    public class AppWebViewClients extends WebViewClient {


        boolean isShowing=false;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if(!isShowing) {
                progressBar = ProgressDialog.show(context, null, "Loading...");
                isShowing=true;
            }


            view.setVisibility(View.INVISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);



            if(view.getUrl().endsWith("login.aspx"))
            {
                logout();

            }else{

                view.setVisibility(View.VISIBLE);

                if(isShowing) {
                    progressBar.dismiss();
                    isShowing= false;
                }

            }


            if(false){//isLogin && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        isLogin = false;
                        // Actions to do after 10 seconds

                        String script ="javascript:document.getElementById('txtlogin').value='"+login+"';document.getElementById('txtsenha').value='"+senha+"';";
                        //script += "document.getElementById('form1').submit();";

                        Log.i("SCRIPT", script);

                        webView.loadUrl(script);
                    }
                }, 500);
            }

        }
    }


}
