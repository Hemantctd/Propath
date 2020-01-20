package org.ctdworld.propath.contract;


import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.User;

public interface ContractResetPassword
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess();
            void onFailed();
        }
        void resetPassword(User user);
    }

    interface View extends ContractBase.View
    {
        void onSuccess();
        void onFailed();
    }

    interface Presenter
    {
        void resetPassword(User user);
    }
}
