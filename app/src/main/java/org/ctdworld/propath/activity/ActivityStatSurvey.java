package org.ctdworld.propath.activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;

public class ActivityStatSurvey extends AppCompatActivity {

    private final String TAG = ActivityStatSurvey.class.getSimpleName();



    Toolbar mToolbar;   // toolbar
    TextView mTxtToolbarTitle;  // for toolbar title
    WebView mWebView;  // WebView to set web url
    ProgressBar mProgressBar;
    String sToolbarTitle = "";  // it will contain toolbar title sent from calling component
    String sWebUrl = "";  // it will contain web url sent from calling component
    ImageView toolbar_img_options_menu;
    Context mContext;
   // long mEnqueueId = 0;
    //BroadcastReceiver mReceiver;
    // to make sure ProgressBar is shown only once when this activity starts, after page loading is complete it's made false
    boolean mBoolPageLoadedFirstTime = true;


    // keys to pass data
    public static final String KEY_TOOLBAR = "title"; // key to set toolbar title
    public static final String KEY_WEB_URL = "url"; // key to set web url
    DownloadManager mDownloadManager;

    //String mStrDownloadedFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_survey);
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


        mTxtToolbarTitle.setText(R.string.stat_data);  // setting toolbar title


        showWebView();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showWebView()
    {

        mWebView.setWebViewClient(new MyBrowser());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);

//
        Log.i(TAG,"webUrl = "+sWebUrl);
        if (sWebUrl != null) {
            mWebView.loadUrl(sWebUrl);
            toolbar_img_options_menu.setVisibility(View.VISIBLE);
            toolbar_img_options_menu.setImageResource(R.drawable.ic_download);

            toolbar_img_options_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PermissionHelper permissionHelper = new PermissionHelper(mContext);
                    String permissionStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    if (permissionHelper.isPermissionGranted(permissionStorage))
                    {
                        mDownloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(sToolbarTitle);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(FileHelper.PUBLIC_DIRECTORY_WITH_APP_FOLDER,"stat data");
                       // Long reference = mDownloadManager.enqueue(request);
                    }
                    else
                    {
                        permissionHelper.requestPermission(permissionStorage, "Storage Permission Required To Download FIle.");
                    }
                }
            });
        }
        else
            Log.e(TAG,"sWebUrl is null in showWebView() ");

    }


    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        toolbar_img_options_menu = findViewById(R.id.toolbar_img_options_menu);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mWebView = findViewById(R.id.activity_webview_webview);
        mProgressBar = findViewById(R.id.activity_webview_progressbar);
    }


    private void prepareToolbar()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }

    }






    private class MyBrowser extends WebViewClient {


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
            Log.i(TAG,"ActivityStatWebView onPageFinished.........................");
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.GONE);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
        mWebView.clearHistory();
    }



}
