package org.ctdworld.propath.helper;

import androidx.viewpager.widget.ViewPager;

public class ConstHelper
{
    public static final int NOT_FOUND = -1;
    public static final String NOT_FOUND_STRING = "not found";

    public static class Tag
    {
        public static String getFragmentTagAddedInViewpager(int viewPagerId, ViewPager viewPager)
        {
           // android:switcher:" + R.id.pager + ":" + ViewPager.getCurrentItem()
            if (viewPager == null)
                return NOT_FOUND_STRING;

           return "android:switcher:" + viewPagerId + ":" + viewPager.getCurrentItem();
        }
        public static class Fragment
        {

            public static final String CAREER_EMPLOYMENT_TOOLS= "employment tools";

            public static final String DASHBOARD = "dashboard";
            public static final String SCHOOL_REVIEW = "school review";
            public static final String NOTES_CATEGORY = "notes category";
            public static final String CREATE_GROUP = "create group";
            public static final String CONTACT_ALL = "contact all";
            public static final String CONTACT_PEOPLE = "contact people";
            public static final String CONTACT_GROUPS = "contact groups";
            public static final String SPEECH_RECOGNITION = "speech recognition";

            public static final String PROFILE_VIEW = "profile view";
            public static final String PROFILE_SEARCH_USER = "search suer";
            public static final String PROFILE_VIEW_IN_UPDATE_PROFILE_ACTIVITY = "profile view in activity";
            public static final String PROFILE_VIEW_REP_ACHIEVEMENT = "rep achievement";
            public static final String PROFILE_UPDATE = "profile update";
            public static final String PROFILE_ADD_REP_ACHIEVEMENT = "add rep achievement";

            public static final String DASHBOARD_DIALOG = "dash dialog";
            public static final String DIALOG_PROFILE_ADD_LINK = "add link";
            public static final String FULL_IMAGE = "full image";
            public static final String DIALOG_EDIT_TEXT = "edit text";
            public static final String DIALOG_CREATE_TRAINING_PLAN_ITEM = "create training plan item";
            public static final String DIALOG_PROGRESS = "progress";
            public static final String CAREER_PLAN_CREATE_UPDATE= "career plan create update";
            public static final String CAREER_PLAN_VIEW= "career plan view";
            public static final String CAREER_USERS_LIST= "career users list";
            public static final String BOTTOM_SHEET_OPTIONS = "option sheet bottom";
            public static final String CREATED_DATA_DETAILS = "created data details";
            public static final String SEARCH = "search";

        }
    }


    public class Upload
    {
        public static final String UPLOAD_IMAGE_TYPE_JPEG = "data:image/jpeg;base64,";
        public static final String UPLOAD_VIDEO_TYPE_MP4 = "data:video/mp4;base64,";
    }


    public class Type
    {
        public static final int RECEIVED = 1;
        public static final int CREATED = 2;
    }



    // values for actions
    public static class Action
    {
        public static final int CREATE = 1;
        public static final int EDIT_OR_UPDATE = 2;
    }



    // # it will contain all request codes to
    public class RequestCode
    {
        public static final int VIEW = 1;
        public static final int CREATE = 2;
        public static final int EDIT = 3;
        public static final int SHARE = 4;
        public static final int COPY = 4;
        public static final int CAMERA_IMAGE = 4;
        public static final int CAMERA_VIDEO = 16;

        public static final int CREATE_EDIT_NEWS_FEED_POST = 5;
        public static final int ACTIVITY_NEWS_FEED_COMMENT = 6;
        public static final int ACTIVITY_NEWS_FEED_SETTING_PAGE = 7;
        public static final int ACTIVITY_NEWS_FEED_SHARE = 8;

        public static final int ACTIVITY_CAREER_PLAN_CREATE_UPDATE = 9;
        public static final int ACTIVITY_CAREER_PLAN_VIEW = 10;
        public static final int FRAGMENT_CAREER_PLAN_LIST = 11;
        public static final int DEVICE_IMAGE = 12;
        public static final int DOCUMENT = 13;
        public static final int SELECT = 14;
        public static final int PROFILE_UPDATE= 15;
        public static final int CREATE_EDIT_NUTRITION_FEED_POST = 16;
        public static final int ACTIVITY_NUTRITION_FEED_COMMENT = 17;
        public static final int ACTIVITY_NUTRITION_FEED_SHARE = 18;

    }

    public class Key
    {
        // # types
        public static final String TYPE_RECEIVED_OR_CREATED = "received or created";
        public static final String ACTION_CREATE_UPDATE = "create or update";

        public static final String IS_DELETED = "id deleted";
        public static final String IS_COPIED = "id copied";
        public static final String TRAINING_PLAN = "training plan";
        public static final String TRAINING_PLAN_SINGLE_PLAN = "single plan";
        public static final String OPTIONS_LIST = "options list";
        public static final String DATA_MODAL = "data_modal";
        public static final String IS_EDITED = "is_deleted";
        public static final String SELECT = "select";
        public static final String ID = "id";  // to put any id

        // News Feed
        public static final String  NEWS_FEED_POST_DATA = "post data";


        public static final String CAREER_USER = "career user";
        public static final String URL = "url";

        // Nutrition Feed

        public static final String  NUTRITION_FEED_POST_DATA = "post data";

    }


}
