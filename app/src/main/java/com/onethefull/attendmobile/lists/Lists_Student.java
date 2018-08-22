package com.onethefull.attendmobile.lists;

import android.os.Parcel;
import android.os.Parcelable;

public class Lists_Student implements Parcelable {

    byte[] image;
    String name_student, tel_parents, email_parents;


    public Lists_Student(byte[] image, String name_student, String tel_parents, String email_parents) {
        this.image = image;
        this.name_student = name_student;
        this.tel_parents = tel_parents;
        this.email_parents = email_parents;
    }


    protected Lists_Student(Parcel in) {
        image = in.createByteArray();
        name_student = in.readString();
        tel_parents = in.readString();
        email_parents = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(image);
        dest.writeString(name_student);
        dest.writeString(tel_parents);
        dest.writeString(email_parents);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lists_Student> CREATOR = new Creator<Lists_Student>() {
        @Override
        public Lists_Student createFromParcel(Parcel in) {
            return new Lists_Student(in);
        }

        @Override
        public Lists_Student[] newArray(int size) {
            return new Lists_Student[size];
        }
    };

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName_student() {
        return name_student;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }

    public String getTel_parents() {
        return tel_parents;
    }

    public void setTel_parents(String tel_parents) {
        this.tel_parents = tel_parents;
    }

    public String getEmail_parents() {
        return email_parents;
    }

    public void setEmail_parents(String email_parents) {
        this.email_parents = email_parents;
    }

    public static Creator<Lists_Student> getCREATOR() {
        return CREATOR;
    }
}
