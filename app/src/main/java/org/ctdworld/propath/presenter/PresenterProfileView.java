package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractProfileView;
import org.ctdworld.propath.interactor.InteractorProfileView;
import org.ctdworld.propath.model.Profile;


public class PresenterProfileView implements ContractProfileView.Presenter, ContractProfileView.Interactor.OnFinishedListener
{
    private ContractProfileView.View listener;
    private ContractProfileView.Interactor interactor;

    public PresenterProfileView(Context context, ContractProfileView.View listener)
    {
        this.listener = listener;
        interactor = new InteractorProfileView(this,context);
    }


    @Override
    public void requestProfile(String userId) {
        interactor.requestProfile(userId);
    }

    @Override
    public void checkFriendStatus(String loginUserId, String otherUserId) {
        interactor.checkFriendStatus(loginUserId, otherUserId);
    }


    @Override
    public void onProfileReceived(Profile profile) {
        listener.onProfileReceived(profile);
    }

    @Override
    public void onReceivedFriendStatus(String friendStatus) {
        listener.onReceivedFriendStatus(friendStatus);
    }

    @Override
    public void onFailed(String message) {
        listener.onFailed(message);
    }
}
