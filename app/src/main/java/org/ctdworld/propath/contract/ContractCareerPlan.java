package org.ctdworld.propath.contract;


import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.model.User;

import java.util.List;

public interface ContractCareerPlan
{
    interface Interactor
    {
        interface OnFinishedListener
        {
          //  void onReceivedCareerPlan(CareerPlan.CareerUser careerUser);
            void onSavedCareerPlan(CareerPlan.CareerUser careerUser);  // called after saving career plan on server
            void onReceivedCareerUsers(List<CareerPlan.CareerUser> careerUserList);  // contains list of all users who have created career plan
            void onCareerPlanUpdated(CareerPlan.CareerUser careerUser);  // called when CareerData is updated
            void onCareerDataDeleted(CareerPlan.CareerUser careerUser);  // called when CareerData is deleted
            void onFailed(String message);

        }
     //   void requestCareerPlan(CareerPlan.CareerUser careerUser);    // to get career plan
        void saveCareerPlan(CareerPlan.CareerUser careerUser);   // to save career plan on server
        void updateCareerPlan(CareerPlan.CareerUser careerUser);   // to update existing career plan
        void requestCareerUsers(CareerPlan.CareerUser careerUser);  // to get all user who have created career plan, career user object contains career data
        void deleteCareerData(CareerPlan.CareerUser careerUser); // to delete CareerData
    }

    interface View
    {
        //  void onReceivedCareerPlan(CareerPlan.CareerUser careerUser);
        void onSavedCareerPlan(CareerPlan.CareerUser careerUser);  // called after saving career plan on server
        void onReceivedCareerUsers(List<CareerPlan.CareerUser> careerUserList);  // contains list of all users who have created career plan
        void onCareerPlanUpdated(CareerPlan.CareerUser careerUser);  // called when CareerData is updated
        void onCareerDataDeleted(CareerPlan.CareerUser careerUser);  // called when CareerData is deleted
        void onFailed(String message);
    }

    interface Presenter
    {
      //  void requestCareerPlan(CareerPlan.CareerUser careerUser);    // to get career plan
        void saveCareerPlan(CareerPlan.CareerUser careerUser);   // to save career plan on server
        void updateCareerPlan(CareerPlan.CareerUser careerUser);   // to update existing career plan
        void requestCareerUsers(CareerPlan.CareerUser careerUser);  // to get all user who have created career plan, career user object contains career data
        void deleteCareerData(CareerPlan.CareerUser careerUser); // to delete CareerData
    }
}
