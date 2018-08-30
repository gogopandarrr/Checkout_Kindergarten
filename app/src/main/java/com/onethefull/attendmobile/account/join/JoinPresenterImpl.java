package com.onethefull.attendmobile.account.join;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;
import com.google.gson.JsonObject;
import com.onethefull.wonderful_cv_library.CV_Package.RegisterNewAccountAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinPresenterImpl implements JoinPresenter{
    private static final String TAG = JoinPresenterImpl.class.getSimpleName();
    private Context mContext;
    private JoinView mJoinView;
    private ApiService service;
    private WonderfulCV wonderfulCV = new WonderfulCV();
    public JoinPresenterImpl(JoinView mJoinView, Context mContext) {
        this.mJoinView = mJoinView;
        this.mContext = mContext;
        wonderfulCV = new WonderfulCV();
        wonderfulCV.getFullServerAddress("1thefull.ml", 5000);
    }

    @Override
    public void performJoin(String id, String pwd, String name) {
        // prepare call in Retrofit 2.0
        service = ApiUtils.getService();
        final JSONObject obj = new JSONObject();
        try {
            obj.put("email", id.trim());
            obj.put("pwd", pwd.trim());
            obj.put("kindergarten_nm", name.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // start call
        service.getJoinResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        Log.d(TAG,"success:: " + object.toString());
                        mJoinView.success();
                    }
                }else {
                    try {
                        JSONObject errorObj = new JSONObject(response.errorBody().string());
                        Log.d(TAG,"error:: " + errorObj.toString());
                        String msg = errorObj.getString("message");
                        mJoinView.validation(msg);
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
                mJoinView.error();
            }
        });
    }

    @Override
    public void performJoinCVservice(String id, String pwd, String name) {
        RegisterNewAccountAsyncTask registerNewAccountTask = new RegisterNewAccountAsyncTask(
                new RegisterNewAccountAsyncTask.AsyncResponse() {

                    @Override
                    public void processFinish(Boolean success) {
                        if (success) {
                            Log.d("CV_Info", "New Account Creation successful");

                        } else {
                            Log.d("CV_Info", "New Account Creation failed");

                        }
                    }
                });


        if (wonderfulCV.checkIfServerConnectionInitialized()) {
            registerNewAccountTask.setNewUserInfo(wonderfulCV.serverAddress + "/api/company",
                    id, name, pwd);
            registerNewAccountTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }
}
