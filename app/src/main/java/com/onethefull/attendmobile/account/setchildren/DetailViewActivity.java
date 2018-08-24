package com.onethefull.attendmobile.account.setchildren;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onethefull.attendmobile.FRActivity;
import com.onethefull.attendmobile.PeopleListActivity;
import com.onethefull.attendmobile.PhotoActivity;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.lists.Lists_Student;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailViewActivity extends AppCompatActivity implements SetChildrenView{

    private Toolbar toolbar;
    private CircleImageView iv_profile;
    private ImageView bt_takePhoto, bt_delete;
    private Button bt_edit, bt_finish;
    private TextView tv_name, tv_tel, tv_email;
    private EditText et_name, et_tel, et_email;
    private LinearLayout layout_text, layout_edit;
    private Lists_Student listsStudent;
    private SetChildrenPresenter childrenPresenter;
    private String id;
    private byte[] image;
    private int mode, position;
    private SharedPrefManager mSharedPrefs;

    //임시
    String cvid = "temp";


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
        bt_delete = findViewById(R.id.btn_delete);
        layout_edit = findViewById(R.id.layout_profile_edit);
        layout_text = findViewById(R.id.layout_profile);
        bt_takePhoto = findViewById(R.id.bt_takePhoto);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("원생정보");

      //원생등록
        childrenPresenter = new SetChildrenPresenterImpl(DetailViewActivity.this, getApplicationContext());


        //저장된 id 가져오기
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());
        id = mSharedPrefs.getLoginId();
        id = id.replace("\"", "");

    }//init


    private void modeCheck(){

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",-1);


            switch (mode){

                case 1 : //등록 모드

                    layout_text.setVisibility(View.GONE);
                    bt_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.GONE);
                    bt_delete.setVisibility(View.GONE);
                    Log.e("mode",mode+"");

                    image = getIntent().getByteArrayExtra("image");
                    Glide.with(this).load(image).into(iv_profile);

                    break;

                case 2 : //보기모드

                    layout_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.GONE);
                    bt_finish.setVisibility(View.GONE);
                    bt_delete.setVisibility(View.GONE);
                    Log.e("mode",mode+"");

                    position = getIntent().getIntExtra("position",-1);
                    listsStudent = getIntent().getParcelableExtra("listsStudent");

                    Glide.with(this).load(listsStudent.getImage()).into(iv_profile);

                    tv_name.setText(listsStudent.getName_student());
                    tv_tel.setText(listsStudent.getTel_parents());
                    tv_email.setText(listsStudent.getEmail_parents());

                    break;


                case 4 : //카메라 재촬영후 수정모드

                    layout_text.setVisibility(View.GONE);
                    bt_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.VISIBLE);
                    bt_delete.setVisibility(View.VISIBLE);
                    Log.e("mode",mode+"");

                    image = getIntent().getByteArrayExtra("image");
                    Log.e("e4",image.length+"");
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


                if (validateForm(name, tel)){
                    //원생등록
                    listsStudent = new Lists_Student(image, name, tel, email);


                    Log.e("id_detail", id+"");
                    childrenPresenter.performJoin(id, name, cvid, tel, email);

                }

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
                bt_delete.setVisibility(View.VISIBLE);


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
                finish();
            }
        });



        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(DetailViewActivity.this);

                builder.setTitle("원생 정보를 삭제하시겠습니까?");

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(DetailViewActivity.this, PeopleListActivity.class);
                        mode = 3;
                        intent.putExtra("position", position);
                        intent.putExtra("mode", mode);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);

                        finish();

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }
        });



    }//


    private boolean validateForm(String name, String tel){

        boolean valid= true;

        if(TextUtils.isEmpty(name)){
            et_name.setError(getResources().getString(R.string.error_void));
            valid= false;
        }else{
            et_name.setError(null);
        }


        if(TextUtils.isEmpty(tel)){
            et_tel.setError(getResources().getString(R.string.error_void));
            valid= false;
        }else{
            et_tel.setError(null);
        }

        return valid;

    }//



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


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void validation(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {


        Toast.makeText(this, "등록 완료", Toast.LENGTH_SHORT).show();
        Intent intent =  new Intent(DetailViewActivity.this, PeopleListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("position", position);
        intent.putExtra("mode", mode);
        intent.putExtra("listsStudent", listsStudent);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        finish();
    }

    @Override
    public void error() {
        Toast.makeText(this, "등록 실패", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void launch(Class cls) {

    }
}//class

