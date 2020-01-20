package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractSignUp;
import org.ctdworld.propath.interactor.InteractorRegister;
import org.ctdworld.propath.model.User;


public class PresenterSignUp implements ContractSignUp.Presenter, ContractSignUp.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterSignUp.class.getSimpleName();
    ContractSignUp.Interactor interactor;
    ContractSignUp.View listenerView;

    public PresenterSignUp(Context context)
    {
        this.interactor = new InteractorRegister(this,context);
        this.listenerView = (ContractSignUp.View) context;
    }


    @Override
    public void createAccount(User user) {
        interactor.createAccount(user);
    }


    @Override
    public void onShowProgress() {
        listenerView.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        listenerView.onHideProgress();
    }

    @Override
    public void onSignUpSuccess() {
        listenerView.onSignUpSuccess();
    }

    @Override
    public void onSignUpFailed() {
        listenerView.onSignUpFailed();
    }

    @Override
    public void onEmailAlreadyRegistered() {
        listenerView.onEmailAlreadyRegistered();
    }


    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        listenerView.onSetViewsDisabledOnProgressBarVisible();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        listenerView.onSetViewsEnabledOnProgressBarGone();
    }

    @Override
    public void onConnectionError() {
        listenerView.onConnectionError();
    }

}
