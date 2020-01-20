package org.ctdworld.propath.database;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MyVolleySingleton extends Volley {

    private String TAG = "MyVolleySingleton";
    private static MyVolleySingleton instance;
    private Context context;
    private RequestQueue requestQueue;

    private MyVolleySingleton(Context context)
    {
        try
        {
 //           Log.i(TAG,"MyVolleySingleton() constructor called");
            this.context = context;
            getRequestQueue();
        }
        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
    }

    public static MyVolleySingleton getInstance(Context context)
    {
        try
        {
//            Log.i("MyVolleySingleton","getInstance() method called");

            if (instance == null)
                instance = new MyVolleySingleton(context);

        }
        catch (Exception e)
        {
            Log.e("MyVolleySingleton",e.getMessage());
            e.printStackTrace();
        }

        return instance;
    }

    private RequestQueue getRequestQueue()
    {
        try
        {
  //          Log.i(TAG,"getRequestQueue() method called");
            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(context);
        }
        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request request)
    {
        try
        {
         //   Log.i(TAG,"addToRequestQueue() method called");
            requestQueue.add(request);
        }
        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
    }

}
