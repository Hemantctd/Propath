package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractStatSurvey;
import org.ctdworld.propath.database.RemoteServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InteractorStatSurvey implements ContractStatSurvey.Interactor {
    private static final String TAG = InteractorStatSurvey.class.getSimpleName();
    private RemoteServer remoteServer;
    Context context;
    private Context mContext;
    private OnFinishedListener listener;


    public InteractorStatSurvey(OnFinishedListener onFinishedListener, Context context) {
        this.listener = onFinishedListener;
        this.mContext = context;
        remoteServer = new RemoteServer(context);


    }


    // statSurvey survey
    private Map<String,String> getParams(String survey_id)
    {

        Map<String,String> params = new HashMap<>();

        params.put("get_survey_stat", "1");
        params.put("survey_id",survey_id);

        Log.i(TAG, "params to stat survey = " + params);

        return params;
    }

    @Override
    public void statSurvey(String survey_id) {
        Log.i(TAG,"statSurvey() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(survey_id), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"server message to statSurvey survey = "+message);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                    {
                        listener.onGetReceivedStatExcel(jsonObject.getString("message"));
                        // listener.onSuccess(jsonObject.get("message"));
                    }

                    else if("0".equals(jsonObject.getString("success")))
                        listener.onFailed("Data Not Found");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in statSurvey() method , "+e.getMessage());
                    e.printStackTrace();
                    listener.onFailed("Failed");
                }
                finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG,"statSurvey() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }


}
