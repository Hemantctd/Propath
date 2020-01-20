package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.GetAthletes;

import java.util.List;


public interface ContractReviewGotAllAthletes {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onGotReviewedAthletes(List<GetAthletes> athletesList);
//            void onResponseToSeeProgressReportSubmitted();
//            void onProgressReportProgressSend();
            void onFailed();
        }
        void getAthletesReviewed();
//        void responseToSeeProgressReport(SchoolReview schoolReview);
//        void requestToSeeProgressReport(SchoolReview schoolReview);
    }

    interface View extends ContractBase.View
    {
        void onGotReviewedAthletes(List<GetAthletes> athletesList);
//        void onResponseToSeeProgressReportSubmitted();
//        void onProgressReportProgressSend();
        void onFailed();
    }

    interface Presenter
    {
        void getAthletesReviewed();
       // void responseToSeeProgressReport(SchoolReview schoolReview);
        //void requestToSeeProgressReport(SchoolReview schoolReview);
    }
}
