package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;

public interface ContractStatSurvey
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess(String msg);
            void onFailed(String msg);
            void onGetReceivedStatExcel(String data);
        }

        void statSurvey(String survey_id);

    }

    interface View  extends ContractBase.View
    {
        void onGetReceivedStatExcel(String data);
        void onSuccess(String msg);
        void onFailed(String msg);
    }

    interface Presenter
    {

        void statSurvey(String survey_id);

    }
}
