package org.ctdworld.propath.contract;


import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.Chat;

public interface ContractChat
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onChatReceived(Chat chat);
            void onChatSendSuccess(Chat chat);
            void onPreviousChatReceived(Chat chat); // to receive previous chat
            void onFileUploaded(Chat chat, int position);
            void onFailed(Chat chat);
        }
        void requestChat(Chat chat);  // to receive chat
        void requestPreviousChat(Chat chat, int countRequest);// to request previous
        void sendChat(Chat chat);
    }

    interface View extends ContractBase.View
    {
        void onChatReceived(Chat chat);  // to receive chat
        void onChatSendSuccess(Chat chat);  // to send chat
        void onPreviousChatReceived(Chat chat); // to receive previous chat
        void onFileUploaded(Chat chat, int position);
        void onFailed(Chat chat);
    }

    interface Presenter
    {
        void requestChat(Chat chat);
        void sendChat(Chat chat);
        void requestPreviousChat(Chat chat, int countRequest); // to request previous chat
    }
}
