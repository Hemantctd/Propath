package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractResetPassword;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InteractorResetPassword implements ContractResetPassword.Interactor
{
    private static final String TAG = InteractorResetPassword.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context context;


    private static final String KEY_CHANGE_PASSWORD_DEFAULT_VALUE = "change_password";  //key to send default value to server
    private static final String CHANGE_PASSWORD_DEFAULT_VALUE = "1";  // default value to send to server
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_OLD_PASS = "oldpassword";
    private static final String KEY_NEW_PASS = "newpassword";
    private static final String KEY_CONFIRM_PASS = "confirmpass";


    // server key to get data from json sent from server
    private static final String RES_KEY_SUCCESS = "success";
    private static final String RES_VAL_SUCCESS = "1";
    private static final String RES_KEY_MESSAGE = "message";



    //  private Random random = new Random();
 //   public static   int confirmation_code;



    public InteractorResetPassword(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
     //   confirmation_code = random.nextInt(999999);
    }

    @Override
    public void resetPassword(final User user)
    {
        Log.i(TAG,"resetPassword() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(user), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                        if (RES_VAL_SUCCESS.equals(jsonObject.getString(RES_KEY_SUCCESS)))
                            listener.onSuccess();
                        else
                            listener.onShowMessage(jsonObject.getString(RES_KEY_MESSAGE));

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in resetPassword() method , "+e.getMessage());
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
                Log.d(TAG,"resetPassword() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }



    private Map<String,String> getParams(User user)
    {

        Map<String,String> params = new HashMap<>();

        params.put(KEY_CHANGE_PASSWORD_DEFAULT_VALUE,CHANGE_PASSWORD_DEFAULT_VALUE);
        params.put(KEY_USER_ID, SessionHelper.getInstance(context).getUser().getUserId());
        params.put(KEY_OLD_PASS,user.getOldPassword());
        params.put(KEY_NEW_PASS, user.getPassword());
        params.put(KEY_CONFIRM_PASS,user.getConfirm_password());

        Log.i(TAG,"params = "+ params);

        return params;
    }

}
