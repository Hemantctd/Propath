package org.ctdworld.propath.contract;


import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.User;

import java.util.List;

public interface ContractGetRegisteredUsers
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onGetRegisteredUsers(List<User> userList);
            void onFailed();
        }
        void requestAllUsers();
    }

    interface View extends ContractBase.View
    {
        void onGetRegisteredUsers(List<User> userList);
        void onFailed();
    }

    interface Presenter
    {
        void requestAllUsers();
    }
}
