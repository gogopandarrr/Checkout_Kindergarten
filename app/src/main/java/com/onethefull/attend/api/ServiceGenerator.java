package com.onethefull.attend.api;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {


  /*  //public static final String SERVER_URL = "http://35.200.206.16:8080/"; // 온니 카페
  *//* public static  String SERVER_URL = "http://35.200.246.186:8080/"; // 테스트
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder = new Retrofit.Builder()
                                                    .baseUrl(SERVER_URL)
                                                    .addConverterFactory(GsonConverterFactory.create());*/
    public static String SERVER_URL; // 테스트
    private static OkHttpClient.Builder httpClient;
    private static Retrofit.Builder builder;

    public ServiceGenerator() {

            SERVER_URL = "http://35.240.201.239:8080/";

        httpClient = new OkHttpClient.Builder();
        builder = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create());
    }

    public static <S> S createService(Class<S> serviceClass) {
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        //.header("Authorization", userPhone)
                        .header("Accept", "application/json; charset=UTF-8")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                //Log.d("base64",request.toString());
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
    public static <S> S createService(Class<S> serviceClass, String userEmail){

        if(userEmail != null){
            String credtials = userEmail;
            final String basic = "Basic" + Base64.encodeToString(credtials.getBytes(), Base64.NO_WRAP);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json; charset=UTF-8")
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }else{
            Log.d("base64","nulllllllllllll");
        }
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
    public static <S> S createService(Class<S> serviceClass, String userEmail, String password){

        if(userEmail != null && password != null){
            String credtials = userEmail + ":" + password;
            final String basic = "Basic" + Base64.encodeToString(credtials.getBytes(), Base64.NO_WRAP);
            //Log.d("base64",basic);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                                                    .header("Authorization", basic)
                                                    .header("Accept", "application/json; charset=UTF-8")
                                                    .header("Content-Type", "application/json")
                                                    .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    //Log.d("base64",request.toString());
                    return chain.proceed(request);
                }
            });
        }else{
            Log.d("base64","nulllllllllllll");
        }
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

}
