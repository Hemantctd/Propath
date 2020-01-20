package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractSignUp;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InteractorRegister implements ContractSignUp.Interactor
{
    private static final String TAG = InteractorRegister.class.getSimpleName();
    private ContractSignUp.Interactor.OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context context;


    private static final String SERVER_KEY_REGISTRATION_FOR_DEFAULT_VALUE = "registration";  //key to send default value to server
    private static final String SERVER_REGISTRATION_DEFAULT_VALUE = "1";  // default value to send to server
    private static final String SERVER_KEY_NAME = "name";
    private static final String SERVER_KEY_EMAIL = "email";
    private static final String SERVER_KEY_PASSWORD = "password";
    private static final String SERVER_KEY_CONFIRM_PASSWORD = "repassword";
    private static final String SERVER_KEY_ROLE = "role";
    private static final String SERVER_KEY_FIREBASE_TOKEN = "mobile_token";

  //  private static final String SERVER_KEY_OTP = "otp";


    // on failed response
    private static final String RES_FAIL_KEY_RES = "res";
    private static final String RES_FAIL_VALUE = "0";
    private static final String RES_FAIL_KEY_RESPONSE = "response";
    private static final String RES_FAIL_VALUE_RESPONSE = "Email All ready registred";



    // server key to get data from json sent from server
    private static final String RES_KEY_SUCCESS = "res";
    private static final String RES_VAL_SUCCESS = "success";
    private static final String RES_VAL_FAILED = "0";



    //  private Random random = new Random();
 //   public static   int confirmation_code;



    public InteractorRegister(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
     //   confirmation_code = random.nextInt(999999);
    }

    @Override
    public void createAccount(final User user)
    {
        Log.i(TAG,"createAccount() method called ");

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
                            listener.onSignUpSuccess();
                        else
                        {
                            if (RES_FAIL_VALUE_RESPONSE.equals(jsonObject.getString(RES_FAIL_KEY_RESPONSE)))
                                listener.onEmailAlreadyRegistered();
                            else
                            listener.onSignUpFailed();
                        }


                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in createAccount() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onSignUpFailed();
                }
                finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onConnectionError();
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }



    private Map<String,String> getParams(User user)
    {

       /* Log.i(TAG,"name = "+user.getName());
        Log.i(TAG,"email = "+user.getEmail());
        Log.i(TAG,"password = "+user.getPassword());*/

        Map<String,String> map = new HashMap<>();

        map.put(SERVER_KEY_REGISTRATION_FOR_DEFAULT_VALUE,SERVER_REGISTRATION_DEFAULT_VALUE);
        map.put(SERVER_KEY_NAME, user.getName());
        map.put(SERVER_KEY_EMAIL, user.getEmail());
        map.put(SERVER_KEY_PASSWORD, user.getPassword());
        map.put(SERVER_KEY_CONFIRM_PASSWORD,user.getConfirm_password());
        map.put(SERVER_KEY_ROLE,user.getRoleId());
        map.put(SERVER_KEY_FIREBASE_TOKEN, SessionHelper.getInstance(context).getUser().getFirebaseToken());
     //   map.put(SERVER_KEY_OTP,user.getOtp());

        Log.i(TAG,"params = "+ map);

        return map;
    }


 /*   private User createUserSession(JSONObject jsonObject)
    {
        User user = new User();
        try
        {
            user.setUserId(jsonObject.getString(RES_KEY_USER_ID));
            user.setName(jsonObject.getString(RES_KEY_NAME));
            user.setEmail(jsonObject.getString(RES_KEY_EMAIL));
            user.setRole(jsonObject.getString(RES_KEY_ROLE));
            user.setAuthToken(jsonObject.getString(RES_KEY_AUTH_TOKEN));
        }
        catch (JSONException e)
        {
            Log.e(TAG,"Error in getUserDataFromServerJsonObject() method , "+e.getMessage());
            e.printStackTrace();
        }
        return user;
    }*/

   /* private void sendOtpToEmail(String to, String otp)
    {
        Log.i(TAG,"Sending mail.........................");
        SendMailHelper sendMailHelper = new SendMailHelper(context,to, "Athlete OTP", "Your OTP IS " + otp, new SendMailHelper.Listener() {
            @Override
            public void onSuccess() {
                listener.onSignUpSuccess();
            }

            @Override
            public void onFailed() {
                 Log.i(TAG,"Mail not sent ");
                 listener.onSignUpFailed();
            }
        });

        sendMailHelper.execute();
    }*/


}
