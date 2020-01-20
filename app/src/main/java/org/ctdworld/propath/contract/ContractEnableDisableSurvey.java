package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;


public interface ContractEnableDisableSurvey {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onFailed(String msg);
            void onSuccess(String msg);

        }
        void enabledisableSurvey(String status,String show_anonymous, String survey_id);

    }

    interface View extends ContractBase.View
    {
        void onFailed(String msg);
        void onSuccess(String msg);

    }

    interface Presenter
    {
        void enabledisableSurvey(String status,String show_anonymous, String survey_id);
    }
}
