package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ctdworld.propath.contract.ContractGetAllSurvey;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InteractorGetAllSurvey implements ContractGetAllSurvey.Interactor
{
    private static final String TAG = InteractorGetAllSurvey.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context context;


    public InteractorGetAllSurvey(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.context = context;

    }

    private Map<String,String> getParams()
    {

        Map<String,String> params = new HashMap<>();

        params.put("get_all_survey", "1");
        params.put("user_id", SessionHelper.getInstance(context).getUser().getUserId());

        return params;
    }


    @Override
    public void getAllSurvey() {


        Log.i(TAG,"getAllSurvey() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to get all survey = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                    {
                        Gson gson = new Gson();
                        Survey survey = gson.fromJson(message, Survey.class);
                        listener.onReceivedGetAllSurvey(survey);

                    }

                        // listener.onGetReceivedAllSurvey(onGetAllSurveyList(jsonObject.getJSONArray("message")));

                    else if("0".equals(jsonObject.getString("success")))
                    {

                    }
                       // listener.onFailed("Data Not Found");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in getAllSurvey() method , "+e.getMessage());
                    e.printStackTrace();
                  //  listener.onFailed("Failed");
                }
                finally {
                    listener.onHideProgress();
                    listener.onSetViewsEnabledOnProgressBarGone();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error)
            {
                Log.d(TAG,"requestAllSurvey() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }


   /* public void onGetAllSurveyList(JSONArray jsonArray)
    {
        Log.d(TAG," JSON ARRAY : " + jsonArray);

        List<List<List<SurveyQusetions>>> surveyFinalList = new ArrayList<>();


        for (int i = 0; i < jsonArray.length(); i++)
        {
            List<List<SurveyQusetions>> surveyOneObject = new ArrayList<>();
            List<SurveyQusetions> surveyCommonDataList = new ArrayList<>();

            try
            {
                JSONObject object = jsonArray.getJSONObject(i);
                SurveyQusetions surveyQusetions = new SurveyQusetions();
                surveyQusetions.setSurvey_id(object.getString("id"));
                surveyQusetions.setUser_id(object.getString("user_id"));
                surveyQusetions.setSurveyTitle(object.getString("title"));
                surveyQusetions.setHit_count(object.getString("hit_count"));
                surveyQusetions.setSurvey_anonymous_type(object.getString("show_anonymous"));
                surveyQusetions.setSurvey_enable_type(object.getString("status"));
                surveyQusetions.setSurveyDesc(object.getString("description"));
                surveyQusetions.setSurveyQusetionType(object.getString("question_type"));
               // surveyQusetions.setUser_name(object.getString("shared_by"));
                surveyQusetions.setSurvey_no_of_question(object.getString("question_no"));

                surveyCommonDataList.add(surveyQusetions);

                List<SurveyQusetions> listQuestions = new ArrayList<>();


                JSONArray jsonArray1 = object.getJSONArray("Survey");
                Log.d(TAG,"JSON ARRAY 1 : " + jsonArray1);
                for (int j = 0 ; j < jsonArray1.length(); j++)
                {

                    JSONObject jsonObject = jsonArray1.getJSONObject(j);
                    SurveyQusetions surveyQusetions1 = new SurveyQusetions();
                    surveyQusetions1.setQuestions(jsonObject.getString("survey_question"));
                    listQuestions.add(surveyQusetions1);
                }

                //  surveyList.add(surveyQusetions);
                surveyOneObject.add(surveyCommonDataList);
                surveyOneObject.add(listQuestions);
                surveyFinalList.add(surveyOneObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        listener.onReceivedGetAllSurvey(new SurveyQusetions(),surveyFinalList);


        // return surveyList;
    }*/

//    private List<SurveyQusetions> onGetAllSurveyList(JSONArray jsonArray)
//    {
//        List<SurveyQusetions> surveyList = new ArrayList<>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                JSONObject object = jsonArray.getJSONObject(i);
//                SurveyQusetions surveyQusetions = new SurveyQusetions();
//
//                surveyQusetions.setSurvey_id(object.getString("id"));
//                surveyQusetions.setUser_id(object.getString("user_id"));
//                surveyQusetions.setSurveyTitle(object.getString("title"));
//                surveyQusetions.setHit_count(object.getString("hit_count"));
//
//                surveyList.add(surveyQusetions);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return surveyList;
//    }


}
