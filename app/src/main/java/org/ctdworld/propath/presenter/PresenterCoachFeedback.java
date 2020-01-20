package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractCoachFeedback;
import org.ctdworld.propath.interactor.InteractorCoachFeedback;
import org.ctdworld.propath.model.CoachData;

import java.util.List;


public abstract class PresenterCoachFeedback implements ContractCoachFeedback.Presenter, ContractCoachFeedback.Interactor.OnFinishedListener
{
    //private static final String TAG = PresenterCoachFeedback.class.getSimpleName();
    ContractCoachFeedback.Interactor interactor;
    ContractCoachFeedback.View listenerView;

    public PresenterCoachFeedback(Context context, ContractCoachFeedback.View view)
    {
        this.interactor = new InteractorCoachFeedback(this,context);
        this.listenerView = view;
    }


    @Override
    public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {
        listenerView.onReceivedCoachFeedbackData(coachDataList);
    }

    @Override
    public void onReceivedCoachFeedbackDescription(CoachData coachData) {
        listenerView.onReceivedCoachFeedbackDescription(coachData);
    }

    @Override
    public void onFailed() {
      listenerView.onFailed();
    }

    @Override
    public void onSuccess(String msg) {
        listenerView.onSuccess(msg);
    }

    @Override
    public void onCoachFeedbackDeleted(String id) {
       listenerView.onCoachFeedbackDeleted(id);
    }

    @Override
    public void onReceivedCoachSelfReview(List<CoachData> coachData) {
        listenerView.onReceivedCoachSelfReview(coachData);
    }

    @Override
    public void onReceivedCoachSelfReviewDescription(CoachData coachData) {
        listenerView.onReceivedCoachSelfReviewDescription(coachData);
    }


    @Override
    public void getCoachFeedbackDescription(String userId, String review_id) {
        interactor.getCoachFeedbackDescription(userId,review_id);
    }

    @Override
    public void getCoachFeedback(String athlete_id, String roleId) {
      interactor.getCoachFeedback(athlete_id,roleId);
    }

    @Override
    public void deleteCoachFeedback(String id, String roleId) {
      interactor.deleteCoachFeedback(id , roleId);
    }

    @Override
    public void editCoachFeedback(CoachData coachData) {
        interactor.editCoachFeedback(coachData);
    }

    @Override
    public void createCoachSelfReview(CoachData coachData) {
        interactor.createCoachSelfReview(coachData);
    }

    @Override
    public void getCoachSelfReview(String athleteId) {
       interactor.getCoachSelfReview(athleteId);
    }

    @Override
    public void getCoachSelfReviewDescription(String userId, String review_id) {
        interactor.getCoachSelfReviewDescription(userId,review_id);

    }

    @Override
    public void editCoachSelfReviewFeedback(CoachData coachData) {
        interactor.editCoachSelfReviewFeedback(coachData);
    }

    @Override
    public void deleteCoachSelfReview(String id, String athleteId) {
        interactor.deleteCoachSelfReview(id,athleteId);
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
