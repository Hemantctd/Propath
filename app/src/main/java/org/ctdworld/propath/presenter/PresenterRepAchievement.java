package org.ctdworld.propath.presenter;

import android.content.Context;


import org.ctdworld.propath.contract.ContractRepAchievement;
import org.ctdworld.propath.interactor.InteractorRepAchievement;
import org.ctdworld.propath.model.RepAchievement;

import java.util.List;


public class PresenterRepAchievement implements ContractRepAchievement.Presenter, ContractRepAchievement.Interactor.OnFinishedListener
{
    private ContractRepAchievement.View listener;
    private ContractRepAchievement.Interactor interactor;

    public PresenterRepAchievement(Context context, ContractRepAchievement.View listener)
    {
        this.listener = listener;
        interactor = new InteractorRepAchievement(this,context);
    }




    @Override
    public void requestRepAchievement(String userId) {
        interactor.requestRepAchievement(userId);
    }

    @Override
    public void addRepAchievement(RepAchievement repAchievement) {
        interactor.addRepAchievement(repAchievement);
    }

    @Override
    public void editRepAchievement(RepAchievement repAchievement) {
      interactor.editRepAchievement(repAchievement);
    }

    @Override
    public void deleteRepAchievement(int position,String rep_achieve_id) {
        interactor.deleteRepAchievement(position,rep_achieve_id);
    }


    @Override
    public void onShowProgress() {
        listener.onShowProgress();
    }

    @Override
    public void onHideProgress() {
        listener.onHideProgress();
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        listener.onSetViewsDisabledOnProgressBarVisible();
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        listener.onSetViewsEnabledOnProgressBarGone();
    }

    @Override
    public void onShowMessage(String message) {
        listener.onShowMessage(message);
    }


    @Override
    public void onReceivedRepList(List<RepAchievement> listRepAchievement) {
        listener.onReceivedRepList(listRepAchievement);
    }

    @Override
    public void onAddRepSuccessfully() {
        listener.onAddRepSuccessfully();
    }

    @Override
    public void onEditSuccessfully() {
        listener.onEditSuccessfully();
    }

    @Override
    public void onDeleted(int position) {
        listener.onDeleted(position);
    }

}
