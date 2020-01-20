package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractRepAchievement;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UploadSingleImageHelper;
import org.ctdworld.propath.model.RepAchievement;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorRepAchievement implements ContractRepAchievement.Interactor
{
    private static final String TAG = InteractorRepAchievement.class.getSimpleName();
    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;


    public InteractorRepAchievement(OnFinishedListener listener, Context context)
    {
        this.mListener = listener;
        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }

    // getting user representative achievement
    @Override
    public void requestRepAchievement(final String userId)
    {
        Log.i(TAG,"updateProfile() method called...........................");

        new Thread(new Runnable() {
            @Override
            public void run()
            {

                Map<String,String> params = new HashMap<>();
                params.put("get_athlete_representative","1");
                params.put("user_id", userId);

                mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
                    @Override
                    public void onVolleySuccessResponse(String message)
                    {
                        Log.i(TAG,"profile view server message = "+message);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(message.trim());
                            if ("1".equals(jsonObject.getString("success")))
                                mListener.onReceivedRepList(getListRepAchievement(jsonObject.getJSONArray("message")));
                            else if ("Not Found".equals(jsonObject.getString("message")))
                            {
                                mListener.onShowMessage("No Data Found");

                            }
                            else
                                mListener.onShowMessage("Failed...");

                            mListener.onHideProgress();
                        }
                        catch (JSONException e) {
                            Log.e(TAG,"Error in requestProfile() method , "+e.getMessage());
                            e.printStackTrace();
                            mListener.onHideProgress();
                        }
                    }

                    @Override
                    public void onVolleyErrorResponse(VolleyError error)
                    {
                        Log.e(TAG,"Error in volley in requestProfile() method , "+error.getMessage());
                        error.printStackTrace();
                        mListener.onHideProgress();
                        mListener.onSetViewsEnabledOnProgressBarGone();
                    }
                });

            }
        }).start();
    }

    //Add Rep Achievement

    @Override
    public void addRepAchievement(final RepAchievement repAchievement)
    {
        Log.i(TAG,"updateProfile() method called...........................");

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (repAchievement.getRepMediaBase64() != null)
                {
                    Map<String,String> params = new HashMap<>();
                    params.put("image_upload","1");
                    params.put("photo", repAchievement.getRepMediaBase64());

                    UploadSingleImageHelper uploadSingleImageHelper = new UploadSingleImageHelper(mContext,params);
                    uploadSingleImageHelper.uploadSingleImage(new UploadSingleImageHelper.UploadSingleImageListener()
                    {
                        @Override
                        public void onSuccess(String repImageUrl)
                        {
                            Log.i(TAG,"rep_achievement image uploaded successfully, imageUrl = "+repImageUrl);
                            saveRepAchievementData(repAchievement, repImageUrl);
                        }

                        @Override
                        public void onFailed()
                        {
                            saveRepAchievementData(repAchievement,"");
                        }
                    });
                }
                else
                    saveRepAchievementData(repAchievement,"");

            }
        }).start();

    }

    // edit rep achievement
    @Override
    public void editRepAchievement(final RepAchievement repAchievement)
    {

        Log.i(TAG,"editRepAchievement() method called...........................");

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (repAchievement.getRepMediaBase64() != null)
                {
                    Map<String,String> params = new HashMap<>();
                    params.put("image_upload","1");
                    params.put("photo", repAchievement.getRepMediaBase64());

                    UploadSingleImageHelper uploadSingleImageHelper = new UploadSingleImageHelper(mContext,params);
                    uploadSingleImageHelper.uploadSingleImage(new UploadSingleImageHelper.UploadSingleImageListener()
                    {
                        @Override
                        public void onSuccess(String repImageUrl)
                        {
                            Log.i(TAG,"rep_achievement image uploaded successfully, imageUrl = "+repImageUrl);
                            editRepAchievementData(repAchievement, repImageUrl);
                        }

                        @Override
                        public void onFailed()
                        {
                            editRepAchievementData(repAchievement,"");
                        }
                    });
                }
                else
                    editRepAchievementData(repAchievement,"");

            }
        }).start();

    }


    // delete achievements
    @Override
    public void deleteRepAchievement(final int position, String rep_achieve_id) {

        Log.i(TAG,"deleteRepAchievement() method called");
        Map<String,String> params = new HashMap<>();
        params.put("delete_athlete_representative","1");
        params.put("id", rep_achieve_id);


        Log.i(TAG,"params : " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"profile view server message = "+message);
                try
                {
                    JSONObject jsonObject = new JSONObject(message.trim());
                    if ("1".equals(jsonObject.getString("success"))) {
                      //  DialogHelper.showSimpleCustomDialog(mContext, jsonObject.getString("message"));
                        mListener.onDeleted(position);
                    }


                    else
                        DialogHelper.showSimpleCustomDialog(mContext,jsonObject.getString("message"));

                }
                catch (JSONException e) {

                    e.printStackTrace();
                    mListener.onHideProgress();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.e(TAG,"Error in volley in deleteAchievement() method , "+error.getMessage());
                error.printStackTrace();
                mListener.onHideProgress();
                mListener.onSetViewsEnabledOnProgressBarGone();
            }
        });

    }


    // returns list of rep achievements
    private List<RepAchievement> getListRepAchievement(JSONArray jsonArrayMessage)
    {
        List<RepAchievement> listRepAchievement = new ArrayList<>();

        try
        {
            if (jsonArrayMessage != null)
            {
                for (int i=0; i<jsonArrayMessage.length() ; i++)
                {
                    JSONObject jsonObject = jsonArrayMessage.getJSONObject(i);
                    RepAchievement repAchievement = new RepAchievement();

                 //   Log.i(TAG,"team = "+jsonObject.getString("team"));

                    repAchievement.setRepTeam(jsonObject.getString("team"));
                    repAchievement.setRepLocation(jsonObject.getString("location"));
                    repAchievement.setRepFromMonth(jsonObject.getString("from_month"));
                    repAchievement.setRepFromYear(jsonObject.getString("from_year"));
                    repAchievement.setRepToMonth(jsonObject.getString("to_month"));
                    repAchievement.setRepToYear(jsonObject.getString("to_year"));
                    repAchievement.setRepRole(jsonObject.getString("role"));
                    repAchievement.setRepLink(jsonObject.getString("link")); // setting empty for now, not available in api
                    repAchievement.setRepID(jsonObject.getString("Id"));

                    String imageName = jsonObject.getString("media"); // getting image name from json
                    String imageUrl = "";
                    if (!imageName.isEmpty())
                        imageUrl = RemoteServer.BASE_IMAGE_URL+imageName;  // initializing image URL

                    repAchievement.setRepMediaUrl(imageUrl);  // setting image URL to repAchievement
                  //  Log.i(TAG,"Media Url = "+medialUrl);


                    listRepAchievement.add(repAchievement);
                }

                Log.i(TAG,"total rep achievemnet = "+listRepAchievement.size());
            }
            else
                Log.e(TAG,"jsonArrayMessage is null in getListRepAchievement");

        }
        catch (Exception e)
        {
            Log.e(TAG,"error in getListRepAchievement() method , "+e.getMessage());
            e.printStackTrace();
        }

        return listRepAchievement;
    }


    // this method will save representative achievement data on server with rep media url
    private void saveRepAchievementData(RepAchievement repAchievement, String repMediaUrl)
    {
        Log.i(TAG,"updateProfileData() method called...........................");
        mRemoteServer.sendData(RemoteServer.URL, getRepParams(repAchievement, repMediaUrl), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                mListener.onSetViewsEnabledOnProgressBarGone();
                Log.i(TAG,"server message = "+message);

                try
                {
                    JSONObject jsonObject = null;
                    if (message.trim().length() > 0) {
                        jsonObject = new JSONObject(message.trim());
                    }
                    if (jsonObject != null && "1".equals(jsonObject.getString("success")))
                    {
                      //  DialogHelper.showSimpleCustomDialog(mContext,"Add Achievement Successfully");
                        mListener.onAddRepSuccessfully();
                    }
                    else
                        mListener.onShowMessage("Completed...");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"error in updateProfileData() method , "+e.getMessage());
                    e.printStackTrace();
                }
                finally
                {
                    mListener.onHideProgress();
                    mListener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onSetViewsEnabledOnProgressBarGone();
                mListener.onHideProgress();
                mListener.onShowMessage("Connection Error");
                // Log.e(TAG,"Error in onVolleyErrorResponse , "+error.getMessage());
                error.printStackTrace();
            }

        });
    }


    private Map<String,String> getRepParams(RepAchievement repAchievement, String repMediaUrl)
    {
        Map<String,String> params = new HashMap<>();
        try
        {
            params.put("athlete_representative","1");
            params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
            params.put("team",repAchievement.getRepTeam());
            params.put("media",repMediaUrl);
            params.put("location",repAchievement.getRepLocation());
            params.put("from_month",repAchievement.getRepFromMonth());
            params.put("from_year",repAchievement.getRepFromYear());
            params.put("to_month",repAchievement.getRepToMonth());
            params.put("to_year",repAchievement.getRepToYear());
            params.put("role",repAchievement.getRepRole());
            params.put("link",repAchievement.getRepLink());


            Log.i(TAG,"profile rep achievement params sent to server = "+params);

        }
        catch (Exception e)
        {
            Log.e(TAG,"error in getProfileRepParams() method , "+e.getMessage());
            e.printStackTrace();
        }

        return params;
    }



    // this method will edit representative achievement data on server with rep media url
    private void editRepAchievementData(RepAchievement repAchievement, String repMediaUrl)
    {
        Log.i(TAG," editRepAchievementData() method called...........................");
        mRemoteServer.sendData(RemoteServer.URL, getEditRepParams(repAchievement, repMediaUrl), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                mListener.onSetViewsEnabledOnProgressBarGone();
                Log.i(TAG,"server message = "+message);

                try
                {
                    JSONObject jsonObject = null;
                    if (message.trim().length() > 0) {
                        jsonObject = new JSONObject(message.trim());
                    }
                    if (jsonObject != null && "1".equals(jsonObject.getString("success")))
                    {
                      //  DialogHelper.showSimpleCustomDialog(mContext," Edit Achievement Successfully");
                        mListener.onEditSuccessfully();
                    }
                    else
                        mListener.onShowMessage("Editing failed...");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"error in editAchievementData() method , "+e.getMessage());
                    mListener.onShowMessage("Editing failed...");
                    e.printStackTrace();
                }
                finally
                {
                    mListener.onHideProgress();
                    mListener.onSetViewsEnabledOnProgressBarGone();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onSetViewsEnabledOnProgressBarGone();
                mListener.onHideProgress();
                mListener.onShowMessage("Editing failed...");
                // Log.e(TAG,"Error in onVolleyErrorResponse , "+error.getMessage());
                error.printStackTrace();
            }

        });
    }



    private Map<String,String> getEditRepParams(RepAchievement repAchievement, String repMediaUrl)
    {
        Map<String,String> params = new HashMap<>();
        try
        {
        params.put("edit_athlete_representative","1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("media",repAchievement.getRepMediaBase64());
        params.put("team",repAchievement.getRepTeam());
        params.put("location",repAchievement.getRepLocation());
        params.put("from_month",repAchievement.getRepFromMonth());
        params.put("from_year",repAchievement.getRepFromYear());
        params.put("to_month",repAchievement.getRepToMonth());
        params.put("to_year",repAchievement.getRepToYear());
        params.put("role",repAchievement.getRepRole());
        params.put("link",repAchievement.getRepLink());
        params.put("id",repAchievement.getRepID());

            Log.i(TAG,"edit rep achievement params sent to server = "+params);

        }
        catch (Exception e)
        {
            Log.e(TAG,"error in editRepParams() method , "+e.getMessage());
            e.printStackTrace();
        }

        return params;
    }

}
