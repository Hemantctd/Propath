package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractSelfReviewedAthletes;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractorSelfReviewedAthletes implements ContractSelfReviewedAthletes.Interactor {
    private static final String TAG = InteractorSelfReviewedAthletes.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context context;


    public InteractorSelfReviewedAthletes(OnFinishedListener onFinishedListener, Context context) {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }


    private Map<String, String> getParams() {

        Map<String, String> params = new HashMap<>();

        params.put("get_selfreviewd_athlete", "1");
        params.put("user_id", SessionHelper.getInstance(context).getUser().getUserId());
        Log.i(TAG, "params = " + params);

        return params;
    }

    private List<GetAthletes> getAllReviewedAthleteList(JSONArray jsonArray) {


        List<GetAthletes> userList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);

                Log.d(TAG, "JSON ARRAY : " + object.getString("user_name"));
                String user_name = object.getString("user_name");

                GetAthletes getAthletes = new GetAthletes();
                getAthletes.setName(user_name);
                getAthletes.setUserId(object.getString("user_id"));

                String picUrl = RemoteServer.BASE_IMAGE_URL + object.getString("profile_image");
                getAthletes.setUserPicUrl(picUrl);
                getAthletes.setEmail(object.getString("user_email"));
                getAthletes.setRoleId(object.getString("user_role"));
                getAthletes.setStatus(object.getString("user_status"));
                getAthletes.setSchoolreviewed(object.getString("school_reviewd"));
                getAthletes.setRequestStatus(object.getString("show_report_status"));

                userList.add(getAthletes);

            } catch (JSONException e) {
                Log.e(TAG, "Error in getAllReviewedAthleteList() method , " + e.getMessage());
                e.printStackTrace();
            }
        }
        return userList;

    }

    @Override
    public void getAthletesReviewed() {

        Log.i(TAG, "requestAllReviewedAthletes() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {

                // Log.i(TAG,"server message to get athletes reviewed= "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());
                    Log.d(TAG, "server message athletes = " + message);

                    if ("1".equals(jsonObject.getString("res")))
                        listener.onGotReviewedAthletes(getAllReviewedAthleteList(jsonObject.getJSONArray("data")));
                    else
                        listener.onShowMessage(jsonObject.getString("message"));

                } catch (JSONException e) {
                    Log.e(TAG, "Error in requestAllReviewedAthletes() method , " + e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                } finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG, "requestAllReviewedAthletes() method volley error = " + error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });

    }
}
