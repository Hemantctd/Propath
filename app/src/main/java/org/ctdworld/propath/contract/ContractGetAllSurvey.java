package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyFreeResponse;
import org.ctdworld.propath.model.SurveyQusetions;

import java.util.List;


public interface ContractGetAllSurvey {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {

            void onReceivedGetAllSurvey(Survey survey);

            //void onGetReceivedAllSurvey(List<SurveyQusetions> qusetionsList);
            void onFailed(String msg);
            void onSuccess(String msg);

        }
        void getAllSurvey();

    }

    interface View
    {
        void onReceivedGetAllSurvey(Survey survey);

        // void onGetReceivedAllSurvey(List<SurveyQusetions> qusetionsList);
        void onFailed(String msg);
        void onSuccess(String msg);
        void onShowMessage(String message);
        void onShowProgress();
        void onHideProgress();
        void onSetViewsDisabledOnProgressBarVisible();
        void onSetViewsEnabledOnProgressBarGone();

    }

    interface Presenter
    {
        void getAllSurvey();

    }
}
