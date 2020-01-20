package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractProfileUpdate;
import org.ctdworld.propath.interactor.InteractorProfileUpdate;
import org.ctdworld.propath.model.Profile;


public class PresenterProfileUpdate implements ContractProfileUpdate.Presenter, ContractProfileUpdate.Interactor.OnFinishedListener
{
    private ContractProfileUpdate.View listener;
    private ContractProfileUpdate.Interactor interactor;

    public PresenterProfileUpdate(Context context, ContractProfileUpdate.View listener)
    {
        this.listener = listener;
        interactor = new InteractorProfileUpdate(this,context);
    }


//    @Override
//    public void saveUserProfile(Profile updateProfile) {
//        interactor.saveUserProfile(updateProfile);
//    }

    @Override
    public void updateProfile(Profile profile) {
        interactor.updateProfile(profile);
    }



    @Override
    public void onComplete(String message) {
        listener.onComplete(message);
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


}
