package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.Chat;

public interface ContractGroupChatInjury
{
    interface Interactor
    {
        void createGroup(Chat chat);  // to create group
        void editGroupName(Chat chat);// to request previous
        void editGroupImage(Chat chat); // to editGroupImage
        void removeParticipant(Chat chat, int positionInAdapter); // to remove participant
        void exitGroup(Chat chat); // to exit group
        void requestGroupList(Chat chat);
        void requestGroupMemberList(Chat chat);



        // super interface for all listener
        interface OnFinishedListener{
            void onShowMessage(String message);
            void onFailed();
            void onGroupCreated(Chat chat);
            void onEditGroupNameSuccessful(Chat chat);
            void onEditGroupImageSuccessful(Chat chat);
            void onRemoveParticipantSuccess(Chat chat, int positionInAdapter);
            void onGroupExitSuccess(Chat chat);
            void onReceivedGroup(Chat chat);
            void onReReceivedGroupMember(Chat chat);
        }
    }


    // super View for all views
    interface View{
        void onFailed();
        void onGroupCreated(Chat chat);
        void onEditGroupNameSuccessful(Chat chat);
        void onEditGroupImageSuccessful(Chat chat);
        void onRemoveParticipantSuccess(Chat chat, int positionInAdapter);
        void onGroupExitSuccess(Chat chat);
        void onShowMessage(String message);
        void onReceivedGroup(Chat chat);
        void onReReceivedGroupMember(Chat chat);
    };


    interface Presenter
    {
        void createGroup(Chat chat);  // to create group
        void editGroupName(Chat chat);// to request previous
        void editGroupImage(Chat chat); // to editGroupImage
        void removeParticipant(Chat groupChat, int positionInAdapter); // to remove participant
        void exitGroup(Chat groupChat); // to exit group
        void requestGroupList(Chat chat);
        void requestGroupMemberList(Chat groupChatInjury);
    }
}
