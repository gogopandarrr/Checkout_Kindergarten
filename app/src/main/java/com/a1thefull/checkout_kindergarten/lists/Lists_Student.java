package com.a1thefull.checkout_kindergarten.lists;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Lists_Student implements Parcelable {

    Bitmap facePhoto;
    String name_student, tel_parents, email_parents;


    public Lists_Student(Bitmap facePhoto, String name_student, String tel_parents, String email_parents) {
        this.facePhoto = facePhoto;
        this.name_student = name_student;
        this.tel_parents = tel_parents;
        this.email_parents = email_parents;
    }

    protected Lists_Student(Parcel in) {
        facePhoto = in.readParcelable(Bitmap.class.getClassLoader());
        name_student = in.readString();
        tel_parents = in.readString();
        email_parents = in.readString();
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

    public Bitmap getFacePhoto() {
        return facePhoto;
    }

    public void setFacePhoto(Bitmap facePhoto) {
        this.facePhoto = facePhoto;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(facePhoto, i);
        parcel.writeString(name_student);
        parcel.writeString(tel_parents);
        parcel.writeString(email_parents);
    }
}
