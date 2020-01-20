package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractGetAllAthletes;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractorGetAllAthletes implements ContractGetAllAthletes.Interactor
{
    private static final String TAG = InteractorGetAllAthletes.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context context;

    String messages = "";



    public InteractorGetAllAthletes(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }

    @Override
    public void requestAllAthletes()
    {
        Log.i(TAG,"requestAllAthletes() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get all athletes = "+message);
                JSONObject jsonObject;
                try {
                        jsonObject = new JSONObject(message.trim());
                        Log.d(TAG,"server message athletes = "+message);

                        if ("1".equals(jsonObject.getString("res"))) {
                            messages = jsonObject.getString("message");
                            listener.onGetAllAthletes(getAllAthleteList(jsonObject.getJSONArray("data")));

                        }

                        else
                            listener.onShowMessage(jsonObject.getString(""));

                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in requestAllAthletes() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onFailed(messages);
                }
                finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestAllAthletes() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }



    private Map<String,String> getParams()
    {

        Map<String,String> params = new HashMap<>();

            params.put("get_all_athlete", "1");
            params.put("user_id", SessionHelper.getInstance(context).getUser().getUserId());
        String loggedInUser = SessionHelper.getInstance(context).getUser().getRoleId();
        String teacherRoleId = RoleHelper.TEACHER_ROLE_ID;
        String coachRoleId = RoleHelper.COACH_ROLE_ID;
        if (loggedInUser.equals(teacherRoleId)) {
            params.put("request_type", "school_review");
        }
        else if (loggedInUser.equals(coachRoleId))
        {
            params.put("request_type", "coach_feedback");
        }
        Log.i(TAG,"params = "+ params);

        return params;
    }

    private  List<GetAthletes> getAllAthleteList(JSONArray jsonArray) {
        List<GetAthletes> userList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);

                GetAthletes getAthletes = new GetAthletes();
                getAthletes.setName(object.getString("user_name"));
                getAthletes.setUserId(object.getString("user_id"));

                String picUrl = RemoteServer.BASE_IMAGE_URL + object.getString("profile_image");
                getAthletes.setUserPicUrl(picUrl);
                getAthletes.setEmail(object.getString("user_email"));
                getAthletes.setRoleId(object.getString("user_role"));
                getAthletes.setStatus(object.getString("user_status"));
                getAthletes.setSchoolreviewed(object.getString("school_reviewd"));
                String loggedInUser = SessionHelper.getInstance(context).getUser().getRoleId();
                String teacherRoleId = RoleHelper.TEACHER_ROLE_ID;
                String coachRoleId = RoleHelper.COACH_ROLE_ID;
                if (loggedInUser.equals(teacherRoleId)) {
                    getAthletes.setSchool_recview_counter(object.getString("school_review_counter"));
                    getAthletes.setRequestStatus(object.getString("show_report_status"));

                }
                else if (loggedInUser.equals(coachRoleId)) {
                    getAthletes.setCoach_feedback_counter(object.getString("coach_feedback_counter"));
                }


                userList.add(getAthletes);

            } catch (JSONException e) {
                Log.e(TAG, "Error in getAllAthleteList() method , " + e.getMessage());
                e.printStackTrace();
            }
        }
        return userList;
    }
}
