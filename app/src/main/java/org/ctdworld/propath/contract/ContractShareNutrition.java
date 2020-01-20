package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.Nutrition;

public interface ContractShareNutrition
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onSuccess(String msg);
            void onFailed(String msg);
        }

        void shareNutrition(Nutrition nutritionData, String groupId);
    }

    interface View extends ContractBase.View
    {

        void onSuccess(String msg);
        void onFailed(String msg);

    }

    interface Presenter
    {
        void shareNutrition(Nutrition nutritionData, String groupId);


    }
}
