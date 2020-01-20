package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.SelfReview;

import java.util.List;

public interface ContractSelfReviewReport
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void getOnReceivedSelfReview(SelfReview selfReview, List<SelfReview> selfReviewList);
            void onFailed();
        }
        void getSelfReview(String userId, String review_id);
    }

    interface View extends ContractBase.View
    {
        void getOnReceivedSelfReview(SelfReview selfReview, List<SelfReview> selfReviewList);
        void onFailed();
    }

    interface Presenter
    {
        void getSelfReview(String userId, String review_id);
    }
}
