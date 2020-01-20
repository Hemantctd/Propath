package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.RepAchievement;

import java.util.List;

public interface ContractRepAchievement
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onReceivedRepList(List<RepAchievement> listRepAchievement);
            void onAddRepSuccessfully();
            void onEditSuccessfully();
            void onDeleted(int position);
        }
        void  requestRepAchievement(String userId);
        void  addRepAchievement(RepAchievement repAchievement);
        void  editRepAchievement(RepAchievement repAchievement);
        void  deleteRepAchievement(int position,String rep_achieve_id);
    }

    interface View extends ContractBase.View
    {
        void onReceivedRepList(List<RepAchievement> listRepAchievement);
        void onDeleted(int position);
        void onAddRepSuccessfully();
        void onEditSuccessfully();

    }

    interface Presenter
    {
        void requestRepAchievement(String userId);
        void  addRepAchievement(RepAchievement repAchievement);
        void  editRepAchievement(RepAchievement repAchievement);
        void  deleteRepAchievement(int position,String rep_achieve_id);
    }
}
