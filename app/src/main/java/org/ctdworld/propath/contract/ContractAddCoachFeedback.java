package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.CoachData;


public interface ContractAddCoachFeedback {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onFailed(String msg);
            void onSuccess(String msg);

        }
        void requestToGiveFeedback(CoachData coachData);

    }

    interface View extends ContractBase.View
    {
        void onFailed(String msg);
        void onSuccess(String msg);

    }

    interface Presenter
    {
        void requestToGiveFeedback(CoachData coachData);
    }
}
