package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractContact;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractorContact implements ContractContact.Interactor
{
    private static final String TAG = InteractorContact.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context context;


    //Constructor
    public InteractorContact(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }


    // method to get all friends from server
    @Override
    public void requestAllContacts()
    {
        Log.i(TAG,"requestAllContacts() method called ");

        Map<String,String> params = new HashMap<>();
        params.put("get_all_friends","1");
        params.put("user_id",SessionHelper.getInstance(context).getUser().getUserId());
        params.put("status","2");  // status 2 is to get all users who have accepted my request, 0 for those who have not accepted
        params.put("request_for","Friend_request");

        Log.i(TAG,"params to get all friends = "+ params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get all contacts = "+message);
                JSONObject jsonObject;
                try {
                        jsonObject = new JSONObject(message.trim());

                        if ("1".equals(jsonObject.getString("res")))
                            listener.onReceiveAllContact(getAllContacts(jsonObject.getJSONArray("message")));
                        else if("0".equals(jsonObject.getString("res")))
                            listener.onShowMessage(jsonObject.getString("message"));
                        else
                            listener.onFailed();

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in requestAllContacts() method , "+e.getMessage());
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
                Log.d(TAG,"requestAllContacts() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
               // listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    @Override
    public void deleteContact(final String userId)
    {
        Log.i(TAG,"deleteContact() method called ");
        if (userId == null || userId.isEmpty())
            return;

        Log.i(TAG,"deleting user, delete_contact_id_from = "+SessionHelper.getUserId(context));


        Map<String,String> params = new HashMap<>();
        params.put("delete_contact_friend","1");
        params.put("delete_contact_id_from",SessionHelper.getUserId(context));

       // String[] deleteUserArr = new String[]{userId};
        for (int i=0; i<1; i++)
        {
            Log.i(TAG,"deleting user, delete_contact_id_to["+i+"] = "+userId);
            params.put("delete_contact_id_to["+i+"]", userId);

        }


        Log.i(TAG,"params to delete contact = "+ params);
        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"server message to delete contact = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                        listener.onContactDeleted(userId);
                    else if("0".equals(jsonObject.getString("success")))
                        listener.onShowMessage(jsonObject.getString("message"));
                    else
                        listener.onFailed();

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in deleteContact() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestAllContacts() method volley error = "+error.getMessage());
                error.printStackTrace();
            }
        });

    }


   /* private Map<String,String> getParams()
    {

        Map<String,String> params = new HashMap<>();

        params.put("get_all_friends","1");
        params.put("user_id",SessionHelper.getInstance(context).getUser().getUserId());
        params.put("status","2");  // status 2 is to get all users who have accepted my request, 0 for those who have not accepted
        params.put("request_for","Friend_request");

        Log.i(TAG,"params to get all friends = "+ params);

        return params;
    }*/

    private List<User> getAllContacts(JSONArray jsonArray)
    {
        List<User> userList = new ArrayList<>();

        for (int i=0 ; i<jsonArray.length() ; i++)
        {
            try
            {
                JSONObject object = jsonArray.getJSONObject(i);
               // JSONObject objectUserDetails = object.getJSONObject("user_details");

                    User user = new User();
                    user.setUserId(object.getString("user_id"));
                    user.setName(object.getString("user_name"));

                    String picUrl = RemoteServer.BASE_IMAGE_URL+object.getString("profile_image");
                    user.setUserPicUrl(picUrl);
                    user.setEmail(object.getString("user_email"));
                    user.setLocation(object.getString("address"));
                    user.setSports(object.getString("athelete_spot_code"));

                userList.add(user);

                    Log.i(TAG,"contact user = "+user.toString());

            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in getAllContacts() method , "+e.getMessage());
                e.printStackTrace();
            }
        }



        return userList;
    }

}
