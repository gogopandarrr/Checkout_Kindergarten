package com.onethefull.attendmobile.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class User {

    @SerializedName("USER_PW")
    @Expose
    private String password;

    @SerializedName("username")
    @Expose
    private String name;

    @SerializedName("userEmail")
    @Expose
    private String email;

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("status_code")
    @Expose
    private String status_code;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("idx")
    @Expose
    private String idx;

    @SerializedName("RECEIVE_USER_TEL")
    @Expose
    private String receive_user_tel;

    @SerializedName("cvid")
    @Expose
    private String cvid;


    @SerializedName("USER_TEL")
    @Expose
    private String tel;

    @SerializedName("marketname1")
    @Expose
    private String market_name;

    @SerializedName("marketname2")
    @Expose
    private String place;

    @SerializedName("marketimg")
    @Expose
    private String marketimg;

    @SerializedName("userRole")
    @Expose
    private String user_role;

    @SerializedName("firstVisit")
    @Expose
    private String firstVisit;

    public String getFirstVisit() {
        return firstVisit;
    }

    public void setFirstVisit(String firstVisit) {
        this.firstVisit = firstVisit;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getCvid() {
        return cvid;
    }

    public void setCvid(String cvid) {
        this.cvid = cvid;
    }

    public String getMarketimg() {
        return marketimg;
    }

    public void setMarketimg(String marketimg) {
        this.marketimg = marketimg;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMarket_name() {
        return market_name;
    }

    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    private HashMap<String, String> userInfo;

    public HashMap<String, String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(HashMap<String, String> userInfo) {
        this.userInfo = userInfo;
    }


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getReceive_user_tel() {
        return receive_user_tel;
    }

    public void setReceive_user_tel(String receive_user_tel) {
        this.receive_user_tel = receive_user_tel;
    }


    public String getEmail() {      return email;   }

    public void setEmail(String email) {     this.email = email;   }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_code() {
        return status_code;
    }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

     public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", status_code='" + status_code + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", idx='" + idx + '\'' +
                ", receive_user_tel='" + receive_user_tel + '\'' +
                ", cvid='" + cvid + '\'' +
                ", tel='" + tel + '\'' +
                ", market_name='" + market_name + '\'' +
                ", place='" + place + '\'' +
                ", marketimg='" + marketimg + '\'' +
                ", user_role='" + user_role + '\'' +
                ", firstVisit='" + firstVisit + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
