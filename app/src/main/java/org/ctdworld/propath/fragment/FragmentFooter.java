package org.ctdworld.propath.fragment;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.AppController;
import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNotification;
import org.ctdworld.propath.activity.ActivityMain;
import org.ctdworld.propath.activity.ActivityChatUserGroupList;
import org.ctdworld.propath.activity.ActivityNewsFeed;
import org.ctdworld.propath.helper.ActivityHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.prefrence.PreferenceNotification;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFooter extends Fragment implements View.OnClickListener, PreferenceNotification.NotificationCountUpdateListener
{
    private static final String TAG = FragmentFooter.class.getSimpleName();

    Context mContext;
    TextView tvNotification;

    public FragmentFooter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_footer, container, false);
        init();

        tvNotification = view.findViewById(R.id.footer_notification_textview);

        view.findViewById(R.id.footer_home).setOnClickListener(this);
        view.findViewById(R.id.footer_message).setOnClickListener(this);
        //view.findViewById(R.id.footer_news_feed).setOnClickListener(this);
        view.findViewById(R.id.footer_event).setOnClickListener(this);
        view.findViewById(R.id.footer_notification_layout).setOnClickListener(this);


        return view;
    }

    private void init()
    {
        mContext = getContext();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // initializing to get update whenever notification count get updated.
        AppController.initializeNotificationCountListener(this);

        tvNotification.setText(String.valueOf(PreferenceNotification.getTotalNotificationCount()));
        Log.i(TAG,"notification count = "+PreferenceNotification.getTotalNotificationCount());
    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.footer_home:
                if ( !ActivityHelper.getInstance().isMainVisible())
                    startActivity(new Intent(mContext, ActivityMain.class));
                /*if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(mContext, ActivityNMaiewsFeed.class));*/
                    break;

            case R.id.footer_message:
                if ( !ActivityHelper.getInstance().isChatVisible())
                {
                    Intent intent = new Intent(mContext, ActivityChatUserGroupList.class);
                    // from bottom menu we have to show only  one to one chat user and groups of group chat. here we are not showing injury management group
                    intent.putExtra(ActivityChatUserGroupList.KEY_TYPE_CHAT_LIST, ActivityChatUserGroupList.VALUE_GROUP_AND_ONE_TO_ONE);
                    startActivity(intent);
                }
                break;

            case R.id.footer_news_feed:
               /* if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(mContext, ActivityNewsFeed.class));*/
                break;

            case R.id.footer_event:

                showGoogleCalendarApp();

                break;


            case R.id.footer_notification_layout:
                if ( !ActivityHelper.getInstance().isNotificationVisible())
                {
                    startActivity(new Intent(mContext, ActivityNotification.class));

                    // clearing notification after clicking on notification
                    PreferenceNotification.clearNotificationCount();
                }
                break;

        }

    }


    private void showGoogleCalendarApp()
    {
        PackageManager pm = mContext.getPackageManager();
        try
        {
            pm.getPackageInfo("com.google.android.calendar", PackageManager.GET_ACTIVITIES);
            Log.i(TAG,"google calendar app is installed");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.calendar");
            // Intent intent1 = Intent.createChooser(intent,"Open Calendar");
            startActivity(intent);

        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.i(TAG,"google calendar app is not installed");
            Log.i(TAG,"now opening play store");

            String title = "\""+getString(R.string.app_name)+"\""+" would like to use Google calendar" ;
            String message = "Allow "+getString(R.string.app_name)+" to use Google calendar";
            DialogHelper.showCustomDialog(mContext, title, message, "Allow", "Don't allow", new DialogHelper.ShowDialogListener() {
                @Override
                public void onOkClicked() {
                    try
                    {
                        Intent openMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.android.calendar"));
                        startActivity(openMarket);
                    }
                    catch (ActivityNotFoundException e1)
                    {
                        Log.e(TAG,"google play is not installed, showGoogleCalendarApp() method");
                    }
                    catch (Exception e1) {}

                }

                @Override
                public void onCancelClicked() {

                }
            });



        }
    }


    // this method is called whenever notification is updated in PreferenceNotification class
    @Override
    public void onNotificationCountUpdated(final int notificationCount)
    {
        Log.i(TAG,"onNotificationCountUpdated() method called ,  count = "+notificationCount);
        if (getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tvNotification != null)
                        tvNotification.setText(""+notificationCount);
                }
            });
        }

    }
}
