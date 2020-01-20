package org.ctdworld.propath.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.ctdworld.propath.R;

import java.util.ArrayList;

public class BottomSheetOption implements Parcelable
{
    public static final int OPTION_EDIT = 1;
    public static final int OPTION_IMAGE = 2;
    public static final int OPTION_VIDEO = 3;
    public static final int OPTION_ADD_ACHIEVEMENT = 4;
    public static final int OPTION_CREATE_GROUP = 5;
    public static final int OPTION_ADD_CONTACT = 6;
    public static final int OPTION_SHARE = 7;
    public static final int OPTION_DELETE = 8;
    public static final int OPTION_STATISTICS = 9;
    public static final int OPTION_SETTING = 10;
    public static final int OPTION_COPY = 11;
    public static final int OPTION_SEARCH = 12;
    public static final int OPTION_REMOVE = 13;
    public static final int OPTION_LINK = 14;
    public static final int OPTION_EDIT_2 = 15;
    public static final int OPTION_HIDE = 16;
    public static final int OPTION_SAVE = 17;





    private static final String TAG = BottomSheetOption.class.getSimpleName();
    private static ArrayList<BottomSheetOption> mListOption =  new ArrayList<>();


    private String title;
    private int imgResourceId;

    public int getOptionId() {
        return optionId;
    }

    private int optionId;



    protected BottomSheetOption() {
        /*title = in.readString();
        imgResourceId = in.readInt();*/
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(imgResourceId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BottomSheetOption> CREATOR = new Creator<BottomSheetOption>() {
        @Override
        public BottomSheetOption createFromParcel(Parcel in) {
            return new BottomSheetOption();
        }

        @Override
        public BottomSheetOption[] newArray(int size) {
            return new BottomSheetOption[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }



    // # builder
    public static class Builder
    {
        public Builder()
        {
            if (mListOption != null)
                mListOption.clear();
        }

        public Builder addOption(int option, String title)
        {
            BottomSheetOption bottomSheetOption = new BottomSheetOption();
            bottomSheetOption.title = title;
            bottomSheetOption.imgResourceId = getIconResourceId(option);
            bottomSheetOption.optionId = option;

            if (mListOption != null)
                mListOption.add(bottomSheetOption);
            else
                Log.e(TAG,"mListOption is null in addOption() method in Builder class");

            return this;
        }


        public ArrayList<BottomSheetOption> build()
        {
            return mListOption;
        }


        int getIconResourceId(int option)
        {
            int resourceId = -1;
            switch (option)
            {
                case OPTION_EDIT:
                    resourceId = R.drawable.ic_edit;
                    break;

                case OPTION_IMAGE:
                    resourceId = R.drawable.ic_add_image;
                    break;

                case OPTION_VIDEO:
                    resourceId = R.drawable.img_video;
                    break;

                case OPTION_ADD_ACHIEVEMENT:
                    resourceId = R.drawable.ic_add_image;
                    break;

                case OPTION_CREATE_GROUP:
                    resourceId = R.drawable.ic_add_to_group;
                break;

                case OPTION_ADD_CONTACT:
                    resourceId = R.drawable.ic_add_contact;
                    break;

                case OPTION_SHARE:
                    resourceId = R.drawable.ic_share;
                    break;

                case OPTION_DELETE:
                    resourceId = R.drawable.ic_delete;
                    break;

                case OPTION_STATISTICS:
                    resourceId = R.drawable.survey_icon;
                    break;

                case OPTION_COPY:
                    resourceId = R.drawable.ic_survey_copy_icon;
                    break;

                case OPTION_SETTING:
                    resourceId = R.drawable.ic_settings;
                    break;

                case OPTION_SEARCH:
                    resourceId = R.drawable.ic_search;
                    break;

                case OPTION_REMOVE:
                    resourceId = R.drawable.ic_cancel;
                    break;

                case OPTION_LINK:
                resourceId = R.drawable.ic_link;
                break;

                case OPTION_EDIT_2:
                resourceId = R.drawable.ic_edit;
                break;

                case OPTION_HIDE:
                    resourceId = R.drawable.hide;
                    break;

                case OPTION_SAVE:
                    resourceId = R.drawable.ic_notes_save;
                    break;

            }

            return resourceId;
        }
    }


}
