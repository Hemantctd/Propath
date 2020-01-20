package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractProfileView;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.model.Profile;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InteractorProfileView implements ContractProfileView.Interactor
{
    private static final String TAG = InteractorProfileView.class.getSimpleName();
    private OnFinishedListener mListener;
    private RemoteServer mRemoteServer;


    public InteractorProfileView(OnFinishedListener listener, Context context)
    {
        this.mListener = listener;
        this.mRemoteServer = new RemoteServer(context);
    }

    // this method will upload profile image and get profile pic URL then data will be saved on server with profile pic URL
    @Override
    public void requestProfile(final String userId)
    {
        Log.i(TAG,"requestProfile() method called...........................");

        new Thread(new Runnable() {
            @Override
            public void run()
            {

                Map<String,String> params = new HashMap<>();
                params.put("Profile","1");
                params.put("userid", userId);

                Log.i(TAG,"params to get profile = "+params);

                mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
                    @Override
                    public void onVolleySuccessResponse(String message)
                    {
                        Log.i(TAG,"profile view server message = "+message);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("success")))
                            {
                                String strData = jsonObject.getString("data");
                                if (strData != null)
                                {
                                    if (!strData.isEmpty())
                                    {
                                        if (!strData.equals("null"))
                                        {
                                            //  Log.i(TAG,"strData = "+strData);
                                            mListener.onProfileReceived(getUserProfileFromJson(jsonObject.getJSONObject("data")));
                                        }

                                    }

                                }

                            }
                            else
                                mListener.onFailed(null);
                        }
                        catch (JSONException e) {
                            mListener.onFailed(null);
                            Log.e(TAG,"Error in requestProfile() method , "+e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVolleyErrorResponse(VolleyError error)
                    {
                        Log.e(TAG,"Error in volley in requestProfile() method , "+error.getMessage());
                        error.printStackTrace();
                       mListener.onFailed(null);
                    }
                });

            }
        }).start();
    }


    @Override
    public void checkFriendStatus(final String loginUserId, final String otherUserId) {
        Log.i(TAG,"checkFriendStatus() method called...........................");

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Map<String,String> params = new HashMap<>();
                params.put("check_friend_status","1");
                params.put("my_user_id", loginUserId);
                params.put("other_user_id", otherUserId);

                Log.i(TAG,"params to check friend status = "+params);

                mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
                    @Override
                    public void onVolleySuccessResponse(String message)
                    {
                        Log.i(TAG,"server message for friend status = "+message);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(message.trim());
                            String result = jsonObject.getString("result");
                            if ("1".equals(jsonObject.getString("res")))
                            {
                                if ("Request Pending".equals(result))
                                {
                                    Log.i(TAG,"status = "+result);
                                    mListener.onReceivedFriendStatus(Request.REQUEST_STATUS_PENDING);
                                }
                            }
                            if ("2".equals(jsonObject.getString("res")))
                            {
                                if ("Friend".equals(result))
                                {
                                    Log.i(TAG,"status = "+result);
                                    mListener.onReceivedFriendStatus(Request.REQUEST_STATUS_ACCEPT);
                                }
                            }
                            else if ("0".equals(jsonObject.getString("res")))
                            {
                                if ("Not a friend".equals(result))
                                {
                                    Log.i(TAG,"status = "+result);
                                    mListener.onReceivedFriendStatus(Request.REQUEST_STATUS_REJECT);
                                }
                            }

                        }
                        catch (JSONException e) {
                            Log.e(TAG,"Error in checkFriendStatus() method , "+e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVolleyErrorResponse(VolleyError error)
                    {
                        Log.e(TAG,"Error in volley in checkFriendStatus() method , "+error.getMessage());
                        error.printStackTrace();
                       /* mListener.onHideProgress();
                        mListener.onSetViewsEnabledOnProgressBarGone();*/
                    }
                });

            }
        }).start();
    }


    private Profile getUserProfileFromJson(JSONObject jsonData)
    {
        Profile profile = new Profile();

        try
        {
            profile.setId(jsonData.getString("UserID"));
            profile.setName(jsonData.getString("UserName"));
            profile.setSportName(jsonData.getString("athelete_spot_code"));
            profile.setAddress(jsonData.getString("address"));
            profile.setAthleteBio(jsonData.getString("bio_details"));
            profile.setHighlightReel(jsonData.getString("athlete_highlight"));
            profile.setPlaylist(jsonData.getString("athlete_playlist"));
            profile.setRoleName(jsonData.getString("UserRole"));

            String  picUrl = RemoteServer.BASE_IMAGE_URL+jsonData.getString("profile_image");
            profile.setPicUrl(picUrl);
        }
        catch (Exception e)
        {
            Log.e(TAG,"error in getUserProfileFromJson() method , "+e.getMessage());
            e.printStackTrace();
        }

        return profile;
    }



}