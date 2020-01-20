package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.GetGroupNames;

import java.util.List;

public interface ContractGetAllGroups {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onGetAllGroups(List<GetGroupNames> groupList);
            void onFailed();
        }

        void requestAllGroups();
    }

    interface View extends ContractBase.View
    {
        void onGetAllGroups(List<GetGroupNames> groupList);
        void onFailed();
    }

    interface Presenter
    {
        void requestAllGroups();
    }
}
