package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractEnableDisableSurvey;
import org.ctdworld.propath.interactor.InteractorEnableDisableSurvey;


public abstract class PresenterEnableDisableSurvey implements ContractEnableDisableSurvey.Presenter, ContractEnableDisableSurvey.Interactor.OnFinishedListener
{
    private ContractEnableDisableSurvey.View listener;
    private ContractEnableDisableSurvey.Interactor interactor;

    public PresenterEnableDisableSurvey(Context context, ContractEnableDisableSurvey.View listener)
    {
        this.listener = listener;
        interactor = new InteractorEnableDisableSurvey(this,context);
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
    public void enabledisableSurvey(String status,String show_anonymous,String survey_id) {

        interactor.enabledisableSurvey(status,show_anonymous,survey_id);

    }
}
