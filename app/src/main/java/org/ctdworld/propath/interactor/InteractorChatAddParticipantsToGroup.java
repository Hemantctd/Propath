package org.ctdworld.propath.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;
import org.ctdworld.propath.contract.ContractChatAddParticipantsToGroup;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractorChatAddParticipantsToGroup implements ContractChatAddParticipantsToGroup.Interactor
{
    private static final String TAG = InteractorChatAddParticipantsToGroup.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context mContext;


    public InteractorChatAddParticipantsToGroup(Context context, OnFinishedListener listener )
    {
        this.listener = listener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;
    }


    // to get all chat with single user
    @Override
    public void  addParticipantsToGroup(Chat chat, List<User> userList)
    {
        Log.i(TAG,"addParticipantsToGroup() method called ");

//        Log.i(TAG,"user array = "+userList.toArray(new User[]{}));
        String[] arrayUserId = new String[userList.size()];
       // StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<userList.size(); i++)
        {
            arrayUserId[i] = userList.get(i).getUserId();
           /* if (i==userList.size()-1)
                stringBuilder.append(userList.get(i).getUserId());  // not to add , at end
            else
                stringBuilder.append(userList.get(i).getUserId()+",");  // adding , to separate id */

        }

        Log.i(TAG,"user id array = "+ Arrays.toString(arrayUserId)/*stringBuilder.toString()*/);


        Map<String,Object> params = new HashMap<>();
        params.put("add_group_memeber","1");
        params.put("admin_id", chat.getGroupAdminId());
        params.put("group_id", chat.getChattingToId());
        params.put("group_type", chat.getGroupType());
        params.put("group_member[]", Arrays.toString(arrayUserId)/*stringBuilder.toString()*/);
     //   params.put("to_id",chat.getChattingToId());

        Log.i(TAG,"params to add participants to group = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {

                Log.i(TAG,"server messageto add participants to group = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());
                    if ("1".equals(jsonObject.getString("res")))
                    {
                        /*JSONObject result = jsonObject.getJSONObject("result");
                        GroupChatInjury groupChatInjury = new GroupChatInjury();

                        groupChatInjury.setAdminId(result.getString("admin_id"));
                        groupChatInjury.setGroupName(result.getString("group_name"));
                        groupChatInjury.setGroupPicUrl(result.getString("ic_add_to_group"));
                        groupChatInjury.setGroupType(result.getString("group_type"));
                        groupChatInjury.setGroupId(result.getString("id"));  // group id*/

                        listener.onParticipantsAddedSuccessfully();
                    }
                    else
                        listener.onFailed(null);

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in createGroup() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onFailed(null);
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                listener.onFailed("Error in establishing connection with server");
                Log.d(TAG,"addParticipants() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });
    }



    // it requests for contacts who are not added in group and filters contacts depending on group type(group_chat, injury_management)
    @Override
    public void requestContactUsers(final Chat chat)
    {
        Log.i(TAG,"requestContactUser() method called ");
        Map<String,String> params = new HashMap<>();
        params.put("contact_to_be_added","1");
        params.put("user_id",chat.getGroupAdminId());  // user_id is admin id
        params.put("group_id",chat.getChattingToId());

        Log.i(TAG,"params to get contact users who are not in group= "+ params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get users who are not in group = "+message);

                new ParseJsonForGroupMembers().execute(message, chat);

                /*JSONObject jsonObject;
                try
                {
                    jsonObject = new JSONObject(message.trim());

                    List<User> userList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("message");

                    for (int i=0 ; i<jsonArray.length() ; i++)
                    {
                        try
                        {


                            JSONObject object = jsonArray.getJSONObject(i);
                            User user = new User();
                            user.setUserId(object.getString("user_id"));
                            user.setName(object.getString("user_name"));
                            String picUrl = RemoteServer.BASE_IMAGE_URL + object.getString("profile_image");
                            user.setUserPicUrl(picUrl);
                            user.setEmail(object.getString("user_email"));
                            userList.add(user);
                            Log.i(TAG, "contact user = " + user.toString());

                        } catch (JSONException e) {
                            Log.e(TAG, "Error in getAllContacts() method , " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    listener.onReceivedContactUsers(userList);
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in requestAllContacts() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onFailed(null);
                }*/
            }
            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestAllContacts() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onFailed(null);
            }
        });
    }






    // handling response for Edit Group name
    class ParseJsonForGroupMembers extends AsyncTask<Object, User, List<User>>
    {
        List<User> userList = new ArrayList<>();
        @Override
        protected List<User> doInBackground(Object... params)
        {
            JSONObject jsonObject;
            Chat chat = new Chat();
            try
            {
                jsonObject = new JSONObject((String) params[0]);
                chat = (Chat) params[1];
                JSONArray jsonArray = jsonObject.getJSONArray("message");

                for (int i=0 ; i<jsonArray.length() ; i++)
                {
                    try
                    {
                        // # roles mentioned below are not to be shown if group type is injury management
                        String roleTrainer = RoleHelper.TRAINER_ROLE_ID;
                        String roleNutritionist =  RoleHelper.NUTRITIONIST_ROLE_ID;
                        String roleStatistician = RoleHelper.STATISTICIAN_ROLE_ID;
                        String roleTeacher = RoleHelper.TEACHER_ROLE_ID;
                        String groupType = chat.getGroupType();

                        JSONObject objectData = jsonArray.getJSONObject(i);
                        String userRole = objectData.getString("user_role");
                        Log.i(TAG,"adding participants group type = "+groupType);

                        if (Chat.GROUP_TYPE_INJURY_MANAGEMENT.contains(groupType))
                        {
                            // not adding user if it's equal to any of role mentioned in condition
                            if (roleTrainer.contains(userRole) || roleNutritionist.contains(userRole) || roleStatistician.contains(userRole) || roleTeacher.contains(userRole))
                                Log.i(TAG,"adding participants , removing role from adding list , role = "+userRole);
                            else
                            {
                                Log.i(TAG,"adding participants , adding role = "+userRole);
                                User user = new User();
                                user.setUserId(objectData .getString("user_id"));
                                user.setName(objectData .getString("user_name"));
                                String picUrl = RemoteServer.BASE_IMAGE_URL + objectData .getString("profile_image");
                                Log.i(TAG,"adding participants , profile image");
                                user.setUserPicUrl(picUrl);
                                user.setEmail(objectData .getString("user_email"));
                                userList.add(user);
                            }
                        }
                        else
                            {
                                Log.i(TAG,"adding participants , adding role = "+userRole);
                            User user = new User();
                            user.setUserId(objectData .getString("user_id"));
                            user.setName(objectData .getString("user_name"));
                            String picUrl = RemoteServer.BASE_IMAGE_URL + objectData .getString("profile_image");
                            Log.i(TAG,"adding participants , profile image = "+picUrl);
                            user.setUserPicUrl(picUrl);
                            user.setEmail(objectData .getString("user_email"));
                            userList.add(user);
                            //  Log.i(TAG, "contact user = " + user.toString());
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "Error in getAllContacts() method , " + e.getMessage());
                        e.printStackTrace();
                    }
                }

            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in requestAllContacts() method , "+e.getMessage());
                e.printStackTrace();
                listener.onFailed(null);
            }

            return userList;
        }


        @Override
        protected void onPostExecute(List<User> userList) {
            Log.i(TAG,"onPostExecute thread = "+Thread.currentThread().getId());
            listener.onReceivedContactUsers(userList);
        }
    }



   // #it filters contacts depending on group type(group_chat, injury_management) and sends
   private void filterUserListByGroupTypeAndSend(List<User> userList, String groupType)
   {
       if (listener != null)
           listener.onReceivedContactUsers(userList);
   }

}
