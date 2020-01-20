package org.ctdworld.propath.contract;


import org.ctdworld.propath.base.BaseOnFinishedListener;
import org.ctdworld.propath.base.BaseView;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;

import java.util.ArrayList;


/*# this contract class will be used in view, create, edit note pages*/
public interface ContractTrainingPlan
{
    interface Interactor
    {


        // # onFinishedListener
        interface OnFinishedListener extends BaseOnFinishedListener
        {
            void onTrainingPlanCreated(TrainingPlan.PlanData trainingPlanData);
            void onTrainingPlanReceived(TrainingPlan trainingPlan);
            void onTrainingPlanDeleted(PlanData trainingPlanData);
            void onTrainingPlanEdited(TrainingPlan.PlanData trainingPlanData);
            void onTrainingPlanShared();
            void onFailed(String message);
            void onShowMessage(String message);
        }

        interface OnFinishedPlanItemListener extends BaseOnFinishedListener
        {
            void onTrainingPlanItemDeleted(PlanData.PlanItem planItem);
            void onTrainingPlanItemEdited(TrainingPlan.PlanData.PlanItem planItem);
            void onFailedPlanItem(String message);
            void onShowMessagePlanItem(String message);
        }


        // # interactor
        void createTrainingPlan(TrainingPlan.PlanData trainingPlanData);
        void getTrainingPlanList(PlanData trainingPlanData);
        void deleteTrainingPlan(ArrayList<TrainingPlan.PlanData> trainingPlanData);
        void editTrainingPlan(PlanData trainingPlanData, ArrayList<TrainingPlan.PlanData.PlanItem> editItems, ArrayList<PlanData.PlanItem> deleteItems, ArrayList<TrainingPlan.PlanData.PlanItem> addNewItems);
        void shareTrainingPlan(TrainingPlan.PlanData trainingPlanData, String[] groupIdArray, String[] userIdArray);
        void editTrainingPlanItem(String planId, PlanData.PlanItem planItem);
        void deleteTrainingPlanItem(String planId, PlanData.PlanItem planItem);
    }


    // # view
    interface View extends BaseView
    {
        void onTrainingPlanCreated(PlanData trainingPlanData);
        void onTrainingPlanReceived(TrainingPlan trainingPlan);
        void onTrainingPlanDeleted(PlanData trainingPlanData);
        void onTrainingPlanEdited(PlanData trainingPlanData);
        void onTrainingPlanShared();
        void onFailed(String message);
        void onShowMessage(String message);
    }

    interface PlanItemView extends BaseView
    {
        void onTrainingPlanItemDeleted(TrainingPlan.PlanData.PlanItem planItem);
        void onTrainingPlanItemEdited(PlanData.PlanItem planItem);
        void onFailedPlanItem(String message);
        void onShowMessagePlanItem(String message);
    }

    // # presenter
    interface Presenter
    {
        void createTrainingPlan(PlanData trainingPlanData);
        void getTrainingPlanList(TrainingPlan.PlanData trainingPlanData);
        void deleteTrainingPlan(ArrayList<TrainingPlan.PlanData> trainingPlanData);
        void editTrainingPlan(PlanData trainingPlanData, ArrayList<PlanData.PlanItem> editItems, ArrayList<TrainingPlan.PlanData.PlanItem> deleteItems, ArrayList<PlanData.PlanItem> addNewItems);
        void shareTrainingPlan(PlanData trainingPlanData, String[] groupIdArray, String[] userIdArray);
        void editTrainingPlanItem(String planId, TrainingPlan.PlanData.PlanItem planItem);
        void deleteTrainingPlanItem(String planId, TrainingPlan.PlanData.PlanItem planItem);
    }
}
