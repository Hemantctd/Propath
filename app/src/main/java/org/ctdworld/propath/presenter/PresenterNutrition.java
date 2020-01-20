package org.ctdworld.propath.presenter;

import android.content.Context;
import android.util.Log;

import org.ctdworld.propath.contract.ContractNutrition;

import org.ctdworld.propath.interactor.InteractorNutrition;
import org.ctdworld.propath.model.Nutrition;

import java.util.List;


public class PresenterNutrition implements ContractNutrition.Presenter, ContractNutrition.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterNutrition.class.getSimpleName();

    private ContractNutrition.View listenerView;
    private ContractNutrition.Interactor interactor;


    public PresenterNutrition(Context context, ContractNutrition.View listener)
    {
        this.listenerView = listener;
        interactor = new InteractorNutrition(this,context);
    }


    @Override
    public void onGetReceivedNutritionData(List<Nutrition> nutritionList) {
        listenerView.onGetReceivedNutritionData(nutritionList);
    }

    @Override
    public void onGetReceivedNutritionComments(List<Nutrition> nutritionList) {
        listenerView.onGetReceivedNutritionComments(nutritionList);
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
    public void onLikeDislike(int position,Nutrition nutrition) {
        Log.d(TAG,"position :... " +position);
        listenerView.onLikeDislike(position,nutrition);
    }

    @Override
    public void viewNutritionComments(String post_id) {
        interactor.viewNutritionComments(post_id);
    }

    @Override
    public void likeDislike(int position, Nutrition nutrition) {
        interactor.likeDislike(position,nutrition);
    }

    @Override
    public void postNutrition(Nutrition nutritionData) {
        interactor.postNutrition(nutritionData);
    }

    @Override
    public void viewNutrition() {
       interactor.viewNutrition();
    }

    @Override
    public void postComments(Nutrition nutritionData) {
        interactor.postComments(nutritionData);
    }
}
