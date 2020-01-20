package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.model.Response;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InteractorRequest implements ContractRequest.Interactor
{
    private static final String TAG = InteractorRequest.class.getSimpleName();
    private OnFinishedListener mListener;
    private RemoteServer mRemoteServer;
    private Context mContext;




    public InteractorRequest(OnFinishedListener onFinishedListener, Context mContext)
    {
        this.mListener = onFinishedListener;
        mRemoteServer = new RemoteServer(mContext);
        this.mContext = mContext;
    }




    @Override
    public void sendRequest(String toUserId, String requestFor)
    {
        Log.i(TAG,"sendRequest() method called ");

        Map<String,String> params = new HashMap<>();

        params.put("send_report_request","1");
        params.put("request_from", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("request_to",toUserId); //  userId   id of to whom request is being sent.
        params.put("request_for", requestFor); // passing request type(request for), like friend request, progress report request, coach feedback

        // old
       /* params.put("send_request","1");
        params.put("to_request_id",userId);
        params.put("from_request_id", SessionHelper.getInstance(mContext).getUser().getUserId());*/

        Log.i(TAG,"params = "+ params);
        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message while sending request = "+message);

                try {

                    if (RemoteServer.isJsonValid(message))  // checking string is valid json
                    {
                        JSONObject jsonObject = new JSONObject(message.trim());

                        if ("1".equals(jsonObject.getString("res")))
                            mListener.onRequestSentSuccessfully();
                        else
                        {
                            if ("You allready Sent Request.".equals(jsonObject.getString("Result")))
                                mListener.onShowMessage("Friend request is already sent");
                            else
                                mListener.onFailed(null);
                        }
                    }
                    else
                        mListener.onFailed(null);
                }
                catch (JSONException e)
                {
                    mListener.onFailed(null);
                    Log.e(TAG,"Error in sendRequest() method , "+e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"sendRequest() method volley error = "+error.getMessage());
                error.printStackTrace();
                mListener.onFailed(null);
            }
        });
    }



    // getting requests of all types
    @Override
    public void getAllRequests()
    {
        Map<String,String> params = new HashMap<>();
        params.put("connection_request_status","1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());

        Log.i(TAG,"params to get all requests = "+ params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server response to connection request list = "+message);

                try {


                    Request request = new Gson().fromJson(message, Request.class);
                    if (request.getResponseStatus(request).equals(Response.STATUS_SUCCESS))
                        mListener.onReceivedAllRequests(request.getRequestList());
                   /* JSONObject jsonObject= new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("res")))
                        showConnectionRequestStatusList(jsonObject.getJSONArray("message"));
                    else {
                        mListener.onFailed();
                    }
*/
                }
                catch (Exception e)
                {
                    Log.e(TAG,"Error in requestConnectionRequestStatus() method , "+e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed(null);
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestConnectionRequestStatus() method volley error = "+error.getMessage());
                error.printStackTrace();
                mListener.onFailed(null);
            }
        });


    }



    @Override
    public void respondToRequest(Request.Data request) 
    {
        Map<String,String> params = new HashMap<>();
        params.put("response_report_request","1");
        params.put("status",request.getRequestStatus());
        params.put("from_id",request.getRequestToUserId());
        params.put("to_id",request.getRequestFromUserId());
        params.put("request_for",request.getRequestFor());


        Log.i(TAG,"params to respond connection request = "+ params);
        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"server message while connection request response = "+message);
                try
                {
                    if (!RemoteServer.isJsonValid(message))
                    {
                        mListener.onFailed(mContext.getString(R.string.error_occurred));
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(message.trim());
                    if ("1".equals(jsonObject.getString("res")))
                        mListener.onRespondedSuccessfully();
                    else
                    {
                        mListener.onFailed(null);
                    }
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in responseConnectionRequest() method , "+e.getMessage());
                    e.printStackTrace();
                    mListener.onFailed(null);
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"responseConnectionRequest() method volley error = "+error.getMessage());
                error.printStackTrace();
                mListener.onFailed(null);
            }
        });
    }


}
