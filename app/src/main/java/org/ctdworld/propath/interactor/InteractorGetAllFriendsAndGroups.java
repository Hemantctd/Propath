package org.ctdworld.propath.interactor;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractGetAllFriendsAndGroups;
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


public class InteractorGetAllFriendsAndGroups implements ContractGetAllFriendsAndGroups.Interactor
{

    public static final String TAG = InteractorGetAllFriendsAndGroups.class.getSimpleName();
    public OnFinishedListener listener;
    public RemoteServer remoteServer;
    public Context context;



    public InteractorGetAllFriendsAndGroups(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }


    @Override
    public void requestAllFriendsAndGroups(String survey_id) {

        Log.i(TAG,"requestAllFriendsAndGroups() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(survey_id), new RemoteServer.VolleyStringListener()
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
                        listener.onGetAllFriendsAndGroups(getAllFriendsAndGroupsList(jsonObject.getJSONArray("result")));
                    else
                        listener.onShowMessage(jsonObject.getString(""));

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in requestAllFriendsandGroups() method , "+e.getMessage());
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
                Log.d(TAG,"requestAllFriendsandGroups() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }



    private Map<String,String> getParams(String survey_id)
    {

        Map<String,String> params = new HashMap<>();

        params.put("get_frnd_group_survey","1");
        params.put("user_id",SessionHelper.getInstance(context).getUser().getUserId());
        params.put("survey_id",survey_id);

        Log.i(TAG,"params = "+ params);

        return params;
    }

    private List<GetGroupNames> getAllFriendsAndGroupsList(JSONArray jsonArray)
    {
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

                if (object.getString("type").equals("Group"))
                {
                    getGroupNames.setGroup_id(object.getString("id"));
                    Log.d(TAG,"group_id"+ object.getString("id"));
                }
                else
                {
                    getGroupNames.setUser_id(object.getString("id"));
                    Log.d(TAG,"user_id"+ object.getString("id"));

                }

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