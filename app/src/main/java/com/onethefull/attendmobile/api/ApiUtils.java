package com.onethefull.attendmobile.api;

public class ApiUtils {
    public static final String BASE_URL = "http://35.240.201.239:8080/";

    public static ApiService getService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

}
