package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractSelfReview;
import org.ctdworld.propath.interactor.InteractorSelfReview;
import org.ctdworld.propath.model.SelfReview;

import java.util.List;


public class PresenterSelfReview implements ContractSelfReview.Presenter, ContractSelfReview.Interactor.OnFinishedListener
{
    private ContractSelfReview.View listener;
    private ContractSelfReview.Interactor interactor;

    public PresenterSelfReview(Context context, ContractSelfReview.View listener)
    {
        this.listener = listener;
        interactor = new InteractorSelfReview(this,context);
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
    public void onSelfListDeleted(String id) {
            listener.onSelfListDeleted(id);    }

    @Override
    public void onReceivedSelfReportList(List<SelfReview> selfReviewList) {
        listener.onReceivedSelfReportList(selfReviewList);
    }

    @Override
    public void saveSelfReview(List<SelfReview> selfReview) {
      interactor.saveSelfReview(selfReview);
    }

    @Override
    public void editSelfReview(List<SelfReview> selfReviewList, SelfReview selfReview) {
     interactor.editSelfReview(selfReviewList,selfReview);
    }

    @Override
    public void getSelfReview(String athlete_id) {
        interactor.getSelfReview(athlete_id);
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
