package org.ctdworld.propath.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgressListData implements Parcelable
{
    public final String name;

    public ProgressListData(String name) {
        this.name = name;
    }

    protected ProgressListData(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ProgressListData> CREATOR = new Creator<ProgressListData>() {
        @Override
        public ProgressListData createFromParcel(Parcel in) {
            return new ProgressListData(in);
        }

        @Override
        public ProgressListData[] newArray(int size) {
            return new ProgressListData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(name);
    }
}
