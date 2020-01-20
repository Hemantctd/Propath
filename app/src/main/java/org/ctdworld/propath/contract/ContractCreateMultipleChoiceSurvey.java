package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyMultipleChoice;

import java.util.List;

public interface ContractCreateMultipleChoiceSurvey {


    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess(String msg);
            void onFailed(String msg);
        }
        void saveSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice);

    }

    interface View  extends ContractBase.View
    {
        void onSuccess(String msg);
        void onFailed(String msg);
    }

    interface Presenter
    {
        void saveSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice);
    }
}
