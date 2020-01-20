package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractUpdateProfile;
import org.ctdworld.propath.interactor.InteractorUpdateProfile;
import org.ctdworld.propath.model.Profile;


public class PresenterUpdateProfile implements ContractUpdateProfile.Presenter, ContractUpdateProfile.Interactor.OnFinishedListener
{
    private ContractUpdateProfile.View listener;
    private ContractUpdateProfile.Interactor interactor;

    public PresenterUpdateProfile(Context context, ContractUpdateProfile.View listener)
    {
        this.listener = listener;
        interactor = new InteractorUpdateProfile(this,context);
    }


    @Override
    public void updateProfile(Profile profile) {
        interactor.updateProfile(profile);
    }




    @Override
    public void onShowProgress() {
        listener.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        listener.onHideProgress();
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        listener.onSetViewsDisabledOnProgressBarVisible();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        listener.onSetViewsEnabledOnProgressBarGone();
    }

    @Override
    public void onShowMessage(String message) {
        listener.onShowMessage(message);
    }


    @Override
    public void onFailed() {
        listener.onFailed();
    }
}
