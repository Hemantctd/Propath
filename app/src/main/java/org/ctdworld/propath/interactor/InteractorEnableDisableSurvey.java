package org.ctdworld.propath.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;

import org.ctdworld.propath.activity.ActivitySurvey;
import org.ctdworld.propath.contract.ContractEnableDisableSurvey;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InteractorEnableDisableSurvey implements ContractEnableDisableSurvey.Interactor
{
    private static final String TAG = InteractorEnableDisableSurvey.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context context;


    public InteractorEnableDisableSurvey(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;

    }

    @Override
    public void enabledisableSurvey(String status, String show_anonymous, String survey_id) {

        Log.d(TAG," enabledisableSurvey called :" );
        remoteServer.sendData(RemoteServer.URL, getParamsOfEnableDisableSurvey(status,show_anonymous,survey_id), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message)
            {
                JSONObject jsonObject;

                Log.i(TAG,"server response while getting enabledisableSurvey = "+message);

                try {
                    jsonObject = new JSONObject(message.trim());
                    Log.d(TAG,"server message = "+message);
                    if ("1".equals(jsonObject.getString("success"))) {
                        DialogHelper.showSimpleCustomDialog(context, "Success", new DialogHelper.ShowSimpleDialogListener() {
                            @Override
                            public void onOkClicked() {
                                context.startActivity(new Intent(context, ActivitySurvey.class));
                            }
                        });
                    }
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in enabledisableSurvey( method , "+e.getMessage());
                    e.printStackTrace();
                }
                finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG,"enableDisableSurvey() method error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    private Map<String, String> getParamsOfEnableDisableSurvey(String status,String show_anonymous,String survey_id) {

        Map<String, String> params = new HashMap<>();
        params.put("enable_disable_survey", "1");
        params.put("survey_id", survey_id);
        params.put("status", status);
        params.put("show_anonymous",show_anonymous);

        Log.i(TAG, "params to get enabledisableSurvey = " + params);

        return params;
    }
}
