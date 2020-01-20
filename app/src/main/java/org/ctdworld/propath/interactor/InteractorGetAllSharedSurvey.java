package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.contract.ContractGetAllSharedSurvey;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class InteractorGetAllSharedSurvey implements ContractGetAllSharedSurvey.Interactor
{
    private static final String TAG = InteractorGetAllSharedSurvey.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context mContext;


    public InteractorGetAllSharedSurvey(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;

    }

    private Map<String,String> getParams()
    {

        Map<String,String> params = new HashMap<>();

        params.put("get_shared_survey", "1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());

        return params;
    }


    @Override
    public void getAllSharedSurvey() {


        Log.i(TAG,"getAllSharedSurvey() method called ");

        remoteServer.sendData(RemoteServer.URL, getParams(), new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to getAllSharedSurvey = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                        onGetAllSharedSurveyList(jsonObject.getJSONArray("message"));
                       /// listener.onGetReceivedAllSharedSurvey(onGetAllSharedSurveyList(jsonObject.getJSONArray("message")));

                    else if("0".equals(jsonObject.getString("success")))
                    {

                    }
                       // listener.onFailed("Data Not Found");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in getAllSharedSurvey() method , "+e.getMessage());
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
                Log.d(TAG,"getAllSharedSurvey() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }

    @Override
    public void submitSurvey(String survey_id, List<SurveyQusetions> surveyQusetionsList, List<SurveyQusetions> surveyAnswersList) {
        String uuid = UUID.randomUUID().toString();
        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);
            for(SurveyQusetions question : surveyQusetionsList)
            {
                uploadRequest.addParameter("question[]",question.getQuestions());
            }
            for(SurveyQusetions answers : surveyAnswersList)
            {
                uploadRequest.addParameter("answer[]",answers.getAnswers());
            }
            uploadRequest.addParameter("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("submit_survey","1");
            uploadRequest.addParameter("survey_id",survey_id);

         /*   uploadRequest.setNotificationConfig(new UploadNotificationConfig());
            uploadRequest.setMaxRetries(3);*/
            UploadNotificationConfig config = new UploadNotificationConfig();
            uploadRequest.setNotificationConfig(config);
            config.getCompleted().autoClear = true;
            uploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {

                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                }

                @Override
                public void onCompleted(final Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String responseString = serverResponse.getBodyAsString();
                    Log.d(TAG,"response string *******" +responseString);
                      try {
                                JSONObject objectData = new JSONObject(responseString);

                                if (objectData != null)
                                {
                                    if ("1".equals(objectData.getString("res"))) {
                                         listener.onSuccess(objectData.getString("result"));

                                    }
                                    else
                                    listener.onFailed(objectData.getString("result"));
                                }
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                      finally {
                          listener.onHideProgress();
                          listener.onSetViewsEnabledOnProgressBarGone();
                      }

                  //  Log.i(TAG,"file upload response answers  = "+responseString);
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {

                }

            });
            uploadRequest.startUpload();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    //    private List<SurveyQusetions> onGetAllSharedSurveyList(JSONArray jsonArray)
//    {
    public void onGetAllSharedSurveyList(JSONArray jsonArray)
    {
        Log.d(TAG,"JSON ARRAY : " + jsonArray);

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
                surveyQusetions.setUser_name(object.getString("shared_by"));
                surveyQusetions.setSurvey_no_of_question(object.getString("question_no"));
                surveyQusetions.setIsCompleted(object.getString("isCompleted"));
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

        listener.onGetReceivedAllSharedSurvey(new SurveyQusetions(),surveyFinalList);


        // return surveyList;
    }



    // submitted survey



    @Override
    public void getAllSubmittedSurvey(String survey_id) {
        Log.i(TAG,"getAllSubmittedSurvey() method called ");

        Map<String,String> params = new HashMap<>();
        params.put("survey_id",survey_id);
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("get_submitted_survey","1");

        Log.d(TAG,"params : " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener()
        {
            @Override
            public void onVolleySuccessResponse(String message)
            {

                Log.i(TAG,"server message to getAllSubmittedSurvey = "+message);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                        onGetAllSubmittedSurveyList(jsonObject.getJSONObject("message"));
                        /// listener.onGetReceivedAllSharedSurvey(onGetAllSharedSurveyList(jsonObject.getJSONArray("message")));

                    else if("0".equals(jsonObject.getString("success")))
                    {

                    }
                    // listener.onFailed("Data Not Found");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in getAllSubmittedSurvey() method , "+e.getMessage());
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
                Log.d(TAG,"getAllSubmittedSurvey() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });

    }

    public void onGetAllSubmittedSurveyList(JSONObject jsonObject)
    {
        List <SurveyQusetions> surveyQusetionAnswerList = new ArrayList<>();
        SurveyQusetions surveyQusetions = new SurveyQusetions();

        try {
            surveyQusetions.setSurveyTitle(jsonObject.getString("title"));
            surveyQusetions.setSurvey_no_of_question(jsonObject.getString("question_no"));
            surveyQusetions.setSurveyDesc(jsonObject.getString("description"));
            surveyQusetions.setSurveyQusetionType(jsonObject.getString("question_type"));
            JSONArray jsonArray = jsonObject.getJSONArray("Survey");

             for (int i = 0 ; i < jsonArray.length(); i++)
             {
                 SurveyQusetions surveyQusetions1 = new SurveyQusetions();
                 JSONObject object1 = jsonArray.getJSONObject(i);
                 surveyQusetions1.setQuestions(object1.getString("survey_question"));
                 surveyQusetions1.setAnswers(object1.getString("survey_answer"));

                 surveyQusetionAnswerList.add(surveyQusetions1);
                 Log.d(TAG,"survey_questions " + surveyQusetions1.getQuestions());
                 Log.d(TAG,"survey_answers " + surveyQusetions1.getAnswers());

             }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


       listener.onGetReceivedAllSubmittedSurvey(surveyQusetions,surveyQusetionAnswerList);


        // return surveyList;
    }


}
