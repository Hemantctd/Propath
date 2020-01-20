package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractAddCoachFeedback;
import org.ctdworld.propath.interactor.InteractorAddCoachFeedback;
import org.ctdworld.propath.model.CoachData;


public abstract class PresenterAddCoachFeedback implements ContractAddCoachFeedback.Presenter, ContractAddCoachFeedback.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterAddCoachFeedback.class.getSimpleName();
    ContractAddCoachFeedback.Interactor interactor;
    ContractAddCoachFeedback.View listenerView;

    public PresenterAddCoachFeedback(Context context)
    {
        this.interactor = new InteractorAddCoachFeedback(this,context);
        this.listenerView = (ContractAddCoachFeedback.View) context;
    }

    @Override
    public void onFailed(String msg) {
        listenerView.onFailed(msg);
    }

    @Override
    public void onSuccess(String msg) {
      listenerView.onSuccess(msg);
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
    public void requestToGiveFeedback(CoachData coachData) {
        interactor.requestToGiveFeedback(coachData);

    }
}
