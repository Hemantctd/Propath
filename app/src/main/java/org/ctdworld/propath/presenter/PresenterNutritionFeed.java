package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractNewsFeed;
import org.ctdworld.propath.contract.ContractNutritionFeed;
import org.ctdworld.propath.interactor.InteractorNewsFeed;
import org.ctdworld.propath.interactor.InteractorNutritionFeed;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.model.NutritionFeed;

import java.util.List;


public class PresenterNutritionFeed implements ContractNutritionFeed.Presenter, ContractNutritionFeed.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterNutritionFeed.class.getSimpleName();
    private ContractNutritionFeed.Interactor mInteractor;
    private ContractNutritionFeed.View mListener;

    public PresenterNutritionFeed(Context context, ContractNutritionFeed.View view)
    {
        this.mInteractor = new InteractorNutritionFeed(this,context);
        this.mListener = view;
    }


    @Override
    public void onPostCreated(NutritionFeed.PostData postData) {
        mListener.onPostCreated(postData);
    }

    @Override
    public void onPostEdited(NutritionFeed.PostData postData) {
        mListener.onPostEdited(postData);
    }

    @Override
    public void onPostListReceived(List<NutritionFeed.PostData> postDataList) {
        mListener.onPostListReceived(postDataList);
    }

    @Override
    public void onPostLikeUpdated(NutritionFeed.PostData postData) {
        mListener.onPostLikeUpdated(postData);
    }

    @Override
    public void onPostCommentsReceived(List<NutritionFeed.PostData.PostComment> postCommentList) {
        mListener.onPostCommentsReceived(postCommentList);
    }

    @Override
    public void onPostCommentAdded(NutritionFeed.PostData.PostComment postComment) {
        mListener.onPostCommentAdded(postComment);
    }

    @Override
    public void onPostCommentEdited(NutritionFeed.PostData.PostComment postComment) {
        mListener.onPostCommentEdited(postComment);
    }

    @Override
    public void onPostCommentDeleted(NutritionFeed.PostData.PostComment postComment) {
        mListener.onPostCommentDeleted(postComment);
    }

    @Override
    public void onFailed(int failType, String message) {
        mListener.onFailed(failType, message);
    }

    @Override
    public void onPostShared(NutritionFeed.PostData postData) {
        mListener.onPostShared(postData);
    }

    @Override
    public void createPost(NutritionFeed.PostData postData) {
        mInteractor.createPost(postData);
    }

    @Override
    public void editPost(NutritionFeed.PostData postData) {
        mInteractor.editPost(postData);
    }

    @Override
    public void requestPostDataList(String userId) {
        mInteractor.requestPostDataList(userId);
    }

    @Override
    public void updateLikeOnPost(NutritionFeed.PostData postData) {
        mInteractor.updateLikeOnPost(postData);
    }

    @Override
    public void requestPostComments(NutritionFeed.PostData.PostComment postComment) {
        mInteractor.requestPostComments(postComment);
    }

    @Override
    public void addPostComment(NutritionFeed.PostData.PostComment postComment) {
        mInteractor.addPostComment(postComment);
    }

    @Override
    public void editPostComment(NutritionFeed.PostData.PostComment postComment) {
        mInteractor.editPostComment(postComment);
    }

    @Override
    public void deletePostComment(NutritionFeed.PostData.PostComment postComment) {
        mInteractor.deletePostComment(postComment);
    }

    @Override
    public void sharePost(NutritionFeed.PostData postData) {
        mInteractor.sharePost(postData);
    }
}
