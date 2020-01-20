package org.ctdworld.propath.presenter;

import android.content.Context;


import org.ctdworld.propath.contract.ContractGetRegisteredUsers;
import org.ctdworld.propath.interactor.InteractorGetRegisteredUsers;
import org.ctdworld.propath.model.User;

import java.util.List;


public abstract class PresenterGetRegisteredUsers implements ContractGetRegisteredUsers.Presenter, ContractGetRegisteredUsers.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetRegisteredUsers.class.getSimpleName();
    ContractGetRegisteredUsers.Interactor interactor;
    ContractGetRegisteredUsers.View listenerView;

    public PresenterGetRegisteredUsers(Context context, ContractGetRegisteredUsers.View view)
    {
        this.interactor = new InteractorGetRegisteredUsers(this,context);
        this.listenerView = view;
    }



    @Override
    public void requestAllUsers() {
        interactor.requestAllUsers();
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
    public void onGetRegisteredUsers(List<User> userList) {
        listenerView.onGetRegisteredUsers(userList);
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
