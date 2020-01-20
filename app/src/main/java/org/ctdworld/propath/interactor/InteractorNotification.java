package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import org.ctdworld.propath.contract.ContractNotification;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Request;

import java.util.HashMap;
import java.util.Map;

public class InteractorNotification implements ContractNotification.Interactor
{
    private static final String TAG = InteractorNotification.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context context;




    public InteractorNotification(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }







    /* @Override
    public void responseConnectionRequest(Request connectionRequest)
    {
        Log.i(TAG,"responseConnectionRequest() method called ");


            remoteServer.sendData(RemoteServer.URL, getResponseParams(connectionRequest), new RemoteServer.VolleyStringListener()
            {
                @Override
                public void onVolleySuccessResponse(String message)
                {

                    Log.i(TAG,"server message while connection request response = "+message);
                    JSONArray jsonArray;
                    try {
                            jsonArray = new JSONArray(message.trim());
                            if (jsonArray.length()>0)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if ("1".equals(jsonObject.getString("res")))
                                    listener.onSuccess();
                                else
                                {
                                    listener.onShowMessage(jsonObject.getString("Result"));
                                }
                            }


                    }
                    catch (JSONException e)
                    {
                        Log.e(TAG,"Error in responseConnectionRequest() method , "+e.getMessage());
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
                    Log.d(TAG,"responseConnectionRequest() method volley error = "+error.getMessage());
                    error.printStackTrace();
                    listener.onHideProgress();
                   // listener.onShowMessage("Connection Error");
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            });




    }*/

/*
    // returns params for responding connection request
    private Map<String, String> getResponseParams(Request request) {

        Map<String, String> params = new HashMap<>();

        params.put("responseByfriend", "1");
        //  params.put("requestid",request.getRequestId());
        params.put("status", request.getStatus());
        params.put("from_id", request.getFromId());
        params.put("to_id", request.getToId());

        Log.i(TAG, "connection request response params = " + params);

        return params;
    }*/


  /*  private List<User> getAllUsersList(JSONArray jsonArray)
    {
        List<User> userList = new ArrayList<>();

        for (int i=0 ; i<jsonArray.length() ; i++)
        {
            try
            {
                User user = new User();
                JSONObject object = jsonArray.getJSONObject(i);

                user.setUserId(object.getString("user_id"));
                user.setName(object.getString("user_name"));

                String picUrl = RemoteServer.BASE_IMAGE_URL+object.getString("profile_image");
                user.setMessageFromUserPicUrl(picUrl);
                user.setEmail(object.getString("user_email"));

                userList.add(user);
            }
            catch (JSONException e)
            {
                Log.e(TAG,"Error in getAllUsersList() method , "+e.getMessage());
                e.printStackTrace();
            }
        }



        return userList;
    }*/

}
