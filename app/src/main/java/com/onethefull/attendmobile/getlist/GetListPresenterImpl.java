package com.onethefull.attendmobile.getlist;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetListPresenterImpl implements GetListPresenter {
    private static final String TAG = GetListPresenterImpl.class.getSimpleName();
    private Context mContext;
    private GetListView getListView;
    private ApiService service;


    public GetListPresenterImpl(GetListView getListView, Context mContext) {
        this.getListView = getListView;
        this.mContext = mContext;

    }

    @Override
    public void getInfo(String id) {
        service = ApiUtils.getService();
        JSONObject obj = new JSONObject();
        try {
            obj.put("USER_EMAIL", id);

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

                        JsonArray datas = object.getAsJsonArray("data");

                        for (int i = 0 ; i < datas.size(); i++){

                            JsonObject studentInfo = (JsonObject) datas.get(i);

                            String name = studentInfo.get("CHILDREN_NAME").toString();
                            String email_parent = studentInfo.get("PARENT_EMAIL").toString();
                            String tel_parent = studentInfo.get("PARENT_TEL").toString();
                            String date_registration = studentInfo.get("REGISTRATION_DATE").toString();




                        }

                        getListView.success();
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
