package org.ctdworld.propath.contract;

import org.ctdworld.propath.base.ContractBase;
import org.ctdworld.propath.model.GetGroupNames;

import java.util.List;


public interface ContractGetAllFriendsAndGroups {

    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onGetAllFriendsAndGroups(List<GetGroupNames> friendsAndGroupList);
            void onFailed();
        }

        void requestAllFriendsAndGroups(String survey_id);
    }

    interface View extends ContractBase.View
    {
        void onGetAllFriendsAndGroups(List<GetGroupNames> friendsAndGroupList);
        void onFailed();
    }

    interface Presenter
    {
        void requestAllFriendsAndGroups(String survey_id);
    }
}
