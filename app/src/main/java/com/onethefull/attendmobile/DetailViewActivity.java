package com.onethefull.attendmobile;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.onethefull.attendmobile.api.TinyDB;
import com.onethefull.attendmobile.deletelist.DeleteListPresenter;
import com.onethefull.attendmobile.deletelist.DeleteListView;
import com.onethefull.attendmobile.deletelist.DeletePresenterImpl;
import com.bumptech.glide.Glide;
import com.onethefull.attendmobile.lists.Lists_downInfo;
import com.onethefull.attendmobile.updatelist.UpdateChildrenPresenterImp;
import com.onethefull.attendmobile.updatelist.UpdateChildrenView;
import com.onethefull.attendmobile.updatelist.UpdatePresenter;
import com.onethefull.wonderful_cv_library.CV_Package.CVServiceConnectionManager;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailViewActivity extends AppCompatActivity implements SetChildrenView, DeleteListView, UpdateChildrenView{

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
    private UpdatePresenter updatePresenter;
    private SharedPrefManager mSharedPrefs;
    private ArrayList<Object> stp;
    private ArrayList<Identity> userList = new ArrayList<>();
    WonderfulCV wonderfulCV = new WonderfulCV();

    String id, cvid;
    byte[] image;
    int mode;

    TinyDB tinyDB;




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

        tinyDB = new TinyDB(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("원생정보");

      //원생등록 준비
        childrenPresenter = new SetChildrenPresenterImpl(DetailViewActivity.this, getApplicationContext());
      //삭제 준비
        deleteListPresenter = new DeletePresenterImpl(DetailViewActivity.this, getApplicationContext());
      //업데이트 준비
        updatePresenter = new UpdateChildrenPresenterImp(DetailViewActivity.this, getApplicationContext());

     //cv서버 연결
        wonderfulCV.getFullServerAddress("1thefull.ml", 5000);
        //토큰 받기
        wonderfulCV.retrieveTokenFromStorage();



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
                    tv_name.setText(listsDownInfo.getName());
                    tv_tel.setText(listsDownInfo.getTel());
                    tv_email.setText(listsDownInfo.getEmail());
                    cvid = listsDownInfo.getCvid();

                    stp = tinyDB.getListObject("userList", Identity.class);
                    userList.clear();
                    for (Object obj : stp){
                        userList.add((Identity) obj);
                    }

                    //유저 리스트 cvid로 이미지 주소 찾기
                    for (int i = 0; i < userList.size(); i++) {

                        String list_cvid = userList.get(i).id.toString();

                        if (list_cvid.equals(cvid)) {

                            String urlString = "http://1thefull.ml:5000/faceimages/" + userList.get(i).imageName;
                            Glide.with(this).load(urlString).into(iv_profile);

                            //사진 등록안된 유저 보기 모드 진입시 삭제 버튼 보이기
                            if (userList.get(i).imageName.equals("null")){

                                bt_delete.setVisibility(View.VISIBLE);

                            }


                        }//if
                    }//for



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

                switch (mode){

                    case 1 :

                        if (validateForm(name, tel) && image != null){
                            //원생등록
                            childrenPresenter.performJoin(id, name, cvid, tel, email);

                        }else{
                            Toast.makeText(DetailViewActivity.this, R.string.error_formFill, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 2 : //보기-편집 모드


                        updatePresenter.updateChildrenInfo(id, cvid, name, tel, email);


                        break;



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
                et_name.setText(listsDownInfo.getName());
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
                    Toast.makeText(DetailViewActivity.this, R.string.error_formFill2, Toast.LENGTH_SHORT).show();
                }

            }
        });



        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(DetailViewActivity.this);

                builder.setTitle(R.string.delete_sutudent);

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        deleteListPresenter.deleteList(id, cvid);


                        Integer userId = Integer.parseInt(cvid);
                        CVServiceConnectionManager.deleteUser(wonderfulCV.serverAddress, wonderfulCV.token, userId);



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
        image = intent.getByteArrayExtra("image");
        Glide.with(this).load(image).into(iv_profile);


        if (cvid.equals("-1")){

            finish();
            Toast.makeText(this, R.string.error_cvid, Toast.LENGTH_SHORT).show();
        }


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
    public void updateSuccess() {

        Toast.makeText(this, R.string.update_info_done, Toast.LENGTH_SHORT).show();
        Intent intent =  new Intent(DetailViewActivity.this, PeopleListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void success() {


        Toast.makeText(this, "등록 완료", Toast.LENGTH_SHORT).show();
        Intent intent =  new Intent(DetailViewActivity.this, PeopleListActivity.class);

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
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteError() {

        Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show();
    }
}//class

