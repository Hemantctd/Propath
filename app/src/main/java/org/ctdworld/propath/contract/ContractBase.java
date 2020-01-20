package org.ctdworld.propath.contract;

public interface ContractBase
{
    interface OnFinishedListener
    {
        void onShowProgress();
        void onHideProgress();
        void onSetViewsDisabledOnProgressBarVisible();
        void onSetViewsEnabledOnProgressBarGone();
        void onShowMessage(String message);
    }

    interface View
    {
        void onShowProgress();
        void onHideProgress();
        void onSetViewsDisabledOnProgressBarVisible();
        void onSetViewsEnabledOnProgressBarGone();
        void onShowMessage(String message);
    }

}
