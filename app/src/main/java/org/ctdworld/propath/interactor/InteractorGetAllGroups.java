package org.ctdworld.propath.interactor;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractGetAllGroups;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.GetGroupNames;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorGetAllGroups implements ContractGetAllGroups.Interactor
{

    public static final String TAG = InteractorGetAllGroups.class.getSimpleName();
    public OnFinishedListener listener;
    public RemoteServer remoteServer;
    public Context context;



    public InteractorGetAllGroups(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }


    @Override
    public void requestAllGroups() {

        Log.i(TAG,"requestAllGroups() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get all groups and friends = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());
                    Log.d(TAG,"server message FriendsandGroups = "+message);

                    if ("1".equals(jsonObject.getString("res")))
                        listener.onGetAllGroups(getAllGroupsList(jsonObject.getJSONArray("result")));
                    else
                        listener.onShowMessage(jsonObject.getString(""));

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in get_frnd_group_nutrition() method , "+e.getMessage());
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
                Log.d(TAG,"get_frnd_group_nutrition() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }



    private Map<String,String> getParams()
    {

        Map<String,String> params = new HashMap<>();

        params.put("get_frnd_group_nutrition","1");
        params.put("user_id",SessionHelper.getInstance(context).getUser().getUserId());

        Log.i(TAG,"params = "+ params);

        return params;
    }

    private List<GetGroupNames> getAllGroupsList(JSONArray jsonArray)
    {
        Log.d(TAG,"JSON Array : " + jsonArray);
        List<GetGroupNames> userList = new ArrayList<>();

        for (int i=0 ; i<jsonArray.length() ; i++)
        {
            try
            {
                JSONObject object = jsonArray.getJSONObject(i);
               // Log.d(TAG,"List............ :  : " +jsonArray);
                GetGroupNames getGroupNames = new GetGroupNames();
                getGroupNames.setName(object.getString("name"));
                getGroupNames.setType(object.getString("type"));
                getGroupNames.setGroup_id(object.getString("id"));
                getGroupNames.setProfile_image(object.getString("image"));



                userList.add(getGroupNames);


         }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in getAllFriendsAndGroupsList() method , "+e.getMessage());
                e.printStackTrace();
            }
        }
       return userList;
//
    }


}