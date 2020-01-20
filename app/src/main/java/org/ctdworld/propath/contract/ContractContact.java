package org.ctdworld.propath.contract;


import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.User;

import java.util.List;

public interface ContractContact
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onReceiveAllContact(List<User> userList);
            void onContactDeleted(String userId);
            void onFailed();
        }
        void requestAllContacts();
        void deleteContact(String userId);
    }

    interface View extends ContractBase.View
    {
        void onReceiveAllContacts(List<User> userList);
        void onContactDeleted(String userId);
        void onFailed();
    }

    interface Presenter
    {
        void requestAllContacts();
        void deleteContact(String userId);
    }
}
