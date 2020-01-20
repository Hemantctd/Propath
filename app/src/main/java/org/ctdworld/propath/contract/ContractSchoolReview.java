package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.SchoolReview;

import java.util.List;

public interface ContractSchoolReview
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess(String msg);
            void onFailed(String msg);
            void onProgressListDeleted(String id);
            void onReceivedProgressReportList(List<SchoolReview> schoolReviewList);
        }
        void saveSchoolReview(List<SchoolReview> schoolReview);
        void editSchoolReview(List<SchoolReview> schoolReviewList, SchoolReview schoolReview);
        void getSchoolReview(String athlete_id);
        void deleteReview(String id);

    }

    interface View extends ContractBase.View
    {
        void onSuccess(String msg);
        void onFailed(String msg);
        void onProgressListDeleted(String id);
        void onReceivedProgressReportList(List<SchoolReview> schoolReviewList);

    }

    interface Presenter
    {
        void saveSchoolReview(List<SchoolReview> schoolReview);
        void editSchoolReview(List<SchoolReview> schoolReviewList, SchoolReview schoolReview);
        void getSchoolReview(String athlete_id);
        void deleteReview(String id);

    }
}
