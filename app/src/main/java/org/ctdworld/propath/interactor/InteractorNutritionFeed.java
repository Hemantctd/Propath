package org.ctdworld.propath.interactor;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractNutritionFeed;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.NutritionFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractorNutritionFeed implements ContractNutritionFeed.Interactor {
    private static final String TAG = InteractorNutritionFeed.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context mContext;


    //Constructor
    public InteractorNutritionFeed(OnFinishedListener onFinishedListener, Context context) {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;
    }


    // create new post
    @Override
    public void createPost(final NutritionFeed.PostData postData) {
        try {
            if (postData == null)
                return;

            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, RemoteServer.URL);

            if (postData.getPostMediaUrl() != null && !postData.getPostMediaUrl().isEmpty())
                multipartUploadRequest.addFileToUpload(postData.getPostMediaUrl(), "file"); //adding media

            multipartUploadRequest.addParameter("group_id[]", Arrays.toString(new String[]{postData.getGroupID()}));
            multipartUploadRequest.addParameter("add_nutrition_feed", "1");
            multipartUploadRequest.addParameter("title", postData.getPostMessage()); //post message
            multipartUploadRequest.addParameter("user_id", SessionHelper.getUserId(mContext));
            multipartUploadRequest.addParameter("comment_status", String.valueOf(postData.getPostCommentPermission()));


            multipartUploadRequest.setNotificationConfig(new UploadNotificationConfig());
            multipartUploadRequest.setMaxRetries(RemoteServer.MAX_REQUEST_TRY);
            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    DialogHelper.showSimpleCustomDialog(mContext, "Failed...");
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    String response = serverResponse.getBodyAsString();
                    try {
                        JSONObject serverResponse1 = new JSONObject(response);
                        if (serverResponse1.get("res").toString().equals("1")) {
                            listener.onPostCreated(postData);
                        } else
                            listener.onFailed(NutritionFeed.FAILED_CREATING_POST, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    Toast.makeText(mContext, "Cancelled...", Toast.LENGTH_SHORT).show();

                }
            })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            listener.onFailed(NutritionFeed.FAILED_CREATING_POST, null);
            //Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    // saving edited post
    @Override
    public void editPost(final NutritionFeed.PostData postData) {
        int isImageBeingPosted = 0; // default value if image is not being posted
        try {
            String media = postData.getPostMediaUrl();

            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, RemoteServer.URL);

            if (media != null && !media.isEmpty() && !Patterns.WEB_URL.matcher(media).matches())
                multipartUploadRequest.addFileToUpload(postData.getPostMediaUrl(), "file"); //Adding file

            multipartUploadRequest.addParameter("title", postData.getPostMessage()); // adding post message
            multipartUploadRequest.addParameter("edit_nutrition_feed", "1");
            multipartUploadRequest.addParameter("comment_status", "1");
            multipartUploadRequest.addParameter("post_id", postData.getPostId());
            multipartUploadRequest.addParameter("image_status", String.valueOf(isImageBeingPosted));

            multipartUploadRequest.setNotificationConfig(new UploadNotificationConfig());
            multipartUploadRequest.setMaxRetries(2);
            multipartUploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                    listener.onFailed(NutritionFeed.FAILED_EDITING_POST, mContext.getString(R.string.error_occurred));
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                    String response = serverResponse.getBodyAsString();
                    Log.d(TAG, "server response to edit nutrition feed post : " + response);
                    try {
                        JSONObject serverResponse1 = new JSONObject(response);
                        if (serverResponse1.get("res").toString().equals("1")) {
                            listener.onPostEdited(postData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailed(NutritionFeed.FAILED_EDITING_POST, mContext.getString(R.string.error_occurred));
                    }
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    listener.onFailed(NutritionFeed.FAILED_EDITING_POST, mContext.getString(R.string.canceled));
                }
            });
            multipartUploadRequest.startUpload(); //Starting the upload


        } catch (Exception exc) {
            listener.onFailed(NutritionFeed.FAILED_EDITING_POST, mContext.getString(R.string.error_occurred));
            Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    // requesting to get post data list from server
    @Override
    public void requestPostDataList(String userId) {
        if (userId == null || listener == null || mContext == null)
            return;

        Map<String, String> params = new HashMap<>();
        params.put("view_nutrition_feed", "1");
        params.put("user_id", userId);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG, "response to get nutrition feed list = " + response);

                try {
                    Gson gson = new Gson();
                    NutritionFeed nutritionFeed = gson.fromJson(response, NutritionFeed.class);
                    if (nutritionFeed != null && nutritionFeed.getStatus() != null && nutritionFeed.getStatus().equals("1")) {
                        listener.onPostListReceived(nutritionFeed.getListPostData());
                    } else
                        listener.onFailed(NutritionFeed.FAILED_RECEIVING_POST_LIST, null);
                } catch (Exception e) {
                    listener.onFailed(NutritionFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_occurred));
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NutritionFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_connecting_to_server));
            }
        });
    }


    @Override
    public void updateLikeOnPost(final NutritionFeed.PostData postData) {

        Map<String, String> params = new HashMap<>();
        params.put("add_like_nutrition", "1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("post_id", postData.getPostId());
        params.put("status", postData.getLikeStatus());
        Log.d(TAG, "params to update like  : " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.get("res").toString().equals("1")) {
                        /*if (postData.getLikeStatus().equals(NewsFeed.POST_LIKED))
                            postData.setPostLikeCount(String.valueOf(Integer.parseInt(postData.getPostLikeCount())+1));
                        else
                            postData.setPostLikeCount(String.valueOf(Integer.parseInt(postData.getPostLikeCount())-1));
*/
                        // listener.onPostLikeUpdated(postData);  // updating post
                    } else
                        listener.onFailed(NutritionFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_occurred));
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailed(NutritionFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NutritionFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_connecting_to_server));
            }
        });
    }


    @Override
    public void requestPostComments(NutritionFeed.PostData.PostComment postComment) {
        Map<String, String> params = new HashMap<>();
        params.put("view_comment_nutrition", "1");
        params.put("post_id", postComment.getPostId());

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG, "response to get nutrition feed comment list = " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("res").toString().equals("1")) {
                        Gson gson = new Gson();
                        NutritionFeed.PostData responsePost = gson.fromJson(response, NutritionFeed.PostData.class);

                        if (responsePost != null)
                            listener.onPostCommentsReceived(responsePost.getPostCommentList());
                    } else
                        listener.onFailed(NutritionFeed.FAILED_GETTING_COMMENTS, null);

                } catch (Exception e) {
                    listener.onFailed(NutritionFeed.FAILED_GETTING_COMMENTS, null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NutritionFeed.FAILED_GETTING_COMMENTS, null);
            }
        });
    }


    // adding new comment on post
    @Override
    public void addPostComment(final NutritionFeed.PostData.PostComment postComment) {
        Log.i(TAG, "adding new comment on nutrition feed post");
        Map<String, String> params = new HashMap<>();
        params.put("add_comment_nutrition", "1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("post_id", postComment.getPostId());
        params.put("comment", postComment.getCommentMessage());
        Log.d(TAG, "params to add comment in nutrition feed post : " + params);


        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                try {
                    Log.i(TAG, "server response to add comment on nutrition feed post = " + response);
                    JSONObject obj = new JSONObject(response);
                    if (obj.get("res").toString().equals("1")) {

                        if (listener != null) {
                            listener.onPostCommentAdded(postComment);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

            }
        });


    }


    @Override
    public void editPostComment(final NutritionFeed.PostData.PostComment postComment) {
        Map<String, String> params = new HashMap<>();
        params.put("edit_comment_nutrition", "1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("post_id", postComment.getPostId());
        params.put("comment_id", postComment.getCommentId());
        params.put("comment", postComment.getCommentMessage());
        Log.d(TAG, "params to edit nutrition feed comment  : " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG, "Server response edit nutrition feed post comment " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.get("res").toString().equals("1")) {
                        System.out.println("comment updated successfully , response = " + response);
                        listener.onPostCommentEdited(postComment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

            }
        });
    }


    @Override
    public void deletePostComment(final NutritionFeed.PostData.PostComment postComment) {
        Map<String, String> params = new HashMap<>();
        params.put("delete_comment_nutrition", "1");
        params.put("comment_id", postComment.getCommentId());
        Log.d(TAG, "params to delete post comment : " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG, "Server response edit nutrition feed post comment " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.get("res").toString().equals("1")) {
                        listener.onPostCommentDeleted(postComment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {

            }
        });
    }


    @Override
    public void sharePost(final NutritionFeed.PostData postData) {

        Map<String, String> params = new HashMap<>();
        params.put("share_nutrition_feed", "1");
        params.put("post_id", postData.getPostId());
        params.put("post_share_by", postData.getPostSharedBy());
        params.put("comment_status", "1");
        params.put("share_text", postData.getPostSharedMessage());
        params.put("group_id[]", "" + postData.getGroupID());

        Log.i(TAG, "params to share nutrition feed post : " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    if (obj.get("res").toString().equals("1")) {
                        listener.onPostShared(postData);
                    } else
                        listener.onFailed(NutritionFeed.FAILED_SHARING_POST, mContext.getString(R.string.error_occurred));

                } catch (JSONException e) {
                    listener.onFailed(NutritionFeed.FAILED_SHARING_POST, mContext.getString(R.string.error_occurred));
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NutritionFeed.FAILED_SHARING_POST, mContext.getString(R.string.error_connecting_to_server));
            }
        });


    }
}
