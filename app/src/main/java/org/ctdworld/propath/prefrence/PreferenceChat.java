package org.ctdworld.propath.prefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferenceChat
{
    private static final String TAG = PreferenceChat.class.getSimpleName();
 //   private static PreferenceChat mInstance = null;
    private SharedPreferences mPreferences;
    private Context mContext;


    // keys to check chatting status whether chatting is being done or not
    private static final String KEY_CHATTING_STATUS_PREF = "chat status pref";
    private static final String KEY_CHATTING_STATUS = "chatting status";  // TO CHECK WHETHER CHATTING IS BEING DONE OR NOT
    // keys to put values of user to whom chatting is being done
    private static final String KEY_CHATTING_STATUS_CHATTING_TO_ID = "chatting to id";



    // keys to put received chats details
    private static final String KEY_CHAT_RECEIVED_TOTAL_MESSAGE_IN_NOTIFICATION = "total message from";
    private static final String KEY_CHAT_RECEIVED_NOTIFICATION_ID = "notification id";



    // creating shared preference object to deal with received messages
    public PreferenceChat(Context context, String userIdChatReceiveFrom)
    {
        mContext = context;
        // creating SharedPreference for user id
        mPreferences = context.getSharedPreferences(userIdChatReceiveFrom,Context.MODE_PRIVATE);
    }


    // to set chatting is being done or not in chat page
    public static void setChattingStatus(Context context, boolean isChattingOn, String userIdChattingTo)
    {
        Log.i(TAG,"setting chatting status and user id to whom chatting is being done");
        SharedPreferences.Editor editor =  context.getSharedPreferences(KEY_CHATTING_STATUS_PREF, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_CHATTING_STATUS,isChattingOn);
        editor.putString(KEY_CHATTING_STATUS_CHATTING_TO_ID, userIdChattingTo);
        editor.apply();

    }

    // returns whether chat is being done or not
    public static boolean getChattingStatus(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(KEY_CHATTING_STATUS_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_CHATTING_STATUS, false); // default value is false
    }


    // returns user id to whom logged in user is chatting
    public static String getCurrentChattingUserId(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(KEY_CHATTING_STATUS_PREF, Context.MODE_PRIVATE);
        return preferences.getString(KEY_CHATTING_STATUS_CHATTING_TO_ID, null); // default value is false
    }

    public static void clearingChattingStatus(Context context)
    {
        Log.i(TAG,"clearingChattingStatus");
        SharedPreferences preferences = context.getSharedPreferences(KEY_CHATTING_STATUS_PREF, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }



    // this adds count of messages
    public void setChatReceivedMessageCountNotification(int messageCount, int notificationId)
    {
        int previousTotalMessage = mPreferences.getInt(KEY_CHAT_RECEIVED_TOTAL_MESSAGE_IN_NOTIFICATION, 0);
        int totalMessages = previousTotalMessage+messageCount;
        SharedPreferences.Editor editor =  mPreferences.edit();
        editor.putInt(KEY_CHAT_RECEIVED_TOTAL_MESSAGE_IN_NOTIFICATION, totalMessages);
        editor.putInt(KEY_CHAT_RECEIVED_NOTIFICATION_ID, notificationId);
        editor.apply();
    }






    // returns to messages in notification tray
    public int getChatReceivedMessageCount()
    {
        return mPreferences.getInt(KEY_CHAT_RECEIVED_TOTAL_MESSAGE_IN_NOTIFICATION, 0); // default value is false
    }


    // returns notification id
    public int getNotificationId()
    {
        return mPreferences.getInt(KEY_CHAT_RECEIVED_NOTIFICATION_ID, 0); // default value is
    }


    public void clearUserChatDetails()
    {
        mPreferences.edit().clear().apply();
    }

}
