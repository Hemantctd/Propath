package org.ctdworld.propath.presenter;

import android.content.Context;
import org.ctdworld.propath.contract.ContractSelfReviewedAthletes;
import org.ctdworld.propath.interactor.InteractorSelfReviewedAthletes;
import org.ctdworld.propath.model.GetAthletes;

import java.util.List;


public abstract class PresenterSelfReviewedAthletes implements ContractSelfReviewedAthletes.Presenter, ContractSelfReviewedAthletes.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterSelfReviewedAthletes.class.getSimpleName();
    ContractSelfReviewedAthletes.Interactor interactor;
    ContractSelfReviewedAthletes.View listenerView;

    public PresenterSelfReviewedAthletes(Context context, ContractSelfReviewedAthletes.View listenerView)
    {
        this.interactor = new InteractorSelfReviewedAthletes(this,context);
        this.listenerView = listenerView;

    }


    @Override
    public void onGotReviewedAthletes(List<GetAthletes> athletesList) {
        listenerView.onGotReviewedAthletes(athletesList);
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
    public void getAthletesReviewed() {
        {
            interactor.getAthletesReviewed();
        }
    }


}
