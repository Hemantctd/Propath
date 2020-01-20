package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;
import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractSignIn;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InteractorSignIn implements ContractSignIn.Interactor
{
    private static final String TAG = InteractorSignIn.class.getSimpleName();
    ContractSignIn.Interactor.OnFinishedListener listener;
    Context context;
    RemoteServer remoteServer;


    private static final String SERVER_KEY_LOGIN_DEFAULT_VALUE = "login";
    private static final String SERVER_LOGIN_DEFAULT_VALUE = "1";
    private static final String SERVER_KEY_EMAIL = "user_email";
    private static final String SERVER_KEY_PASSWORD = "password";



    private static final String SERVER_RESPONSE_KEY_SUCCESS = "success";
    private static final String SERVER_RESPONSE_SUCCESS_VALUE= "1";
    private static final String SERVER_RESPONSE_FAILED_VALUE= "0";

    private static final String SERVER_RESPONSE_FAIL_KEY_MESSAGE= "message";
    private static final String SERVER_RESPONSE_FAIL_VALUE_WRONG_EMAIL_PASSWORD= "Invalid username or password.";


    private static final String SERVER_RESPONSE_KEY_DATA_JSON_OBJECT = "data";
    private static final String SERVER_RESPONSE_KEY_USER_ID = "UserID";
    private static final String SERVER_RESPONSE_KEY_USER_NAME = "UserName";
    private static final String SERVER_RESPONSE_KEY_USER_EMAIL_ID = "EmailID";
    private static final String SERVER_RESPONSE_KEY_AUTH_TOKEN = "AuthToken";
    private static final String SERVER_RESPONSE_KEY_USER_ROLE_ID = "UserRole";
    private static final String SERVER_RESPONSE_KEY_USER_STATUS = "Userstatus";
    private static final String SERVER_RESPONSE_KEY_USER_ACTIVITY = "UserActivity";



    public InteractorSignIn(ContractSignIn.Interactor.OnFinishedListener listener, Context context)
    {
        this.listener = listener;
        this.context = context;
        this.remoteServer = new RemoteServer(context);
    }


    // to check credentials and sign in
    @Override
    public void signIn(final User user)
    {
        Log.i(TAG,"Signing in...........................");

        remoteServer.sendData(RemoteServer.URL, mGetMap(user), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"server message = "+message);
                JSONObject jsonObject;
                try
                {
                    jsonObject = new JSONObject(message);
                   if (SERVER_RESPONSE_SUCCESS_VALUE.equals(jsonObject.getString(SERVER_RESPONSE_KEY_SUCCESS)))
                   {
                       Log.i(TAG,"Signed in successfully...now creating user session");
                       createUserSession(jsonObject.getJSONObject(SERVER_RESPONSE_KEY_DATA_JSON_OBJECT));
                   }
                   else  if (SERVER_RESPONSE_FAILED_VALUE.equals(jsonObject.getString(SERVER_RESPONSE_KEY_SUCCESS)))
                   {
                       listener.onShowMessage(jsonObject.getString(SERVER_RESPONSE_FAIL_KEY_MESSAGE));
                   }

                   /*   // listener.onSuccess(getUserFromServer(jsonObject.getJSONObject(SERVER_RESPONSE_KEY_DATA_JSON_OBJECT)));
                   else if (SERVER_RESPONSE_FAILED_VALUE.equals(jsonObject.getString(SERVER_RESPONSE_KEY_SUCCESS)))
                   {
                       if (SERVER_RESPONSE_FAIL_VALUE_WRONG_EMAIL_PASSWORD.equals(jsonObject.getString(SERVER_RESPONSE_FAIL_KEY_MESSAGE)))
                           listener.onShowMessage("Wrong Email Or Password");
                   }
                   else
                       listener.onShowMessage("Login Failed...");
*/
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    listener.onShowMessage("Login Failed...");
                }
                finally
                {
                    listener.onSetViewsEnabledOnProgressBarGone();
                    listener.onHideProgress();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                listener.onSetViewsEnabledOnProgressBarGone();
                listener.onHideProgress();
                listener.onConnectionError();
                Log.e(TAG,"Error in onVolleyErrorResponse , "+error.getMessage());
                error.printStackTrace();
            }

        });
    }

    // returns map object to send to server by volley
    private Map<String,String> mGetMap(User user)
    {


        Map<String,String> map = new HashMap<>();
        map.put(SERVER_KEY_LOGIN_DEFAULT_VALUE,SERVER_LOGIN_DEFAULT_VALUE);
        map.put(SERVER_KEY_EMAIL,user.getEmail());
        map.put(SERVER_KEY_PASSWORD,user.getPassword());
        map.put("mobile_token",SessionHelper.getInstance(context).getUser().getFirebaseToken());


        Log.i(TAG,"params used to login = "+map);


        return map;
    }

    private void createUserSession(JSONObject jsonObject)
    {
        Log.i(TAG,"createUserSession() method called...now sending data to create session");

        SessionHelper sessionHelper = SessionHelper.getInstance(context);
        User user = new User();
        try
        {
            user.setUserId(jsonObject.getString(SERVER_RESPONSE_KEY_USER_ID));
            user.setName(jsonObject.getString(SERVER_RESPONSE_KEY_USER_NAME));
            user.setEmail(jsonObject.getString(SERVER_RESPONSE_KEY_USER_EMAIL_ID));
            user.setRoleId(jsonObject.getString(SERVER_RESPONSE_KEY_USER_ROLE_ID));
            user.setAuthToken(jsonObject.getString(SERVER_RESPONSE_KEY_AUTH_TOKEN));
            user.setStatus(jsonObject.getString(SERVER_RESPONSE_KEY_USER_STATUS));
            user.setActivity(jsonObject.getString(SERVER_RESPONSE_KEY_USER_ACTIVITY));

            user.setUserPicUrl(jsonObject.getString("UserProfile"));



            sessionHelper.saveUser(user);  // saving user session
            sessionHelper.setUserLoggedIn(); // now saving user as logged in

            listener.onSuccess();

        }
        catch (JSONException e)
        {
            Log.i(TAG,"error in createUserSession() method");
            e.printStackTrace();
        }

    }

}
