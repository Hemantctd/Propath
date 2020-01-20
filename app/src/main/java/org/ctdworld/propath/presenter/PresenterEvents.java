package org.ctdworld.propath.presenter;

import android.content.Context;


import org.ctdworld.propath.contract.ContractEvents;
import org.ctdworld.propath.interactor.InteractorEvents;
import org.ctdworld.propath.model.Event;

import java.util.List;

public class PresenterEvents implements ContractEvents.Presenter, ContractEvents.Interactor.OnFinishedListener
{
    ContractEvents.View listener;
    ContractEvents.Interactor interactor;

    public PresenterEvents(Context context, ContractEvents.View listener)
    {
        this.listener = listener;
        interactor = new InteractorEvents(this,context);
    }



    @Override
    public void requestEventsData() {
        interactor.requestEventsData();
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
    public void onShowMessage(String message) {
        listener.onShowMessage(message);
    }

    @Override
    public void onUpdateUi(List<Event> listUpcomingEvents, List<Event> listTodayEvents) {
        listener.onUpdateUi(listUpcomingEvents, listTodayEvents);
    }

}
