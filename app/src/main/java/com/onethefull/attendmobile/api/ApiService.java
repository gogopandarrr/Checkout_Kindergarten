package com.onethefull.attendmobile.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("join")
    Call<JsonObject> getJoinResult(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonObject> getLoginResult(@Body String body);

}
