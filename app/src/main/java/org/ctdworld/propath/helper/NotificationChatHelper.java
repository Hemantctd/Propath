package org.ctdworld.propath.helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.RemoteMessage;
import org.ctdworld.propath.activity.ActivityChat;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.PreferenceChat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class NotificationChatHelper
{
    private final String TAG = NotificationChatHelper.class.getSimpleName();
    private Context mContext;
    private PreferenceChat mPreferenceChat;



    public NotificationChatHelper(Context mContext) {
        this.mContext = mContext;
    }

    // method to handle data came from server
    public void handleData(RemoteMessage remoteMessage)
    {
        Log.i(TAG,"handleData() method called*******************************************************************************");
        try
        {
            if (remoteMessage.getData() != null)
            {

                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                Chat chat = new Chat();

                if (jsonObject != null)
                {
                    Log.i(TAG,"jsonObjectData = "+jsonObject);   // getting main JsonObject
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");    // getting data json object from outer most json object
                    String strMessageJson = jsonObjectData.getString("message"); // contains all details of sender
                    JSONObject objectMessage = new JSONObject(strMessageJson);

                    // parsing chat_detail JsonObject and setting data to Chat object to update chat page if user is on chat page otherwise to show in notification
                    if (objectMessage != null)
                    {
                        JSONObject jsonObjectChat = objectMessage.getJSONObject("chat_detail");
                        if (jsonObjectChat != null)
                        {
                            String messageFromUserId = jsonObjectChat.getString("from_id");
                            // creating PreferenceChat object to save chat notification details for this current user who has sent message
                            mPreferenceChat = new PreferenceChat(mContext, messageFromUserId);

                            chat.setId(jsonObjectChat.getString("data_id"));  // key data_id id for chat id
                            chat.setMessageType(jsonObjectChat.getString("msg_type"));
                            chat.setGroupType(jsonObjectChat.getString("group_type"));
                            chat.setMessageChatType(jsonObjectChat.getString("chat_type"));

                            // #checking if chat type is not OtoO then it's group chat or injury management chat so adding group details in Chat object
                            if (!Chat.CHAT_TYPE_ONE_TO_ONE.contains(jsonObjectChat.getString("chat_type")))
                            {
                               // chat.setMessageFromUserId(jsonObjectChat.getString(messageFromUserId));
                               // chat.setMessageFromUserName(jsonObjectChat.getString("group_name"));
                              //  chat.setMessageFromUserPicUrl(jsonObjectChat.getString("ic_add_to_group"));
                                chat.setChattingToPicUrl(jsonObjectChat.getString("ic_add_to_group"));
                                chat.setChattingToId(jsonObjectChat.getString("group_id"));
                                chat.setChattingToName(jsonObjectChat.getString("group_name"));
                                chat.setGroupAdminId(jsonObjectChat.getString("admin_id"));
                            }
                            else // # chat type is OtoO setting data in Chat object
                            {

                               // chat.setMessageFromUserId(messageFromUserId);
                               // chat.setMessageFromUserName(jsonObjectChat.getString("from_name"));
                               // chat.setMessageFromUserPicUrl(jsonObjectChat.getString("profile_image"));

                                chat.setChattingToPicUrl(jsonObjectChat.getString("profile_image"));
                                chat.setChattingToId(jsonObjectChat.getString(messageFromUserId));
                                chat.setChattingToName(jsonObjectChat.getString("from_name"));
                            }

                            // formatting data and time
                            String strDate =  DateTimeHelper.getDateTime(jsonObjectChat.getString("date_time"), DateTimeHelper.FORMAT_DATE_TIME) ;
                            chat.setMessageDateTime(strDate); // setting date and time
                            // jsonUrls is JsonObject which contains urls of image and video jsonUrls also contains text message
                            JSONArray jsonUrls = jsonObjectChat.getJSONArray("urls");

                            //setting received message count and notification id in PreferenceChat
                            if (jsonUrls != null)
                            {
                                // if notification id in PreferenceChat is  0 it means no notification has been shown yet, so assigning random notification id
                                if (mPreferenceChat.getNotificationId() == 0)
                                    mPreferenceChat.setChatReceivedMessageCountNotification(jsonUrls.length(), NotificationHelper.getNotificationId());
                                    // if not 0 then getting notification id from PreferenceChat to show message in current notification
                                else
                                    mPreferenceChat.setChatReceivedMessageCountNotification(jsonUrls.length(), mPreferenceChat.getNotificationId());
                            }


                            Log.i(TAG,"user data, catting status = "+PreferenceChat.getChattingStatus(mContext));
                            //checking if chatting page is opened and received message is for current user to whom logged in user is chatting
                            if (PreferenceChat.getChattingStatus(mContext))
                            {
                                String currentChattingUserId = PreferenceChat.getCurrentChattingUserId(mContext);
                                Log.i(TAG,"chatting status = "+PreferenceChat.getChattingStatus(mContext));
                                Log.i(TAG,"currentChattingUserId = "+currentChattingUserId);
                                Log.i(TAG,"messageFromUserIde = "+messageFromUserId);

                                if (currentChattingUserId != null && chat.getMessageFromUserId() != null)
                                {
                                    //checking if received message is for current user to whom logged in user is chatting
                                    if (chat.getMessageFromUserId().equalsIgnoreCase(currentChattingUserId))
                                    {
                                        updateChatPage(jsonUrls, chat);
                                    }
                                    // showing notification
                                    else {
                                        int totalMessageInTray = mPreferenceChat.getChatReceivedMessageCount();
                                        Log.i(TAG,"showing chat notification, total messages = "+totalMessageInTray);
                                        showChatNotification(chat, totalMessageInTray);
                                    }

                                }
                                // showing notification
                                else {
                                    int totalMessageInTray = mPreferenceChat.getChatReceivedMessageCount();
                                    Log.e(TAG,"currentChattingUserId or chat.getMessageFromUserId() is null in handleData() method");
                                    Log.i(TAG,"showing chat notification, total messages = "+totalMessageInTray);
                                    showChatNotification(chat, totalMessageInTray);
                                }
                            }
                            // showing notification
                            else
                            {
                                int totalMessageInTray = mPreferenceChat.getChatReceivedMessageCount();
                                Log.i(TAG,"showing chat notification, total messages = "+totalMessageInTray);
                                showChatNotification(chat, totalMessageInTray);
                            }
                        }
                        else
                            Log.e(TAG,"jsonObjectChat is null in handleData() method");
                    }
                    else
                        Log.e(TAG,"jsonObjectMessage is null handleData() method");
                }
            }

        }
        catch (Exception e)
        {
            Log.e(TAG,"error in handleData() method , "+e.getMessage());
            e.printStackTrace();
        }

    }


    // this method takes urls JsonArray and parses data to update chat page, chat argument contains chat details except message(description)
    private void updateChatPage(JSONArray jsonUrls, Chat chat)
    {
        Log.i(TAG,"updating chat list");
       // ContractChat.Presenter presenter = new PresenterChat(mContext);
        try
        {
            if (jsonUrls != null)
            {
                for (int i=0; i<jsonUrls.length(); i++)
                {
                    // objectDescription contains url of image and video objectDescription also contains text message
                    JSONObject objectDescription = jsonUrls.getJSONObject(i);
                    chat.setMessage(objectDescription.getString("description"));
                   // ((PresenterChat) presenter).onChatReceived(chat);

                    Intent intent = new Intent("chat_receiver");
                    intent.putExtra("chat", chat);

                    Log.i(TAG,"sending chat broadcast..................................................");
                    mContext.sendBroadcast(intent);
                }
            }
            else
                Log.e(TAG,"jsonUrls(chat message) is null in updateChatPage() method");
        }
        catch (JSONException e)
        {
            Log.e(TAG,"Error in updateChatPage() method");
            e.printStackTrace();
        }


    }


    // it creates notification for connection request which contains two buttons Accept and Reject
    private void showChatNotification(Chat chat, int totalMessages)
    {
        // getting notification id from PreferenceChat for the user who has sent chat
        int notificationId = mPreferenceChat.getNotificationId();  // getting notification id from PreferenceChat
        Log.i(TAG,"showing chat notification , notification Id = "+notificationId);

        // creating Intent object to send chat to ActivityChat
        Intent intentChat = new Intent(mContext, ActivityChat.class);
        intentChat.setAction(ActivityChat.KEY_CHAT_ACTION);
        intentChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentChat.putExtra(ActivityChat.KEY_CHAT_NOTIFICATION_ID,notificationId);
        intentChat.putExtra(ActivityChat.KEY_CHAT, chat);

        // creating PendingIntent object for chat
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,notificationId,intentChat,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationHelper notificationHelper = new NotificationHelper(mContext);
        NotificationCompat.Builder builder = notificationHelper.getNotificationBuilder(chat.getMessageFromUserName(),"Sent Message "+totalMessages, NotificationHelper.CHANNEL_CHAT, false);
        builder.setContentIntent(pendingIntent);
        notificationHelper.showChatNotification(builder, NotificationHelper.CHANNEL_CHAT, notificationId); // showing notification
    }



    private void getChat(JSONObject jsonObject)
    {

    }


}
