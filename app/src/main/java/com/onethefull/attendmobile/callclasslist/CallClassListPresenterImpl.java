package com.onethefull.attendmobile.callclasslist;

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

public class CallClassListPresenterImpl implements CallClassListPresenter{

    private static final String TAG = CallClassListPresenterImpl.class.getSimpleName();
    private Context context;
    private ApiService service;
    private CallClassListView callClassListView;


    public CallClassListPresenterImpl(CallClassListView callClassListView, Context context) {
        this.context = context;
        this.callClassListView = callClassListView;
    }

    @Override
    public void callClassList(String id) {

        service = ApiUtils.getService();
        final JSONObject obj = new JSONObject();

        try {
            obj.put("USER_EMAIL", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        service.callClassListResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){

                    JsonObject object = response.body();
                    if (object != null){
                        Log.d(TAG, "success:: " + object.toString());
                        callClassListView.callClassSuccess();
                    }
                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG, "error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        callClassListView.validation(msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure!!");
                callClassListView.error();
            }
        });
    }
}
