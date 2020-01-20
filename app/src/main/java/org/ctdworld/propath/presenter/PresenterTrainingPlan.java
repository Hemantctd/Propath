package org.ctdworld.propath.presenter;

import android.content.Context;
import android.util.Log;

import org.ctdworld.propath.base.BaseView;
import org.ctdworld.propath.contract.ContractTrainingPlan;
import org.ctdworld.propath.interactor.InteractorTrainingPlan;
import org.ctdworld.propath.model.TrainingPlan;
import org.ctdworld.propath.model.TrainingPlan.PlanData;

import java.util.ArrayList;


public class PresenterTrainingPlan implements ContractTrainingPlan.Presenter, ContractTrainingPlan.Interactor.OnFinishedListener,ContractTrainingPlan.Interactor.OnFinishedPlanItemListener
{
    private static final String TAG = PresenterTrainingPlan.class.getSimpleName();
    private ContractTrainingPlan.Interactor interactor;
    private ContractTrainingPlan.View listenerPlanView;
    private ContractTrainingPlan.PlanItemView listenerItemView;

    public PresenterTrainingPlan(Context context, BaseView view)
    {
        this.interactor = new InteractorTrainingPlan(this,context);

        if (view instanceof ContractTrainingPlan.PlanItemView)
            listenerItemView = (ContractTrainingPlan.PlanItemView) view;

        else if (view instanceof ContractTrainingPlan.View)
            this.listenerPlanView= (ContractTrainingPlan.View) view;

    }


    @Override
    public void createTrainingPlan(PlanData trainingPlanData) {
        interactor.createTrainingPlan(trainingPlanData);
    }

    @Override
    public void getTrainingPlanList(PlanData trainingPlanData) {
        interactor.getTrainingPlanList(trainingPlanData);
    }

    @Override
    public void deleteTrainingPlan(ArrayList<PlanData> trainingPlanData) {
        interactor.deleteTrainingPlan(trainingPlanData);
    }

    @Override
    public void editTrainingPlan(PlanData trainingPlanData, ArrayList<TrainingPlan.PlanData.PlanItem> editItems, ArrayList<PlanData.PlanItem> deleteItems, ArrayList<PlanData.PlanItem> addNewItems) {
        interactor.editTrainingPlan(trainingPlanData, editItems, deleteItems, addNewItems);
    }

    @Override
    public void shareTrainingPlan(PlanData trainingPlanData, String[] groupIdArray, String[] userIdArray) {
        interactor.shareTrainingPlan(trainingPlanData, groupIdArray, userIdArray);
    }

    @Override
    public void editTrainingPlanItem(String planId, PlanData.PlanItem planItem) {
        interactor.editTrainingPlanItem(planId, planItem);
    }

    @Override
    public void deleteTrainingPlanItem(String planId, PlanData.PlanItem planItem) {
        interactor.deleteTrainingPlanItem(planId, planItem);
    }

   /* @Override
    public void createTrainingPlanItem(TrainingPlan.PlanData trainingPlan) {
        interactor.createTrainingPlanItem(trainingPlan);
    }

    @Override
    public void getTrainingPlanItemList(TrainingPlan.PlanData trainingPlan) {
        interactor.getTrainingPlanItemList(trainingPlan);
    }

    @Override
    public void editTrainingPlanItem(TrainingPlan.PlanData trainingPlan) {
        interactor.editTrainingPlanItem(trainingPlan);
    }

    @Override
    public void deleteTrainingPlanItem(TrainingPlan.PlanData trainingPlan) {
        interactor.deleteTrainingPlanItem(trainingPlan);
    }*/

    @Override
    public void onTrainingPlanCreated(PlanData trainingPlanData) {
        if (listenerPlanView != null)
            listenerPlanView.onTrainingPlanCreated(trainingPlanData);
    }

    @Override
    public void onTrainingPlanReceived(TrainingPlan trainingPlan) {
        if (listenerPlanView != null)
            listenerPlanView.onTrainingPlanReceived(trainingPlan);
    }


    @Override
    public void onTrainingPlanDeleted(PlanData trainingPlanData) {
        if (listenerPlanView != null)
            listenerPlanView.onTrainingPlanDeleted(trainingPlanData);
    }

    @Override
    public void onTrainingPlanEdited(TrainingPlan.PlanData trainingPlanData) {
        if (listenerPlanView != null)
            listenerPlanView.onTrainingPlanEdited(trainingPlanData);
    }

    @Override
    public void onTrainingPlanShared() {
        if (listenerPlanView != null)
            listenerPlanView.onTrainingPlanShared();
    }

    @Override
    public void onTrainingPlanItemDeleted(PlanData.PlanItem planItem)
    {
        Log.i(TAG,"listenerItemView = "+listenerItemView);
        if (listenerItemView != null)
            listenerItemView.onTrainingPlanItemDeleted(planItem);
    }

    @Override
    public void onTrainingPlanItemEdited(PlanData.PlanItem planItem) {
        Log.i(TAG,"listenerItemView = "+listenerItemView);
        if (listenerItemView != null)
            listenerItemView.onTrainingPlanItemEdited(planItem);
    }

    @Override
    public void onFailedPlanItem(String message) {
        if (listenerItemView != null)
            listenerItemView.onFailedPlanItem(message);
    }

    @Override
    public void onShowMessagePlanItem(String message) {
        if (listenerItemView != null)
            listenerItemView.onShowMessagePlanItem(message);
    }


/*    @Override
    public void onTrainingPlanItemCreated(TrainingPlan.PlanData.PlanItem planItem) {
        listener.onTrainingPlanItemCreated(planItem);
    }

    @Override
    public void onTrainingPlanItemReceived(TrainingPlan.PlanData.PlanItem planItem) {
        listener.onTrainingPlanItemReceived(planItem);
    }

    @Override
    public void onTrainingPlanItemEdited(TrainingPlan.PlanData.PlanItem planItem) {
        listener.onTrainingPlanItemEdited(planItem);
    }

    @Override
    public void onTrainingPlanItemDeleted(TrainingPlan.PlanData.PlanItem planItem) {
        listener.onTrainingPlanItemDeleted(planItem);
    }*/

    @Override
    public void onFailed(String message) {
        if (listenerPlanView != null)
            listenerPlanView.onFailed(message);
    }

    @Override
    public void onShowMessage(String message) {
        if (listenerPlanView != null)
            listenerPlanView.onShowMessage(message);
    }
}
