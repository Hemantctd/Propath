package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.NewsFeed;

import java.util.List;


/*# this contract class will be used in view, create, edit note pages*/
public interface ContractNewsFeed
{

    interface Interactor
    {
        // # onFinishedListener
        interface OnFinishedListener
        {
            void onPostCreated(NewsFeed.PostData postData);  // to add new post
            void onPostEdited(NewsFeed.PostData postData);  // to edit existing post
            void onPostListReceived(List<NewsFeed.PostData> postDataList);   //all posts received from server
            void onPostLikeUpdated(NewsFeed.PostData postData); // called after post like is updated
            void onPostCommentsReceived(List<NewsFeed.PostData.PostComment> postCommentList);
            void onPostCommentAdded(NewsFeed.PostData.PostComment postComment);
            void onPostCommentEdited(NewsFeed.PostData.PostComment postComment);
            void onPostCommentDeleted(NewsFeed.PostData.PostComment postComment);
            void onFailed(int failType, String message);    // contains failure type while requesting to server. failTypes are mentioned in NewsFeed modal class
            void onPostShared(NewsFeed.PostData postData);  // called when post is shared successfully
        }


        // # interactor
        void createPost(NewsFeed.PostData postData);  // to add new post
        void editPost(NewsFeed.PostData postData);  // to edit existing post
        void requestPostDataList(String userId);   //all posts received from server
        void updateLikeOnPost(NewsFeed.PostData postData);  // updates like of post
        void requestPostComments(NewsFeed.PostData.PostComment postComment);  // to get comments of particular post
        void addPostComment(NewsFeed.PostData.PostComment postComment);
        void editPostComment(NewsFeed.PostData.PostComment postComment);
        void deletePostComment(NewsFeed.PostData.PostComment postComment);
        void sharePost(NewsFeed.PostData postData);  // to share post
    }


    // # view
    interface View
    {
        void onPostCreated(NewsFeed.PostData postData);  // to add new post
        void onPostEdited(NewsFeed.PostData postData);  // to edit existing post
        void onPostListReceived(List<NewsFeed.PostData> postDataList);   //all posts received from server
        void onPostLikeUpdated(NewsFeed.PostData postData); // called after post like is updated
        void onPostCommentsReceived(List<NewsFeed.PostData.PostComment> postCommentList);
        void onPostCommentAdded(NewsFeed.PostData.PostComment postComment);
        void onPostCommentEdited(NewsFeed.PostData.PostComment postComment);
        void onPostCommentDeleted(NewsFeed.PostData.PostComment postComment);
        void onFailed(int failType, String message);    // contains failure type while requesting to server
        void onPostShared(NewsFeed.PostData postData);  // called when post is shared successfully

    }

    // # presenter
    interface Presenter
    {
        void createPost(NewsFeed.PostData postData);  // to add new post
        void editPost(NewsFeed.PostData postData);  // to edit existing post
        void requestPostDataList(String userId);   //all posts received from server
        void updateLikeOnPost(NewsFeed.PostData postData);  // updates like of post
        void requestPostComments(NewsFeed.PostData.PostComment postComment);  // to get comments of particular post
        void addPostComment(NewsFeed.PostData.PostComment postComment);
        void editPostComment(NewsFeed.PostData.PostComment postComment);
        void deletePostComment(NewsFeed.PostData.PostComment postComment);
        void sharePost(NewsFeed.PostData postData); // to share post
    }
}
