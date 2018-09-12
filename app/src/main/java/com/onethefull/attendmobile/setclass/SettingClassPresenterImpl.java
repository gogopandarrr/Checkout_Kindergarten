package com.onethefull.attendmobile.setclass;

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

public class SettingClassPresenterImpl implements SettingClassPresenter {

    private static final String TAG = SettingClassPresenter.class.getSimpleName();
    private Context context;
    private SettingClassView settingClassView;
    private ApiService service;
    private SharedPrefManager mSharedPrefs;

    public SettingClassPresenterImpl(SettingClassView settingClassView, Context context) {
        this.context = context;
        this.settingClassView = settingClassView;
        mSharedPrefs = SharedPrefManager.getInstance(context);
    }

    @Override
    public void performSettingClass(String id, String className) {
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();

        try {
            obj.put("USER_EMAIL", id);
            obj.put("KINDERGARTEN_CLASSNAME", className);
            Log.d(TAG, obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        service.getSettingTimeResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        Log.d(TAG, "success:: " + object.toString());
                        settingClassView.success();
                    }
                } else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG, "error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        settingClassView.validation(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure!!");
                settingClassView.error();
            }

        });

        }//
}
