package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentNewsFeed;
import org.ctdworld.propath.helper.ActivityHelper;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.NavigationDrawerHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.prefrence.SessionHelper;

public class ActivityMain extends AppCompatActivity
{
    private static final String TAG = ActivityMain.class.getSimpleName();
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    Toolbar mToolbar;
    NavigationView mNavigationView;
    TextView mTxtToolbarTitle;
    private Context mContext;
    private ProgressBar mProgressBar;
    private NavigationDrawerHelper mNavigationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        prepareToolbar();
        prepareNavigationDrawer();
        setListeners();

      //  Log.i(TAG,"firebase token = "+SessionHelper.getInstance(mContext).getFirebaseRegistrationToken());

        requestRequiredPermissions();

    }

    private void requestRequiredPermissions() {
        PermissionHelper permissionHelper = new PermissionHelper(mContext);
    if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
        permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, "Please Give Microphone Permission For Voice To Text Feature...");
    }

    // initializing
    private void init()
    {
        mContext = this;
        mNavigationHelper = new NavigationDrawerHelper();
        mToolbar = findViewById(R.id.main_activity_toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mNavigationView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        mProgressBar = findViewById(R.id.activity_main_progress_bar);
    }


    // to prepare actionbar
    private void prepareToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTxtToolbarTitle.setText("NewsFeed");
    }


    // to manage navigation drawer
    private void prepareNavigationDrawer()
    {
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset){}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
               // Log.i(TAG,"Drawer opened");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                //  Log.i(TAG,"Drawer closed");
                Intent intent = mNavigationHelper.getIntentToStartActivity();
                if (intent != null)
                    startActivity(intent);

                mNavigationHelper = new NavigationDrawerHelper();

            }

            @Override
            public void onDrawerStateChanged(int newState) {}
        });


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item)
            {

                mDrawerLayout.closeDrawer(GravityCompat.START);


                if (item.getItemId() == R.id.menu_shop)
                {
                    DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
                    //PreferenceNotification.addNotificationReceived();
                }
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (item.getItemId() == R.id.menu_invite)
                                invitePeopleToApp();
                            else
                                mNavigationHelper.setIntentToStartActivity(mContext,item.getItemId());
                        }
                    }).start();
                }



                return true;
            }
        });

    }

    // sets listeners
    private void setListeners()
    {
        mNavigationView.findViewById(R.id.activity_main_navigation_log_out).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!UtilHelper.isConnectedToInternet(mContext))
                {
                    DialogHelper.showSimpleCustomDialog(mContext, mContext.getString(R.string.title_no_connection),mContext.getString(R.string.message_no_connection));
                    return;
                }
                    mProgressBar.setVisibility(View.VISIBLE);
                    logout();
            }
        });
    }

    private void invitePeopleToApp()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=org.niuctd.propath");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invitation From Propath");
        startActivity(Intent.createChooser(intent, "Share"));

    }


    // to logout
    private void logout()
    {
        Log.i(TAG,"logout() method called ");
        mDrawerLayout.closeDrawer(GravityCompat.START);

        SessionHelper.getInstance(mContext).clearSession(new SessionHelper.LogOutListener()
        {
            @Override
            public void onLogOutSuccess()
            {
                Log.i(TAG,"logged out successfully...");
                mProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(mContext,ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();

            }

            @Override
            public void onLogOutFailed(String message)
            {
                if (message == null || message.isEmpty())
                    message = "Logout Failed...";

                Log.i(TAG,message);
                DialogHelper.showSimpleCustomDialog(mContext,message);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ConstHelper.RequestCode.CREATE_EDIT_NEWS_FEED_POST)
            {
                FragmentNewsFeed fragment = (FragmentNewsFeed) getSupportFragmentManager().findFragmentById(R.id.frag_container);
                if (fragment != null)
                    fragment.loadPostDataList();// requestPostDataList();
            }

                // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_COMMENT && data != null && data.getExtras() != null) {
                NewsFeed.PostData postData = (NewsFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NEWS_FEED_POST_DATA);
                FragmentNewsFeed fragment = (FragmentNewsFeed) getSupportFragmentManager().findFragmentById(R.id.frag_container);
                if (fragment != null)
                    fragment.onPostUpdated(postData);
            }


            // getting latest data of news feed post, from comment activity
            else if (requestCode == ConstHelper.RequestCode.ACTIVITY_NEWS_FEED_SHARE && data != null && data.getExtras() != null) {
                NewsFeed.PostData postData = (NewsFeed.PostData) data.getExtras().getSerializable(ConstHelper.Key.NEWS_FEED_POST_DATA);
                FragmentNewsFeed fragment = (FragmentNewsFeed) getSupportFragmentManager().findFragmentById(R.id.frag_container);
                if (fragment != null)
                    fragment.addPost(postData);
            }


        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if (item.getItemId() == android.R.id.home)
             mDrawerLayout.openDrawer(GravityCompat.START);

         return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        ActivityHelper.getInstance().setMainVisible(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ActivityHelper.getInstance().setMainVisible(false);
    }

}
