package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractSchoolReview;
import org.ctdworld.propath.interactor.InteractorSchoolReview;
import org.ctdworld.propath.model.SchoolReview;


import java.util.List;


public class PresenterSchoolReview implements ContractSchoolReview.Presenter, ContractSchoolReview.Interactor.OnFinishedListener
{
    private ContractSchoolReview.View listener;
    private ContractSchoolReview.Interactor interactor;

    public PresenterSchoolReview(Context context, ContractSchoolReview.View listener)
    {
        this.listener = listener;
        interactor = new InteractorSchoolReview(this,context);
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
    public void onProgressListDeleted(String id) {
        listener.onProgressListDeleted(id);
    }

    @Override
    public void onReceivedProgressReportList(List<SchoolReview> schoolReviewList) {
        listener.onReceivedProgressReportList(schoolReviewList);
    }

    @Override
    public void saveSchoolReview(List<SchoolReview> schoolReview) {
      interactor.saveSchoolReview(schoolReview);
    }

    @Override
    public void editSchoolReview(List<SchoolReview> schoolReviewList, SchoolReview schoolReview) {
        interactor.editSchoolReview(schoolReviewList,schoolReview);
    }

    @Override
    public void getSchoolReview(String athlete_id) {
      interactor.getSchoolReview(athlete_id);
    }

    @Override
    public void deleteReview(String id) {
        interactor.deleteReview(id);
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
