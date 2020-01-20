package org.ctdworld.propath;

import android.app.Application;

import net.gotev.uploadservice.UploadService;

import org.ctdworld.propath.prefrence.PreferenceNotification;


public class AppController extends Application
{
    private static AppController mContext;
    private static PreferenceNotification.NotificationCountUpdateListener mNotificationCountUpdateListener;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mContext = this;


        // setting net.gotev:uploadservice:3.4.2 library to upload data as multipart
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.yourcompany.yourapp";
    }


    public static AppController getContext()
    {
        return mContext;
    }


    public static final void initializeNotificationCountListener(PreferenceNotification.NotificationCountUpdateListener listener)
    {
        mNotificationCountUpdateListener = listener;
    }

    public static PreferenceNotification.NotificationCountUpdateListener getNotificationCountUpdateListener()
    {
        return mNotificationCountUpdateListener;
    }

}
