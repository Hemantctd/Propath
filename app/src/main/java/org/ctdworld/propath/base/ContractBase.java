package org.ctdworld.propath.base;

public interface ContractBase
{
    public interface OnFinishedListener
    {
        void onShowProgress();
        void onHideProgress();
        void onSuccess();
        void onFailed();
        void onSetViewsDisabledOnProgressBarVisible();
        void onSetViewsEnabledOnProgressBarGone();
        void onShowMessage(String message);
    }

    public interface View
    {
        void onShowProgress();
        void onHideProgress();
        void onSuccess();
        void onFailed();
        void onSetViewsDisabledOnProgressBarVisible();
        void onSetViewsEnabledOnProgressBarGone();
        void onShowMessage(String message);
    }
}
