package com.onethefull.attendmobile.deleteclass;

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

public class DeleteClassPresenterImpl implements DeleteClassPresenter {

    private static final String TAG = DeleteClassPresenterImpl.class.getSimpleName();
    private Context context;
    private ApiService service;
    private DeleteClassView deleteClassView;


    public DeleteClassPresenterImpl(DeleteClassView deleteClassView, Context context) {
        this.context = context;
        this.deleteClassView = deleteClassView;
    }

    @Override
    public void deleteClass(String id, String code) {

        service = ApiUtils.getService();
        final JSONObject obj = new JSONObject();

        try {
            obj.put("USER_EMAIL", id);
            obj.put("KINDERGARTEN_CLASSCODE", code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.deleteClassResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){

                        Log.d(TAG, "success:: " + object.toString());
                        deleteClassView.deleteSuccess();
                    }

                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG, "error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        deleteClassView.validation(msg);

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
                deleteClassView.error();

            }
        });


    }
}
