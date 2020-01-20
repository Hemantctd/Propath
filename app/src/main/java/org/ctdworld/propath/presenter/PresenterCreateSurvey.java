package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.interactor.InteractorCreateSurvey;
import org.ctdworld.propath.model.GetGroupNames;
import org.ctdworld.propath.model.Survey;

import java.util.List;


public class PresenterCreateSurvey implements ContractSurvey.Presenter, ContractSurvey.Interactor.OnFinishedListener
{
    private ContractSurvey.View listener;
    private ContractSurvey.Interactor interactor;

    public PresenterCreateSurvey(Context context, ContractSurvey.View listener)
    {
        this.listener = listener;
        interactor = new InteractorCreateSurvey(this,context);
    }

    @Override
    public void onSuccess(String msg) {
        listener.onSuccess(msg);
    }

    @Override
    public void onFailed(String msg) {
        listener.onFailed(msg);
    }

    @Override
    public void onReceivedSurvey(Survey.SurveyData surveyData) {
        listener.onReceivedSurvey(surveyData);
    }


    @Override
    public void saveSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList, Survey.SurveyData.FreeResponse surveyFreeResponse) {
      interactor.saveSurvey(surveyQuestionsList,surveyFreeResponse);
    }

    @Override
    public void editSurvey(List<Survey.SurveyData.FreeResponse.Questions> surveyQusetionsList, Survey.SurveyData.FreeResponse surveyFreeResponse) {
        interactor.editFreeResponseSurvey(surveyQusetionsList,surveyFreeResponse);

    }

    @Override
    public void deleteSurvey(String survey_id,String survey_type) {
        interactor.deleteSurvey(survey_id, survey_type);
    }

    @Override
    public void shareSurvey(GetGroupNames groupNamesList) {
        interactor.shareSurvey(groupNamesList);
    }

    @Override
    public void copySurvey(String survey_id, String title,String survey_type) {
        interactor.copySurvey(survey_id,title,survey_type);
    }

    @Override
    public void saveYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionList, Survey.SurveyData.YesNo surveyYesNo) {
        interactor.saveYesNoSurvey(surveyQuestionList,surveyYesNo);
    }

    @Override
    public void requestSurvey() {
         interactor.requestSurvey();
    }

    @Override
    public void editMultipleChoiceSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice) {
        interactor.editMultipleChoiceSurvey(surveyQuestionsList,surveyMultipleChoice);
    }

    @Override
    public void editYesNoSurvey(List<Survey.SurveyData.YesNo.Questions> surveyQuestionsList, Survey.SurveyData.YesNo surveyYesNo) {
          interactor.editYesNoSurvey(surveyQuestionsList,surveyYesNo);
    }

    @Override
    public void onShowProgress() {
        listener.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        listener.onHideProgress();
    }

    @Override
    public void onSuccess() {
        listener.onSuccess();

    }

    @Override
    public void onFailed() {
        listener.onFailed();

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
       listener.onSetViewsDisabledOnProgressBarVisible();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
      listener.onSetViewsEnabledOnProgressBarGone();
    }

    @Override
    public void onShowMessage(String message) {
      listener.onShowMessage(message);
    }
}
