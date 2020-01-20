package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.GetGroupNames;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyFreeResponse;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.model.SurveyYesNo;

import java.util.List;

public interface ContractSurvey
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess(String msg);
            void onFailed(String msg);
            void onReceivedSurvey(Survey.SurveyData surveyData);

        }
        void saveSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList, Survey.SurveyData.FreeResponse surveyFreeResponse);
        void editFreeResponseSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQusetionsList, Survey.SurveyData.FreeResponse surveyFreeResponse);
        void deleteSurvey(String survey_id,String survey_type);
        void shareSurvey(GetGroupNames groupNames);
        void copySurvey(String survey_id, String title,String survey_type);
        void saveYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionList, Survey.SurveyData.YesNo surveyYesNo);
        void requestSurvey();
        void editMultipleChoiceSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice);
        void editYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionsList, Survey.SurveyData.YesNo surveyYesNo);


    }

    interface View  extends ContractBase.View
    {
        void onSuccess(String msg);
        void onFailed(String msg);
        void onReceivedSurvey(Survey.SurveyData surveyData);

    }

    interface Presenter
    {
        void saveSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList, Survey.SurveyData.FreeResponse surveyFreeResponse);
        void editSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQusetionsList, Survey.SurveyData.FreeResponse surveyFreeResponse);

        void deleteSurvey(String survey_id,String survey_type);
        void shareSurvey(GetGroupNames groupNames);
        void copySurvey(String survey_id, String title,String survey_type);
        void saveYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionList, Survey.SurveyData.YesNo surveyYesNo);
        void requestSurvey();
        void editMultipleChoiceSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice);
        void editYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionsList, Survey.SurveyData.YesNo surveyYesNo);


    }
}
