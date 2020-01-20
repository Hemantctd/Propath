package org.ctdworld.propath.presenter;

import android.content.Context;


import org.ctdworld.propath.contract.ContractNewsFeed;
import org.ctdworld.propath.interactor.InteractorNewsFeed;
import org.ctdworld.propath.model.NewsFeed;

import java.util.List;


public class PresenterNewsFeed implements ContractNewsFeed.Presenter, ContractNewsFeed.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterNewsFeed.class.getSimpleName();
    private ContractNewsFeed.Interactor mInteractor;
    private ContractNewsFeed.View mListener;

    public PresenterNewsFeed(Context context, ContractNewsFeed.View view)
    {
        this.mInteractor = new InteractorNewsFeed(this,context);
        this.mListener = view;
    }


    @Override
    public void onPostCreated(NewsFeed.PostData postData) {
        mListener.onPostCreated(postData);
    }

    @Override
    public void onPostEdited(NewsFeed.PostData postData) {
        mListener.onPostEdited(postData);
    }

    @Override
    public void onPostListReceived(List<NewsFeed.PostData> postDataList) {
        mListener.onPostListReceived(postDataList);
    }

    @Override
    public void onPostLikeUpdated(NewsFeed.PostData postData) {
        mListener.onPostLikeUpdated(postData);
    }

    @Override
    public void onPostCommentsReceived(List<NewsFeed.PostData.PostComment> postCommentList) {
        mListener.onPostCommentsReceived(postCommentList);
    }

    @Override
    public void onPostCommentAdded(NewsFeed.PostData.PostComment postComment) {
        mListener.onPostCommentAdded(postComment);
    }

    @Override
    public void onPostCommentEdited(NewsFeed.PostData.PostComment postComment) {
        mListener.onPostCommentEdited(postComment);
    }

    @Override
    public void onPostCommentDeleted(NewsFeed.PostData.PostComment postComment) {
        mListener.onPostCommentDeleted(postComment);
    }

    @Override
    public void onFailed(int failType, String message) {
        mListener.onFailed(failType, message);
    }

    @Override
    public void onPostShared(NewsFeed.PostData postData) {
        mListener.onPostShared(postData);
    }

    @Override
    public void createPost(NewsFeed.PostData postData) {
        mInteractor.createPost(postData);
    }

    @Override
    public void editPost(NewsFeed.PostData postData) {
        mInteractor.editPost(postData);
    }

    @Override
    public void requestPostDataList(String userId) {
        mInteractor.requestPostDataList(userId);
    }

    @Override
    public void updateLikeOnPost(NewsFeed.PostData postData) {
        mInteractor.updateLikeOnPost(postData);
    }

    @Override
    public void requestPostComments(NewsFeed.PostData.PostComment postComment) {
        mInteractor.requestPostComments(postComment);
    }

    @Override
    public void addPostComment(NewsFeed.PostData.PostComment postComment) {
        mInteractor.addPostComment(postComment);
    }

    @Override
    public void editPostComment(NewsFeed.PostData.PostComment postComment) {
        mInteractor.editPostComment(postComment);
    }

    @Override
    public void deletePostComment(NewsFeed.PostData.PostComment postComment) {
        mInteractor.deletePostComment(postComment);
    }

    @Override
    public void sharePost(NewsFeed.PostData postData) {
        mInteractor.sharePost(postData);
    }
}
