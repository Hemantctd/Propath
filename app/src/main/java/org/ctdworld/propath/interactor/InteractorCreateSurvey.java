package org.ctdworld.propath.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;
import org.ctdworld.propath.activity.ActivitySurvey;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.GetGroupNames;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;


public class  InteractorCreateSurvey implements ContractSurvey.Interactor {
    private static final String TAG = InteractorCreateSurvey.class.getSimpleName();
    private RemoteServer remoteServer;
    Context context;
    private Context mContext;
    private OnFinishedListener listener;


    public InteractorCreateSurvey(OnFinishedListener onFinishedListener, Context context) {
        this.listener = onFinishedListener;
        this.mContext = context;
        remoteServer = new RemoteServer(context);


    }





















    //*******************************************************save free response survey***************************************************//
    @Override
    public void saveSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList, Survey.SurveyData.FreeResponse surveyFreeResponse)
    {

        String uuid = UUID.randomUUID().toString();
        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);
            for (int i = 0; i < surveyQuestionsList.size() ; i++)
            {
                Survey.SurveyData.FreeResponse.Questions surveyQuestions = surveyQuestionsList.get(i);

                uploadRequest.addParameter("question["+i+"]",surveyQuestions.getQuestion());
                if  (surveyQuestions.getQuestionImg() != null && !surveyQuestions.getQuestionImg().equals(""))
                    uploadRequest.addFileToUpload(surveyQuestions.getQuestionImg(),"survey_image["+i+"]");
            }
           /* for(Survey.SurveyData.FreeResponse.Questions question : surveyQuestionsList)
            {
                uploadRequest.addParameter("question[]",question.getQuestion());
                uploadRequest.addFileToUpload(question.getQuestionImg(),"question_file["+i+"]");
            }*/
            uploadRequest.addParameter("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("add_survey","1");
            uploadRequest.addParameter("title",surveyFreeResponse.getTitle());
            uploadRequest.addParameter("question_type",surveyFreeResponse.getQuestionType());
            uploadRequest.addParameter("description",surveyFreeResponse.getDescription());
            uploadRequest.addParameter("question_no",surveyFreeResponse.getQuestionNo());
           /* uploadRequest.setNotificationConfig(new UploadNotificationConfig());
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
                                listener.onSuccess(objectData.getString("result"));
                            else
                                listener.onFailed("Failed");

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                        listener.onFailed("Connection Error...");

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























    //*******************************************************edit free response survey**********************************************************//
    @Override
    public void editFreeResponseSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQusetionsList, Survey.SurveyData.FreeResponse surveyFreeResponse) {

        String uuid = UUID.randomUUID().toString();
        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);
            for (int i = 0; i < surveyQusetionsList.size() ; i++)
            {
                Survey.SurveyData.FreeResponse.Questions surveyQuestions = surveyQusetionsList.get(i);

                uploadRequest.addParameter("question["+i+"]",surveyQuestions.getQuestion());

                if  (surveyQuestions.getQuestionImg() != null && !surveyQuestions.getQuestionImg().equals(""))  {
                    if (!Patterns.WEB_URL.matcher(surveyQuestions.getQuestionImg()).matches())
                     uploadRequest.addFileToUpload(surveyQuestions.getQuestionImg(), "survey_image[" + i + "]");
                    else
                        uploadRequest.addParameter("imageURL[" + i + "]",surveyQuestions.getQuestionImg());

                }
                else
                    uploadRequest.addParameter("survey_image[" + i + "]",surveyQuestions.getQuestionImg());

            }
                //if  (!surveyQuestions.getQuestionImg().equals("blank_image")) {
                    //uploadRequest.addFileToUpload(surveyQuestions.getQuestionImg(), "survey_image[" + i + "]");
                //}



                /*File f = new File(surveyQuestions.getQuestionImg());
                String mimetype = new MimetypesFileTypeMap().getContentType(f);
                String type = mimetype.split("/")[0];
                if (type.equals("image") && surveyQuestions.getQuestionImg() != null) {
                    {
                       // try {
                            uploadRequest.addFileToUpload(surveyQuestions.getQuestionImg(), "survey_image[" + i + "]");
                       *//* } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }*//*
                    }
                }
*/
/*                if  (surveyQuestions.getQuestionImg() != null && !surveyQuestions.getQuestionImg().equals("")) {

                    if (!Patterns.WEB_URL.matcher(surveyQuestions.getQuestionImg()).matches())
                        uploadRequest.addFileToUpload(surveyQuestions.getQuestionImg(), "survey_image[" + i + "]");
                }*/


            uploadRequest.addParameter("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("edit_survey","1");
            uploadRequest.addParameter("survey_id",surveyFreeResponse.getId());
            uploadRequest.addParameter("title",surveyFreeResponse.getTitle());
            uploadRequest.addParameter("question_type",surveyFreeResponse.getQuestionType());
            uploadRequest.addParameter("description",surveyFreeResponse.getDescription());
            uploadRequest.addParameter("question_no", String.valueOf(surveyQusetionsList.size()));
           /* uploadRequest.setNotificationConfig(new UploadNotificationConfig());
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
                    Log.d(TAG,"editFreeResponseSurvey() method error = "+exception.getMessage());
                    exception.printStackTrace();
                    listener.onHideProgress();
                    listener.onFailed("Connection Error...");
                    listener.onSetViewsEnabledOnProgressBarGone();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String responseString = serverResponse.getBodyAsString();

                    try {
                        JSONObject objectData = new JSONObject(responseString);

                            if ("1".equals(objectData.getString("res")))
                                listener.onSuccess(objectData.getString("result"));
                            else
                                listener.onFailed(objectData.getString("result"));

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                        listener.onFailed("Connection Error...");

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
























    //***********************************************************delete survey*******************************************************************//

    @Override
    public void deleteSurvey(String survey_id,String survey_type)
    {
        Map<String,String> params = new HashMap<>();

        params.put("delete_survey", "1");
        params.put("survey_id",survey_id);
        params.put("survey_type",survey_type);
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());

        Log.i(TAG, "params to delete survey = " + params);




        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"server message to delete survey = "+message);

                JSONObject jsonObject;
                try {
                   jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                         listener.onSuccess(jsonObject.getString("message"));
                    else
                        listener.onFailed("Failed...");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in deleteSurvey() method , "+e.getMessage());
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
                Log.d(TAG,"deleteSurvey() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });
    }










    //**************************************************************share survey*******************************************************************//
    @Override
    public void shareSurvey(GetGroupNames groupNames) {
        Log.i(TAG,"shareSurvey() method called ");

        Map<String,String> params = new HashMap<>();

        params.put("share_survey", "1");
        params.put("survey_id",groupNames.getSurvey_id());
        params.put("user_id[]",groupNames.getUser_id());
        params.put("group_id[]",groupNames.getGroup_id());


        Log.i(TAG, "params to share survey = " + params);






        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"server message to share survey = "+message);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("success")))
                    {
                        DialogHelper.showSimpleCustomDialog(mContext, "Shared Successfully", new DialogHelper.ShowSimpleDialogListener() {
                            @Override
                            public void onOkClicked() {
                                mContext.startActivity(new Intent(mContext,ActivitySurvey.class));
                            }
                        });
                        // listener.onSuccess(jsonObject.get("message"));
                    }

                    else if("0".equals(jsonObject.getString("success")))
                        listener.onFailed("Data Not Found");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in shareSurvey() method , "+e.getMessage());
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
                Log.d(TAG,"shareSurvey() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();
            }
        });


    }





    // ******************************************************copy survey*******************************************************//
    @Override
    public void copySurvey(String survey_id, String title,String survey_type) {
        Log.i(TAG,"copySurvey() method called ");

        Map<String,String> params = new HashMap<>();

        params.put("copy_survey", "1");
        params.put("survey_id",survey_id);
        params.put("survey_title",title);
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("survey_type",survey_type);

        Log.i(TAG, "params to copy survey = " + params);

        remoteServer.sendData(RemoteServer.URL,params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.i(TAG,"server message to copy survey = "+message);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(message.trim());

                    if ("1".equals(jsonObject.getString("res")))
                        listener.onSuccess("Survey Copied Successfully");
                    else
                        listener.onFailed("Failed");
                }
                catch (JSONException e)
                {
                    Log.e(TAG,"Error in copySurvey() method , "+e.getMessage());
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

                Log.d(TAG,"copySurvey() method volley error = "+error.getMessage());
                error.printStackTrace();
                listener.onHideProgress();
                listener.onShowMessage("Connection Error");
                listener.onSetViewsEnabledOnProgressBarGone();

            }
        });
    }




























    // ******************************************************save yes/no survey*******************************************************//

    @Override
    public void saveYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionList, Survey.SurveyData.YesNo surveyYesNo) {
        Log.i(TAG,"saveYesNoSurvey called");

        String uuid = UUID.randomUUID().toString();
        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

            for (int i = 0; i < surveyQuestionList.size() ; i++)
            {
                Survey.SurveyData.YesNo.Questions surveyQuestions = surveyQuestionList.get(i);

                uploadRequest.addParameter("add_question["+i+"]",surveyQuestions.getQuestion());
                if  (surveyQuestions.getQuestionImageLink() != null && !surveyQuestions.getQuestionImageLink().equals(""))
                    uploadRequest.addFileToUpload(surveyQuestions.getQuestionImageLink(),"question_file["+i+"]");
            }
            uploadRequest.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("create_yes_no_survey","1");
            uploadRequest.addParameter("title",surveyYesNo.getTitle());
            uploadRequest.addParameter("question_type",surveyYesNo.getQuestionType());
            uploadRequest.addParameter("description",surveyYesNo.getDescription());
            uploadRequest.addParameter("num_of_questions", String.valueOf(surveyQuestionList.size()));
           /* uploadRequest.setNotificationConfig(new UploadNotificationConfig());
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
                        listener.onFailed("Connection Error...");

                    }

                    finally {
                        listener.onHideProgress();
                        listener.onSetViewsEnabledOnProgressBarGone();

                    }
                    Log.i(TAG,"file upload response yes/no questions = "+responseString);
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




























    // ******************************************************get all survey*******************************************************//

    @Override
    public void requestSurvey()
    {
        Log.i(TAG,"requestSurvey called");

           Map<String,String> params = new HashMap<>();
           params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
           params.put("get_all_survey_new","1");
           Log.i(TAG,"params" + params);

           remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
               @Override
               public void onVolleySuccessResponse(String response) {


                   Log.i(TAG,"requestSurvey response " +response);

                   try {
                       JSONObject jsonObject = new JSONObject(response);
                       String message = jsonObject.getString("message");
                       if (message.equals("Not Found"))
                       {
                           listener.onShowMessage("Data Not Found");
                       }
                       else
                       {
                           Survey surveyResponse = new Gson().fromJson(response, Survey.class);
                           if (surveyResponse.getSuccess().equals("1"))
                               listener.onReceivedSurvey(surveyResponse.getSurveyData());
                           else
                               listener.onFailed(null);

                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }



               }

               @Override
               public void onVolleyErrorResponse(VolleyError error) {
                   listener.onFailed(null);

               }
           });
    }


















    // ******************************************************edit multiple choice survey*******************************************************//

    @Override
    public void editMultipleChoiceSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice)
    {



        String uuid = UUID.randomUUID().toString();
        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);



            List<Survey.SurveyData.MultipleChoice.Questions> addNewQuestionsWithOptions = new ArrayList<>();
            List<Survey.SurveyData.MultipleChoice.Questions> editQuestionsWithOptions = new ArrayList<>();

           /* for (int i = 0; i < surveyQuestionsList.size(); i++)
            {
                Survey.SurveyData.MultipleChoice.Questions questions = surveyQuestionsList.get(i);
                if (questions.getQuestionId() != null && !questions.getQuestionId().isEmpty()) {
                    editQuestionsWithOptions.add(questions);
                }
                else
                {
                    addNewQuestionsWithOptions.add(questions);
                }

                //subArr[i] = selfReview1.getSubjectID();
            }

            for (int i = 0; i < addNewQuestionsWithOptions.size(); i++)
            {
                Survey.SurveyData.MultipleChoice.Questions surveyQuestions = surveyQuestionsList.get(i);

                uploadRequest.addParameter("question_title["+i+"]",surveyQuestions.getQuestion());

                if  (surveyQuestions.getQuestionImageLink() != null && !surveyQuestions.getQuestionImageLink().equals("")) {
                    if (!Patterns.WEB_URL.matcher(surveyQuestions.getQuestionImageLink()).matches())
                        uploadRequest.addFileToUpload(surveyQuestions.getQuestionImageLink(), "question_file[" + i + "]");
                }

                List<String> optionsList = surveyQuestions.getOptionList();

                for (int j = 0; j < optionsList.size() ; j++)
                {
                    String questionOption = optionsList.get(j);
                    uploadRequest.addParameter("question_option["+i+"]["+j+"]",questionOption);
                    Log.d(TAG,"question_option" + questionOption);

                }
            }


            for (int i = 0; i < editQuestionsWithOptions.size(); i++)
            {
                Survey.SurveyData.MultipleChoice.Questions surveyQuestions = surveyQuestionsList.get(i);

                uploadRequest.addParameter("up_ques["+surveyQuestions.getQuestionId()+"][question_title]",surveyQuestions.getQuestion());
                uploadRequest.addParameter("up_ques["+surveyQuestions.getQuestionId()+"][question_type]","Multi-Choice");

                if  (surveyQuestions.getQuestionImageLink() != null && !surveyQuestions.getQuestionImageLink().equals("")) {
                    if (!Patterns.WEB_URL.matcher(surveyQuestions.getQuestionImageLink()).matches())
                        uploadRequest.addFileToUpload(surveyQuestions.getQuestionImageLink(), "up_ques[" + surveyQuestions.getQuestionId() + "][link]");
                }

                List<String> optionsList = surveyQuestions.getOptionList();

                for (int j = 0; j < optionsList.size() ; j++)
                {
                    String questionOption = optionsList.get(j);
                    uploadRequest.addParameter("up_ques["+surveyQuestions.getQuestionId()+"][question_option]["+j+"]",questionOption);
                    Log.d(TAG,"question_option" + questionOption);

                }
            }*/

            for (int i = 0; i < surveyQuestionsList.size() ; i++)
            {
                Survey.SurveyData.MultipleChoice.Questions surveyQuestions = surveyQuestionsList.get(i);

                uploadRequest.addParameter("question_title["+i+"]",surveyQuestions.getQuestion());

                if  (surveyQuestions.getQuestionImageLink() != null && !surveyQuestions.getQuestionImageLink().equals("")) {
                    if (!Patterns.WEB_URL.matcher(surveyQuestions.getQuestionImageLink()).matches())
                        uploadRequest.addFileToUpload(surveyQuestions.getQuestionImageLink(), "question_file[" + i + "]");
                    else
                    {
                        uploadRequest.addParameter("imagename[" + i + "]",surveyQuestions.getQuestionImage());
                        uploadRequest.addParameter("imageURL[" + i + "]",surveyQuestions.getQuestionImageLink());
                    }
                }
                else
                    {
                        uploadRequest.addParameter("question_file[" + i + "]", "");
                    }

                List<String> optionsList = surveyQuestions.getOptionList();

                for (int j = 0; j < optionsList.size() ; j++)
                {
                    String questionOption = optionsList.get(j);
                    uploadRequest.addParameter("question_option["+i+"]["+j+"]",questionOption);
                    Log.d(TAG,"question_option" + questionOption);

                }
            }

            uploadRequest.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("edit_survey_multiple","1");
            uploadRequest.addParameter("survey_multiple_id",surveyMultipleChoice.getId());
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
                                listener.onSuccess("Survey Updated Successfully.");
                            else
                                listener.onFailed("Failed...");
                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                        listener.onFailed("Connection Error...");

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



















    // ******************************************************edit yes/no survey*******************************************************//


    @Override
    public void editYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionsList, Survey.SurveyData.YesNo surveyYesNo) {
        String uuid = UUID.randomUUID().toString();
        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

            for (int i = 0; i < surveyQuestionsList.size() ; i++)
            {
                Survey.SurveyData.YesNo.Questions surveyQuestions = surveyQuestionsList.get(i);

                uploadRequest.addParameter("add_question["+i+"]",surveyQuestions.getQuestion());

                if  (surveyQuestions.getQuestionImageLink() != null && !surveyQuestions.getQuestionImageLink().equals("")) {
                    if (!Patterns.WEB_URL.matcher(surveyQuestions.getQuestionImageLink()).matches())
                        uploadRequest.addFileToUpload(surveyQuestions.getQuestionImageLink(), "question_file[" + i + "]");
                    else
                    {
                        uploadRequest.addParameter("imagename[" + i + "]",surveyQuestions.getFilename());
                        uploadRequest.addParameter("imageURL[" + i + "]",surveyQuestions.getQuestionImageLink());
                    }
                }
                else
                {
                    uploadRequest.addParameter("question_file[" + i + "]","");
                }
            }
            uploadRequest.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("edit_yes_no_survey","1");
            uploadRequest.addParameter("survey_id",surveyYesNo.getId());
            uploadRequest.addParameter("title",surveyYesNo.getTitle());
            uploadRequest.addParameter("question_type",surveyYesNo.getQuestionType());
            uploadRequest.addParameter("description",surveyYesNo.getDescription());
            uploadRequest.addParameter("num_of_questions", String.valueOf(surveyQuestionsList.size()));
           /* uploadRequest.setNotificationConfig(new UploadNotificationConfig());
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
                    Log.d(TAG,"editYesNoSurvey() method error = "+exception.getMessage());
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
                               listener.onSuccess("Survey Updated Successfully..");
                            else
                                listener.onFailed("Failed...");

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                        listener.onFailed("Connection Error...");

                    }

                    finally {
                        listener.onHideProgress();
                        listener.onSetViewsEnabledOnProgressBarGone();
                    }
                    Log.i(TAG,"file upload response editYesNoSurvey yes/no questions = "+responseString);
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
