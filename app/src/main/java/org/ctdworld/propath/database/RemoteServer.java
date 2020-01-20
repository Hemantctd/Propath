package org.ctdworld.propath.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RemoteServer {
     private String TAG = "RemoteServer";
     private Context context;
     private MyVolleySingleton myVolleySingleton;
     private VolleyJsonListener mJsonListener;

     public static final String BASE64_IMAGE_TYPE_JPEG = "data:image/jpeg;base64,";

     public static final int MAX_REQUEST_TRY = 2;

    public static String scheme = "https";
    public static final String URL = scheme+"://ctdworld.co/athlete/jsondata.php";
    public static final String BASE_IMAGE_URL = scheme+"://ctdworld.co/athlete/uploads/profile/";



    public RemoteServer(Context context)
    {
        try {
            this.context = context;
            this.myVolleySingleton = MyVolleySingleton.getInstance(context);
        }
        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
    }



    public static Uri.Builder getUriBuilder()
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme);
        builder.authority("ctdworld.co"); // adding base URL
        builder.path("/athlete/jsondata.php");

        return builder;
    }



    public void sendData(final String url, final Map sendData,  final VolleyStringListener volleyStringListener)
    {
     Log.i(TAG,"Sending and receiving data....................................");
        Log.i(TAG,"Used Url = "+url);

        try
        {

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            volleyStringListener.onVolleySuccessResponse(response);
                        }
                    },

                        new Response.ErrorListener()
                        {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            volleyStringListener.onVolleyErrorResponse(error);
                        }
                    })

                    {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            return sendData;
                        }
                    };

            request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            myVolleySingleton.addToRequestQueue(request);

        }

        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }


    }


    public void getJson(String url, final VolleyJsonListener listener)
    {
        Log.i(TAG,"Getting json from url = "+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        listener.onJsonResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        listener.onJsonErrorResponse(error);
                    }
                });

       myVolleySingleton.addToRequestQueue(jsonObjectRequest);

    }


    public void getBitmap(String url, final BitamapListener listener, int maxWidth, int maxHeight)
    {
        try
        {
  //          Log.i(TAG,"getBitmap method called");

            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>()
            {
                @Override
                public void onResponse(Bitmap response)
                {
                    listener.onBitmapResponse(response);
                }

            }, maxWidth, maxHeight, null, null,
                    new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    listener.onBitmapErrorResponse(error);
                }
            });


            myVolleySingleton.addToRequestQueue(imageRequest);

        }
        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }


    }




    public interface BitamapListener
    {
        void onBitmapResponse(Bitmap bitmap);
        void onBitmapErrorResponse(VolleyError error);
    }

    public interface VolleyStringListener
    {
        void onVolleySuccessResponse(String response);
        void onVolleyErrorResponse(VolleyError error);
    }

    public interface VolleyJsonListener
    {
        void onJsonResponse(JSONObject jsonObject);
        void onJsonErrorResponse(VolleyError error);
    }



    // checking if string is valid json or not
    public static boolean isJsonValid(String jsonString)
    {
        try {
            if (jsonString == null || jsonString.isEmpty())
                return false;

            new JSONObject(jsonString);
        }
        catch (JSONException e)
        {
            return false;
        }

        return true;
    }



    // returns server error message depending on server response code
    public static String getServerErrorMessage(VolleyError volleyError)
    {
        String message = "Connection Error...";

        if (volleyError == null || volleyError.networkResponse == null)
            return message;

        switch (volleyError.networkResponse.statusCode)
        {
            case 204:
                message = "No Content";
                break;

            case 400:
                message = "Bad request";
                break;

            case 401:
                message = "Unauthorized";
                break;

            case 415:
                message = "Unsupported media type";
                break;

            case 500:
                message = "Internal Server Error";
                break;

            case 505:
                message = "HTTP version not supported";
                break;

            case 511:
                message = "Network authentication required";
                break;

        }

        return message;
    }


    // return error message if response from server is not valid json
    public static String getJsonNotValidErrorMessage()
    {
       return "Invalid data from server";
    }

}
