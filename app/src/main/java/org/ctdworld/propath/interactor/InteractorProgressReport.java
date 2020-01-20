package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractProgressReport;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.SchoolReview;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorProgressReport implements ContractProgressReport.Interactor {
    private static final String TAG = InteractorProgressReport.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context context;


    public InteractorProgressReport(OnFinishedListener onFinishedListener, Context context) {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }

    @Override
    public void getProgressReport(String userId,String review_id)
    {

        Log.d(TAG," review_id :" +review_id );

        Log.d(TAG," getProgressReport called :" );
        remoteServer.sendData(RemoteServer.URL, getParamsOfProgressReport(userId,review_id), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                JSONObject jsonObject;

                Log.i(TAG,"server response while getting progress report = "+message);

                try {
                    jsonObject = new JSONObject(message.trim());
                    Log.d(TAG,"server message = "+message);
                    if ("1".equals(jsonObject.getString("res"))) {
                         Log.d(TAG,"called");
                         parseJsonAndUpdateUI(jsonObject.getJSONArray("data"));
                    }
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in sendProgressReport() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onFailed();
                }

                finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();

                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG,"sendProgressReport() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    private Map<String, String> getParamsOfProgressReport(String userId,String review_id) {

        Map<String, String> params = new HashMap<>();
        params.put("get_school_review_detail", "1");
        params.put("user_id", userId);
        params.put("review_id", review_id);

        Log.i(TAG, "params to get school report = " + params);

        return params;
    }


    private void parseJsonAndUpdateUI(JSONArray jsonArray)
    {
        Log.d(TAG, "jsonArray :" + jsonArray);
        SchoolReview schoolReview = new SchoolReview();
        List<SchoolReview> progressList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                schoolReview.setComments(jsonObject.getString("comment"));
                schoolReview.setTeacherName(jsonObject.getString("teacher_name"));
                schoolReview.setDate(jsonObject.getString("date"));
                schoolReview.setAthleteName(jsonObject.getString("user_name"));
                schoolReview.setImprovements(jsonObject.getString("improvements_needed"));
                schoolReview.setStrengths(jsonObject.getString("strengths"));
                schoolReview.setSuggestions(jsonObject.getString("suggestions"));
                schoolReview.setAssistance(jsonObject.getString("assistance_requested"));
                schoolReview.setQualification(jsonObject.getString("qualification"));
                String picUrl = RemoteServer.BASE_IMAGE_URL+jsonObject.getString("profile_image");
                schoolReview.setUserPicUrl(picUrl);

                JSONArray array = jsonObject.getJSONArray("review");
                for (int j = 0 ; j< array.length(); j++) {
                    SchoolReview schoolReview1 = new SchoolReview();
                    JSONObject subjectgradeObject = array.getJSONObject(j);
                    schoolReview1.setSubject(subjectgradeObject.getString("subject"));
                    schoolReview1.setGrade(subjectgradeObject.getString("grade"));
                    schoolReview1.setSubjectID(subjectgradeObject.getString("subject_id"));
                    progressList.add(schoolReview1);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


            listener.getOnReceivedProgressReport(schoolReview,progressList);

    }
}
