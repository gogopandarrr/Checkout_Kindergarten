package com.onethefull.attend.account.join;

import android.content.Context;
import android.util.Log;

import com.onethefull.attend.api.ApiService;
import com.onethefull.attend.api.ApiUtils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinPresenterImpl implements JoinPresenter{
    private static final String TAG = JoinPresenterImpl.class.getSimpleName();
    private Context mContext;
    private JoinView mJoinView;
    private ApiService service;

    public JoinPresenterImpl(JoinView mJoinView, Context mContext) {
        this.mJoinView = mJoinView;
        this.mContext = mContext;
    }

    @Override
    public void performJoin(String id, String pwd, String name) {
        // prepare call in Retrofit 2.0
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", id);
            obj.put("pwd", pwd);
            obj.put("kindergarten_nm", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // start call
        service.getJoinResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        Log.d(TAG,"success:: " + object.toString());
                        mJoinView.success();
                    }
                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        mJoinView.validation(msg);
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
                mJoinView.error();
            }
        });
    }
}
