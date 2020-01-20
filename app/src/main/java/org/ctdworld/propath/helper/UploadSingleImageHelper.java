package org.ctdworld.propath.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.database.RemoteServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class UploadSingleImageHelper
{
    private static final String TAG = UploadSingleImageHelper.class.getSimpleName();
    private RemoteServer mRemoteServer;

    private static final String RES_KEY_IMAGE_URL = "data";  // key to get image url
    private static final String RES_KEY_SUCCESS = "success";
    private static final String RES_VALUE_SUCCESS= "1";

    private Map<String,String> mParams;



    public UploadSingleImageHelper(Context context, Map<String,String> params)
    {
        this.mRemoteServer = new RemoteServer(context);
        this.mParams = params;
    }


    public interface UploadSingleImageListener{void onSuccess(String imageUrl); void onFailed();}


    public void uploadSingleImage(final UploadSingleImageListener listener)
    {
        Log.i(TAG,"uploadSingleImage() method called ");

        mRemoteServer.sendData(RemoteServer.URL, mParams, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"server response in uploadSingleImage() method = "+message);
                try
                {
                    JSONObject jsonObject = new JSONObject(message.trim());
                    if (RES_VALUE_SUCCESS.equals(jsonObject.getString(RES_KEY_SUCCESS)))
                    {
                        String imageUrl = jsonObject.getString("data");
                        listener.onSuccess(imageUrl);
                    }
                    else
                        listener.onFailed();

                }
                catch (JSONException e)
                {
                    listener.onFailed();
                    Log.i(TAG,"error in uploadSingleImage() method ");
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"volley error in uploadSingleImage() method  , "+error.getMessage());
                listener.onFailed();
                error.printStackTrace();
            }
        });


    }
}
