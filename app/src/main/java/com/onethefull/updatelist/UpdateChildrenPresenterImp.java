package com.onethefull.updatelist;

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

public class UpdateChildrenPresenterImp implements UpdatePresenter{
    private static final String TAG = UpdateChildrenPresenterImp.class.getSimpleName();
    Context context;
    ApiService service;
    UpdateChildrenView updateChildrenView;


    public UpdateChildrenPresenterImp(UpdateChildrenView updateChildrenView, Context context) {
        this.updateChildrenView = updateChildrenView;
        this.context = context;
    }

    @Override
    public void updateChildrenInfo(String id, String cvid, String name, String tel, String email) {
        service = ApiUtils.getService();
        final JSONObject obj = new JSONObject();

        try {
            obj.put("USER_EMAIL", id);
            obj.put("CHILDREN_CVID",cvid);
            obj.put("CHILDREN_NAME", name);
            obj.put("PARENT_TEL", tel);
            obj.put("PARENT_EMAIL", email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.updateChildrenResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){
                        Log.d(TAG, "success:: " + object.toString());
                        updateChildrenView.updateSuccess();

                    }else{

                        try {
                            JSONObject errorObj = new JSONObject(response.errorBody().string());
                            Log.d(TAG,"error:: " + errorObj.toString());
                            String msg = errorObj.getString("message");
                            updateChildrenView.validation(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }




                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure!!");
                updateChildrenView.error();
            }
        });



    }
}
