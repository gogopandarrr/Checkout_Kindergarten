package com.onethefull.attendmobile.api;

import android.database.Observable;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("join")
    Call<JsonObject> getJoinResult(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonObject> getLoginResult(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("set_children")
    Call<JsonObject> getChildrenResult (@Body String body);


    @Headers("Content-Type: application/json")
    @POST("children_list")
    Call<JsonObject> getListResult (@Body String body);


    @Headers("Content-Type: application/json")
    @POST("change_kindergartenNM")
    Call<JsonObject> changeKindergartenResult (@Body String body);

    @Headers("Content-Type: application/json")
    @POST("mod_children")
    Call<JsonObject> updateChildrenResult (@Body String body);


    @Headers("Content-Type: application/json")
    @POST("get_attendance_list")
    Call<JsonObject> getAttenanceListResult (@Body String body);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "/delete_children", hasBody = true)
    Call<JsonObject> deleteListResult(@Body String body);
}
