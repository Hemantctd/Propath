package org.ctdworld.propath.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.activity.ActivityNutrition;
import org.ctdworld.propath.contract.ContractNutrition;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Nutrition;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InteractorNutrition implements ContractNutrition.Interactor {

    private static final String TAG = InteractorNutrition.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context mContext;


    public InteractorNutrition(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;

    }

    @Override
    public void likeDislike(final int position, final Nutrition nutrition) {
        Map<String, String> params = new HashMap<>();
        params.put("add_like_nutrition","1");
        params.put("user_id",nutrition.getUserId());
        params.put("post_id",nutrition.getPost_id());
        params.put("status",nutrition.getLike());


        Log.d(TAG,"params : " +params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message)
            {


                try
                {
                    JSONObject jsonObject = new JSONObject(message.trim());
                    if ("1".equals(jsonObject.getString("res"))) {
                        Log.i(TAG,"likeDislike method called = "+message);

                        // # it's updated in adapter
                   /*     if (nutrition.getLike().equals("1"))
                            nutrition.setLike_count(String.valueOf(Integer.valueOf(nutrition.getLike_count())+1));
                        else
                            nutrition.setLike_count(String.valueOf(Integer.valueOf(nutrition.getLike_count())-1));


                        listener.onLikeDislike(position,nutrition);*/
                    }


                }
                catch (JSONException e) {

                    e.printStackTrace();
                   listener.onHideProgress();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void postNutrition(Nutrition nutritionData)
    {

        String uuid = UUID.randomUUID().toString();
        Log.i(TAG, "filePath = " + nutritionData.getFile());

        try {

            Log.d(TAG,"comment_status" +nutritionData.getCommentStatus());
            Log.d(TAG,"file_image" +nutritionData.getFile());
            Log.d(TAG,"title" +nutritionData.getTitle());

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

//                if(nutritionData.getFile() != null && !nutritionData.getFile().isEmpty())
                uploadRequest.addParameter("file",nutritionData.getFile());
                uploadRequest.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
                uploadRequest.addParameter("add_nutrition_feed","1");
                uploadRequest.addParameter("title",nutritionData.getTitle());
                uploadRequest.addParameter("comment_status",nutritionData.getCommentStatus());

            uploadRequest.setNotificationConfig(new UploadNotificationConfig());
                uploadRequest.setMaxRetries(3);
            uploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {

                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    Log.d(TAG,"postNutrition() method error = "+exception.getMessage());
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

                        if (objectData != null) {
                            if ("1".equals(objectData.getString("res"))) {

                                DialogHelper.showSimpleCustomDialog(mContext, objectData.getString("result"), new DialogHelper.ShowSimpleDialogListener() {
                                    @Override
                                    public void onOkClicked() {

                                        mContext.startActivity(new Intent(mContext, ActivityNutrition.class));
                                    }
                                });

                            }
                        }
                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    finally {
                        listener.onHideProgress();
                        listener.onSetViewsEnabledOnProgressBarGone();
                    }

                    Log.d(TAG,"file upload response = "+responseString);
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



    @Override
    public void viewNutrition()
    {
        Map<String, String> params = new HashMap<>();
        params.put("view_nutrition_feed","1");
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.d(TAG,"response : " +message);

                try {
                    final JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res")))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                       // JSONObject objectData = jsonArray.getJSONObject(0);
                        parseNutritionJsonAndUpdateUi(jsonArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
        });


    }

    @Override
    public void viewNutritionComments(String post_id) {

        Map<String, String> params = new HashMap<>();
        params.put("view_comment_nutrition","1");
        params.put("post_id", post_id);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {
                Log.d(TAG,"response : " +message);

                try {
                    final JSONObject jsonObject = new JSONObject(message);
                    if ("1".equals(jsonObject.getString("res")))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        parseNutritionCommentsJsonAndUpdateUi(jsonArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


    }

    @Override
    public void postComments(Nutrition nutritionData) {
        Map<String, String> params = new HashMap<>();
        params.put("add_comment_nutrition","1");
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("comment", nutritionData.getComments());
        params.put("post_id", nutritionData.getPost_id());
        Log.d(TAG,"params : " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String message) {

                Log.d(TAG,"response postComments : " + message);

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.d(TAG,"response postComments error : " + error);

            }
        });

    }


    private void parseNutritionCommentsJsonAndUpdateUi(JSONArray jsonArray) {
        List<Nutrition> nutritionList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Nutrition nutritionData = new Nutrition();
                nutritionData.setComments(jsonObject.getString("comment"));
                nutritionData.setComments_id(jsonObject.getString("id"));
                nutritionData.setComment_by_user_id(jsonObject.getString("user_id"));
                nutritionData.setPost_id(jsonObject.getString("post_id"));
                nutritionData.setComment_by_user_name(jsonObject.getString("name"));
                nutritionData.setComment_date(jsonObject.getString("date_time"));
                nutritionData.setComment_by_image(jsonObject.getString("profile_image"));

                nutritionList.add(nutritionData);

               Log.d(TAG,"COMMENTS :" +nutritionData.getComments());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listener.onGetReceivedNutritionComments(nutritionList);


        }

    }



    // this method parses json and passes data to activity to update ui
    private void parseNutritionJsonAndUpdateUi(JSONArray jsonArray) {
        List<Nutrition> nutritionList = new ArrayList<>();

        for (int i = 0 ; i < jsonArray.length() ; i++)
        {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                   Nutrition nutritionData = new Nutrition();
                   nutritionData.setMedia_url(jsonObject.getString("media_url"));
                   nutritionData.setPost_id(jsonObject.getString("id"));
                   nutritionData.setUserId(jsonObject.getString("user_id"));
                   nutritionData.setUser_name(jsonObject.getString("user_name"));
                   nutritionData.setCommentStatus(jsonObject.getString("comment_status"));
                   nutritionData.setDate_time(jsonObject.getString("date_time"));
                   nutritionData.setTitle(jsonObject.getString("title"));
                   nutritionData.setLike(jsonObject.getString("like"));
                   nutritionData.setLike_count(jsonObject.getString("like_count"));
                   nutritionData.setPost_share_by(jsonObject.getString("post_share_by"));
                   //nutritionData.setShare_with(jsonObject.getString("share_with"));
                   nutritionData.setPost_share_text(jsonObject.getString("post_share_text"));
                   nutritionData.setStatus(jsonObject.getString("status"));
                   nutritionData.setComment_count(jsonObject.getString("comment_count"));
                   nutritionData.setUser_name(jsonObject.getString("user_name"));
                   nutritionData.setUser_profile_image(jsonObject.getString("user_profile_image"));

                nutritionList.add(nutritionData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listener.onGetReceivedNutritionData(nutritionList);

    }


}


