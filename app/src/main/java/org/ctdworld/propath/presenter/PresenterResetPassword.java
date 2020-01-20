package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractResetPassword;
import org.ctdworld.propath.interactor.InteractorResetPassword;
import org.ctdworld.propath.model.User;


public class PresenterResetPassword implements ContractResetPassword.Presenter, ContractResetPassword.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterResetPassword.class.getSimpleName();
    ContractResetPassword.Interactor interactor;
    ContractResetPassword.View listenerView;

    public PresenterResetPassword(Context context)
    {
        this.interactor = new InteractorResetPassword(this,context);
        this.listenerView = (ContractResetPassword.View) context;
    }


    @Override
    public void resetPassword(User user) {
        interactor.resetPassword(user);
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
    public void onSuccess() {
        listenerView.onSuccess();
    }

    @Override
    public void onFailed() {
        listenerView.onFailed();
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
    public void onShowMessage(String message) {
        listenerView.onShowMessage(message);
    }
}
