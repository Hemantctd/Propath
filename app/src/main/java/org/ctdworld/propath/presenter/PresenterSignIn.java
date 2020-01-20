package org.ctdworld.propath.presenter;

import android.content.Context;
import org.ctdworld.propath.contract.ContractSignIn;
import org.ctdworld.propath.interactor.InteractorSignIn;
import org.ctdworld.propath.model.User;

public class PresenterSignIn implements ContractSignIn.Presenter, ContractSignIn.Interactor.OnFinishedListener
{
    ContractSignIn.View listener;
    ContractSignIn.Interactor interactor;

    public PresenterSignIn(Context context)
    {
        this.listener = (ContractSignIn.View) context;
        interactor = new InteractorSignIn(this,context);
    }

    @Override
    public void signIn(User user) {
        interactor.signIn(user);
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
    public void onEmailOrPasswordWrong() {
        listener.onEmailOrPasswordWrong();
    }

    @Override
    public void onSuccess() {
        listener.onSuccess();
    }

    @Override
    public void onShowMessage(String message) {
        listener.onShowMessage(message);
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
    public void onConnectionError() {
        listener.onConnectionError();
    }


}
