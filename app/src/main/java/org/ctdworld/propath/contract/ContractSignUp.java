package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.User;

public interface ContractSignUp
{
    interface Interactor
    {
        interface OnFinishedListener
        {
            void onShowProgress();
            void onHideProgress();
            void onSignUpSuccess();
            void onSignUpFailed();
            void onEmailAlreadyRegistered();
            void onSetViewsDisabledOnProgressBarVisible();
            void onSetViewsEnabledOnProgressBarGone();
            void onConnectionError();
        }
        void createAccount(User user);
    }

    interface View
    {
        void onShowProgress();
        void onHideProgress();
        void onSignUpSuccess();
        void onSignUpFailed();
        void onEmailAlreadyRegistered();
        void onSetViewsDisabledOnProgressBarVisible();
        void onSetViewsEnabledOnProgressBarGone();
        void onConnectionError();
    }

    interface Presenter
    {
        void createAccount(User user);
    }
}
