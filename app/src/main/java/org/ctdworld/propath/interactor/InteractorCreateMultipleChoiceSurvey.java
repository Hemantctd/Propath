package org.ctdworld.propath.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.activity.ActivitySurvey;
import org.ctdworld.propath.contract.ContractCreateMultipleChoiceSurvey;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;


public class InteractorCreateMultipleChoiceSurvey implements ContractCreateMultipleChoiceSurvey.Interactor {
    private static final String TAG = InteractorCreateMultipleChoiceSurvey.class.getSimpleName();
    Context mContext;
    private OnFinishedListener listener;


    public InteractorCreateMultipleChoiceSurvey(OnFinishedListener onFinishedListener,Context context) {
        this.listener = onFinishedListener;
        this.mContext = context;
        //remoteServer = new RemoteServer(mContext);


    }

    @Override
    public void saveSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice) {
        Log.i(TAG,"saveSurvey called");

        String uuid = UUID.randomUUID().toString();
        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

            for (int i = 0; i < surveyQuestionsList.size() ; i++)
            {
                Survey.SurveyData.MultipleChoice.Questions surveyQuestions = surveyQuestionsList.get(i);

                uploadRequest.addParameter("question_title["+i+"]",surveyQuestions.getQuestion());
                if  (surveyQuestions.getQuestionImageLink() != null && !surveyQuestions.getQuestionImageLink().equals(""))
                    uploadRequest.addFileToUpload(surveyQuestions.getQuestionImageLink(),"question_file["+i+"]");

                Log.d(TAG,"question_file" + surveyQuestions.getQuestionImageLink());
                Log.d(TAG,"question_title" + surveyQuestions.getQuestion());

                List<String> optionsList = surveyQuestions.getOptionList();

                 for (int j = 0; j < optionsList.size() ; j++)
                 {
                     String questionOption = optionsList.get(j);
                     uploadRequest.addParameter("question_option["+i+"]["+j+"]",questionOption);
                     Log.d(TAG,"question_option" + questionOption);

                 }
            }
            uploadRequest.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("create_survey_multiple","1");
            uploadRequest.addParameter("survey_title",surveyMultipleChoice.getTitle());
            uploadRequest.addParameter("question_type",surveyMultipleChoice.getQuestionType());
            uploadRequest.addParameter("survey_description",surveyMultipleChoice.getDescription());
            uploadRequest.addParameter("num_of_questions", String.valueOf(surveyQuestionsList.size()));
            /*uploadRequest.setNotificationConfig(new UploadNotificationConfig());
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
                    Log.d(TAG,"addSurvey() method error = "+exception.getMessage());
                    exception.printStackTrace();
                    listener.onHideProgress();
                    listener.onShowMessage("Connection Error");
                    listener.onSetViewsEnabledOnProgressBarGone();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String responseString = serverResponse.getBodyAsString();

                    try {
                        JSONObject objectData = new JSONObject(responseString);
                         if ("1".equals(objectData.getString("res")))
                             listener.onSuccess("Survey Created Successfully..");
                         else
                             listener.onFailed("Failed...");

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    finally {
                        listener.onHideProgress();
                        listener.onSetViewsEnabledOnProgressBarGone();
                    }
                    Log.i(TAG,"file upload response questions = "+responseString);
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
}
