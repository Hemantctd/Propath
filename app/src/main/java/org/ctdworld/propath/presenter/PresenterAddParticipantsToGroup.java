package org.ctdworld.propath.presenter;

import android.content.Context;
import android.util.Log;

import org.ctdworld.propath.contract.ContractChatAddParticipantsToGroup;
import org.ctdworld.propath.interactor.InteractorChatAddParticipantsToGroup;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.model.User;

import java.util.List;


public class PresenterAddParticipantsToGroup implements ContractChatAddParticipantsToGroup.Presenter, ContractChatAddParticipantsToGroup.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterAddParticipantsToGroup.class.getSimpleName();
    private ContractChatAddParticipantsToGroup.Interactor interactor;
    private ContractChatAddParticipantsToGroup.View listener;

    public PresenterAddParticipantsToGroup(Context context, ContractChatAddParticipantsToGroup.View listener)
    {
        this.interactor = new InteractorChatAddParticipantsToGroup(context, this);
        this.listener = listener;
        Log.i(TAG,"listener = "+listener);

    }


    @Override
    public void onShowMessage(String message) {
        listener.onShowMessage(message);
    }

    @Override
    public void onFailed(String message) {
        listener.onFailed(message);
    }

    @Override
    public void onReceivedContactUsers(List<User> userList) {
        listener.onReceivedContactUsers(userList);
    }

    @Override
    public void onParticipantsAddedSuccessfully() {
        listener.onParticipantsAddedSuccessfully();
    }

    @Override
    public void requestContactUsers(Chat chat) {
        interactor.requestContactUsers(chat);
    }

    @Override
    public void addParticipantsToGroup(Chat chat, List<User> userList) {
        interactor.addParticipantsToGroup(chat, userList);
    }
}
