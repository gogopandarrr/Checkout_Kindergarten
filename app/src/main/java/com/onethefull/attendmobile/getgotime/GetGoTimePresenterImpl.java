package com.onethefull.attendmobile.getgotime;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;
import com.onethefull.attendmobile.api.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetGoTimePresenterImpl implements GetGoTimePresenter {
    private static final String TAG = GetGoTimePresenterImpl.class.getSimpleName();
    private Context context;
    private GetGoTimeView getGoTimeView;
    ApiService service;
    private SharedPrefManager mSharedPrefs;

    public GetGoTimePresenterImpl(GetGoTimeView getGoTimeView, Context context) {
        this.context = context;
        this.getGoTimeView = getGoTimeView;
        mSharedPrefs = SharedPrefManager.getInstance(context);
    }

    @Override
    public void performGetGoTime(String id) {
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();

        try {
            obj.put("USER_EMAIL", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.getGoBackTimeResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){
                        Log.d(TAG,"success:: " + object.toString());

                        String goTime = object.get("gohome_time").toString().replace("\"","");
                        getGoTimeView.getGotimeSuccess(goTime);
                    }
                }else{
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        getGoTimeView.validation(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG,"onFailure!!" );
                getGoTimeView.error();
            }
        });


    }
}
