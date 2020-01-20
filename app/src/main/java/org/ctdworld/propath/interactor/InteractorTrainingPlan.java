package org.ctdworld.propath.interactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.base.BaseOnFinishedListener;
import org.ctdworld.propath.contract.ContractTrainingPlan;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.model.Response;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


// this interactor will be used for view, create and edit page
public class
InteractorTrainingPlan implements ContractTrainingPlan.Interactor {
    private static final String TAG = InteractorTrainingPlan.class.getSimpleName();
    private ContractTrainingPlan.Interactor.OnFinishedListener mListenerPlan;
    private ContractTrainingPlan.Interactor.OnFinishedPlanItemListener mListenerPlanItem;
    private Context mContext;
    private RemoteServer mRemoteServer;


    public InteractorTrainingPlan(BaseOnFinishedListener listener, Context context) {
        if (listener instanceof ContractTrainingPlan.Interactor.OnFinishedListener) {
            Log.i(TAG, "BaseOnFinishedListener is type of OnFinishedListener");
            this.mListenerPlan = (OnFinishedListener) listener;
        }

        if (listener instanceof ContractTrainingPlan.Interactor.OnFinishedPlanItemListener) {
            Log.i(TAG, "BaseOnFinishedListener is type of OnFinishedPlanItemListener");
            mListenerPlanItem = (OnFinishedPlanItemListener) listener;
        }

        this.mContext = context;
        this.mRemoteServer = new RemoteServer(context);
    }


    // creating new training plan
    @Override
    public void createTrainingPlan(final TrainingPlan.PlanData trainingPlanData) {
        Log.i(TAG, "createTrainingPlan() method called");
        Log.i(TAG, "title = " + trainingPlanData.getTitle());
        Log.i(TAG, "description = " + trainingPlanData.getDescription());
        //   Log.i(TAG,"item list size = "+trainingPlanData.getPlanItemArrayList().size());

        try {

            String uuid = UUID.randomUUID().toString();
            MultipartUploadRequest requestUpload = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);
            requestUpload.addParameter("create_training_plan_new", "1");
            requestUpload.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
            requestUpload.addParameter("plan_title", trainingPlanData.getTitle());
            requestUpload.addParameter("plan_description", trainingPlanData.getDescription());
            requestUpload.addParameter("num_of_media", String.valueOf(trainingPlanData.getPlanItemList().size()));

            // adding plan item
            if (trainingPlanData.getPlanItemList() != null) {
                // # adding plan item title
                for (int i = 0; i < trainingPlanData.getPlanItemList().size(); i++) {
                    TrainingPlan.PlanData.PlanItem planItem = trainingPlanData.getPlanItemList().get(i);

                    Log.i(TAG, "item title = " + planItem.getTitle());
                    requestUpload.addParameter("item_title[" + i + "]", planItem.getTitle());

                    Log.i(TAG, "link = " + planItem.getLink());
                    requestUpload.addParameter("youtube_link[" + i + "]", planItem.getLink());

                    Log.i(TAG, "item media type = " + planItem.getMediaType());
                    if (planItem.getMediaType() != null)
                        requestUpload.addParameter("mediatype[" + i + "]", planItem.getMediaType());

                    Log.i(TAG, "item media url = " + planItem.getMediaUrl());
                    if (planItem.getMediaUrl() != null && !planItem.getMediaUrl().isEmpty())
                        requestUpload.addFileToUpload(planItem.getMediaUrl(), "mediafile[" + i + "]");
                }

            }


            requestUpload.setNotificationConfig(new UploadNotificationConfig());
            requestUpload.setMaxRetries(3);
            requestUpload.setDelegate(new UploadStatusDelegate() {


                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "editing training plan , progress = " + uploadInfo.getProgressPercent());
                    uploadInfo.describeContents();
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    if (mListenerPlan != null)
                        mListenerPlan.onFailed("Creating training plan failed");
                    exception.printStackTrace();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String response = serverResponse.getBodyAsString();
                    Log.i(TAG, "creating training plan completed , response = " + response);

                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if ("1".equalsIgnoreCase(jsonObject.getString("res"))) {
                                JSONObject jsonResult = jsonObject.getJSONObject("result");
                                trainingPlanData.setId(jsonResult.getString("training_id")); // # adding title id
                                if (mListenerPlan != null)
                                    mListenerPlan.onTrainingPlanCreated(trainingPlanData);
                            } else if (mListenerPlan != null)
                                mListenerPlan.onFailed("Creating training plan failed");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (mListenerPlan != null)
                                mListenerPlan.onFailed("Creating training plan failed");
                        }

                    }

                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "creating training plan cancelled , progress = " + uploadInfo.getProgressPercent());
                    // DialogHelper.showSimpleCustomDialog(mContext,"Cancelled...");
                }
            });

            // # uploading data
            requestUpload.startUpload();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException in createTrainingPlan() method = " + e.getMessage());
            e.printStackTrace();
            if (mListenerPlan != null)
                mListenerPlan.onFailed(null);

        } catch (MalformedURLException e) {
            if (mListenerPlan != null)
                mListenerPlan.onFailed(null);
            Log.e(TAG, "MalformedURLException in createTrainingPlan() method = " + e.getMessage());
            e.printStackTrace();
        }
    }


    // this method edits whole plan with items
    @Override
    public void editTrainingPlan(final TrainingPlan.PlanData trainingPlanData, ArrayList<TrainingPlan.PlanData.PlanItem> editItems, ArrayList<TrainingPlan.PlanData.PlanItem> deleteItems, ArrayList<TrainingPlan.PlanData.PlanItem> addNewItems) {
        Log.i(TAG, "editTrainingPlan() method called");
        Log.i(TAG, "plan_id = " + trainingPlanData.getId());
        try {
            String uuid = UUID.randomUUID().toString();
            MultipartUploadRequest requestUpload = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);
            requestUpload.addParameter("edit_training_plan_whole", "1");
            requestUpload.addParameter("trainingplan_id", trainingPlanData.getId());
            requestUpload.addParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
            requestUpload.addParameter("edit_plan_title", trainingPlanData.getTitle());
            requestUpload.addParameter("edit_plan_description", trainingPlanData.getDescription());

            // # adding new plan items
            if (addNewItems != null)
                for (int i = 0; i < addNewItems.size(); i++) {
                    TrainingPlan.PlanData.PlanItem planItem = addNewItems.get(i);

                    requestUpload.addParameter("add_item_title[" + i + "]", planItem.getTitle());

                    if (planItem.getLink() != null && !planItem.getLink().isEmpty())
                        requestUpload.addParameter("add_youtube_link[" + i + "]", planItem.getLink());
                    else
                        requestUpload.addParameter("add_youtube_link[" + i + "]", "");

                    if (planItem.getMediaType() != null && !planItem.getMediaType().isEmpty())
                        requestUpload.addParameter("add_mediatype[" + i + "]", planItem.getMediaType());
                    else
                        requestUpload.addParameter("add_mediatype[" + i + "]", "");

                    if (planItem.getMediaUrl() != null && !planItem.getMediaUrl().isEmpty() && !Patterns.WEB_URL.matcher(planItem.getMediaUrl()).matches())
                        requestUpload.addFileToUpload(planItem.getMediaUrl(), "add_mediafile[" + i + "]");

                }

            // # editing existing plan items
            if (editItems != null)
                for (int i = 0; i < editItems.size(); i++) {
                    TrainingPlan.PlanData.PlanItem editItem = editItems.get(i);

                    Log.i(TAG, "edit item id = " + editItem.getId());
                    requestUpload.addParameter("edit_item_id[" + i + "]", editItem.getId());

                    Log.i(TAG, "edit item title = " + editItem.getTitle());
                    requestUpload.addParameter("edit_item_title[" + i + "]", editItem.getTitle());


                    if (editItem.getLink() == null || editItem.getLink().isEmpty())
                        requestUpload.addParameter("edit_youtube_link[" + i + "]", "");
                    else
                        requestUpload.addParameter("edit_youtube_link[" + i + "]", editItem.getLink());


                    if (editItem.getMediaType() == null || editItem.getMediaType().isEmpty())
                        requestUpload.addParameter("edit_file_type[" + i + "]", "");
                    else
                        requestUpload.addParameter("edit_file_type[" + i + "]", editItem.getMediaType());

                    String mediaUrl = editItem.getMediaUrl();
                    if (mediaUrl != null && !mediaUrl.isEmpty() && !Patterns.WEB_URL.matcher(mediaUrl).matches())
                        requestUpload.addFileToUpload(editItem.getMediaUrl(), "edit_file_path[" + i + "]");

                }

            // # deleting existing plan items
            if (deleteItems != null)
                for (int i = 0; i < deleteItems.size(); i++) {
                    TrainingPlan.PlanData.PlanItem planItem = deleteItems.get(i);

                    Log.i(TAG, "delete item id = " + planItem.getId());
                    // requestUpload.addParameter("delete["+i+"]", planItem.getTitle());
                    requestUpload.addParameter("delete_item_id[" + i + "]", planItem.getId());

               /* Log.i(TAG,"edit item title = "+planItem.getTitle());
                requestUpload.addParameter("["+i+"]", planItem.getTitle());

                Log.i(TAG,"edit item media type = "+planItem.getMediaType());
                requestUpload.addParameter("["+i+"]", planItem.getMediaType());

                Log.i(TAG,"edit item media url = "+planItem.getMediaUrl());
                requestUpload.addFileToUpload(planItem.getMediaUrl(),"["+i+"]");*/
                }


            requestUpload.setNotificationConfig(new UploadNotificationConfig());
            requestUpload.setMaxRetries(3);
            requestUpload.setDelegate(new UploadStatusDelegate() {


                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "editing training plan , progress = " + uploadInfo.getProgressPercent());
                    uploadInfo.describeContents();
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    if (mListenerPlan != null)
                        mListenerPlan.onFailed("editing training plan failed");
                    exception.printStackTrace();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String response = serverResponse.getBodyAsString();
                    Log.i(TAG, "creating training plan completed , response = " + response);

                    if (response != null) {
                        try {
                            TrainingPlan responseTrainPlan = new Gson().fromJson(response, TrainingPlan.class);
                            if (responseTrainPlan != null && Response.KEY_SUCCESS_STATUS.equals(responseTrainPlan.getResponseStatus(responseTrainPlan))) {
                                List<TrainingPlan.PlanData> planDataList = responseTrainPlan.getPlanDataList();
                                if (mListenerPlan != null && planDataList != null && planDataList.size() != 0)
                                    mListenerPlan.onTrainingPlanEdited(responseTrainPlan.getPlanDataList().get(0));
                                else if (mListenerPlan != null)
                                    mListenerPlan.onFailed("Creating training plan failed");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "creating training plan cancelled , progress = " + uploadInfo.getProgressPercent());
                    // DialogHelper.showSimpleCustomDialog(mContext,"Cancelled...");
                }
            });

            // # uploading data
            requestUpload.startUpload();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException in editTrainingPlan() method = " + e.getMessage());
            e.printStackTrace();
            if (mListenerPlan != null)
                mListenerPlan.onFailed(null);

        } catch (MalformedURLException e) {
            if (mListenerPlan != null)
                mListenerPlan.onFailed(null);
            Log.e(TAG, "MalformedURLException in editTrainingPlan() method = " + e.getMessage());
            e.printStackTrace();
        }
    }


    // to get training plan list
    @Override
    public void getTrainingPlanList(final TrainingPlan.PlanData trainingPlanData) {

        Uri.Builder uriBuilder = RemoteServer.getUriBuilder();
      /*  mUriBuilder = new Uri.Builder();
        mUriBuilder.scheme("https");
        mUriBuilder.authority("ctdworld.co"); // adding base URL
        mUriBuilder.path("/athlete/jsondata.php");*/

        uriBuilder.appendQueryParameter("list_trainingplan", "1");
        uriBuilder.appendQueryParameter("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());


       /* Map<String, String> params = new HashMap<>();
        params.put("get_all_trainingplan","1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());

        Log.i(TAG,"params to get all training plans = "+params);*/

        mRemoteServer.sendData(uriBuilder.build().toString(), new HashMap(), new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "server message to get all training plan list , " + message);
                Gson gson = new Gson();
                try {
                    TrainingPlan planTesting = gson.fromJson(message, TrainingPlan.class);
                    mListenerPlan.onTrainingPlanReceived(planTesting);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.e(TAG, "error while while getting training plan list , " + error.getMessage());
                if (mListenerPlan != null)
                    mListenerPlan.onFailed("Some error occurred...");
                error.printStackTrace();
            }
        });
    }


    @Override
    public void deleteTrainingPlan(final ArrayList<TrainingPlan.PlanData> trainingPlanData) {
        if (trainingPlanData == null) {
            Log.e(TAG, "trainingPlanData is null in deleteTrainingPlan() method");
            return;
        }

        final List<String> ids = new ArrayList<>();
        for (TrainingPlan.PlanData planData : trainingPlanData) {
            ids.add(planData.getId());
        }

        Map<String, String> params = new HashMap<>();
        params.put("delete_trainingplan", "1");
        params.put("training_plan_id[]", ids.toString());

        Log.i(TAG, "params to delete training plans = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "server message to delete training plans , " + message);
                try {
                    final JSONObject jsonObject = new JSONObject(message);

                    if ("1".equals(jsonObject.getString("success"))) {
                        for (TrainingPlan.PlanData trainingPlanData : trainingPlanData) {
                            if (mListenerPlan != null)
                                mListenerPlan.onTrainingPlanDeleted(trainingPlanData);
                        }
                    } else if (mListenerPlan != null)
                        mListenerPlan.onFailed(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mListenerPlan != null)
                        mListenerPlan.onFailed("Some error occurred");
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                Log.e(TAG, "error while creating category , " + error.getMessage());
                if (mListenerPlan != null)
                    mListenerPlan.onFailed("Editing training plan failed");
                error.printStackTrace();
            }
        });
    }


    @Override
    public void shareTrainingPlan(final TrainingPlan.PlanData trainingPlanData, String[] groupIdArray, String[] userIdArray) {


        if (trainingPlanData == null) {
            Log.e(TAG, "trainingPlanData is null in shareTrainingPlan() method");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("share_trainingplan", "1");
        params.put("training_plan_id", trainingPlanData.getId());
        params.put("group_id[]", Arrays.toString(groupIdArray));
        params.put("user_id[]", Arrays.toString(userIdArray));

        Log.i(TAG, "params to share training plan = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "remote message to share training plan  = " + message);
                try {
                    if (message == null)
                        return;

                    JSONObject jsonObject = new JSONObject(message);
                    if (mListenerPlan != null) {
                        if ("1".equalsIgnoreCase(jsonObject.getString("success")))
                            mListenerPlan.onTrainingPlanShared();
                        else
                            mListenerPlan.onFailed("Sharing training plan failed");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mListenerPlan != null)
                        mListenerPlan.onFailed("Sharing training plan failed");
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                if (mListenerPlan != null)
                    mListenerPlan.onFailed("Sharing training plan failed");
                error.printStackTrace();
                Log.e(TAG, "error while sharing training plan");
            }
        });
    }


    // this method edits only plan item
    @Override
    public void editTrainingPlanItem(final String planId, final TrainingPlan.PlanData.PlanItem planItem) {
        Log.i(TAG, "editTrainingPlanItem() method called");

        if (planId == null || planItem == null) {
            Log.e(TAG, "planId or planItem is null in editTrainingPlan() method, returning...");
            return;
        }
        Log.i(TAG, "plan item id = " + planItem.getId());
        Log.i(TAG, "item media url = " + planItem.getMediaUrl());
        Log.i(TAG, "item youtube link = " + planItem.getLink());

        try {
            String uuid = UUID.randomUUID().toString();
            MultipartUploadRequest requestUpload = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);
            requestUpload.addParameter("edit_training_item", "1");
            requestUpload.addParameter("item_id", planItem.getId());
            requestUpload.addParameter("title", planItem.getTitle());

            if (planItem.getMediaType() != null && !planItem.getMediaType().isEmpty())
                requestUpload.addParameter("file_type", planItem.getMediaType());
            else
                requestUpload.addParameter("file_type", "");


            if (planItem.getLink() != null && !planItem.getLink().isEmpty())
                requestUpload.addParameter("youtube_link", planItem.getLink());
            else
                requestUpload.addParameter("youtube_link", "");

            if (planItem.getMediaUrl() != null && !planItem.getMediaUrl().isEmpty() && !Patterns.WEB_URL.matcher(planItem.getMediaUrl()).matches())
                requestUpload.addFileToUpload(planItem.getMediaUrl(), "file_path");


            requestUpload.setNotificationConfig(new UploadNotificationConfig());
            requestUpload.setMaxRetries(3);
            requestUpload.setDelegate(new UploadStatusDelegate() {


                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "editing training plan item, progress = " + uploadInfo.getProgressPercent());
                    uploadInfo.describeContents();
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    if (mListenerPlanItem != null)
                        mListenerPlanItem.onFailedPlanItem("editing training plan item failed");
                    exception.printStackTrace();
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String response = serverResponse.getBodyAsString();
                    Log.i(TAG, "editing training plan item completed , response = " + response);
                    if (response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if ("1".equalsIgnoreCase(jsonObject.getString("success"))) {
                                Log.i(TAG, "calling onTrainingPlanItemEdited() method");
                                if (mListenerPlanItem != null)
                                    mListenerPlanItem.onTrainingPlanItemEdited(planItem);
                            } else {
                                if (mListenerPlanItem != null)
                                    mListenerPlanItem.onFailedPlanItem("Editing training plan item failed");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (mListenerPlanItem != null)
                                mListenerPlanItem.onFailedPlanItem("Editing training plan item failed");
                        }

                    }

                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Log.i(TAG, "editing training plan item cancelled , progress = " + uploadInfo.getProgressPercent());
                }
            });

            // # uploading data
            requestUpload.startUpload();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException in editTrainingPlanItem() method = " + e.getMessage());
            e.printStackTrace();
            if (mListenerPlanItem != null)
                mListenerPlanItem.onFailedPlanItem(null);

        } catch (MalformedURLException e) {
            if (mListenerPlanItem != null)
                mListenerPlanItem.onFailedPlanItem(null);
            Log.e(TAG, "MalformedURLException in editTrainingPlanItem() method = " + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            if (mListenerPlanItem != null)
                mListenerPlanItem.onFailedPlanItem(null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            if (mListenerPlanItem != null)
                mListenerPlanItem.onFailedPlanItem(null);
        }
    }


    @Override
    public void deleteTrainingPlanItem(String planId, final TrainingPlan.PlanData.PlanItem planItem) {
        if (planItem == null) {
            Log.e(TAG, "planItem is null in deleteTrainingPlanItem() method");
            return;
        }


        Map<String, String> params = new HashMap<>();
        params.put("delete_training_plan_new_item_rn", "1");
        params.put("training_plan_id", planId);
        params.put("item_id", planItem.getId());

        Log.i(TAG, "params to delete training plan item = " + params);

        mRemoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(final String message) {
                Log.i(TAG, "server message to delete training plan item , " + message);
                try {
                    final JSONObject jsonObject = new JSONObject(message);

                    if ("1".equals(jsonObject.getString("success"))) {
                        if (mListenerPlanItem != null)
                            mListenerPlanItem.onTrainingPlanItemDeleted(planItem);
                    } else if (mListenerPlanItem != null)
                        mListenerPlanItem.onFailedPlanItem("Failed...");
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mListenerPlanItem != null)
                        mListenerPlanItem.onFailedPlanItem("Some error occurred");
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                if (error == null)
                    return;

                Log.e(TAG, "error while deleting training plan item , " + error.getMessage());
                if (mListenerPlanItem != null)
                    mListenerPlanItem.onFailedPlanItem("Deleting training plan item failed");
                error.printStackTrace();
            }
        });

    }


}
