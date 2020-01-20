package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.Event;

import java.util.List;

public interface ContractEvents
{
    interface Interactor
    {
        interface OnFinishedListener
        {
            void onShowProgress();
            void onHideProgress();
            void onShowMessage(String message);
            void onUpdateUi(List<Event> listUpcomingEvents, List<Event> listTodayEvents);
          //  void onShowMap(Event event);
        }
        void requestEventsData();
    }

    interface View
    {
        void onShowProgress();
        void onHideProgress();
        void onShowMessage(String message);
        void onUpdateUi(List<Event> listUpcomingEvents, List<Event> listTodayEvents);
      //  void onShowMap(Event event);
    }

    interface Presenter
    {
        void requestEventsData();
    }
}
