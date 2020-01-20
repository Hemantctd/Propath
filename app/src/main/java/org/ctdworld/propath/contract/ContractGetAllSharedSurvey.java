package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.SurveyQusetions;

import java.util.List;


public interface ContractGetAllSharedSurvey {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {

            void onGetReceivedAllSubmittedSurvey(SurveyQusetions surveyQusetions,List<SurveyQusetions> qusetionsList);
            void onGetReceivedAllSharedSurvey(SurveyQusetions surveyQusetions, List<List<List<SurveyQusetions>>> qusetionsList);
            void onFailed(String msg);
            void onSuccess(String msg);

        }
        void getAllSharedSurvey();
        void submitSurvey(String survey_id,List<SurveyQusetions> surveyQusetionsList, List<SurveyQusetions> surveyAnswersList);
        void getAllSubmittedSurvey(String survey_id);
    }

    interface View extends ContractBase.View
    {
        void onGetReceivedAllSubmittedSurvey(SurveyQusetions surveyQusetions, List<SurveyQusetions> qusetionsList);
        void onGetReceivedAllSharedSurvey(SurveyQusetions surveyQusetions, List<List<List<SurveyQusetions>>> qusetionsList);
        void onFailed(String msg);
        void onSuccess(String msg);

    }

    interface Presenter
    {
        void getAllSharedSurvey();
        void submitSurvey(String survey_id,List<SurveyQusetions> surveyQusetionsList, List<SurveyQusetions> surveyAnswersList);
        void getAllSubmittedSurvey(String survey_id);


    }
}
