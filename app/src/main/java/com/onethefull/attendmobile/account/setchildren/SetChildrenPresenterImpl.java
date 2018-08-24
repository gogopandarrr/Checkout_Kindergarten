package com.onethefull.attendmobile.account.setchildren;

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

public class SetChildrenPresenterImpl implements SetChildrenPresenter{
    private static final String TAG = SetChildrenPresenterImpl.class.getSimpleName();
    private Context mContext;
    private SetChildrenView setChildrenView;
    private ApiService service;

    public SetChildrenPresenterImpl(SetChildrenView setChildrenView, Context mContext) {
        this.setChildrenView = setChildrenView;
        this.mContext = mContext;
    }

    @Override
    public void performJoin(String id, String name, String cvid, String parentsTel, String parentsEmail) {
        // prepare call in Retrofit 2.0
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();
        try {
            obj.put("USER_EMAIL", id);
            obj.put("CHILDREN_NAME", name);
            obj.put("CHILDREN_CVID", cvid);
            obj.put("PARENT_TEL", parentsTel);
            obj.put("PARENT_EMAIL", parentsEmail);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // start call
        service.getChildrenResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        Log.d(TAG,"success:: " + object.toString());
                        setChildrenView.success();
                    }
                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        setChildrenView.validation(msg);
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
                setChildrenView.error();
            }
        });
    }


}
