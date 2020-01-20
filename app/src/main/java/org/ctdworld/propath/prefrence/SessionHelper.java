package org.ctdworld.propath.prefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.ctdworld.propath.R;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Profile;
import org.ctdworld.propath.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.HttpException;
import retrofit2.http.HTTP;

public class SessionHelper {

    private static final String TAG = SessionHelper.class.getSimpleName();
    private static SessionHelper mInstance = null;
    private SharedPreferences mPreferences;
    private Context mContext;


    private static final String KEY_PREF_USER = "user pref";
    private static final String KEY_USER_ID = "user id";
    public static final String KEY_USER_NAME = "user name";
    private static final String KEY_USER_EMAIL = "user email";
    private static final String KEY_USER_ROLE = "user role";
    private static final String KEY_USER_AUTH_TOKEN = "auth token";
    private static final String KEY_USER_STATUS = "user status";
    private static final String KEY_USER_ACTIVITY = "user activity";
    public static final String KEY_USER_PIC_URL = "user pic url";
    private static final String KEY_IS_USER_LOGGED_ID = "is logged in";

    // For firebase
    private SharedPreferences mPreferenceFirebase;
    private static final String KEY_PREF_FIREBASE = "pref firebase";
    private static final String KEY_FIREBASE_TOKEN = "firebase token";


    public interface LogOutListener {
        void onLogOutSuccess();
        void onLogOutFailed(String message);
    }


    private SessionHelper(Context context) {
        mPreferences = context.getSharedPreferences(KEY_PREF_USER, Context.MODE_PRIVATE);
        mPreferenceFirebase = context.getSharedPreferences(KEY_PREF_FIREBASE, Context.MODE_PRIVATE);
        mContext = context;
    }

    public static SessionHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SessionHelper(context);

        return mInstance;
    }


    public SharedPreferences getPreference() {
        return mPreferences;
    }


    public void saveUser(User user) {
        Log.i(TAG, "saveUser method called...now creating session");

        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_ROLE, user.getRoleId());
        editor.putString(KEY_USER_AUTH_TOKEN, user.getAuthToken());
        editor.putString(KEY_USER_STATUS, user.getStatus());
        editor.putString(KEY_USER_ACTIVITY, user.getActivity());
        editor.putString(KEY_USER_PIC_URL, user.getUserPicUrl());

        editor.apply();

        Log.i(TAG, "saveUser method called...session created successfully");

    }


    public User getUser() {

        User user = new User();

        user.setUserId(mPreferences.getString(KEY_USER_ID, ""));
        user.setEmail(mPreferences.getString(KEY_USER_EMAIL, ""));
        user.setRoleId(mPreferences.getString(KEY_USER_ROLE, ""));
        user.setName(mPreferences.getString(KEY_USER_NAME, ""));
        user.setAuthToken(mPreferences.getString(KEY_USER_AUTH_TOKEN, ""));
        user.setStatus(mPreferences.getString(KEY_USER_STATUS, ""));
        user.setActivity(mPreferences.getString(KEY_USER_ACTIVITY, ""));
        user.setUserPicUrl(mPreferences.getString(KEY_USER_PIC_URL, ""));
        user.setFirebaseToken(mPreferenceFirebase.getString(KEY_FIREBASE_TOKEN, ""));  // setting firebase token

        return user;
    }


    public boolean isUserLoggedIn() {
        return mPreferences.getBoolean(KEY_IS_USER_LOGGED_ID, false); // default value is false
    }

    public boolean setUserLoggedIn() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(KEY_IS_USER_LOGGED_ID, true);
        editor.apply();

        return true;
    }

    public void clearSession(final LogOutListener listener) {
        Log.i(TAG, "clearSession() method called ");


        // server key to get success response
        final String SER_RES_KEY_SUCCESS = "success";
        // server response values for success status
        final String SER_RES_VAL_SUCCESS = "1";
        final String SER_RES_VAL_FAILED = "0";

        RemoteServer remoteServer = new RemoteServer(mContext);

        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG, "server message = " + message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message);
                    if (SER_RES_VAL_SUCCESS.equals(jsonObject.getString(SER_RES_KEY_SUCCESS))) {
                        Log.i(TAG, "Signed out successfully...");
                        mPreferences.edit().clear().apply();
                        listener.onLogOutSuccess();
                    }
                    if (SER_RES_VAL_FAILED.equals(jsonObject.getString(SER_RES_KEY_SUCCESS))) {
                        listener.onLogOutFailed("Failed");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onLogOutFailed(e.getMessage());
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null)
                    return;

                boolean s = error.networkResponse.statusCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT;
                Log.e(TAG, "Error in onVolleyErrorResponse , " + error.getMessage());
                listener.onLogOutFailed(error.getMessage());
                error.printStackTrace();
            }

        });

    }


    // getting params to send to server
    private Map<String, String> getParams() {
        // server keys to send data
        String SER_KEY_LOGOUT_DEFAULT_VALUE = "logout";
        String SER_KEY_USER_ID = "user_id";
        String SER_KEY_AUTH_TOKEN = "authtoken";
        // server default value to send
        String SER_LOGOUT_DEFAULT_VALUE = "1";

        Map<String, String> params = new HashMap<>();

        params.put(SER_KEY_LOGOUT_DEFAULT_VALUE, SER_LOGOUT_DEFAULT_VALUE);
        params.put(SER_KEY_USER_ID, mPreferences.getString(KEY_USER_ID, ""));
        params.put(SER_KEY_AUTH_TOKEN, mPreferences.getString(KEY_USER_AUTH_TOKEN, ""));

        Log.i(TAG, "param = " + params);

        return params;
    }


    // # user id
    public static String getUserId(Context context) {
        User user = getInstance(context).getUser();
        if (user != null)
            return user.getUserId();
        else
            return ConstHelper.NOT_FOUND_STRING;
    }


    // setting firebase credentials
    public void setFirebaseRegistrationToken(String firebasetRegistrationToken) {
        // Log.i(TAG,"setFirebaseRegistrationToken() method , token = "+firebasetRegistrationToken);
        SharedPreferences.Editor editor = mPreferenceFirebase.edit();
        editor.putString(KEY_FIREBASE_TOKEN, firebasetRegistrationToken);  // saving
        editor.apply();
    }


    public String getFirebaseRegistrationToken() {
        // Log.i(TAG,"getFirebaseRegistrationToken() method , token = "+mPreferences.getString(KEY_FIREBASE_TOKEN,""));
        return mPreferenceFirebase.getString(KEY_FIREBASE_TOKEN, "");
    }


}
