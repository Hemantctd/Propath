package org.ctdworld.propath.contract;

import org.ctdworld.propath.model.Nutrition;

import java.util.List;

public interface ContractNutrition
{
    interface Interactor
    {
        interface OnFinishedListener extends ContractBase.OnFinishedListener
        {
            void onGetReceivedNutritionData(List<Nutrition> nutritionList);
            void onGetReceivedNutritionComments(List<Nutrition> nutritionList);
            void onSuccess(String msg);
            void onFailed(String msg);
            void onLikeDislike(int position, Nutrition nutrition);
        }
        void likeDislike(int position, Nutrition nutrition);
        void postNutrition(Nutrition nutritionData);
        void viewNutrition();
        void viewNutritionComments(String post_id);
        void postComments(Nutrition nutritionData);
       // void add_comment(Nutrition nutrition);
    }

    interface View extends ContractBase.View
    {
        void onGetReceivedNutritionComments(List<Nutrition> nutritionList);
        void onGetReceivedNutritionData(List<Nutrition> nutritionList);
        void onSuccess(String msg);
        void onFailed(String msg);
        void onLikeDislike(int position, Nutrition nutrition);

    }

    interface Presenter
    {
        void viewNutritionComments(String post_id);
        void likeDislike(int position, Nutrition nutrition);
        void postNutrition(Nutrition nutritionData);
        void viewNutrition();
        void postComments(Nutrition nutritionData);

        // void add_comment(Nutrition nutrition);

    }
}
