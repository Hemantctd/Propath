package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractChat;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.model.Chat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InteractorChat implements ContractChat.Interactor
{
    private static final String TAG = InteractorChat.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context mContext;



    public InteractorChat(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;
    }


    // to get all chat with single user
    @Override
    public void requestChat(final Chat chat)
    {
        Log.i(TAG,"requestChatList() method called ");

        Map<String,String> params = new HashMap<>();
        params.put("get_msg_list","1");
        params.put("from_id",SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("to_id",chat.getChattingToId());
        params.put("chat_type", chat.getMessageChatType());  //# chat type OtoO or MtoM


        Log.i(TAG,"params to get chat list = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG,"server message to get chat list = "+message);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("res")))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (jsonArray != null)
                                    updateChatList(jsonArray);
                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG,"Error in requestChatList() method , "+e.getMessage());
                            e.printStackTrace();
                            listener.onFailed(chat);
                        }
                        finally {
                            listener.onHideProgress();
                            listener.onSetViewsEnabledOnProgressBarGone();
                        }

                    }
                }).start();

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestChatList() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
               // listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }


    @Override
    public void requestPreviousChat(Chat chat, int countRequest)
    {
        Log.i(TAG,"requestChatList() method called ");


        for (int i=0; i<10; i++)
        {
            chat.setMessage("message to test previous "+i*countRequest);
            listener.onPreviousChatReceived(chat);
        }

        /*Map<String,String> params = new HashMap<>();
        params.put("get_msg_list","1");
        params.put("from_id",SessionHelper.getInstance(context).getUser().getUserId());
        params.put("to_id",chat.getChattingToId());


        Log.i(TAG,"params to get chat list = "+ params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get chat list = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("res")))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray != null)
                        {
                            updateChatList(jsonArray);
                        }
                    }
                       *//* else
                            listener.onShowMessage(jsonObject.getString(""));*//*

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in requestChatList() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                }
                finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestChatList() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                // listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });*/
    }


    // to send chat to single user
    @Override
    public void sendChat(Chat chat)
    {
       /* if (true)
        {
            onUploadingFileFailed(chat.getFileUploadingFirstPositionInAdapter(), 1);
            return;
        }
*/


        if (chat != null)
        {
            if (chat.getMessageType().equalsIgnoreCase(Chat.MESSAGE_TYPE_MESSAGE))
            {
                Log.i(TAG,"sending message in chat ***********************");
                sendTextMessage(chat);    // sending text message in chat
            }
           /* else if (chat.getMessageType().equalsIgnoreCase(Chat.MESSAGE_TYPE_IMAGE) || chat.getMessageType().equalsIgnoreCase(Chat.MESSAGE_TYPE_VIDEO))
            {
               // Log.i(TAG,"sending "+chat.getFileArray().length+" files in chat ***********************");
                sendFilesInChat(chat, chat.getFileArray()*//*chat.getFileArray()[0]*//*);    // sending files in chat
            }*/
        }
    }





    // this method calls onChatReceived(Chat) method to update chat list
    private void updateChatList(JSONArray jsonArray)
    {
        for (int i=0 ; i<jsonArray.length() ; i++)
        {
            try
            {
                JSONArray array = jsonArray.getJSONArray(i);

                for (int j=0; j<array.length(); j++)
                {
                    try
                    {
                        Chat chat = new Chat();

                        // fetching chat data
                        JSONObject object = array.getJSONObject(j);
                        chat.setId(object.getString("id"));
                        chat.setMessageFromUserId(object.getString("from_id"));
                        chat.setMessageToUserId(object.getString("to_id"));
                        chat.setMessage(object.getString("description")); // description contains, message, image url and video url
                        chat.setMessageType(object.getString("msg_type"));
                        chat.setMessageFromUserName(object.getString("from_name"));
                        chat.setMessageFromUserPicUrl(object.getString("from_profile_image"));
                        chat.setMessageChatType(object.getString("chat_type"));

                        // setting date and time
                        String dateTimeFromServer = object.getString("date_time");
                       // chat.setMessageDate(mDateTimeHelper.getDateStringDayMonthYear(dateTimeFromServer));
                        chat.setMessageDateTime(dateTimeFromServer);

                        listener.onChatReceived(chat);
                        //   chat.setMessageFromUserPicUrl("");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in updateChatList() method , "+e.getMessage());
                e.printStackTrace();
            }
        }

    }


    //this method sends text message
    private void sendTextMessage(final Chat chat)
    {


        Map<String,String> params = new HashMap<>();
        params.put("send_msg","1");
        params.put("from_id",chat.getMessageFromUserId());
        params.put("to_id",chat.getMessageToUserId());  // user id of the user to whom message is being sent
        params.put("message", chat.getMessage()); // if file is being sent then text message would be empty
        params.put("file","[]");  // if text message is being sent then file would be empty
        params.put("msg_type", chat.getMessageType());
        params.put("chat_type", chat.getMessageChatType());
        params.put("group_type", chat.getGroupType()); // setting group type, if it's not fro group then group_type would be empty("").


        Log.i(TAG,"params to send chat = "+ params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(final String message)
            {
                Log.i(TAG,"server response while sending chat = "+message);


                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(message);
                                if ("1".equals(jsonObject.getString("res")))
                                {
                                    JSONObject objectMessage = jsonObject.getJSONObject("message");

                                    chat.setId(objectMessage.getString("data_id"));
                                    chat.setMessageFromUserId(objectMessage.getString("from_id"));
                                    chat.setMessageToUserId(objectMessage.getString("to_id"));
                                    chat.setMessageType(objectMessage.getString("msg_type"));
                                    chat.setMessageFromUserName(objectMessage.getString("from_name"));
                                    chat.setMessageFromUserPicUrl(objectMessage.getString("profile_image"));

                                    // setting date and time
                                    String dateTimeFromServer = objectMessage.getString("date_time");
                                    chat.setMessageDateTime(dateTimeFromServer);


                                    // urls array contains message and urls of images and videos
                                    JSONArray arrayUrls = objectMessage.getJSONArray("urls");
                                    if (arrayUrls != null)
                                    {
                                        for (int i=0; i<arrayUrls.length() ; i++)
                                        {
                                            // objectUrls contains message and url of image and video
                                            JSONObject objectUrls = arrayUrls.getJSONObject(i);
                                            chat.setMessage(objectUrls.getString("description"));

                                            listener.onChatSendSuccess(chat);  // updating chat list
                                        }
                                    }
                                    else
                                        listener.onFailed(chat);
                                }
                            else
                                listener.onFailed(chat);

                            }
                            catch (JSONException e)
                            {
                                Log.e(TAG,"Error in sendChat() method , "+e.getMessage());
                                e.printStackTrace();

                            }
                            finally {
                                listener.onHideProgress();
                                listener.onSetViewsEnabledOnProgressBarGone();
                            }

                        }
                    }).start();



            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(chat);
            }
        });
    }

}
