package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.Profile;

public interface ContractProfileUpdate
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onComplete(String message);
        }
        //void saveUserProfile(Profile profile);
        void updateProfile(Profile profile);
    }

    interface View extends ContractBase.View
    {
        void onComplete(String message);
    }

    interface Presenter
    {
        //void saveUserProfile(Profile profile);
        void updateProfile(Profile profile);

    }
}
