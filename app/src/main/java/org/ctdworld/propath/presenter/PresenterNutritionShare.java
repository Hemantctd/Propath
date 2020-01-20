package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractShareNutrition;
import org.ctdworld.propath.interactor.InteractorShareNutrition;
import org.ctdworld.propath.model.Nutrition;

public class PresenterNutritionShare implements ContractShareNutrition.Presenter, ContractShareNutrition.Interactor.OnFinishedListener {

    private static final String TAG = PresenterNutrition.class.getSimpleName();

    private ContractShareNutrition.View listenerView;
    private ContractShareNutrition.Interactor interactor;


    public PresenterNutritionShare(Context context, ContractShareNutrition.View listener)
    {
        this.listenerView = listener;
        interactor = new InteractorShareNutrition(this,context);
    }


    @Override
    public void onSuccess(String msg) {
        listenerView.onSuccess(msg);
    }

    @Override
    public void onFailed(String msg) {
      listenerView.onFailed(msg);
    }

    @Override
    public void onShowProgress() {
        listenerView.onShowProgress();
    }

    @Override
    public void onHideProgress() {
       listenerView.onHideProgress();
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
      listenerView.onSetViewsDisabledOnProgressBarVisible();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
      listenerView.onSetViewsEnabledOnProgressBarGone();
    }

    @Override
    public void onShowMessage(String message) {
     listenerView.onShowMessage(message);
    }

    @Override
    public void shareNutrition(Nutrition nutritionData, String groupId) {
      interactor.shareNutrition(nutritionData,groupId);
    }
}
