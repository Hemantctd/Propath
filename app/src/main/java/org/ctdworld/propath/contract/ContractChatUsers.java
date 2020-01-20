package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.Chat;

// this is contact class for list of users to whom logged in user has chatted
public interface ContractChatUsers
{
    interface Interactor
    {
        interface OnFinishedListener
        {
            void onChatUserReceived(Chat chat);
            void onShowMessage(String message);
            void onFailed();
        }
        void requestChatUsers(String userId, String chatListType);  // to request users list to whom logged in user has chatted
    }

    interface View
    {
        void onChatUserReceived(Chat chat);  // to receive chat
        void onShowMessage(String message);
        void onFailed();
    }

    interface Presenter
    {
        void requestChatUsers(String userId, String chatListType);  // to request users list to whom logged in user has chatted
    }
}
