package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractGetAllAthletes;
import org.ctdworld.propath.interactor.InteractorGetAllAthletes;
import org.ctdworld.propath.model.GetAthletes;

import java.util.List;


public abstract class PresenterGetAllAthletes implements ContractGetAllAthletes.Presenter, ContractGetAllAthletes.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetAllAthletes.class.getSimpleName();
    ContractGetAllAthletes.Interactor interactor;
    ContractGetAllAthletes.View listenerView;

    public PresenterGetAllAthletes(Context context, ContractGetAllAthletes.View listenerView)
    {
        this.interactor = new InteractorGetAllAthletes(this,context);
//        this.listenerView = (ContractGetAllAthletes.View) context;
        this.listenerView = listenerView;

    }

    @Override
    public void requestAllAthletes() {
        interactor.requestAllAthletes();
    }

//    @Override
//    public void responseToGiveSchoolReview(SchoolReview schoolReview) {
//        interactor.responseToGiveSchoolReview(schoolReview);
//
//    }
//
//    @Override
//    public void requestToGiveSchoolReview(SchoolReview schoolReview) {
//        interactor.requestToGiveSchoolReview(schoolReview);
//    }

//    @Override
//    public void onResponseToGiveSchoolReviewSubmitted() {
//        listenerView.onResponseToGiveSchoolReviewSubmitted();
//
//    }
//
//    @Override
//    public void onAddSchoolReviewByTeacherRequestSend() {
//        listenerView.onAddSchoolReviewByTeacherRequestSend();
//
//    }

    @Override
    public void onGetAllAthletes(List<GetAthletes> athletesList) {
        listenerView.onGetAllAthletes(athletesList);
    }


    @Override
    public void onSuccess(String msg) {
        listenerView.onSuccess(msg);
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

}
