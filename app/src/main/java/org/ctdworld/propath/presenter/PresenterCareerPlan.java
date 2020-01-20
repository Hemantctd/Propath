package org.ctdworld.propath.presenter;

import android.content.Context;


import org.ctdworld.propath.contract.ContractCareerPlan;
import org.ctdworld.propath.interactor.InteracterCareerPlan;
import org.ctdworld.propath.model.CareerPlan;

import java.util.List;


public class PresenterCareerPlan implements ContractCareerPlan.Presenter, ContractCareerPlan.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterCareerPlan.class.getSimpleName();
    private ContractCareerPlan.Interactor mInteractor;
    private ContractCareerPlan.View mListener;

    public PresenterCareerPlan(Context context, ContractCareerPlan.View view)
    {
        this.mInteractor = new InteracterCareerPlan(this,context);
        this.mListener = view;
    }



   /* @Override
    public void requestCareerPlan(CareerPlan careerPlan) {
        mInteractor.requestCareerPlan(careerPlan);
    }*/

    @Override
    public void saveCareerPlan(CareerPlan.CareerUser careerUser) {
        mInteractor.saveCareerPlan(careerUser);
    }

    @Override
    public void updateCareerPlan(CareerPlan.CareerUser careerUser) {
        mInteractor.updateCareerPlan(careerUser);
    }

    @Override
    public void requestCareerUsers(CareerPlan.CareerUser careerUser) {
        mInteractor.requestCareerUsers(careerUser);
    }

    @Override
    public void deleteCareerData(CareerPlan.CareerUser careerUser) {
        mInteractor.deleteCareerData(careerUser);
    }


    /*@Override
    public void onReceivedCareerPlan(CareerPlan careerPlan) {
        mListener.onReceivedCareerPlan(careerPlan);
    }*/

    @Override
    public void onSavedCareerPlan(CareerPlan.CareerUser careerUser) {
        mListener.onSavedCareerPlan(careerUser);
    }

    @Override
    public void onReceivedCareerUsers(List<CareerPlan.CareerUser> careerUserList) {
        mListener.onReceivedCareerUsers(careerUserList);
    }

    @Override
    public void onCareerPlanUpdated(CareerPlan.CareerUser careerUser) {
        mListener.onCareerPlanUpdated(careerUser);
    }

    @Override
    public void onCareerDataDeleted(CareerPlan.CareerUser careerUser) {
        mListener.onCareerDataDeleted(careerUser);
    }

    @Override
    public void onFailed(String message) {
        mListener.onFailed(message);
    }

}
