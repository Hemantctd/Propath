package org.ctdworld.propath.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.contract.ContractGroupChatInjury;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Chat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractorGroupChatInjury implements ContractGroupChatInjury.Interactor
{
    private static final String TAG = InteractorGroupChatInjury.class.getSimpleName();
    private ContractGroupChatInjury.Interactor.OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context mContext;


    public InteractorGroupChatInjury(Context context, ContractGroupChatInjury.Interactor.OnFinishedListener listener )
    {
        this.listener = listener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;
    }


    // to get all chat with single user
    @Override
    public void createGroup(final Chat chat)
    {
        Log.i(TAG,"createGroup method called ");

        Map<String,String> params = new HashMap<>();
        params.put("create_chat_group","1");
        params.put("admin_id", chat.getGroupAdminId());
        params.put("group_name", chat.getChattingToName());
        params.put("group_type", chat.getGroupType());
     //   params.put("to_id",chat.getChattingToId());

        Log.i(TAG,"params to create group chat or injury management = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {



                        Log.i(TAG,"server message to create group chat or injury management = "+message);
                        new ParseJsonForCreatingGroup().execute(message);
                        Log.i(TAG,"parsing json for create group");
                        /*JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("res")))
                            {
                                JSONObject result = jsonObject.getJSONObject("result");
                                GroupChatInjury groupChatInjury = new GroupChatInjury();

                                groupChatInjury.setAdminId(result.getString("admin_id"));
                                groupChatInjury.setGroupName(result.getString("group_name"));
                                groupChatInjury.setGroupPicUrl(result.getString("ic_add_to_group"));
                                groupChatInjury.setGroupType(result.getString("group_type"));
                                groupChatInjury.setGroupId(result.getString("id"));  // group id

                                listener.onGroupCreated(new GroupChatInjury());
                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG,"Error in createGroup() method , "+e.getMessage());
                            e.printStackTrace();
                            listener.onFailed();
                        }
*/


            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestChatList() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });
    }



    // to edit group name
    @Override
    public void editGroupName(final Chat chat)
    {
        Log.i(TAG,"createGroup method called ");

        Map<String,String> params = new HashMap<>();
        params.put("edit_group_name","1");
        params.put("group_id", chat.getChattingToId());
        params.put("group_name", chat.getChattingToName());

        Log.i(TAG,"params to edit group chat or injury management = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {

                Log.i(TAG,"server message to edit group chat or injury management = "+message);
                //parsing json and updating data
                new ParseJsonForEditGroupName().execute(message);

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestChatList() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });
    }


    // to edit group image
    @Override
    public void editGroupImage(final Chat chat)
    {
        Log.i(TAG,"saving group profile pic");
        String uuid = UUID.randomUUID().toString();
        try
        {
            new MultipartUploadRequest(mContext, uuid, RemoteServer.URL)
                    .addFileToUpload(chat.getChattingToPicUrl(), "file")
                    .addParameter("edit_group_image","1")
                    .addParameter("group_id",chat.getChattingToId())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(3)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                           // Log.i(TAG,"onProgress() method called");

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e(TAG,"error while saving group pic , "+exception.getMessage());
                            exception.printStackTrace();
                            DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
                            listener.onFailed();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.i(TAG,"group pic uploaded successfully");
                           // DialogHelper.showSimpleCustomDialog(mContext,"File Saved Successfully...");
                            String response = serverResponse.getBodyAsString();
                            Log.i(TAG,"group pic uploaded successfully , "+response);

                            listener.onEditGroupImageSuccessful(chat);

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            listener.onFailed();
                            DialogHelper.showSimpleCustomDialog(mContext,"Cancelled...");
                        }
                    })
                    .startUpload();



        }
        catch (Exception e)
        {
            Log.e(TAG,"Exception while saving group pic = "+e.getMessage());
            e.printStackTrace();
        }
    }

    // to remove participant
    @Override
    public void removeParticipant(final Chat chat, final int positionInAdapter)
    {
        Log.i(TAG,"removeParticipant() method called ");

        Map<String,String> params = new HashMap<>();
        params.put("remove_group_memeber","1");
        params.put("group_id", chat.getChattingToId());
        params.put("member_id", chat.getGroupMemberId());

        Log.i(TAG,"params to remove user from group = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {


                Log.i(TAG,"server message to remove user from group = "+message);

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("res")))
                            {
                                listener.onRemoveParticipantSuccess(chat, positionInAdapter);
                            }
                            else
                                listener.onFailed();
                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG,"Error in removeParticipant() method , "+e.getMessage());
                            e.printStackTrace();
                            listener.onFailed();
                        }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"removeParticipant() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });

    }


    // to exit group
    @Override
    public void exitGroup(final Chat chat) {
        Log.i(TAG,"exitGroup() method called ");

        Map<String,String> params = new HashMap<>();
        params.put("remove_group_memeber","1");
        params.put("group_id", chat.getChattingToId());
        params.put("member_id", chat.getUserId());

        Log.i(TAG,"params to exit from group = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {


                Log.i(TAG,"server message to exit from group = "+message);

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("res")))
                            {
                                listener.onGroupExitSuccess(chat);
                            }
                            else
                                listener.onFailed();
                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG,"Error in removeParticipant() method , "+e.getMessage());
                            e.printStackTrace();
                            listener.onFailed();
                        }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"exitGroup() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });
    }


    // this method requests server to get group list of a particular type (group_chat or injury_management)
    @Override
    public void requestGroupList(Chat chat) {
        Log.i(TAG,"requestGroupList method called ");
        Map<String,String> params = new HashMap<>();
        params.put("group_list","1");
        params.put("group_type", chat.getGroupType());
        params.put("user_id", chat.getUserId());

        Log.i(TAG,"params to get group list = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i(TAG,"server message to get group list = "+message);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("res")))
                            {
                                Log.i(TAG,"inside success");
                                JSONArray array = jsonObject.getJSONArray("result");
                                if (array != null)
                                {
                                    Log.i(TAG,"inside array");
                                    for (int i=0; i<array.length() ; i++)
                                    {
                                        Log.i(TAG,"inside array object");
                                        JSONObject result = array.getJSONObject(i);

                                        Chat groupChat = new Chat();

                                        groupChat.setGroupAdminId(result.getString("admin_id"));
                                        groupChat.setChattingToName(result.getString("group_name"));
                                        groupChat.setChattingToPicUrl(result.getString("group_icon"));
                                        groupChat.setGroupType(result.getString("group_type"));
                                        groupChat.setChattingToId(result.getString("id"));  // group id

                                        Log.i(TAG,"calling onGroupReceivedListener() method");
                                        listener.onReceivedGroup(groupChat);
                                    }
                                }
                                else
                                {
                                    listener.onFailed();
                                    Log.e(TAG,"json array is null in requestGroupList() method");
                                }


                            }
                            else
                            {
                                if (listener != null)
                                {
                                    String result = jsonObject.getString("result");
                                    if (result != null && result.equalsIgnoreCase("Date not Found"))
                                        listener.onShowMessage("Data not found");
                                    else
                                        listener.onShowMessage("Data not found");


                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG,"Error in createGroup() method , "+e.getMessage());
                            e.printStackTrace();
                            listener.onFailed();
                        }

                    }
                }).start();

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestChatList() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });
    }

    @Override
    public void requestGroupMemberList(Chat chat)
    {
        Log.i(TAG,"request group member list method called ");
        Map<String,String> params = new HashMap<>();
        params.put("group_member_list","1");
        params.put("group_id", chat.getChattingToId());

        Log.i(TAG,"params to get group members list = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(final String message)
            {
                Log.i(TAG,"server message to get group member list = "+message);
                if (message != null)
                    new ParseJsonForGroupMembers().execute(message);
              //  JSONObject jsonObject;
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestChatList() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });
    }




    // handling response for Edit Group name
    private class ParseJsonForEditGroupName extends AsyncTask<String, Void, Chat>
    {
        @Override
        protected Chat doInBackground(String... jsonObjects)
        {
            Chat chat= null;
            try
            {
                String serverResult = jsonObjects[0];
                if (serverResult != null)
                {
                    JSONObject jsonObject = new JSONObject(serverResult);  // get JsonObject object
                    if ("1".equals(jsonObject.getString("res")))
                    {
                        JSONObject result = jsonObject.getJSONObject("result");
                        if (result !=null)
                        {
                            chat = new Chat();
                            /*   groupChatInjury.setAdminId(result.getString("admin_id"));*/
                            chat.setChattingToName(result.getString("group_name"));
                            /*groupChatInjury.setGroupPicUrl(result.getString("ic_add_to_group"));
                            groupChatInjury.setGroupType(result.getString("group_type"));
                            groupChatInjury.setGroupId(result.getString("id"));  // group id*/

                          //  listener.onEditGroupNameSuccessful(groupChatInjury);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in createGroup() method , "+e.getMessage());
                chat = null; // Assigning null in case error
                e.printStackTrace();
                listener.onFailed();
            }
            return chat;
        }

        @Override
        protected void onPostExecute(Chat chat) {
            if (chat != null)
                listener.onEditGroupNameSuccessful(chat);
            else
                listener.onFailed();
        }
    }




    // handling response for Edit Group name
    private class ParseJsonForEditGroupImage extends AsyncTask<String, Void, Chat>
    {
        @Override
        protected Chat doInBackground(String... jsonObjects)
        {
            Chat chat = null;
            try
            {
                String serverResult = jsonObjects[0];
                if (serverResult != null)
                {
                    JSONObject jsonObject = new JSONObject(serverResult);  // get JsonObject object
                    if ("1".equals(jsonObject.getString("res")))
                    {
                        JSONObject result = jsonObject.getJSONObject("result");
                        if (result !=null)
                        {
                            chat = new Chat();
                            /*   groupChatInjury.setAdminId(result.getString("admin_id"));*/
                            chat.setChattingToName(result.getString("group_name"));
                            /*groupChatInjury.setGroupPicUrl(result.getString("ic_add_to_group"));
                            groupChatInjury.setGroupType(result.getString("group_type"));
                            groupChatInjury.setGroupId(result.getString("id"));  // group id*/

                            //  listener.onEditGroupNameSuccessful(groupChatInjury);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in createGroup() method , "+e.getMessage());
                chat = null; // Assigning null in case error
                e.printStackTrace();
                listener.onFailed();
            }
            return chat;
        }

        @Override
        protected void onPostExecute(Chat chat) {
            if (chat != null)
                listener.onEditGroupNameSuccessful(chat);
            else
                listener.onFailed();
        }
    }




    // this parses json came as result after creating group
    private class ParseJsonForCreatingGroup extends AsyncTask<String, Void, Chat>
    {
        @Override
        protected Chat doInBackground(String... jsonObjects)
        {
            Chat chat = null;
            try
            {
                String serverResult = jsonObjects[0];
                if (serverResult != null)
                {
                    JSONObject jsonObject = new JSONObject(serverResult);  // get JsonObject object
                    if ("1".equals(jsonObject.getString("res")))
                    {

                        JSONObject result = jsonObject.getJSONObject("result");
                        if (result !=null)
                        {
                            chat = new Chat();
                            chat.setGroupAdminId(result.getString("admin_id"));
                            chat.setChattingToName(result.getString("group_name"));
                            chat.setChattingToPicUrl(result.getString("group_icon"));
                            chat.setGroupType(result.getString("group_type"));
                            chat.setChattingToId(result.getString("id"));  // group id
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in createGroup() method , "+e.getMessage());
                chat = null; // Assigning null in case error
                e.printStackTrace();
                listener.onFailed();
            }
            return chat;
        }

        @Override
        protected void onPostExecute(Chat chat)
        {
            Log.i(TAG,"onPostExecute() method called to parse create group json, groupChatInjury = "+chat);
            if (chat != null)
                listener.onGroupCreated(chat);
            else
                listener.onFailed();
        }
    }




    // handling response for Edit Group name
    private class ParseJsonForGroupMembers extends AsyncTask<String, Chat, Boolean>
    {
        boolean status = true;
        @Override
        protected Boolean doInBackground(String... jsonObjects)
        {
            Log.i(TAG,"doInBackground thread = "+Thread.currentThread().getId());

            try
            {
                 JSONObject jsonObject = new JSONObject(jsonObjects[0]);
                if ("1".equals(jsonObject.getString("res")))
                {
                    //  Log.i(TAG,"inside success");
                    JSONArray arrayResult = jsonObject.getJSONArray("result");
                    JSONObject object = arrayResult.getJSONObject(0);
                    JSONArray array = object.getJSONArray("member_list");

                    if (array != null)
                    {
                        // Log.i(TAG,"inside array");
                        for (int i=0; i<array.length() ; i++)
                        {
                            //  Log.i(TAG,"inside array object");
                            JSONObject result = array.getJSONObject(i);

                            Chat chat = new Chat();
                            chat.setGroupMemberId(result.getString("member_id"));
                            chat.setGroupMemberRole(result.getString("member_role"));
                            chat.setGroupMemberName(result.getString("name"));
                            chat.setGroupMemberPicUrl(result.getString("profile_image"));

                            Log.i(TAG,"calling onReReceivedGroupMember() method");
                            publishProgress(chat);
                        }
                    }
                    else
                    {
                        status = false;
                        Log.e(TAG,"json array is null in requestGroupList() method");
                    }

                }
                else
                    status = false;
            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in createGroup() method , "+e.getMessage());
                e.printStackTrace();
                status = false;
            }

            return status;
        }



        @Override
        protected void onProgressUpdate(Chat... values)
        {
            Log.i(TAG,"onProgressUpdate thread = "+Thread.currentThread().getId());
            super.onProgressUpdate(values);
            Chat chat = values[0];
            listener.onReReceivedGroupMember(chat);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            Log.i(TAG,"onPostExecute thread = "+Thread.currentThread().getId());

            if (!status)
                listener.onFailed();
        }
    }


}
