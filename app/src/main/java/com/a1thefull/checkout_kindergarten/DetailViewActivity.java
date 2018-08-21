package com.a1thefull.checkout_kindergarten;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a1thefull.checkout_kindergarten.lists.Lists_Student;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    CircleImageView iv_profile;
    ImageView bt_takePhoto;
    Button bt_edit, bt_finish;
    TextView tv_name, tv_tel, tv_email;
    EditText et_name, et_tel, et_email;
    LinearLayout layout_text, layout_edit;
    Lists_Student listsStudent;
    static byte[] image;
    int mode, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        initView();
        setListener();
        modeCheck();



    }//oc

    private void initView(){

        toolbar = findViewById(R.id.toolbar);
        iv_profile = findViewById(R.id.iv_profile);
        tv_name = findViewById(R.id.tv_stuName);
        tv_tel = findViewById(R.id.tv_stuTel);
        tv_email = findViewById(R.id.tv_stuEmail);
        et_name = findViewById(R.id.et_stuName);
        et_tel = findViewById(R.id.et_stuTel);
        et_email = findViewById(R.id.et_stuEmail);
        bt_edit = findViewById(R.id.btn_edit);
        bt_finish = findViewById(R.id.btn_finish);
        layout_edit = findViewById(R.id.layout_profile_edit);
        layout_text = findViewById(R.id.layout_profile);
        bt_takePhoto = findViewById(R.id.bt_takePhoto);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("원생정보");

    }//init


    private void modeCheck(){

        Intent intent = getIntent();

        mode = intent.getIntExtra("mode",-1);


            switch (mode){

                case 1 :

                    layout_text.setVisibility(View.GONE);
                    bt_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.GONE);

                    image = getIntent().getByteArrayExtra("image");
                    Glide.with(this).load(image).into(iv_profile);

                    break;

                case 2 :


                    layout_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.GONE);
                    bt_finish.setVisibility(View.GONE);

                    position = getIntent().getIntExtra("position",-1);
                    listsStudent = getIntent().getParcelableExtra("listsStudent");

                    Glide.with(this).load(listsStudent.getImage()).into(iv_profile);

                    tv_name.setText(listsStudent.getName_student());
                    tv_tel.setText(listsStudent.getTel_parents());
                    tv_email.setText(listsStudent.getEmail_parents());

                    break;


                case 4 :

                    layout_text.setVisibility(View.GONE);
                    bt_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.VISIBLE);


                    image = getIntent().getByteArrayExtra("image");
                    Glide.with(this).load(image).into(iv_profile);

                    et_name.setText(intent.getStringExtra("name"));
                    et_tel.setText(intent.getStringExtra("tel"));
                    et_email.setText(intent.getStringExtra("email"));


                    break;

            }



    }//




    private void setListener(){
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = et_name.getText().toString();
                String tel = et_tel.getText().toString();
                String email = et_email.getText().toString();

                listsStudent = new Lists_Student(image, name, tel, email);
                Intent intent =  new Intent(DetailViewActivity.this, PeopleListActivity.class);

                    intent.putExtra("position", position);
                    intent.putExtra("mode", mode);
                    intent.putExtra("listsStudent", listsStudent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);

                    finish();

                }


        });


        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout_text.setVisibility(View.GONE);
                bt_edit.setVisibility(View.GONE);
                layout_edit.setVisibility(View.VISIBLE);
                bt_finish.setVisibility(View.VISIBLE);
                bt_takePhoto.setVisibility(View.VISIBLE);


                et_name.setText(listsStudent.getName_student());
                et_tel.setText(listsStudent.getTel_parents());
                et_email.setText(listsStudent.getEmail_parents());


            }
        });


        bt_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mode = 4;
                Intent intent = new Intent(DetailViewActivity.this, PhotoActivity.class);

                intent.putExtra("name",et_name.getText().toString());
                intent.putExtra("tel",et_tel.getText().toString());
                intent.putExtra("email",et_email.getText().toString());
                intent.putExtra("mode", mode);


                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:{
                onBackPressed();
                return true;
            }

        }


        return super.onOptionsItemSelected(item);
    }
}//class

