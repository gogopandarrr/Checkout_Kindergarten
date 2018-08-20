package com.a1thefull.checkout_kindergarten;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.a1thefull.checkout_kindergarten.lists.Lists_Student;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DetailViewActivity extends AppCompatActivity {

    ImageView iv_facePic;
    Button bt_cancel, bt_next;
    EditText edit_username, edit_tel, edit_email;
    Bitmap bmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        initView();
        setImg();
        setListener();



    }//oc

    private void initView(){

        iv_facePic= findViewById(R.id.iv_facePic);
        bt_cancel= findViewById(R.id.bt_cancel);
        bt_next= findViewById(R.id.bt_next);
        edit_username = findViewById(R.id.et_studentname);
        edit_email = findViewById(R.id.et_parentsEmail);
        edit_tel = findViewById(R.id.et_tel);



    }//init



    private void setImg(){


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


    private void setListener(){
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edit_username.getText().toString();
                String tel = edit_tel.getText().toString();
                String email = edit_email.getText().toString();
                int mode =  1;

                Intent intent =  new Intent(DetailViewActivity.this, PeopleListActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.putExtra("mode", mode);
                intent.putExtra("name",name);
                intent.putExtra("tel",tel);
                intent.putExtra("email",email);

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 85, bs);
                intent.putExtra("byteImage",bs.toByteArray());

                startActivity(intent);

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

}//class

