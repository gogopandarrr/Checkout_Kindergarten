package com.onethefull.attendmobile.getlist;

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

public class GetListPresenterImpl implements GetListPresenter {
    private static final String TAG = GetListPresenterImpl.class.getSimpleName();
    private Context mContext;
    private GetListView GetListView;
    private ApiService service;


    public GetListPresenterImpl(GetListView GetListView, Context mContext) {
        this.GetListView = GetListView;
        this.mContext = mContext;

    }

    @Override
    public void getInfo(String id) {
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.getListResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        Log.d(TAG,"success:: " + object.toString());
//                        setChildrenView.success();
                    }
                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
//                        setChildrenView.validation(msg);
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
//                setChildrenView.error();
            }
        });
    }//





}//class
