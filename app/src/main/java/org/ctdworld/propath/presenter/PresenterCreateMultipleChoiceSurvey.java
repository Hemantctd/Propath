package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractCreateMultipleChoiceSurvey;
import org.ctdworld.propath.interactor.InteractorCreateMultipleChoiceSurvey;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyMultipleChoice;

import java.util.List;


public class PresenterCreateMultipleChoiceSurvey implements ContractCreateMultipleChoiceSurvey.Presenter, ContractCreateMultipleChoiceSurvey.Interactor.OnFinishedListener
{
    private ContractCreateMultipleChoiceSurvey.View listener;
    private ContractCreateMultipleChoiceSurvey.Interactor interactor;

    public PresenterCreateMultipleChoiceSurvey(Context context, ContractCreateMultipleChoiceSurvey.View listener)
    {
        this.listener = listener;
        interactor = new InteractorCreateMultipleChoiceSurvey(this,context);
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
    public void onShowProgress() {
        listener.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        listener.onHideProgress();
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

    @Override
    public void saveSurvey(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, Survey.SurveyData.MultipleChoice surveyMultipleChoice) {
        interactor.saveSurvey(surveyQuestionsList,surveyMultipleChoice);
    }
}
