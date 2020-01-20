package org.ctdworld.propath.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;

public class ActivityWebView extends AppCompatActivity
{
    private final String TAG = ActivityWebView.class.getSimpleName();


    Toolbar mToolbar;   // toolbar
    TextView mTxtToolbarTitle;  // for toolbar title
    WebView mWebView;  // WebView to set web url
    ProgressBar mProgressBar;
    String sToolbarTitle = "";  // it will contain toolbar title sent from calling component
    String sWebUrl = "";  // it will contain web url sent from calling component

// to make sure ProgressBar is shown only once when this activity starts, after page loading is complete it's made false
    boolean mBoolPageLoadedFirstTime = true;


    // keys to pass data
    public static final String KEY_TOOLBAR = "title"; // key to set toolbar title
    public static final String KEY_WEB_URL = "url"; // key to set web url
    public static final String DRIVE_LINK ="http://drive.google.com/viewerng/viewer?url=";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        init();   // initializing fields
        prepareToolbar();
        mProgressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();  // getting bundle sent from calling component
        if (bundle != null)
        {
            sToolbarTitle = (String) bundle.get(KEY_TOOLBAR);  // getting toolbar title from bundle sent from calling component
            sWebUrl = (String) bundle.get(KEY_WEB_URL);  // getting toolbar title from bundle sent from calling component
        }
        else
            Log.e(TAG,"bundle is null , onCreate() method ");


        mTxtToolbarTitle.setText(sToolbarTitle);  // setting toolbar title


       showWebView();

    }

    private void showWebView()
    {

        mWebView.getSettings().setAppCacheMaxSize( 10 * 1024 * 1024 ); // 10MB
        mWebView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath() );
        mWebView.getSettings().setAllowFileAccess( true );
        mWebView.getSettings().setAppCacheEnabled( true );
        mWebView.getSettings().setJavaScriptEnabled( true );
        mWebView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
        mWebView.setWebViewClient(new MyBrowser());
//        WebView webView = findViewById(R.id.activity_youtube_webview);
//
//        webView.setWebViewClient(new MyBrowser());
//
//
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//
        Log.i(TAG,"webUrl = "+sWebUrl);
        if (sWebUrl != null)
        {
            if (!(sWebUrl.contains("http://") || sWebUrl.contains("https://")))
                sWebUrl = "https://"+sWebUrl;

            Log.i(TAG,"final webUrl = "+sWebUrl);

            mWebView.loadUrl(sWebUrl);
        }
        else
            Log.e(TAG,"sWebUrl is null in showWebView() ");
          //  webView.loadUrl("https://www.youtube.com/watch?v=f8Cb2bJN6QE&list=PLIKoezbPSaXj_ixFvtks2Dlnv0G-K5ILH");

    }


    private void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mWebView = findViewById(R.id.activity_webview_webview);
        mProgressBar = findViewById(R.id.activity_webview_progressbar);
    }


    private void prepareToolbar()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    private class MyBrowser extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {            super.onPageStarted(view, url, favicon);
        Log.i(TAG,"ActivityWebView onPageStarted.........................");
        if (mBoolPageLoadedFirstTime)
        {                //mProgressBar.setVisibility(View.VISIBLE);
                   mBoolPageLoadedFirstTime = false;   // making false so that ProgressBar is shown only once

        }
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            Log.i(TAG,"ActivityWebView onPageFinished.........................");
           hideProgressBar();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.i(TAG,"onReceivedError() method called");
            startBrowser();
            hideProgressBar();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

            startBrowser();
            Log.i(TAG,"onReceivedSslError() method called");
            Log.e(TAG,"getPrimaryError() , error = "+error.getPrimaryError());
            Log.e(TAG,"getUrl() , error = "+error.getUrl());
            Log.e(TAG,"getUrl() , getCertificate = "+error.getCertificate());

            // handler.proceed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.clearHistory();
    }

    private void hideProgressBar()
    {
        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE)
            mProgressBar.setVisibility(View.GONE);
    }



    // starting activity if there is any error
    private void startBrowser()
    {
        Log.i(TAG,"startBrowser() method called , url = "+sWebUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sWebUrl));
        startActivity(intent);
        finish();
    }
}
