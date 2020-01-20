package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractChatUsers;
import org.ctdworld.propath.interactor.InteractorChatUsersGroupList;
import org.ctdworld.propath.model.Chat;


public class PresenterChatUsers implements ContractChatUsers.Presenter, ContractChatUsers.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterChatUsers.class.getSimpleName();
    ContractChatUsers.Interactor interactor;
    ContractChatUsers.View listenerView;

    public PresenterChatUsers(Context context)
    {
        this.interactor = new InteractorChatUsersGroupList(this,context);
        this.listenerView = (ContractChatUsers.View) context;
    }


    @Override
    public void onChatUserReceived(Chat chat) {
        listenerView.onChatUserReceived(chat);
    }

    @Override
    public void onShowMessage(String message) {
        listenerView.onShowMessage(message);
    }

    @Override
    public void onFailed() {
        listenerView.onFailed();
    }

    @Override
    public void requestChatUsers(String userId, String chatListType) {
        interactor.requestChatUsers(userId, chatListType);
    }


}
