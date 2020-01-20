package org.ctdworld.propath.contract;
import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.SchoolReview;


import java.util.List;

public interface ContractProgressReport
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void getOnReceivedProgressReport(SchoolReview progressReport, List<SchoolReview> progressReportList);
            void onFailed();
        }
        void getProgressReport(String userId, String review_id);
    }

    interface View extends ContractBase.View
    {
        void getOnReceivedProgressReport(SchoolReview progressReport, List<SchoolReview> progressReportList);
        void onFailed();
    }

    interface Presenter
    {
        void getProgressReport(String userId, String review_id);
    }
}
