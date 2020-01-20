package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractGetRegisteredUsers;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractorGetRegisteredUsers implements ContractGetRegisteredUsers.Interactor
{
    private static final String TAG = InteractorGetRegisteredUsers.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context context;




    public InteractorGetRegisteredUsers(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }

    @Override
    public void requestAllUsers()
    {
        Log.i(TAG,"requestAllUsers() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get all registered users = "+message);
                JSONObject jsonObject;
                try {
                        jsonObject = new JSONObject(message.trim());

                        if ("1".equals(jsonObject.getString("res")))
                            listener.onGetRegisteredUsers(getAllUsersList(jsonObject.getJSONArray("message")));
                        else
                            listener.onShowMessage(jsonObject.getString(""));

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in requestAllUsers() method , "+e.getMessage());
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
                Log.d(TAG,"requestAllUsers() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
               // listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }



    private Map<String,String> getParams()
    {

        Map<String,String> params = new HashMap<>();

        params.put("get_all_users","1");
        // sending user id to get all user who are not my friend
        params.put("user_id",SessionHelper.getInstance(context).getUser().getUserId());

        Log.i(TAG,"params = "+ params);

        return params;
    }

    private List<User> getAllUsersList(JSONArray jsonArray)
    {
        List<User> userList = new ArrayList<>();

        for (int i=0 ; i<jsonArray.length() ; i++)
        {
            try
            {
                JSONObject object = jsonArray.getJSONObject(i);

                if (!SessionHelper.getInstance(context).getUser().getUserId().equals(object.getString("user_id")))
                {
                    User user = new User();
                    user.setUserId(object.getString("user_id"));
                    user.setName(object.getString("user_name"));

                    String picUrl = RemoteServer.BASE_IMAGE_URL+object.getString("profile_image");
                    user.setUserPicUrl(picUrl);
                    user.setEmail(object.getString("user_email"));
                    user.setRoleId(object.getString("user_role"));
                    user.setLocation(object.getString("address"));
                    user.setSports(object.getString("athelete_spot_code"));

                    user.setConnectionRequestStatus(object.getString("friend_request_status"));

                    userList.add(user);
                }

            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in getAllUsersList() method , "+e.getMessage());
                e.printStackTrace();
            }
        }



        return userList;
    }

}
