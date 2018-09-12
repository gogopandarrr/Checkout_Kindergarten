package com.onethefull.attendmobile.setgotime;

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

public class SettingTimePresenterImpl implements SettingTimePresenter {
    private static final String TAG = SettingTimePresenterImpl.class.getSimpleName();
    private Context mContext;
    private SettingTimeView mSettingTimeView;
    private ApiService service;
    private SharedPrefManager mSharedPrefs;

    public SettingTimePresenterImpl(SettingTimeView settingTimeView, Context context) {
        this.mSettingTimeView = settingTimeView;
        this.mContext = context;
        mSharedPrefs = SharedPrefManager.getInstance(context);
    }

    @Override
    public void performSettingTime(String id, final String time) {
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();
        try {
            obj.put("USER_EMAIL", id);
            obj.put("GOHOME_TIME", time);
            Log.d(TAG, obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //start call
        service.getSettingTimeResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        Log.d(TAG,"success:: " + object.toString());
                        mSettingTimeView.success();
                        mSharedPrefs.setGoHomeTime(time);
                    }
                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        mSettingTimeView.validation(msg);
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
                mSettingTimeView.error();
            }
        });
    }
}
