package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.Profile;

public interface ContractProfileView
{
    interface Interactor
    {
        interface OnFinishedListener
        {
            void onProfileReceived(Profile profile);
            void onReceivedFriendStatus(String friendStatus);
            void onFailed(String message);
        }
        void requestProfile(String userId);
        void checkFriendStatus(String loginUserId, String otherUserId);
    }

    interface View
    {
        void onProfileReceived(Profile profile);
        void onReceivedFriendStatus(String friendStatus);
        void onFailed(String message);
    }

    interface Presenter
    {
        void requestProfile(String userId);
        void checkFriendStatus(String loginUserId, String otherUserId);
    }
}