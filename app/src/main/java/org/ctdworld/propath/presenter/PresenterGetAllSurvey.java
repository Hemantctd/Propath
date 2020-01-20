package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractGetAllSurvey;
import org.ctdworld.propath.interactor.InteractorGetAllSurvey;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyFreeResponse;
import org.ctdworld.propath.model.SurveyQusetions;

import java.util.List;


public abstract class PresenterGetAllSurvey implements ContractGetAllSurvey.Presenter, ContractGetAllSurvey.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetAllSurvey.class.getSimpleName();
    ContractGetAllSurvey.Interactor interactor;
    ContractGetAllSurvey.View listenerView;

    public PresenterGetAllSurvey(Context context, ContractGetAllSurvey.View listenerView)
    {
        this.interactor = new InteractorGetAllSurvey(this,context);
//        this.listenerView = (ContractGetAllAthletes.View) context;
        this.listenerView = listenerView;

    }




    @Override
    public void onSuccess(String msg) {
        listenerView.onSuccess(msg);
    }


//    @Override
//    public void onGetReceivedAllSurvey(List<SurveyQusetions> qusetionsList) {
//        listenerView.onGetReceivedAllSurvey(qusetionsList);
//    }

    @Override
    public void onReceivedGetAllSurvey(Survey survey) {
        listenerView.onReceivedGetAllSurvey(survey);
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
    public void getAllSurvey() {
       interactor.getAllSurvey();
    }
}
