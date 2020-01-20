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
import org.ctdworld.propath.contract.ContractNewsFeed;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractorNewsFeed implements ContractNewsFeed.Interactor
{
    private static final String TAG = InteractorNewsFeed.class.getSimpleName();
    private OnFinishedListener listener;
    private RemoteServer remoteServer;
    private Context mContext;


    //Constructor
    public InteractorNewsFeed(OnFinishedListener onFinishedListener, Context context)
    {
        this.listener = onFinishedListener;
        remoteServer = new RemoteServer(context);
        this.mContext = context;
    }



    // create new post
    @Override
    public void createPost(final NewsFeed.PostData postData)
    {
        try {
            if (postData == null)
            return;

            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest =   new MultipartUploadRequest(mContext, uploadId, RemoteServer.URL);

            if(postData.getPostMediaUrl() != null && !postData.getPostMediaUrl().isEmpty())
                multipartUploadRequest.addFileToUpload(postData.getPostMediaUrl(), "file"); //adding media

            multipartUploadRequest.addParameter("add_news_feed", "1");
            multipartUploadRequest.addParameter("title", postData.getPostMessage()); //post message
            multipartUploadRequest.addParameter("user_id", SessionHelper.getUserId(mContext));
            multipartUploadRequest.addParameter("comment_status", String.valueOf(postData.getPostCommentPermission()));

            // # post may be visible to public or contacts(friends) only
            //multipartUploadRequest.addParameter("share_with", String.valueOf(mSettings));
            // # now post is visible to contacts(friends) only
            multipartUploadRequest.addParameter("share_with", String.valueOf(postData.getPostVisibility()));


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
                        }
                        else
                            listener.onFailed(NewsFeed.FAILED_CREATING_POST, null);
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
            listener.onFailed(NewsFeed.FAILED_CREATING_POST, null);
            //Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    // saving edited post
    @Override
    public void editPost(final NewsFeed.PostData postData)
    {
        int isImageBeingPosted = 0; // default value if image is not being posted
        try {
           String media = postData.getPostMediaUrl();

            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, RemoteServer.URL);

            if(media != null && !media.isEmpty() && !Patterns.WEB_URL.matcher(media).matches())
                multipartUploadRequest.addFileToUpload(postData.getPostMediaUrl(), "file"); //Adding file

            multipartUploadRequest.addParameter("title", postData.getPostMessage()); // adding post message
            multipartUploadRequest.addParameter("edit_news_feed", "1");
            multipartUploadRequest.addParameter("comment_status", String.valueOf(postData.getPostCommentPermission()));
            multipartUploadRequest.addParameter("share_with", String.valueOf(postData.getPostVisibility()));
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
                    listener.onFailed(NewsFeed.FAILED_EDITING_POST, mContext.getString(R.string.error_occurred));
                }

                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                    String response = serverResponse.getBodyAsString();
                    Log.d(TAG, "server response to edit news feed post : " + response);
                    try {
                        JSONObject serverResponse1 = new JSONObject(response);
                        if (serverResponse1.get("res").toString().equals("1"))
                        {
                            listener.onPostEdited(postData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailed(NewsFeed.FAILED_EDITING_POST, mContext.getString(R.string.error_occurred));
                    }
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    listener.onFailed(NewsFeed.FAILED_EDITING_POST, mContext.getString(R.string.canceled));
                }
            });
            multipartUploadRequest.startUpload(); //Starting the upload



        } catch (Exception exc) {
            listener.onFailed(NewsFeed.FAILED_EDITING_POST, mContext.getString(R.string.error_occurred));
            Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



    // requesting to get post data list from server
    @Override
    public void requestPostDataList(String userId)
    {
        if (userId == null || listener == null || mContext == null)
            return;

        Map<String, String> params = new HashMap<>();
        params.put("view_news_feed", "1");
        params.put("user_id", userId);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG,"response to get news feed list = "+response);

                try{
                    Gson gson = new Gson();
                    NewsFeed newsFeed = gson.fromJson(response, NewsFeed.class);
                    if (newsFeed != null && newsFeed.getStatus() != null && newsFeed.getStatus().equals("1"))
                    {
                        listener.onPostListReceived(newsFeed.getListPostData());
                    }
                    else
                        listener.onFailed(NewsFeed.FAILED_RECEIVING_POST_LIST, null);
                }catch (Exception e)
                {
                    listener.onFailed(NewsFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_occurred));
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NewsFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_connecting_to_server));
            }
        });
    }



    @Override
    public void updateLikeOnPost(final NewsFeed.PostData postData) {

        Map<String, String> params = new HashMap<>();
        params.put("add_like_post", "1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("post_id", postData.getPostId());
        params.put("status", postData.getLikeStatus());
        Log.d(TAG, "params to update like  : " + params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.get("res").toString().equals("1"))
                    {
                        /*if (postData.getLikeStatus().equals(NewsFeed.POST_LIKED))
                            postData.setPostLikeCount(String.valueOf(Integer.parseInt(postData.getPostLikeCount())+1));
                        else
                            postData.setPostLikeCount(String.valueOf(Integer.parseInt(postData.getPostLikeCount())-1));
*/
                       // listener.onPostLikeUpdated(postData);  // updating post
                    }
                    else
                        listener.onFailed(NewsFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_occurred));
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailed(NewsFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NewsFeed.FAILED_RECEIVING_POST_LIST, mContext.getString(R.string.error_connecting_to_server));
            }
        });
    }





    @Override
    public void requestPostComments(NewsFeed.PostData.PostComment postComment) {
        Map<String, String> params = new HashMap<>();
        params.put("view_comment_post", "1");
        params.put("post_id", postComment.getPostId());

        Log.i(TAG,"params to load comments = "+params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                Log.i(TAG,"response to get news feed comment list = "+response);

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.get("res").toString().equals("1"))
                    {
                        Gson gson = new Gson();
                        NewsFeed.PostData responsePost = gson.fromJson(response, NewsFeed.PostData.class);

                        if (responsePost != null)
                            listener.onPostCommentsReceived(responsePost.getPostCommentList());
                    }
                    else
                        listener.onFailed(NewsFeed.FAILED_GETTING_COMMENTS, null);

                }catch (Exception e)
                {
                    listener.onFailed(NewsFeed.FAILED_GETTING_COMMENTS, null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NewsFeed.FAILED_GETTING_COMMENTS, null);
            }
        });
    }





    // adding new comment on post
    @Override
    public void addPostComment(final NewsFeed.PostData.PostComment postComment)
    {
        Log.i(TAG,"adding new comment on news feed post");
        Map<String, String> params = new HashMap<>();
        params.put("add_comment_post", "1");
        params.put("user_id", SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("post_id", postComment.getPostId());
        params.put("comment", postComment.getCommentMessage());
        Log.d(TAG, "params to add comment in news feed post : " + params);


        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response) {
                try {
                    Log.i(TAG,"server response to add comment on news feed post = "+response);
                    JSONObject obj = new JSONObject(response);
                    if (obj.get("res").toString().equals("1")) {

                        if (listener != null)
                        {
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
    public void editPostComment(final NewsFeed.PostData.PostComment postComment) {
        Map<String, String> params = new HashMap<>();
        params.put("edit_comment_post", "1");
        params.put("user_id",SessionHelper.getInstance(mContext).getUser().getUserId());
        params.put("post_id",postComment.getPostId());
        params.put("comment_id",postComment.getCommentId());
        params.put("comment",postComment.getCommentMessage());

        Log.d(TAG,"params to edit news feed comment  : " +params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response)
            {
                Log.i(TAG, "Server response edit news feed post comment "+response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.get("res").toString().equals("1"))
                    {
                        System.out.println("comment updated successfully , response = "+response);
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
    public void deletePostComment(final NewsFeed.PostData.PostComment postComment)
    {
        Map<String, String> params = new HashMap<>();
        params.put("delete_comment_post", "1");
        params.put("comment_id", postComment.getCommentId());
        Log.d(TAG,"params to delete post comment : " +params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response)
            {
                Log.i(TAG, "Server response edit news feed post comment "+response);

                try {
                    JSONObject obj =  new JSONObject(response);
                    if(obj.get("res").toString().equals("1")) {
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
    public void sharePost(final NewsFeed.PostData postData) {

        Map<String, String> params = new HashMap<>();
        params.put("share_news_feed", "1");
        params.put("post_id",postData.getPostId());
        params.put("post_share_by", postData.getPostSharedBy());
        params.put("comment_status", String.valueOf(postData.getPostCommentPermission()));
        params.put("share_with", String.valueOf(postData.getPostVisibility()));
        params.put("share_text",postData.getPostSharedMessage());

        Log.i(TAG,"params to share news feed post : " +params);

        remoteServer.sendData(RemoteServer.URL, params, new RemoteServer.VolleyStringListener() {
            @Override
            public void onVolleySuccessResponse(String response)
            {
                JSONObject obj = null;
                try
                {
                    obj = new JSONObject(response);
                    if(obj.get("res").toString().equals("1"))
                    {
                        listener.onPostShared(postData);
                    }
                    else
                        listener.onFailed(NewsFeed.FAILED_SHARING_POST, mContext.getString(R.string.error_occurred));

                } catch (JSONException e) {
                    listener.onFailed(NewsFeed.FAILED_SHARING_POST, mContext.getString(R.string.error_occurred));
                    e.printStackTrace();
                }

            }

            @Override
            public void onVolleyErrorResponse(VolleyError error) {
                listener.onFailed(NewsFeed.FAILED_SHARING_POST, mContext.getString(R.string.error_connecting_to_server));
            }
        });

    }
}
