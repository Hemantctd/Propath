package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractChatUsers;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Chat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InteractorChatUsersGroupList implements ContractChatUsers.Interactor
{
    private static final String TAG = InteractorChatUsersGroupList.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context mContext;
 //   private DateTimeHelper mDateTimeHelper;




    public InteractorChatUsersGroupList(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
     //   mDateTimeHelper = new DateTimeHelper();
        this.mContext = context;
    }


    // to get list of users to whom logged in user has chatted
    @Override
    public void requestChatUsers(String userId, final String chatListType)
    {
        Log.i(TAG,"requestChatList() method called ");

        Map<String,String> params = new HashMap<>();
        params.put("chat_list","1");
        params.put("user_id",userId);
        params.put("chat_listtype", chatListType);


        Log.i(TAG,"params to get chat users list = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG,"server message to get chat users list = "+message);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("res")))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                if (jsonArray != null && jsonArray.length() > 0)
                                    updateChatUsersList(jsonArray, chatListType);
                                else
                                    listener.onFailed();
                            }
                            else
                            {
                                listener.onFailed();
                            }


                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG,"Error in requestChatUsers() method , "+e.getMessage());
                            e.printStackTrace();
                            listener.onFailed();
                        }

                    }
                }).start();

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestChatUsers() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onFailed();
            }
        });
    }


    // this method calls onChatUsersReceived(Chat) method to update users list. here chatListType is GroupType(group_chat and injury)
    private void updateChatUsersList(JSONArray jsonArray, String chatListType)
    {
        for (int i=0 ; i<jsonArray.length() ; i++)
        {
            Chat chat = new Chat();
            try
            {
                // fetching chat data
                JSONObject object = jsonArray.getJSONObject(i);

                // #json object key "front_id" has been used for both user id(who sent message) and group id, setting user id and group id
                chat.setChattingToId(object.getString("front_id"));
     //           chat.setGroupId(object.getString("front_id"));


                // setting admin id for group
                chat.setGroupAdminId(object.getString("admin_id"));

                // #json object key "name" has been used for both user name and group name , setting value for user name and group name
                chat.setChattingToName(object.getString("name"));  // setting user name
     //           chat.setGroupName(object.getString("name")); // setting group name

                // #json object key "profile_image" has been used for both user pic and group pic , setting value for user pic and group pic
                chat.setChattingToPicUrl(object.getString("profile_image"));
     //           chat.setGroupPicUrl(object.getString("profile_image"));

                chat.setMessageType(object.getString("msg_type"));
                chat.setMessage(object.getString("description"));
                chat.setMessageTime(object.getString("cur_time"));
                chat.setMessageDateTime(object.getString("date_time"));
                chat.setMessageCount(object.getString("count_msg"));
                // #setting chat type OtoO(one to one) or MtoM(group chat or injury management)
                String chatType = object.getString("chat_type");
                chat.setMessageChatType(chatType);
                // #in chat list we are showing only group chat not injury management group, so here MtoM is  for group chat that's
                // why group type is being set Group Chat
                chat.setGroupType(chatListType); // setting group type, chatListType is Group Type

                listener.onChatUserReceived(chat);
            }
            catch (JSONException e)
            {
                listener.onFailed();
                e.printStackTrace();
            }

        }

    }
}
