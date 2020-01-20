package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractSelfReviewReport;
import org.ctdworld.propath.interactor.InteractorSelfReviewReport;
import org.ctdworld.propath.model.SelfReview;

import java.util.List;


public class PresenterSelfReviewReport implements ContractSelfReviewReport.Presenter, ContractSelfReviewReport.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterSelfReviewReport.class.getSimpleName();
    ContractSelfReviewReport.Interactor interactor;
    ContractSelfReviewReport.View listenerView;

    public PresenterSelfReviewReport(Context context, ContractSelfReviewReport.View view)
    {
        this.interactor = new InteractorSelfReviewReport(this,context);
        this.listenerView = view;
    }

    @Override
    public void getOnReceivedSelfReview(SelfReview selfReview, List<SelfReview> selfReviewList) {
        listenerView.getOnReceivedSelfReview(selfReview,selfReviewList);
    }

    @Override
    public void onFailed() {
       onFailed();
    }

    @Override
    public void getSelfReview(String userId,String review_id) {
        interactor.getSelfReview(userId,review_id);
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
