package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.GetAthletes;

import java.util.List;


public interface ContractCoachFeedbackGotAllAthletes {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onGotReviewedAthletes(List<GetAthletes> athletesList);
            void onGotSelfReviewedAthletes(List<GetAthletes> athletesList);

            void onFailed();
        }
        void getAthletesReviewed();
        void getAthleteSelfReviewed();

    }

    interface View extends ContractBase.View
    {
        void onGotReviewedAthletes(List<GetAthletes> athletesList);
        void onGotSelfReviewedAthletes(List<GetAthletes> athletesList);

        void onFailed();
    }

    interface Presenter
    {
        void getAthletesReviewed();
        void getAthleteSelfReviewed();

    }
}
