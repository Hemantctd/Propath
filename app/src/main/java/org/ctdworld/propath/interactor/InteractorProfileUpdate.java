package org.ctdworld.propath.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractProfileUpdate;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.UploadSingleImageHelper;
import org.ctdworld.propath.model.Profile;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InteractorProfileUpdate implements ContractProfileUpdate.Interactor
{
    private static final String TAG = InteractorProfileUpdate.class.getSimpleName();
    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;


    public InteractorProfileUpdate(OnFinishedListener listener, Context context)
    {
        this.mListener = listener;
        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }

    @Override
    public void updateProfile(final Profile profile)
    {
        Log.i(TAG,"profile() method called...........................");

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (profile.getProfilePicBase64() != null)
                {
                    Map<String,String> params = new HashMap<>();
                       params.put("image_upload","1");
                        params.put("photo", profile.getProfilePicBase64());
                    //if (!Patterns.WEB_URL.matcher(profile.getProfilePicBase64()))

                    UploadSingleImageHelper uploadSingleImageHelper = new UploadSingleImageHelper(mContext,params);
                    uploadSingleImageHelper.uploadSingleImage(new UploadSingleImageHelper.UploadSingleImageListener()
                    {
                        @Override
                        public void onSuccess(String profilePicUrl)
                        {
                            Log.i(TAG,"Profile image uploaded successfully, imageUrl = "+profilePicUrl);
                            //saveRepMedia(profile, profilePicUrl);
                            updateProfileData(profile,profilePicUrl);
                        }

                        @Override
                        public void onFailed()
                        {
                            updateProfileData(profile,"");
                           // saveRepMedia(profile,"");
                        }
                    });
                }
                else
                   updateProfileData(profile,profile.getPicUrl());

            }
        }).start();
    }


    // this method will update a pert of profile like, name, sport code, address and athlete bio data on server with profile image
    private void updateProfileData(final Profile profile, String profileImageUrl)
    {
        Log.i(TAG,"updateProfileData() method called...........................");

        Map<String,String> params = new HashMap<>();

        params.put("updateprofile","1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("profile_image",profileImageUrl);
        params.put("user_name",profile.getName());
        params.put("athelete_spot_code",profile.getSportName());
        params.put("bio_details",profile.getAthleteBio());
        params.put("address",profile.getAddress());
        params.put("AuthToken",SessionHelper.getInstance(mContext).getUser().getAuthToken());
        params.put("athlete_playlist",profile.getPlaylist());
        params.put("athlete_highlight",profile.getHighlightReel());

        Log.i(TAG,"params to update profile = "+params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                mListener.onSetViewsEnabledOnProgressBarGone();
                Log.i(TAG,"server message = "+message);
               //  mContext.startActivity(new Intent(mContext,ActivityProfileView.class));
                try
                {
                    JSONObject jsonObject = new JSONObject(message);
                    Log.d(TAG,"JsonObject: "+jsonObject);

                    if ("1".equals(jsonObject.getString("success")))
                    {
                        SharedPreferences preferences = SessionHelper.getInstance(mContext).getPreference();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SessionHelper.KEY_USER_NAME, jsonObject.getJSONObject("data").getString("UserName"));
                        editor.putString(SessionHelper.KEY_USER_PIC_URL,RemoteServer.BASE_IMAGE_URL+jsonObject.getJSONObject("data").getString(
                                "profile_image"));
                        editor.apply();

                        mListener.onComplete("Updated Successfully...");
                    }
                    else
                        mListener.onShowMessage("Updating profile failed...");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
               // saveRepAchievementData(profile,repMediaUrl);
                Log.e(TAG,"error in updateProfileData() method , "+error.getMessage());
                error.printStackTrace();
                mListener.onShowMessage("Updating profile failed...");

            }

        });
    }


}
