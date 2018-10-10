package com.onethefull.attendmobile.modifyclass;

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

public class ModifyClassPresenterImpl implements ModifyClassPresenter{

    private static final String TAG = ModifyClassPresenterImpl.class.getSimpleName();
    private Context context;
    private ApiService service;
    private SharedPrefManager mSharedPrefs;
    private ModifyClassView modifyClassView;


    public ModifyClassPresenterImpl(ModifyClassView modifyClassView, Context context) {
        this.context = context;
        this.modifyClassView = modifyClassView;
        mSharedPrefs = SharedPrefManager.getInstance(context);
    }

    @Override
    public void performModifyClass(String id, String code, String name) {

        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();

        try {
            obj.put("USER_EMAIL", id);
            obj.put("KINDERGARTEN_CLASSCODE", code);
            obj.put("KINDERGARTEN_CLASSNAME", name);
            Log.d(TAG, obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.modifyClassResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){
                        Log.d(TAG, "success:: " + object.toString());
                        modifyClassView.modifySuccess();
                    }

                }else{

                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG, "error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        modifyClassView.validation(msg);
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
                modifyClassView.error();
            }
        });

    }
}
