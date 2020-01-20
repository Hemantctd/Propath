package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.model.User;

import java.util.List;

public interface ContractChatAddParticipantsToGroup
{
    interface Interactor
    {
        void requestContactUsers(Chat chat);  // to create group
        void addParticipantsToGroup(Chat chat, List<User> userList);  // to create group


        // super interface for all listener
        interface OnFinishedListener
        {
            void onShowMessage(String message);
            void onFailed(String message);
            void onReceivedContactUsers(List<User> userList);
            void onParticipantsAddedSuccessfully();
        }
    }


    // super View for all views
    interface View{
        void onShowMessage(String message);
        void onFailed(String message);
        void onReceivedContactUsers(List<User> userList);
        void onParticipantsAddedSuccessfully();
    };


    interface Presenter
    {
        void requestContactUsers(Chat chat);  // to create group
        void addParticipantsToGroup(Chat chat, List<User> userList);  // to create group
    }
}
