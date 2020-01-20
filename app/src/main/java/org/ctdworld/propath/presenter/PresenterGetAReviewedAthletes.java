package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractReviewGotAllAthletes;
import org.ctdworld.propath.interactor.InteractorGetAllReviewedAthletes;
import org.ctdworld.propath.model.GetAthletes;

import java.util.List;


public abstract class PresenterGetAReviewedAthletes implements ContractReviewGotAllAthletes.Presenter, ContractReviewGotAllAthletes.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetAReviewedAthletes.class.getSimpleName();
    ContractReviewGotAllAthletes.Interactor interactor;
    ContractReviewGotAllAthletes.View listenerView;

    public PresenterGetAReviewedAthletes(Context context, ContractReviewGotAllAthletes.View listenerView)
    {
        this.interactor = new InteractorGetAllReviewedAthletes(this,context);
//        this.listenerView = (ContractReviewGotAllAthletes.View) context;
        this.listenerView = listenerView;

    }


    @Override
    public void onGotReviewedAthletes(List<GetAthletes> athletesList) {
        listenerView.onGotReviewedAthletes(athletesList);
    }

//    @Override
//    public void onResponseToSeeProgressReportSubmitted() {
//        listenerView.onResponseToSeeProgressReportSubmitted();
//    }
//
//    @Override
//    public void onProgressReportProgressSend() {
//        listenerView.onProgressReportProgressSend();
//    }

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


//
//    @Override
//    public void responseToSeeProgressReport(SchoolReview schoolReview) {
//        interactor.responseToSeeProgressReport(schoolReview);
//    }
//
//    @Override
//    public void requestToSeeProgressReport(SchoolReview schoolReview) {
//        interactor.requestToSeeProgressReport(schoolReview);
//    }


}
