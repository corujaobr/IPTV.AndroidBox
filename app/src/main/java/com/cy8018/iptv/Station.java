package com.cy8018.iptv;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Station implements Parcelable {

    @SerializedName("name") public String name;
    @SerializedName("logo") public String logo;
    @SerializedName("url") public List<String> url;
    public int index;

    protected Station(Parcel in) {
        name = in.readString();
        logo = in.readString();
        url = in.createStringArrayList();
        index = in.readInt();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(logo);
        parcel.writeStringList(url);
        parcel.writeInt(index);
    }
}
