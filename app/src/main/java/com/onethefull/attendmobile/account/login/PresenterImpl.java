package com.onethefull.attendmobile.account.login;

import android.content.Context;
import android.util.Log;

import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresenterImpl implements LoginPresenter {

    private static final String TAG = PresenterImpl.class.getSimpleName();
    private Context mContext;
    private LoginView mLoginView;
    private ApiService service;
    private SharedPrefManager mSharedPrefs;

    public PresenterImpl(LoginView loginView, Context context) {
        this.mLoginView = loginView;
        this.mContext = context;
        mSharedPrefs = SharedPrefManager.getInstance(context);
    }

    @Override
    public void performLogin(String id, String pwd) {

        service = ApiUtils.getService();
        final JSONObject obj = new JSONObject();

        try {
            obj.put("email", id);
            obj.put("pwd", pwd);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.getLoginResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){
                        Log.d(TAG,"success:: "+ object.toString());
                        String id = object.getAsJsonObject("data").get("email").toString();
                        mSharedPrefs.saveLoginData(id);
                        mLoginView.loginSuccess();
                    }
                }else {

                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        mLoginView.loginValidations(msg);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }



            }//oR

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.d(TAG,"onFailure!!" );
                mLoginView.loginError();


            }
        });


    }//
}
