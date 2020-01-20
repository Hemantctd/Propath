package org.ctdworld.propath.presenter;

import android.content.Context;

import org.ctdworld.propath.contract.ContractGetAllFriendsAndGroups;
import org.ctdworld.propath.interactor.InteractorGetAllFriendsAndGroups;
import org.ctdworld.propath.model.GetGroupNames;

import java.util.List;


public abstract class PresenterGetAllFriendsAndGroups implements ContractGetAllFriendsAndGroups.Presenter, ContractGetAllFriendsAndGroups.Interactor.OnFinishedListener
{
    private static final String TAG = PresenterGetAllFriendsAndGroups.class.getSimpleName();
    ContractGetAllFriendsAndGroups.Interactor interactor;
    ContractGetAllFriendsAndGroups.View listenerView;

    public PresenterGetAllFriendsAndGroups(Context context, ContractGetAllFriendsAndGroups.View listenerView)
    {
        this.interactor = new InteractorGetAllFriendsAndGroups(this,context);
        this.listenerView = listenerView;

    }

    @Override
    public void onGetAllFriendsAndGroups(List<GetGroupNames> friendsAndGroupList) {
        listenerView.onGetAllFriendsAndGroups(friendsAndGroupList);
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
    public void requestAllFriendsAndGroups(String survey_id) {
        interactor.requestAllFriendsAndGroups(survey_id);
    }
}
