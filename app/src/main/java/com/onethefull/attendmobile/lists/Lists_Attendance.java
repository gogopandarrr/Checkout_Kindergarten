package com.onethefull.attendmobile.lists;

import android.os.Parcel;
import android.os.Parcelable;

public class Lists_Attendance implements Parcelable {

    String name, inTime, outTime, cvid;

    public Lists_Attendance(String name, String inTime, String outTime, String cvid) {
        this.name = name;
        this.inTime = inTime;
        this.outTime = outTime;
        this.cvid = cvid;
    }//


    protected Lists_Attendance(Parcel in) {
        name = in.readString();
        inTime = in.readString();
        outTime = in.readString();
        cvid = in.readString();
    }

    public static final Creator<Lists_Attendance> CREATOR = new Creator<Lists_Attendance>() {
        @Override
        public Lists_Attendance createFromParcel(Parcel in) {
            return new Lists_Attendance(in);
        }

        @Override
        public Lists_Attendance[] newArray(int size) {
            return new Lists_Attendance[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getCvid() {
        return cvid;
    }

    public void setCvid(String cvid) {
        this.cvid = cvid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(inTime);
        parcel.writeString(outTime);
        parcel.writeString(cvid);
    }
}//
