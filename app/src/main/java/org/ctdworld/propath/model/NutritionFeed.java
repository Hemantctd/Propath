package org.ctdworld.propath.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NutritionFeed implements Serializable
{

    // # server setting for post visibility
    /*post visibility setting (visible to public or connections). by default post would be visible to connections only to whom it will be shared
         but we are post it for connections, it will not be shown to connections also as long as it's not shared with them*/
   /* public static final int VISIBLE_TO_CONTACTS = 1;
    public static final int VISIBLE_TO_PUBLIC = 0;*/



    // # server setting for comment permission on post
    public static final int COMMENT_ALLOWED = 1;
    public static final int COMMENT_NOT_ALLOWED = 0;



    // # server setting to like post
    public static final String POST_LIKED = "1";
    public static final String POST_NOT_LIKED = "0";


    public static final String POST_NOT_SHARED = "0";  // if post shared then "0"



    // # to check what has failed while requesting to server
    public static final int FAILED_CREATING_POST = 1;
    public static final int FAILED_EDITING_POST = 2;
    public static final int FAILED_RECEIVING_POST_LIST = 3;
    public static final int FAILED_UPDATING_POST_LIKE = 4;
    public static final int FAILED_SHARING_POST = 5;
    public static final int FAILED_GETTING_COMMENTS = 6;


    @SerializedName("res")
    private String status;

    @SerializedName("result")
    private List<PostData> listPostData;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PostData> getListPostData() {
        return listPostData;
    }

    public void setListPostData(List<PostData> postDataList) {
        this.listPostData = postDataList;
    }








    // contains post data
    public static class  PostData implements Serializable
    {


        @SerializedName("id")
        String postId;
        @SerializedName("user_id")
        String postUserId;
        @SerializedName("title")
        String postMessage;
        @SerializedName("media_url")
        String postMediaUrl;
        @SerializedName("like_count")
        String postLikeCount;
  /*      @SerializedName("share_with")
        *//* # post visibility setting (visible to public or connections). by default post would be visible to connections only to whom it will be shared
         but we are post it for connections, it will not be shown to connections also as long as it's not shared with them*//*
        int postVisibility = VISIBLE_TO_CONTACTS;*/
        @SerializedName("comment_status")
        int postCommentPermission = COMMENT_ALLOWED;  // # permission for comment if permission is allowed or not, by default it would be allowed
        @SerializedName("post_share_by")
        String postSharedBy;   // userId of the user who has shared the post
        @SerializedName("post_share_text")
        String postSharedMessage;
        @SerializedName("status")
        String postStatus;
        @SerializedName("date_time")
        String postDateTime;
        @SerializedName("user_name")    // name of the user who has created or shared the post
        String postByUserName;
        @SerializedName("user_profile_image")
        String postByUserProfilePic;
        @SerializedName("like")
        String likeStatus;  // contains if post has been liked by the logged in user
        @SerializedName("comment_count")
        String postCommentCount;  // total number of comments
        @SerializedName("result")
        List<PostComment> postCommentList;  // list of comments
        private PostComment postComment;
        private String groupID;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        private String groupName;

        public String getGroupID() {
            return groupID;
        }

        public void setGroupID(String groupID) {
            this.groupID = groupID;
        }

        public List<PostComment> getPostCommentList() {
            return postCommentList;
        }

        public void setPostCommentList(List<PostComment> postCommentList) {
            this.postCommentList = postCommentList;
        }

        public PostComment getPostComment() {
            return postComment;
        }

        public void setPostComment(PostComment postComment) {
            this.postComment = postComment;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getPostUserId() {
            return postUserId;
        }

        public void setPostUserId(String postUserId) {
            this.postUserId = postUserId;
        }

        public String getPostMessage() {
            return postMessage;
        }

        public void setPostMessage(String postMessage) {
            this.postMessage = postMessage;
        }

        public String getPostMediaUrl() {
            return postMediaUrl;
        }

        public void setPostMediaUrl(String postMediaUrl) {
            this.postMediaUrl = postMediaUrl;
        }

        public String getPostLikeCount() {
            return postLikeCount;
        }

        public void setPostLikeCount(String postLikeCount) {
            this.postLikeCount = postLikeCount;
        }

      /*  public int getPostVisibility() {
            return postVisibility;
        }

        public void setPostVisibility(int postVisibility) {
            this.postVisibility = postVisibility;
        }
*/
        public int getPostCommentPermission() {
            return postCommentPermission;
        }

        public void setPostCommentPermission(int postCommentPermission) {
            this.postCommentPermission = postCommentPermission;
        }

        public String getPostSharedBy() {
            return postSharedBy;
        }

        public void setPostSharedBy(String postSharedBy) {
            this.postSharedBy = postSharedBy;
        }

        public String getPostSharedMessage() {
            return postSharedMessage;
        }

        public void setPostSharedMessage(String postSharedMessage) {
            this.postSharedMessage = postSharedMessage;
        }

        public String getPostStatus() {
            return postStatus;
        }

        public void setPostStatus(String postStatus) {
            this.postStatus = postStatus;
        }

        public String getPostDateTime() {
            return postDateTime;
        }

        public void setPostDateTime(String postDateTime) {
            this.postDateTime = postDateTime;
        }

        public String getPostByUserName() {
            return postByUserName;
        }

        public void setPostByUserName(String postByUserName) {
            this.postByUserName = postByUserName;
        }

        public String getPostByUserProfilePic() {
            return postByUserProfilePic;
        }

        public void setPostByUserProfilePic(String postByUserProfilePic) {
            this.postByUserProfilePic = postByUserProfilePic;
        }

        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String like) {
            this.likeStatus = like;
        }

        public String getPostCommentCount() {
            return postCommentCount;
        }

        public void setPostCommentCount(String postCommentCount) {
            this.postCommentCount = postCommentCount;
        }










        // modal to contain post comment data
        public static class PostComment implements Serializable
        {
            @SerializedName("res")
            String response;             // server response
            @SerializedName("id")
            private String commentId;
            @SerializedName("user_id")
            private String commentUserId;
            @SerializedName("post_id")
            private String postId;
            @SerializedName("comment")
            private String commentMessage;
            @SerializedName("date_time")
            private String commentDateTime;
            @SerializedName("name")
            private String commentUserName;
            @SerializedName("profile_image")
            private String commentUserProfilePic;


            public String getCommentId() {
                return commentId;
            }

            public void setCommentId(String commentId) {
                this.commentId = commentId;
            }

            public String getCommentUserId() {
                return commentUserId;
            }

            public void setCommentUserId(String commentUserId) {
                this.commentUserId = commentUserId;
            }

            public String getPostId() {
                return postId;
            }

            public void setPostId(String postId) {
                this.postId = postId;
            }

            public String getCommentMessage() {
                return commentMessage;
            }

            public void setCommentMessage(String commentMessage) {
                this.commentMessage = commentMessage;
            }

            public String getCommentDateTime() {
                return commentDateTime;
            }

            public void setCommentDateTime(String commentDateTime) {
                this.commentDateTime = commentDateTime;
            }

            public String getCommentUserName() {
                return commentUserName;
            }

            public void setCommentUserName(String commentUserName) {
                this.commentUserName = commentUserName;
            }

            public String getCommentUserProfilePic() {
                return commentUserProfilePic;
            }

            public void setCommentUserProfilePic(String commentUserProfilePic) {
                this.commentUserProfilePic = commentUserProfilePic;
            }
        }
    }

}
