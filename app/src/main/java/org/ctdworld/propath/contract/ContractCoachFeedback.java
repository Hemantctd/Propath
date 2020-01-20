package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.CoachData;

import java.util.List;

public interface ContractCoachFeedback
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onReceivedCoachFeedbackData(List<CoachData> coachDataList);
            void onReceivedCoachFeedbackDescription(CoachData coachData);
            void onFailed();
            void onSuccess(String msg);
            void onCoachFeedbackDeleted(String id);
            void onReceivedCoachSelfReview(List<CoachData> coachData);
            void onReceivedCoachSelfReviewDescription(CoachData coachData);

        }

        void getCoachFeedbackDescription(String userId, String review_id);
        void getCoachFeedback(String athlete_id, String roleId);
        void deleteCoachFeedback(String id, String roleId);
        void editCoachFeedback(CoachData coachData);
        void createCoachSelfReview(CoachData coachData);
        void getCoachSelfReview(String athleteId);
        void getCoachSelfReviewDescription(String userId, String review_id);
        void editCoachSelfReviewFeedback(CoachData coachData);
        void deleteCoachSelfReview(String id, String athleteId);


    }

    interface View extends ContractBase.View
    {
        void onReceivedCoachFeedbackData(List<CoachData> coachDataList);
        void onReceivedCoachFeedbackDescription(CoachData coachData);
        void onFailed();
        void onSuccess(String msg);
        void onCoachFeedbackDeleted(String id);
        void onReceivedCoachSelfReview(List<CoachData> coachData);
        void onReceivedCoachSelfReviewDescription(CoachData coachData);

    }

    interface Presenter
    {
        void getCoachFeedbackDescription(String userId, String review_id);
        void getCoachFeedback(String athlete_id, String roleId);
        void deleteCoachFeedback(String id, String roleId);
        void editCoachFeedback(CoachData coachData);
        void createCoachSelfReview(CoachData coachData);
        void getCoachSelfReview(String athleteId);
        void getCoachSelfReviewDescription(String userId, String review_id);
        void editCoachSelfReviewFeedback(CoachData coachData);
        void deleteCoachSelfReview(String id, String athleteId);



    }
}
