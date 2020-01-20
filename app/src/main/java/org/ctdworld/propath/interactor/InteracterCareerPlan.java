package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractCareerPlan;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.model.CareerPlan.CareerUser;
import org.ctdworld.propath.model.Response;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteracterCareerPlan implements ContractCareerPlan.Interactor
{
    private static final String TAG = InteracterCareerPlan.class.getSimpleName();
    private OnFinishedListener mListener;
    private Context mContext;
    private RemoteServer mRemoteServer;


    public InteracterCareerPlan(OnFinishedListener listener, Context context)
    {
        this.mListener = listener;
        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }




    // saving career plan on server
    @Override
    public void saveCareerPlan(final CareerUser careerUser)
    {
        if (careerUser == null || careerUser.getCareerData() == null)
            return;

        final CareerPlan.CareerData careerData = careerUser.getCareerData();

        Map<String,String> params = new HashMap<>();
        params.put("create_career_plan","1");
        params.put("athlete_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("future_career", careerData.getFutureCareer());
        params.put("location", careerData.getLocationOrganization());
        params.put("scholarship", careerData.getScholarShip());
        params.put("internship", careerData.getInternShip());
        params.put("apprenticeship", careerData.getApprenticeship());
        params.put("school_subject", careerData.getSubjectsNeeded());
        params.put("email", careerData.getEmail());
        params.put("phone_number", careerData.getPhoneNumber());

        Log.i(TAG,"params to career plan = "+params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"remote message for creating career plan = " +message);
                try
                {
                    if (!RemoteServer.isJsonValid(message))
                    {
                        mListener.onFailed(RemoteServer.getJsonNotValidErrorMessage());
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(message);
                    if ("1".contains(jsonObject.getString("success")))
                    {
                        CareerPlan careerPlan = new Gson().fromJson(message, CareerPlan.class);
                       // CareerUser careerUserResponse = new CareerUser();
                      //  careerUserResponse.setCareerData(careerUserResponse);
                        if (careerPlan != null && careerPlan.getCareerUserList() != null && careerPlan.getCareerUserList().size()>0)
                            mListener.onSavedCareerPlan(careerPlan.getCareerUserList().get(0));  // getting first user there will be always 1 user
                        else
                            mListener.onSavedCareerPlan(null);
                    }
                    else if ("0".contains(jsonObject.getString("success")))
                            mListener.onFailed(jsonObject.getString("message"));
                    else
                        mListener.onFailed("Failed...");

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    mListener.onFailed(RemoteServer.getJsonNotValidErrorMessage());
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onFailed(RemoteServer.getServerErrorMessage(error));
                error.printStackTrace();
                Log.e(TAG,"error while saving career plan");
            }
        });

    }





    // updating career plan
    @Override
    public void updateCareerPlan(final CareerUser careerUser)
    {
        Log.i(TAG,"updateCareerPlan() method called...........................");
        
        if (!validateCareerUserCareerData(careerUser))
            return;

        CareerPlan.CareerData careerData = careerUser.getCareerData();
        
        Map<String,String> params = new HashMap<>();


        params.put("update_career_plan","1");
        params.put("athlete_id",careerUser.getUserId());
        params.put("future_career", careerData.getFutureCareer());
        params.put("location", careerData.getLocationOrganization());
        params.put("scholarship", careerData.getScholarShip());
        params.put("school_subject", careerData.getSubjectsNeeded());
        params.put("career_plan_id", careerData.getCareerId());

        Log.i(TAG,"params to update career plan = "+params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"remote message for updating career plan = " +message);
                try
                {
                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject != null)
                    {
                        if ("1".contains(jsonObject.getString("success")))
                            mListener.onCareerPlanUpdated(careerUser);
                        else if ("0".contains(jsonObject.getString("success")))
                            mListener.onFailed(jsonObject.getString("message"));
                        else
                            mListener.onFailed("Failed...");
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    mListener.onFailed("Failed...");
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onFailed("Failed...");
                error.printStackTrace();
                Log.e(TAG,"error while updating career plan");
            }
        });


    }




    // to get all users list who have created career plan, each user contains career plan data
    @Override
    public void requestCareerUsers(final CareerUser careerUser) {
        if (careerUser == null)
            return;

        Map<String, String> params = new HashMap<>();
        params.put("get_all_career_plan","1");
        params.put("user_id", careerUser.getUserId());
     //   params.put("athlete_id",careerUser.getUserId());

        Log.i(TAG,"param to get all career plan list = "+params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"remote message career plan data = " +message);
               /* try
                {*/
                    if (RemoteServer.isJsonValid(message))
                    {
                        CareerPlan careerPlan = new Gson().fromJson(message, CareerPlan.class);
                        //final JSONObject jsonObject = new JSONObject(message);
                        if (careerPlan != null && careerPlan.getResponseStatus(careerPlan).equals("1"))//.equals(jsonObject.getString("res")))
                        {
                            mListener.onReceivedCareerUsers(careerPlan.getCareerUserList());
                           // JSONArray jsonArray = jsonObject.getJSONArray("data");
                           // JSONObject objectData = jsonArray.getJSONObject(0);
                            ///parseJsonAndUpdateUi(objectData);
                        }
                        else
                            mListener.onFailed("Failed getting career plan list...");
                    }
                    else
                        mListener.onFailed(RemoteServer.getJsonNotValidErrorMessage());
                /*}
                catch (JSONException e)
                {
                    mListener.onFailed(RemoteServer.getJsonNotValidErrorMessage());
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onFailed(RemoteServer.getServerErrorMessage(error));
                error.printStackTrace();
            }
        });
    }



    // to delete CareerData
    @Override
    public void deleteCareerData(final CareerUser careerUser)
    {
        Log.i(TAG,"updateCareerPlan() method called...........................");

        if (careerUser == null || careerUser.getCareerDataList() == null)
            return;


        Map<String,String> params = new HashMap<>();

        for (CareerPlan.CareerData careerData : careerUser.getCareerDataList())
        {
            if (careerData != null && careerData.getCareerId() != null)
                params.put("career_plan_id[]", careerData.getCareerId());
        }
        params.put("delete_career_plan","1");
        params.put("athlete_id",careerUser.getUserId());


        Log.i(TAG,"params to delete career plans = "+params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                Log.i(TAG,"remote message to delete career plans = " +message);
                try
                {
                    Response response = new Gson().fromJson(message, Response.class);

                    if (response.getResponseStatus(response).equals(Response.STATUS_SUCCESS))
                        mListener.onCareerDataDeleted(careerUser);
                    else
                        mListener.onFailed(null);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    mListener.onFailed("Failed...");
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                mListener.onFailed("Failed...");
                error.printStackTrace();
                Log.e(TAG,"error while deleting career plans");
            }
        });
    }



    private boolean validateCareerUserCareerData(CareerUser careerUser)
    {
        return careerUser != null && careerUser.getCareerData() != null;
    }



}
