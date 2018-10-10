package com.onethefull.attendmobile.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onethefull.attendmobile.DetailViewActivity;
import com.onethefull.attendmobile.LoginActivity;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.adapter.listener.TableViewListener;
import com.onethefull.attendmobile.api.SharedPrefManager;





public class CustomTwoBtnDialog extends Dialog {


    SharedPrefManager mSharedPrefs;



    public CustomTwoBtnDialog(@NonNull final Context context, final String content, final String type, final String s1) {
        super(context);

        mSharedPrefs = SharedPrefManager.getInstance(getContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_two_btn_dialog);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv_content = findViewById(R.id.tv_content);
        tv_content.setText(content);
        final FrameLayout btnL_con = findViewById(R.id.btnL_con);
        final FrameLayout btnR_con = findViewById(R.id.btnR_con);
        final TextView btnL = findViewById(R.id.btn_L);
        final TextView btnR = findViewById(R.id.btn_R);


        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (type){

                    case "logout":
                        mSharedPrefs.initialize(getContext());
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); }
                        else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); }
                        context.startActivity(intent);
                        break;

                    case "delete_student":
                        DetailViewActivity.executeDelete();
                        dismiss();
                        break;

                    case "call":
                        TableViewListener.call();
                        dismiss();
                        break;

                    case "sms":
                        TableViewListener.sendSMS();
                        dismiss();
                        break;
                }

            }
        });


        btnL_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnL.performClick();
            }
        });
        btnR_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnR.performClick();
            }
        });
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }




}//
