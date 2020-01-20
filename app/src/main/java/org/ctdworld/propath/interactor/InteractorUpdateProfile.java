package org.ctdworld.propath.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.contract.ContractUpdateProfile;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.UploadSingleImageHelper;
import org.ctdworld.propath.model.Profile;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.HashMap;
import java.util.Map;


public class InteractorUpdateProfile implements ContractUpdateProfile.Interactor
{
    private static final String TAG = InteractorUpdateProfile.class.getSimpleName();
    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;


    public InteractorUpdateProfile(OnFinishedListener listener, Context context)
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
                   updateProfileData(profile,"");

            }
        }).start();
    }


    // this method will update a pert of profile like, name, sport code, address and athlete bio data on server with profile image
    private void updateProfileData(final Profile profile, String profileImageUrl)
    {
        Log.i(TAG,"updateProfileData() method called...........................");
        mRemoteServer.sendData(RemoteServer.URL, getProfileParams(profile, profileImageUrl), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                mListener.onSetViewsEnabledOnProgressBarGone();
                Log.i(TAG,"server message = "+message);
                 mContext.startActivity(new Intent(mContext, ActivityProfileView.class));
               // mListener.onComplete("Updated Successfully...");

              /* // saveRepAchievementData(profile,repMediaUrl);
                    JSONObject jsonObject = null;
                    if (message.trim().length() > 0) {
                        jsonObject = new JSONObject(message.trim());
                    }
                    if (jsonObject != null && "1".equals(jsonObject.getString("success")))
                    {

                    }
                    else
                        mListener.onShowMessage("Failed");*/
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
               // saveRepAchievementData(profile,repMediaUrl);
                Log.e(TAG,"error in updateProfileData() method , "+error.getMessage());
                error.printStackTrace();
            }

        });
    }



    private Map<String,String> getProfileParams(Profile profile, String profileImageUrl)
    {
        Map<String,String> params = new HashMap<>();
        try
        {

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


            Log.i(TAG,"profile params sent to server = "+params);

        }
        catch (Exception e)
        {
            Log.e(TAG,"error in getProfileParams() method , "+e.getMessage());
            e.printStackTrace();
        }

        return params;
    }
}
