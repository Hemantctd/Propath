package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.Profile;

public interface ContractUpdateProfile
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onFailed();
        }
        void updateProfile(Profile profile);
    }

    interface View extends ContractBase.View
    {
        void onFailed();
    }

    interface Presenter
    {
        void updateProfile(Profile profile);

    }
}
