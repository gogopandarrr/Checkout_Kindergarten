package com.onethefull.attendmobile.account.resetpwd;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPwdPresenterImpl implements ResetPwdPresenter {

    private static final String TAG = ResetPwdPresenterImpl.class.getSimpleName();
    private Context mContext;
    private ResetPwdView resetPwdView;
    private ApiService service;

    public ResetPwdPresenterImpl(ResetPwdView resetPwdView, Context mContext) {
        this.mContext = mContext;
        this.resetPwdView = resetPwdView;
    }

    @Override
    public void performResetPwd(String email) {
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", email);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.getrequestResetPwdResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        Log.d(TAG,"success:: " + object.toString());
                        resetPwdView.resetSuccess();
                    }
                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        resetPwdView.validation(msg);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG,"onFailure!!" );
                resetPwdView.error();
            }
        });
    }

}
