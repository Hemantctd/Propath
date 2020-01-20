package org.ctdworld.propath.interactor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.activity.ActivityNutrition;
import org.ctdworld.propath.contract.ContractShareNutrition;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Nutrition;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class InteractorShareNutrition implements ContractShareNutrition.Interactor {


    private static final String TAG = InteractorShareNutrition.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    Context mContext;

    String messages = "";



    public InteractorShareNutrition(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;
    }

    @Override
    public void shareNutrition(Nutrition nutritionData, String groupId) {
        String uuid = UUID.randomUUID().toString();
//        Log.i(TAG, "filePath = " + nutritionData.getFile());

        try {

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(mContext, uuid, RemoteServer.URL);

            uploadRequest.addParameter("group_id[]", groupId);
            uploadRequest.addParameter("post_share_by", SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadRequest.addParameter("share_nutrition_feed","1");
            uploadRequest.addParameter("post_id",nutritionData.getPost_id());
            uploadRequest.addParameter("comment_status","1");
            uploadRequest.addParameter("share_text",nutritionData.getTitle());

            uploadRequest.setNotificationConfig(new UploadNotificationConfig());
            uploadRequest.setMaxRetries(3);
            uploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {

                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    Log.d(TAG,"shareNutrition() method error = "+exception.getMessage());
                    exception.printStackTrace();
                    listener.onHideProgress();
                    listener.onShowMessage("Connection Error");
                    listener.onSetViewsEnabledOnProgressBarGone();
                }

                @Override
                public void onCompleted(final Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String responseString = serverResponse.getBodyAsString();

                    try {
                        JSONObject objectData = new JSONObject(responseString);

                        if (objectData != null) {
                            if ("1".equals(objectData.getString("res"))) {

                                DialogHelper.showSimpleCustomDialog(mContext, objectData.getString("result"), new DialogHelper.ShowSimpleDialogListener() {
                                    @Override
                                    public void onOkClicked() {

                                        mContext.startActivity(new Intent(context, ActivityNutrition.class));
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
}


