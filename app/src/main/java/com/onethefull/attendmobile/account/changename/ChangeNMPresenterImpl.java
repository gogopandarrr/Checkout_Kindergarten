package com.onethefull.attendmobile.account.changename;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeNMPresenterImpl implements ChangeNMPresenter {

    private static final String TAG = ChangeNMPresenterImpl.class.getSimpleName();
    private Context context;
    private ApiService service;
    private ChangeNMView changeNMView;
    String kindergarten;

    public ChangeNMPresenterImpl(ChangeNMView changeNMView, Context context) {
        this.changeNMView = changeNMView;
        this.context = context;
    }

    @Override
    public void changeNM(String id, String kindergarten_NM) {
        service = ApiUtils.getService();
        final JSONObject obj = new JSONObject();

        try {

            kindergarten = kindergarten_NM;
            obj.put("USER_EMAIL", id);
            obj.put("KINDERGARTEN_NM", kindergarten_NM);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        service.changeKindergartenResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){
                        Log.d(TAG,"success:: "+ object.toString());
                        changeNMView.changeSuccess(kindergarten);
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                    changeNMView.error();
            }
        });
    }//



}
