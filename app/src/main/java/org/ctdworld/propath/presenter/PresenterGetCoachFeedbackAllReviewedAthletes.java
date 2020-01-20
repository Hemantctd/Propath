package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractCoachFeedbackGotAllAthletes;
import org.ctdworld.propath.interactor.InteractorCoachFeedbackGetAllReviewedAthletes;
import org.ctdworld.propath.model.GetAthletes;

import java.util.List;


public abstract class PresenterGetCoachFeedbackAllReviewedAthletes implements ContractCoachFeedbackGotAllAthletes.Presenter, ContractCoachFeedbackGotAllAthletes.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetCoachFeedbackAllReviewedAthletes.class.getSimpleName();
    ContractCoachFeedbackGotAllAthletes.Interactor interactor;
    ContractCoachFeedbackGotAllAthletes.View listenerView;

    public PresenterGetCoachFeedbackAllReviewedAthletes(Context context, ContractCoachFeedbackGotAllAthletes.View listenerView)
    {
        this.interactor = new InteractorCoachFeedbackGetAllReviewedAthletes(this,context);
        this.listenerView = listenerView;

    }

    @Override
    public void onGotReviewedAthletes(List<GetAthletes> athletesList) {
        listenerView.onGotReviewedAthletes(athletesList);
    }

    @Override
    public void onGotSelfReviewedAthletes(List<GetAthletes> athletesList) {
        listenerView.onGotSelfReviewedAthletes(athletesList);
    }


    @Override
    public void onFailed() {
        listenerView.onFailed();
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
    public void getAthletesReviewed()
        {
            interactor.getAthletesReviewed();
        }


    @Override
    public void getAthleteSelfReviewed() {
        interactor.getAthleteSelfReviewed();
    }

}
