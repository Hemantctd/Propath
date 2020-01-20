package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.model.NutritionFeed;

import java.util.List;


/*# this contract class will be used in view, create, edit note pages*/
public interface ContractNutritionFeed
{

    interface Interactor
    {
        // # onFinishedListener
        interface OnFinishedListener
        {
            void onPostCreated(NutritionFeed.PostData postData);  // to add new post
            void onPostEdited(NutritionFeed.PostData postData);  // to edit existing post
            void onPostListReceived(List<NutritionFeed.PostData> postDataList);   //all posts received from server
            void onPostLikeUpdated(NutritionFeed.PostData postData); // called after post like is updated
            void onPostCommentsReceived(List<NutritionFeed.PostData.PostComment> postCommentList);
            void onPostCommentAdded(NutritionFeed.PostData.PostComment postComment);
            void onPostCommentEdited(NutritionFeed.PostData.PostComment postComment);
            void onPostCommentDeleted(NutritionFeed.PostData.PostComment postComment);
            void onFailed(int failType, String message);    // contains failure type while requesting to server. failTypes are mentioned in NewsFeed modal class
            void onPostShared(NutritionFeed.PostData postData);  // called when post is shared successfully
        }


        // # interactor
        void createPost(NutritionFeed.PostData postData);  // to add new post
        void editPost(NutritionFeed.PostData postData);  // to edit existing post
        void requestPostDataList(String userId);   //all posts received from server
        void updateLikeOnPost(NutritionFeed.PostData postData);  // updates like of post
        void requestPostComments(NutritionFeed.PostData.PostComment postComment);  // to get comments of particular post
        void addPostComment(NutritionFeed.PostData.PostComment postComment);
        void editPostComment(NutritionFeed.PostData.PostComment postComment);
        void deletePostComment(NutritionFeed.PostData.PostComment postComment);
        void sharePost(NutritionFeed.PostData postData);  // to share post
    }


    // # view
    interface View
    {
        void onPostCreated(NutritionFeed.PostData postData);  // to add new post
        void onPostEdited(NutritionFeed.PostData postData);  // to edit existing post
        void onPostListReceived(List<NutritionFeed.PostData> postDataList);   //all posts received from server
        void onPostLikeUpdated(NutritionFeed.PostData postData); // called after post like is updated
        void onPostCommentsReceived(List<NutritionFeed.PostData.PostComment> postCommentList);
        void onPostCommentAdded(NutritionFeed.PostData.PostComment postComment);
        void onPostCommentEdited(NutritionFeed.PostData.PostComment postComment);
        void onPostCommentDeleted(NutritionFeed.PostData.PostComment postComment);
        void onFailed(int failType, String message);    // contains failure type while requesting to server
        void onPostShared(NutritionFeed.PostData postData);  // called when post is shared successfully

    }

    // # presenter
    interface Presenter
    {
        void createPost(NutritionFeed.PostData postData);  // to add new post
        void editPost(NutritionFeed.PostData postData);  // to edit existing post
        void requestPostDataList(String userId);   //all posts received from server
        void updateLikeOnPost(NutritionFeed.PostData postData);  // updates like of post
        void requestPostComments(NutritionFeed.PostData.PostComment postComment);  // to get comments of particular post
        void addPostComment(NutritionFeed.PostData.PostComment postComment);
        void editPostComment(NutritionFeed.PostData.PostComment postComment);
        void deletePostComment(NutritionFeed.PostData.PostComment postComment);
        void sharePost(NutritionFeed.PostData postData); // to share post
    }
}
