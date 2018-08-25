package com.onethefull.attendmobile.deletelist;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;
import com.onethefull.attendmobile.lists.Lists_downInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeletePresenterImpl implements DeleteListPresenter {

    private static final String TAG = DeleteListPresenter.class.getSimpleName();
    private Context context;
    private ApiService service;
    private DeleteListView deleteListView;

    public DeletePresenterImpl(DeleteListView deleteListView, Context context) {
        this.deleteListView = deleteListView;
        this.context = context;

    }


    @Override
    public void deleteList(String id, String cvid) {

        service = ApiUtils.getService();
        final JSONObject obj = new JSONObject();

        try {
            obj.put("USER_EMAIL", id);
            obj.put("CHILDREN_CVID", cvid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, obj+"");
        service.deleteListResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){
                        Log.d(TAG,"success:: "+ object.toString());
                        deleteListView.deleteSuccess();
                    }
                }else {

                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        deleteListView.validation(msg);
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
                deleteListView.deleteError();

            }
        });





    }


}
