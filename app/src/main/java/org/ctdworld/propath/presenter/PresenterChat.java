package org.ctdworld.propath.presenter;

import android.content.Context;


import org.ctdworld.propath.contract.ContractChat;
import org.ctdworld.propath.interactor.InteractorChat;
import org.ctdworld.propath.model.Chat;


public abstract class PresenterChat implements ContractChat.Presenter, ContractChat.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterChat.class.getSimpleName();
    ContractChat.Interactor interactor;
    ContractChat.View listenerView;

    public PresenterChat(Context context)
    {
        this.interactor = new InteractorChat(this,context);
        this.listenerView = (ContractChat.View) context;
    }



    @Override
    public void requestChat(Chat chat) {
        interactor.requestChat(chat);
    }


    @Override
    public void sendChat(Chat chat) {
        interactor.sendChat(chat);
    }

    @Override
    public void requestPreviousChat(Chat chat, int countRequest) {
        interactor.requestPreviousChat(chat, countRequest);
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
    public void onChatReceived(Chat chat) {
        listenerView.onChatReceived(chat);
    }

    @Override
    public void onChatSendSuccess(Chat chat) {
        listenerView.onChatSendSuccess(chat);
    }

    @Override
    public void onPreviousChatReceived(Chat chat) {
        listenerView.onPreviousChatReceived(chat);
    }

    @Override
    public void onFileUploaded(Chat chat, int position) {
        listenerView.onFileUploaded(chat, position);
    }

    @Override
    public void onFailed(Chat chat) {
        listenerView.onFailed(chat);
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
