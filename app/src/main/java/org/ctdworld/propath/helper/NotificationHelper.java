package org.ctdworld.propath.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import org.ctdworld.propath.R;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelper
{
    private final String TAG = NotificationHelper.class.getSimpleName();
    private Context mContext;

    //creating channels
    public static final String CHANNEL_CHAT = "chat channel";
    public static final String CHANNEL_REQUEST = "request channel";


    //constructor
    public NotificationHelper(Context context) {
        this.mContext = context;
    }



    // it returns random notification id
    public static int getNotificationId()
    {
        Random random = new Random();
        return random.nextInt(10000);
    }






    // this method creates notification builder object with all common requirements and returns builder. Action in addAction() method will be added in class which will use
    // this builder
    public NotificationCompat.Builder getNotificationBuilder(String title, String message, String notificationId, boolean autoCancel)
    {

        long[] pattern = {500, 500, 500, 500, 500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);  // getting default notification URI to notify

        // creating notification Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, notificationId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))  // to show icon on right side
                .setAutoCancel(autoCancel)
                .setVibrate(pattern)
                .setLights(Color.YELLOW, 1, 1)
                .setSound(defaultSoundUri);

                // setting message if message is not null
                if (message != null)
                    notificationBuilder.setContentText(message);


        return notificationBuilder;
    }







    // to show chat notification, this method will show only one message and it will keep updating dta
    public void showChatNotification(NotificationCompat.Builder notificationBuilder, String channelId, int notificationId)
    {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        // checking if current android version is oreo or greater than oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
           // String channelId = mContext.getResources().getString(R.string.default_notification_chanel_id);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Athlete Life", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Athlete Life");

            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setChannelId(channelId);

        }

        if (notificationManager != null)
        {
            // #Checking whether notification is enabled or not in setting page to show notification.
            // # all notifications will be shown from here, this has been in each and every page to show notification, so if we disabled
            // # notification here it will be disabled at each and every place
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (preferences != null)
            {
                boolean isChecked = preferences.getBoolean(mContext.getString(R.string.preference_notification), true);
                if (isChecked)
                {
                    Log.i(TAG,"notification is enabled in setting so showing notification");
                    notificationManager.notify(notificationId, notificationBuilder.build());
                }
                else
                    Log.e(TAG,"notification is disabled in setting so not showing notification");
            }
        }
        else
            Log.e(TAG,"notificationManager is null in showNotification() method ");

    }









    // to show request notification, this method will show different notification for all request
    public void showRequestNotification(NotificationCompat.Builder notificationBuilder, String channelId, int notificationId)
    {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        // checking if current android version is oreo or greater than oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //String channelId = mContext.getResources().getString(R.string.default_notification_chanel_id);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Requests", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Request");

            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setChannelId(channelId);

        }
        if (notificationManager != null)
            notificationManager.notify(notificationId, notificationBuilder.build());
        else
            Log.e(TAG,"notificationManager is null in showNotification() method ");

    }
}
