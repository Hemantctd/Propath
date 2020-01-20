package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractProgressReport;
import org.ctdworld.propath.interactor.InteractorProgressReport;
import org.ctdworld.propath.model.SchoolReview;

import java.util.List;


public abstract class PresenterProgressReport implements ContractProgressReport.Presenter, ContractProgressReport.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterProgressReport.class.getSimpleName();
    ContractProgressReport.Interactor interactor;
    ContractProgressReport.View listenerView;

    public PresenterProgressReport(Context context, ContractProgressReport.View view)
    {
        this.interactor = new InteractorProgressReport(this,context);
        this.listenerView = view;
    }

    @Override
    public void getOnReceivedProgressReport(SchoolReview progressReport, List<SchoolReview> progressReportList) {
        listenerView.getOnReceivedProgressReport(progressReport,progressReportList);
    }

    @Override
    public void onFailed() {
       onFailed();
    }

    @Override
    public void getProgressReport(String userId,String review_id) {
        interactor.getProgressReport(userId,review_id);
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
