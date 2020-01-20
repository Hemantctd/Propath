package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.SelfReview;

import java.util.List;

public interface ContractSelfReview
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess(String msg);
            void onFailed(String msg);
            void onSelfListDeleted(String id);
            void onReceivedSelfReportList(List<SelfReview> selfReviewList);
        }
        void saveSelfReview(List<SelfReview> selfReview);
        void editSelfReview(List<SelfReview> selfReviewList, SelfReview selfReview);
        void getSelfReview(String athlete_id);
        void deleteReview(String id);
    }

    interface View extends ContractBase.View
    {
        void onSuccess(String msg);
        void onFailed(String msg);
        void onSelfListDeleted(String id);
        void onReceivedSelfReportList(List<SelfReview> selfReviewList);
    }

    interface Presenter
    {
        void saveSelfReview(List<SelfReview> selfReview);
        void editSelfReview(List<SelfReview> selfReviewList, SelfReview selfReview);
        void getSelfReview(String athlete_id);
        void deleteReview(String id);
    }
}
