package com.onethefull.attendmobile.lists;

import android.os.Parcel;
import android.os.Parcelable;

public class Lists_downInfo implements Parcelable{

    String name, tel, email, date, cvid;

    public Lists_downInfo(String name, String tel, String email, String date, String cvid) {
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.date = date;
        this.cvid = cvid;
    }

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

    public String getCvid() {
        return cvid;
    }

    public void setCvid(String cvid) {
        this.cvid = cvid;
    }

    public static Creator<Lists_downInfo> getCREATOR() {
        return CREATOR;
    }

    protected Lists_downInfo(Parcel in) {
        name = in.readString();
        tel = in.readString();
        email = in.readString();
        date = in.readString();
        cvid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tel);
        dest.writeString(email);
        dest.writeString(date);
        dest.writeString(cvid);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
