package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractStatSurvey;
import org.ctdworld.propath.interactor.InteractorStatSurvey;


public abstract class PresenterStatSurvey implements ContractStatSurvey.Presenter, ContractStatSurvey.Interactor.OnFinishedListener
{
    private ContractStatSurvey.View listener;
    private ContractStatSurvey.Interactor interactor;

    public PresenterStatSurvey(Context context, ContractStatSurvey.View listener)
    {
        this.listener = listener;
        interactor = new InteractorStatSurvey(this,context);
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
    public void onGetReceivedStatExcel(String data) {
        listener.onGetReceivedStatExcel(data);
    }


    @Override
    public void statSurvey(String survey_id) {
        interactor.statSurvey(survey_id);
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
}
