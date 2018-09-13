package com.onethefull.attendmobile.getattendance;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.api.ApiService;
import com.onethefull.attendmobile.api.ApiUtils;
import com.onethefull.attendmobile.lists.Lists_Attendance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAttendancePresenterImpl implements GetAttendancePresenter {

    private static final String TAG = GetAttendanceView.class.getSimpleName();
    private Context context;
    private ApiService service;
    private GetAttendanceView attendanceView;
    private ArrayList<Lists_Attendance> attendanceArrayList = new ArrayList<>();
    private String date_copy;

    public GetAttendancePresenterImpl(Context context, GetAttendanceView attendanceView) {
        this.context = context;
        this.attendanceView = attendanceView;
    }

    @Override
    public void getAttendanceList(String id, String date) {
        service = ApiUtils.getService();
        final JSONObject obj =  new JSONObject();
        try {
            obj.put("USER_EMAIL", id);
            obj.put("SELECT_DATE", date);

            date_copy = date;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        service.getAttenanceListResult(obj.toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    JsonObject object = response.body();
                    if (object != null){

                        JsonArray datas = object.getAsJsonArray("AttendanceList");

                        for (int i = 0; i < datas.size(); i++){
                            JsonObject attendanceInfo = (JsonObject) datas.get(i);

                            String name = attendanceInfo.get("CHILDREN_NAME").toString().replace("\"","");
                            String inTime = attendanceInfo.get("ATTENDANCE_TIME").toString().replace("\"","");
                            String outTime = attendanceInfo.get("GOHOME_TIME").toString().replace("\"","");
                            String cvid = attendanceInfo.get("CHILDREN_CVID").toString().replace("\"","");

                            String tel;
                            if (attendanceInfo.get("PARENT_TEL") != null){
                                tel = attendanceInfo.get("PARENT_TEL").toString().replace("\"","");
                            }else{
                                tel = context.getResources().getString(R.string.deleted_student);

                            }



                            attendanceArrayList.add(new Lists_Attendance(name, inTime, outTime, cvid, tel));

                        }

                        attendanceView.success(date_copy, attendanceArrayList);
                        attendanceArrayList.clear();

                    }else{

                        try {
                            JSONObject errorObj = new JSONObject(response.errorBody().string());
                            Log.d(TAG, "error:: " + errorObj.toString());
                            String msg = errorObj.getString("message");
                            attendanceView.validation(msg);
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
                Log.d(TAG,"onFailure!!" );

            }
        });



    }//





}// class
