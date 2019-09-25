package com.banyan.androidiws.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.banyan.androidiws.R;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;

public class Activity_Web_View extends AppCompatActivity {

    public static final String TAG_URL = "URL";
    public static final String TAG_SCREEN_TITLE = "Screen_Title";

    private Toolbar mToolbar;

    private WebView mWebView;

    private ProgressBar progressBar;

    private Session_Manager session;

    private Utility utility;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__web_view);

        /******************************
         *  SESSION
         *******************************/

        session = new Session_Manager(this);
        session.checkLogin();

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        /******************************
         *  GET DATA
         *******************************/

        String url = sharedPreferences.getString(TAG_URL, "");
        String screen_title = sharedPreferences.getString(TAG_SCREEN_TITLE, "");


        /******************************
         *  FIND VIEW BY ID
         *******************************/


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(screen_title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_primary_24dp));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar_busmap);

        mWebView = (WebView) findViewById(R.id.webView1);

        /******************************
         *  SET UP
         *******************************/
        Function_Verify_Network_Available(this);

        StartWebview(url);

    }

    private void StartWebview(String str_url) {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(str_url);
        mWebView.setWebViewClient(new Activity_Web_View.HelloWebViewClient());
    }


    private class HelloWebViewClient extends WebViewClient {

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

          /*  String cookies = CookieManager.getInstance().getCookie(url);
            Log.d(TAG, "All the cookies in a string:" + cookies);

            for (int i = 0; i < 5; i++) {
                System.out.println("COOKIESSSSS--::: " + cookies);
            }*/
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
            loadError();
        }

        private void loadError() {
            String html = "<html><body><table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
                    + "<tr>"
                    + "<td><div align=\"center\"><font color=\"#606060\" size=\"10pt\">No Internet Try Again Later!</font></div></td>"
                    + "</tr>" + "</table><html><body>";
            System.out.println("html " + html);

            String base64 = android.util.Base64.encodeToString(html.getBytes(),
                    android.util.Base64.DEFAULT);
            mWebView.loadData(base64, "text/html; charset=utf-8", "base64");
            System.out.println("loaded html");
        }
    }


    public void Function_Verify_Network_Available(Context context) {

        System.out.println("#### Function_Verify_Network_Available ");
        try {

            if (!utility.IsNetworkAvailable(Activity_Web_View.this)) {
                utility.Function_Show_Not_Network_Message(Activity_Web_View.this);
            }

        } catch (Exception e) {
            System.out.println("### Exception e " + e.getLocalizedMessage());
        }
    }
}