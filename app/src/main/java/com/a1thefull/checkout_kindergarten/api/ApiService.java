package com.a1thefull.checkout_kindergarten.api;

import com.google.gson.JsonArray;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {


    //@FormUrlEncoded
    @POST("login")
    Call<User> requestLogin(@Body HashMap<String, String> map);

    // 비회원 URL 전송
    @POST("send_cellphone_confirm/downloadURL")
    Call<User> sendURL(@Body HashMap<String, String> map);


    // 사용자 정보 요청 from cvid
    @POST("pudding/send_cvid2")
    Call<User> cvidSend(@Body HashMap<String, String> map);

    // 유저 정보 호출
    @POST("get_userInfo")
    Call<User> getUserInfo(@Body HashMap<String, String> map);

    // 유저 정보 업데이트
    @POST("pudding/update/CVID")
    Call<User> updateUserInfo(@Body HashMap<String, String> map);

    // CV 정보 확인
    @POST("pudding/CVID_Infos")
    Call<User> cvInfo(@Body HashMap<String, String> map);

}
