package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.GetAthletes;

import java.util.List;


public interface ContractGetAllAthletes {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onGetAllAthletes(List<GetAthletes> athletesList);
           // void onResponseToGiveSchoolReviewSubmitted();
           // void onAddSchoolReviewByTeacherRequestSend();
            void onFailed(String msg);
            void onSuccess(String msg);

        }
        void requestAllAthletes();
       // void responseToGiveSchoolReview(SchoolReview schoolReview);
       // void requestToGiveSchoolReview(SchoolReview schoolReview);
    }

    interface View extends ContractBase.View
    {
        void onGetAllAthletes(List<GetAthletes> athletesList);
       // void onResponseToGiveSchoolReviewSubmitted();
       // void onAddSchoolReviewByTeacherRequestSend();
        void onFailed(String msg);
        void onSuccess(String msg);

    }

    interface Presenter
    {
        void requestAllAthletes();
       // void responseToGiveSchoolReview(SchoolReview schoolReview);
      //  void requestToGiveSchoolReview(SchoolReview schoolReview);
    }
}
