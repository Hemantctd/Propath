package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.contract.ContractAddCoachFeedback;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InteractorAddCoachFeedback implements ContractAddCoachFeedback.Interactor
{
    private static final String TAG = InteractorAddCoachFeedback.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context context;


    public InteractorAddCoachFeedback(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;
    }

    @Override
    public void requestToGiveFeedback(CoachData coachData) {

        remoteServer.sendData(RemoteServer.URL, getParams(coachData), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
             Log.i(TAG,"server message to add coach feedback = "+message);

                Log.i(TAG,"server message = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                        listener.onSuccess(jsonObject.getString("message"));
                    else
                    {
                        if ("0".equals(jsonObject.getString("success")))
                            listener.onFailed(jsonObject.getString("message"));
                    }


                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in addCoachFeedback() method , "+e.getMessage());
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
                Log.d(TAG,"addCoachFeedback() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }


    private Map<String,String> getParams(CoachData coachData)
    {

        Map<String,String> params = new HashMap<>();

            params.put("add_coach_feedback", "1");
            params.put("workons", coachData.getWorkons());
            params.put("strenths", coachData.getStrenths());
            params.put("event", coachData.getEvents());
            params.put("athlete_id",coachData.getAthleteID());
            params.put("coach_id", SessionHelper.getInstance(context).getUser().getUserId());
            params.put("improvement_needed",coachData.getImprovements());
            params.put("suggestions", coachData.getSuggestions());
            params.put("assistance_requested", coachData.getAssistanceRequired() );
            params.put("assistance_offered", coachData.getAssistanceOffered());

        Log.i(TAG,"params = "+ params);

        return params;
    }
}
