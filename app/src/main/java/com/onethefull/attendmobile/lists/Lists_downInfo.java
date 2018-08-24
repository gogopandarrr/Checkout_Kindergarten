package com.onethefull.attendmobile.lists;

import android.os.Parcel;
import android.os.Parcelable;

public class Lists_downInfo implements Parcelable{

    String name, tel, email, date;

    public Lists_downInfo(String name, String tel, String email, String date) {
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.date = date;
    }

    protected Lists_downInfo(Parcel in) {
        name = in.readString();
        tel = in.readString();
        email = in.readString();
        date = in.readString();
    }

    public static final Creator<Lists_downInfo> CREATOR = new Creator<Lists_downInfo>() {
        @Override
        public Lists_downInfo createFromParcel(Parcel in) {
            return new Lists_downInfo(in);
        }

        @Override
        public Lists_downInfo[] newArray(int size) {
            return new Lists_downInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(tel);
        parcel.writeString(email);
        parcel.writeString(date);
    }
}
