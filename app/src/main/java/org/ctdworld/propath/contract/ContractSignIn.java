package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.User;

public interface ContractSignIn
{
    interface Interactor
    {
        interface OnFinishedListener
        {
            void onShowProgress();
            void onHideProgress();
            void onEmailOrPasswordWrong();
            void onSuccess();
            void onShowMessage(String message);
            void onSetViewsDisabledOnProgressBarVisible();
            void onSetViewsEnabledOnProgressBarGone();
            void onConnectionError();
        }
        void signIn(User user);
    }

    interface View
    {
        void onShowProgress();
        void onHideProgress();
        void onEmailOrPasswordWrong();
        void onSuccess();
        void onShowMessage(String message);
        void onSetViewsDisabledOnProgressBarVisible();
        void onSetViewsEnabledOnProgressBarGone();
        void onConnectionError();
    }

    interface Presenter
    {
        void signIn(User user);
    }
}
