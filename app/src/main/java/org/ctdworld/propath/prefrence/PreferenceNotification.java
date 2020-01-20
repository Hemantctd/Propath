package org.ctdworld.propath.prefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.ctdworld.propath.AppController;

public class PreferenceNotification
{
    private static final String TAG = PreferenceChat.class.getSimpleName();
    //   private static PreferenceChat mInstance = null;
    private static SharedPreferences mPreferences;
   // private Context mContext;


    // listener to update whenever notification count is updated
    public interface NotificationCountUpdateListener {void onNotificationCountUpdated(int notificationCount);}


    // keys
    private static final String KEY_NOTIFICATION_PREF = "pref notification";
    private static final String KEY_NOTIFICATION_COUNT = "notification count";  // TO CHECK WHETHER CHATTING IS BEING DONE OR NOT


    // creating shared preference object
    static
    {
        mPreferences = AppController.getContext().getSharedPreferences(KEY_NOTIFICATION_PREF,Context.MODE_PRIVATE);
        Log.i(TAG,"mPreferences initialized successfully");
    }




    // to increment notification count
    public static void addNotificationReceived()
    {

        Log.i(TAG,"addNotificationReceived");
        int totalNotifications = mPreferences.getInt(KEY_NOTIFICATION_COUNT, 0);
        totalNotifications++;  // incrementing to save as total notifications

        SharedPreferences.Editor editor =  mPreferences.edit();
        editor.putInt(KEY_NOTIFICATION_COUNT,totalNotifications);
        editor.apply();

        NotificationCountUpdateListener listener = AppController.getNotificationCountUpdateListener();
        if (listener != null)
            listener.onNotificationCountUpdated(getTotalNotificationCount());
        else
            Log.e(TAG,"listener is null in addNotificationReceived() method");

    }

    // returns total count of notifications
    public static int getTotalNotificationCount()
    {
        Log.i(TAG,"getTotalNotificationCount, count = "+mPreferences.getInt(KEY_NOTIFICATION_COUNT, 0));
        //  SharedPreferences preferences = context.getSharedPreferences(KEY_CHATTING_STATUS_PREF, Context.MODE_PRIVATE);
        return mPreferences.getInt(KEY_NOTIFICATION_COUNT, 0); // default value is false
    }

    // clearing notification after clicking on notification
    public static void clearNotificationCount()
    {
        Log.i(TAG,"clearNotificationCount");
        //  SharedPreferences preferences = context.getSharedPreferences(KEY_CHATTING_STATUS_PREF, Context.MODE_PRIVATE);
        if (mPreferences != null)
            mPreferences.edit().clear().apply();
    }

}
