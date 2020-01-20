package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractGetAllGroups;
import org.ctdworld.propath.interactor.InteractorGetAllGroups;
import org.ctdworld.propath.model.GetGroupNames;

import java.util.List;


public class PresenterGetAllGroups implements ContractGetAllGroups.Presenter, ContractGetAllGroups.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetAllGroups.class.getSimpleName();
    ContractGetAllGroups.Interactor interactor;
    ContractGetAllGroups.View listenerView;

    public PresenterGetAllGroups(Context context, ContractGetAllGroups.View listenerView)
    {
        this.interactor = new InteractorGetAllGroups(this,context);
        this.listenerView = listenerView;

    }

    @Override
    public void onGetAllGroups(List<GetGroupNames> groupList) {
        listenerView.onGetAllGroups(groupList);
    }

    @Override
    public void onFailed() {
        listenerView.onFailed();
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
    public void requestAllGroups() {
        interactor.requestAllGroups();
    }
}
