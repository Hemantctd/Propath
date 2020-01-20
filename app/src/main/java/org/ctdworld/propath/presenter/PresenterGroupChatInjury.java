package org.ctdworld.propath.presenter;

import android.content.Context;
import android.util.Log;

import org.ctdworld.propath.contract.ContractGroupChatInjury;
import org.ctdworld.propath.interactor.InteractorGroupChatInjury;
import org.ctdworld.propath.model.Chat;


public class PresenterGroupChatInjury implements ContractGroupChatInjury.Presenter, ContractGroupChatInjury.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGroupChatInjury.class.getSimpleName();
    private ContractGroupChatInjury.Interactor interactor;
    private ContractGroupChatInjury.View listener;

    public PresenterGroupChatInjury(Context context, ContractGroupChatInjury.View listener)
    {
        this.interactor = new InteractorGroupChatInjury(context, this);
        this.listener = listener;
        Log.i(TAG,"listener = "+listener);

    }


    @Override
    public void onShowMessage(String message) {
        listener.onShowMessage(message);
    }

    @Override
    public void onFailed() {
        listener.onFailed();
    }

    @Override
    public void onGroupCreated(Chat chat) {
        listener.onGroupCreated(chat);
    }

    @Override
    public void onEditGroupNameSuccessful(Chat chat) {
        listener.onEditGroupNameSuccessful(chat);
    }

    @Override
    public void onEditGroupImageSuccessful(Chat chat) {
        listener.onEditGroupImageSuccessful(chat);
    }

    @Override
    public void onRemoveParticipantSuccess(Chat chat, int positionInAdapter) {
        listener.onRemoveParticipantSuccess(chat, positionInAdapter);
    }

    @Override
    public void onGroupExitSuccess(Chat chat) {
        listener.onGroupExitSuccess(chat);
    }

    @Override
    public void onReceivedGroup(Chat chat) {
        listener.onReceivedGroup(chat);
    }

    @Override
    public void onReReceivedGroupMember(Chat chat) {
        listener.onReReceivedGroupMember(chat);
    }

    @Override
    public void createGroup(Chat chat) {
        interactor.createGroup(chat);
    }

    @Override
    public void editGroupName(Chat chat) {
        interactor.editGroupName(chat);
    }

    @Override
    public void editGroupImage(Chat chat) {
        interactor.editGroupImage(chat);
    }

    @Override
    public void removeParticipant(Chat groupChat, int positionInAdapter) {
        interactor.removeParticipant(groupChat, positionInAdapter);
    }

    @Override
    public void exitGroup(Chat groupChat) {
        interactor.exitGroup(groupChat);
    }

    @Override
    public void requestGroupList(Chat chat) {
        interactor.requestGroupList(chat);
    }


    @Override
    public void requestGroupMemberList(Chat groupChatInjury) {
        interactor.requestGroupMemberList(groupChatInjury);
    }
}
