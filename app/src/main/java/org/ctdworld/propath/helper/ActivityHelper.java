package org.ctdworld.propath.helper;


/* this class is being used to confirm whether the activity is visible or not*/
public class ActivityHelper
{
    private boolean isMainVisible = false;
    private boolean isChatVisible = false;
    private boolean isNewsFeedVisible = false;

    public boolean isNutritionFeedVisible() {
        return isNutritionFeedVisible;
    }

    public void setNutritionFeedVisible(boolean nutritionFeedVisible) {
        isNutritionFeedVisible = nutritionFeedVisible;
    }

    private boolean isEventVisible = false;
    private boolean isNotificationVisible = false;
    private boolean isNutritionFeedVisible = false;

    private static ActivityHelper activityHelper = new ActivityHelper();

    private ActivityHelper() { }  // constructor



    // to get instance
    public static ActivityHelper getInstance()
    {
        if (activityHelper == null)
            activityHelper = new ActivityHelper();

        return activityHelper;

    }


    public boolean isMainVisible() {
        return isMainVisible;
    }

    public void setMainVisible(boolean mainVisible) {
        isMainVisible = mainVisible;
    }

    public boolean isChatVisible() {
        return isChatVisible;
    }

    public void setChatVisible(boolean chatVisible) {
        isChatVisible = chatVisible;
    }

    public boolean isNewsFeedVisible() {
        return isNewsFeedVisible;
    }

    public void setNewsFeedVisible(boolean newsFeedVisible) {
        isNewsFeedVisible = newsFeedVisible;
    }

    public boolean isEventVisible() {
        return isEventVisible;
    }

    public void setEventVisible(boolean eventVisible) {
        isEventVisible = eventVisible;
    }

    public boolean isNotificationVisible() {
        return isNotificationVisible;
    }

    public void setNotificationVisible(boolean notificationVisible) {
        isNotificationVisible = notificationVisible;
    }
}
