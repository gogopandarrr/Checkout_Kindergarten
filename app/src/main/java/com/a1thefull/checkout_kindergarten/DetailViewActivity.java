package com.a1thefull.checkout_kindergarten;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.FileInputStream;
import java.io.IOException;

public class DetailViewActivity extends AppCompatActivity {

    ImageView iv_facePic;
    Button bt_cancel, bt_next;
    EditText et_username, et_tel, et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        initView();
        setImg();




    }//oc

    private void initView(){

        iv_facePic= findViewById(R.id.iv_facePic);
        bt_cancel= findViewById(R.id.bt_cancel);
        bt_next= findViewById(R.id.bt_next);
        et_username = findViewById(R.id.et_username);
        et_tel = findViewById(R.id.et_email);
        et_tel = findViewById(R.id.et_tel);



    }//init



    private void setImg(){

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");

        try {

            FileInputStream is = DetailViewActivity.this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this).load(bmp).into(iv_facePic);

    }//setImg

}//class

