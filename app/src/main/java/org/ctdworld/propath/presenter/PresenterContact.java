package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractContact;
import org.ctdworld.propath.interactor.InteractorContact;
import org.ctdworld.propath.model.User;

import java.util.List;


public class PresenterContact implements ContractContact.Presenter, ContractContact.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterContact.class.getSimpleName();
    ContractContact.Interactor interactor;
    ContractContact.View listenerView;

    public PresenterContact(Context context, ContractContact.View view)
    {
        this.interactor = new InteractorContact(this,context);
        this.listenerView = view;
    }



    @Override
    public void requestAllContacts() {
        interactor.requestAllContacts();
    }

    @Override
    public void deleteContact(String userId) {
        interactor.deleteContact(userId);
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

    }


    @Override
    public void onReceiveAllContact(List<User> userList) {
        listenerView.onReceiveAllContacts(userList);
    }

    @Override
    public void onContactDeleted(String userId) {
        listenerView.onContactDeleted(userId);
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
