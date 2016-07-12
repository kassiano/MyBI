package br.com.mybinuvem.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import org.apache.http.util.EncodingUtils;


public class PrincipalActivity extends AppCompatActivity {


    ProgressDialog progressBar;
    WebView webView;
    Context context;
    String LINKAPP;
    final String LOGOUT ="modulos_sair.aspx";

    boolean isLogginOut=false;

    boolean isLogin= true;
    String login="",senha="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();


        if(intent!=null){

            LINKAPP = intent.getStringExtra("url");
            login = intent.getStringExtra("login");
            senha = intent.getStringExtra("senha");

        }else{
            logout();
        }


        webView =(WebView) findViewById(R.id.webView);

        context = this;


        webView.setWebViewClient(new AppWebViewClients());

        //webView.setWebChromeClient(new WebChromeClient());

        /*
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });*/

        // webView.loadUrl("http://twitter.com/AdamSchefter/statuses/236553635608285184");
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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){

            webView.loadUrl(LINKAPP);
        }else {

            webView.postUrl(LINKAPP + "/login.aspx", EncodingUtils.getBytes(postData, "BASE64"));

        }

    }

    private void logout() {

        webView.loadUrl(String.format("%s/%s", LINKAPP , LOGOUT));

        isLogginOut = true;

    }




    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }


        super.onBackPressed();
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

            if(isShowing) {
                progressBar.dismiss();
                isShowing= false;
            }

            if(view.getUrl().endsWith("login.aspx"))
            {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }


            if(isLogin && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        isLogin = false;
                        // Actions to do after 10 seconds
                        webView.loadUrl("javascript:document.getElementById('txtlogin').value='"+login+"';document.getElementById('txtsenha').value='"+senha+"';");
                    }
                }, 500);
            }







            /*
            if(!isLogin) {
                progressBar.dismiss();
            }


            if(isLogin) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        isLogin = false;
                        // Actions to do after 10 seconds
                        webView.loadUrl("javascript:document.getElementById('txtlogin').value='kassiano';document.getElementById('txtsenha').value='kassiano';document.getElementById('ASPxButton1_I').click();");
                    }
                }, 1000);
            }

            //view.loadUrl("javascript:document.getElementById('txtlogin').value='kassiano';document.getElementById('txtsenha').value='kassiano';document.getElementById('ASPxButton1_I').click();");
            //view.loadUrl("javascript:document.getElementById('txtlogin').value='kassiano';document.getElementById('txtsenha').value='kassiano';");

            if(isLogginOut){
                //Intent intent1 = new Intent(context, LoginActivity.class);
                //startActivity(intent1);
            }
*/
        }
    }


}
