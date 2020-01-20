package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractGetAllSharedSurvey;
import org.ctdworld.propath.interactor.InteractorGetAllSharedSurvey;
import org.ctdworld.propath.model.SurveyQusetions;

import java.util.List;


public abstract class PresenterGetAllSharedSurvey implements ContractGetAllSharedSurvey.Presenter, ContractGetAllSharedSurvey.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetAllSharedSurvey.class.getSimpleName();
    ContractGetAllSharedSurvey.Interactor interactor;
    ContractGetAllSharedSurvey.View listenerView;

    public PresenterGetAllSharedSurvey(Context context, ContractGetAllSharedSurvey.View listenerView)
    {
        this.interactor = new InteractorGetAllSharedSurvey(this,context);
        this.listenerView = listenerView;

    }




    @Override
    public void onSuccess(String msg) {
        listenerView.onSuccess(msg);
    }


    @Override
    public void onGetReceivedAllSubmittedSurvey(SurveyQusetions surveyQusetions, List<SurveyQusetions> qusetionsList) {
        listenerView.onGetReceivedAllSubmittedSurvey(surveyQusetions,qusetionsList);
    }

    @Override
    public void onGetReceivedAllSharedSurvey(SurveyQusetions surveyQusetions, List<List<List<SurveyQusetions>>> qusetionsList) {
        listenerView.onGetReceivedAllSharedSurvey(surveyQusetions,qusetionsList);
    }

    @Override
    public void onFailed(String msg) {
        listenerView.onFailed(msg);
    }
    @Override
    public void onShowProgress() {
        listenerView.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        listenerView.onHideProgress();
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        listenerView.onSetViewsDisabledOnProgressBarVisible();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        listenerView.onSetViewsEnabledOnProgressBarGone();
    }

    @Override
    public void onShowMessage(String message) {
        listenerView.onShowMessage(message);
    }

    @Override
    public void getAllSharedSurvey() {
       interactor.getAllSharedSurvey();
    }

    @Override
    public void submitSurvey(String survey_id, List<SurveyQusetions> surveyQusetionsList, List<SurveyQusetions> surveyAnswersList) {
        interactor.submitSurvey(survey_id,surveyQusetionsList,surveyAnswersList);
    }

    @Override
    public void getAllSubmittedSurvey(String survey_id) {
     interactor.getAllSubmittedSurvey(survey_id);
    }
}
