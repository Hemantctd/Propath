package org.ctdworld.propath.contract;


import org.ctdworld.propath.base.ContractBase;

public interface ContractNotification
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess();
            void onFailed();
        }

    }

    interface View extends ContractBase.View
    {
        void onSuccess();
        void onFailed();
    }

    interface Presenter
    {

    }
}
