package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import org.ctdworld.propath.contract.ContractSelfReviewReport;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.SelfReview;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorSelfReviewReport implements ContractSelfReviewReport.Interactor {
    private static final String TAG = InteractorSelfReviewReport.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context context;


    public InteractorSelfReviewReport(OnFinishedListener onFinishedListener, Context context) {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }

    @Override
    public void getSelfReview(String userId,String review_id)
    {

        Log.d(TAG," getProgressReport called :" );
        remoteServer.sendData(RemoteServer.URL, getParamsOfSelfReview(userId,review_id), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                JSONObject jsonObject;

                Log.i(TAG,"server response while getting self review report = "+message);

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
                    Log.e(TAG,"Error in sendSelfReviewReport() method , "+e.getMessage());
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
                error.printStackTrace();
                listener.onHideProgress();
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    private Map<String, String> getParamsOfSelfReview(String userId,String review_id) {

        Map<String, String> params = new HashMap<>();
        params.put("get_self_review_detail", "1");
        params.put("user_id", userId);
        params.put("review_id", review_id);

        Log.i(TAG, "params to get school report = " + params);

        return params;
    }


    private void parseJsonAndUpdateUI(JSONArray jsonArray)
    {
        Log.d(TAG, "jsonArray :" + jsonArray);
        SelfReview selfReview = new SelfReview();
        List<SelfReview> selfReviewList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                selfReview.setComments(jsonObject.getString("comment"));
                selfReview.setDate(jsonObject.getString("date"));
                selfReview.setAthleteName(jsonObject.getString("user_name"));
                selfReview.setQualification(jsonObject.getString("label"));
                selfReview.setReviewId(jsonObject.getString("id"));
                selfReview.setImprovements(jsonObject.getString("improvements_needed"));
                selfReview.setStrengths(jsonObject.getString("strengths"));
                selfReview.setSuggestions(jsonObject.getString("suggestions"));
                selfReview.setAssistance(jsonObject.getString("assistance_requested"));

                String picUrl = RemoteServer.BASE_IMAGE_URL+jsonObject.getString("profile_image");
                selfReview.setUserPicUrl(picUrl);

                JSONArray array = jsonObject.getJSONArray("review");
                for (int j = 0 ; j < array.length(); j++) {
                    SelfReview selfReview1 = new SelfReview();
                    JSONObject subjectgradeObject = array.getJSONObject(j);
                    selfReview1.setSubject(subjectgradeObject.getString("subject"));
                    selfReview1.setGrade(subjectgradeObject.getString("grade"));
                    selfReview1.setSubjectID(subjectgradeObject.getString("subject_id"));
                    selfReviewList.add(selfReview1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


            listener.getOnReceivedSelfReview(selfReview,selfReviewList);

    }
}
