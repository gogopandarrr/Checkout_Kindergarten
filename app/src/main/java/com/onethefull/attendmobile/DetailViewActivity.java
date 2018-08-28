package com.onethefull.attendmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
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

import com.onethefull.attendmobile.account.setchildren.SetChildrenPresenter;
import com.onethefull.attendmobile.account.setchildren.SetChildrenPresenterImpl;
import com.onethefull.attendmobile.account.setchildren.SetChildrenView;
import com.onethefull.attendmobile.api.SharedPrefManager;
import com.onethefull.attendmobile.deletelist.DeleteListPresenter;
import com.onethefull.attendmobile.deletelist.DeleteListView;
import com.onethefull.attendmobile.deletelist.DeletePresenterImpl;
import com.bumptech.glide.Glide;
import com.onethefull.attendmobile.lists.Lists_Student;
import com.onethefull.attendmobile.lists.Lists_downInfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailViewActivity extends AppCompatActivity implements SetChildrenView, DeleteListView{

    private static final String TAG = DetailViewActivity.class.getSimpleName();
    private Toolbar toolbar;
    private CircleImageView iv_profile;
    private ImageView bt_takePhoto, bt_delete;
    private Button bt_edit, bt_finish;
    private TextView tv_name, tv_tel, tv_email;
    private EditText et_name, et_tel, et_email;
    private LinearLayout layout_text, layout_edit;
    private Lists_downInfo listsDownInfo;
    private SetChildrenPresenter childrenPresenter;
    private DeleteListPresenter deleteListPresenter;
    String id, cvid;
    byte[] image;
    private int mode, position;
    private SharedPrefManager mSharedPrefs;
    private Lists_Student listsStudent;



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

      //원생등록 준비
        childrenPresenter = new SetChildrenPresenterImpl(DetailViewActivity.this, getApplicationContext());
      //삭제 준비
        deleteListPresenter = new DeletePresenterImpl(DetailViewActivity.this, getApplicationContext());



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
                    bt_takePhoto.setVisibility(View.VISIBLE);
                    bt_delete.setVisibility(View.GONE);

                    break;

                case 2 : //보기모드

                    layout_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.GONE);
                    bt_finish.setVisibility(View.GONE);
                    bt_delete.setVisibility(View.GONE);

                    listsDownInfo = getIntent().getParcelableExtra("listsStudent");

                    Glide.with(this).load(image).into(iv_profile);

                    tv_name.setText(listsDownInfo.getName());
                    tv_tel.setText(listsDownInfo.getTel());
                    tv_email.setText(listsDownInfo.getEmail());
                    cvid = listsDownInfo.getCvid();
                    position = getIntent().getIntExtra("position",-1);

                    break;


                case 4 : //편집 모드

                    layout_text.setVisibility(View.GONE);
                    bt_edit.setVisibility(View.GONE);
                    bt_takePhoto.setVisibility(View.GONE);
                    bt_delete.setVisibility(View.VISIBLE);


//                    image = getIntent().getByteArrayExtra("image");

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


                if (validateForm(name, tel) && image != null){
                    //원생등록
                    childrenPresenter.performJoin(id, name, cvid, tel, email);

                }else{
                    Toast.makeText(DetailViewActivity.this, "사진과 필수 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                bt_takePhoto.setVisibility(View.GONE);
                bt_delete.setVisibility(View.VISIBLE);
                et_name.setText(listsDownInfo.getEmail());
                et_tel.setText(listsDownInfo.getTel());
                et_email.setText(listsDownInfo.getEmail());


            }
        });


        bt_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validateForm(et_name.getText().toString(), et_tel.getText().toString())){

                    Intent intent = new Intent(DetailViewActivity.this, FRActivity.class);
                    intent.putExtra("name",et_name.getText().toString());
                    intent.putExtra("tel",et_tel.getText().toString());
                    intent.putExtra("email",et_email.getText().toString());
                    startActivity(intent);

                }else{
                    Toast.makeText(DetailViewActivity.this, "필수 칸을 먼저 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

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


                        deleteListPresenter.deleteList(id, cvid);



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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        cvid = intent.getStringExtra("cvid");
        Log.e(TAG, cvid+"<------------");
        image = intent.getByteArrayExtra("image");
        Glide.with(this).load(image).into(iv_profile);


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

//        listsStudent = new Lists_Student(et_name.getText().toString(), et_tel.getText().toString(), et_email.getText().toString(), cvid);
//        intent.putExtra("listsStudent", listsStudent);

        mode = 1;
        intent.putExtra("mode", mode);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    public void error() {
        Toast.makeText(this, "등록 실패", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void deleteSuccess() {

        Toast.makeText(this, "삭제 성공", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DetailViewActivity.this, PeopleListActivity.class);
        mode = 3;
        intent.putExtra("mode", 3);
        intent.putExtra("position",position);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteError() {

        Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show();
    }
}//class

