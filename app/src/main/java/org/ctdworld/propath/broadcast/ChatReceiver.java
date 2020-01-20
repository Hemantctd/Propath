package org.ctdworld.propath.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.ctdworld.propath.contract.ContractChat;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.presenter.PresenterChat;


// this receiver is being used to receive live chat and update chat page
public class ChatReceiver extends BroadcastReceiver
{
    private static final String TAG = ChatReceiver.class.getSimpleName();
    ContractChat.Presenter presenter;

    public static final String KEY_CHAT_ID = "chat id";
    public static final String KEY_CHAT_FROM_ID = "from id";
    public static final String KEY_CHAT_TO_ID = "to id";
    public static final String KEY_CHAT_MESSAGE = "message";
    public static final String KEY_CHAT_IMAGE_URL = "image url";
    public static final String KEY_CHAT_VIDEO_URL = "video url";
    public static final String KEY_CHAT_DATE_TIME = "date time";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG,"chat received  ");


        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                Chat chat = new Chat();
                chat.setId(bundle.getString(KEY_CHAT_ID));
                chat.setMessageFromUserId(bundle.getString(KEY_CHAT_FROM_ID));
                chat.setChattingToId(bundle.getString(KEY_CHAT_TO_ID));
                chat.setMessage(bundle.getString(KEY_CHAT_MESSAGE));
                String picUrl = RemoteServer.BASE_IMAGE_URL+bundle.getString(KEY_CHAT_IMAGE_URL);
                chat.setMessageDateTime(bundle.getString(KEY_CHAT_DATE_TIME));

                ((PresenterChat) presenter).onChatReceived(chat);

            }
            else
                Log.e(TAG,"bundle is null in onReceive() method ");
        }
        else
            Log.e(TAG,"intent is null in onReceive() method");




    }
}
