package org.ctdworld.propath.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FullSizeImageVideo implements Parcelable
{
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private String url;
    private int type;

    public FullSizeImageVideo(String url, int type) {
        this.url = url;
        this.type = type;
    }

    protected FullSizeImageVideo(Parcel in) {
        url = in.readString();
        type = in.readInt();
    }

    public static final Creator<FullSizeImageVideo> CREATOR = new Creator<FullSizeImageVideo>() {
        @Override
        public FullSizeImageVideo createFromParcel(Parcel in) {
            return new FullSizeImageVideo(in);
        }

        @Override
        public FullSizeImageVideo[] newArray(int size) {
            return new FullSizeImageVideo[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeInt(type);
    }
}
