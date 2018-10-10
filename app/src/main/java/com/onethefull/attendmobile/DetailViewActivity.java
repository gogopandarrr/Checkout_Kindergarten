package com.onethefull.attendmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.onethefull.attendmobile.dialog.CustomTwoBtnDialog;
import com.onethefull.attendmobile.lists.Lists_downInfo;
import com.onethefull.attendmobile.updatelist.UpdateChildrenPresenterImp;
import com.onethefull.attendmobile.updatelist.UpdateChildrenView;
import com.onethefull.attendmobile.updatelist.UpdatePresenter;
import com.onethefull.wonderful_cv_library.CV_Package.CVServiceConnectionManager;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailViewActivity extends AppCompatActivity implements SetChildrenView, DeleteListView, UpdateChildrenView{

    private static final String TAG = DetailViewActivity.class.getSimpleName();
    private static DeleteListPresenter deleteListPresenter;


    private Lists_downInfo listsDownInfo;

    private SetChildrenPresenter childrenPresenter;
    private UpdatePresenter updatePresenter;

    private ArrayList<Object> stp;
    private ArrayList<Identity> userList = new ArrayList<>();
    public static String id;
    public static String cvid;
    private byte[] image;
    private int mode;

    private static WonderfulCV wonderfulCV = new WonderfulCV();
    private TinyDB tinyDB;
    private SharedPrefManager mSharedPrefs;
    private CustomTwoBtnDialog customTwoBtnDialog;


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_profile) CircleImageView iv_profile;
    @BindView(R.id.bt_takePhoto) ImageView bt_takePhoto;
    @BindView(R.id.btn_delete) Button bt_delete;
    @BindView(R.id.btn_edit) Button bt_edit;
    @BindView(R.id.btn_finish) Button bt_finish;
    @BindView(R.id.tv_stuName) TextView tv_name;
    @BindView(R.id.tv_stuTel) TextView tv_tel;
    @BindView(R.id.tv_stuEmail) TextView tv_email;
    @BindView(R.id.et_stuName) EditText et_name;
    @BindView(R.id.et_stuTel) EditText et_tel;
    @BindView(R.id.et_stuEmail) EditText et_email;
    @BindView(R.id.layout_profile_edit) LinearLayout layout_edit;
    @BindView(R.id.layout_profile) LinearLayout layout_text;

    @BindString(R.string.app_name) String appname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        ButterKnife.bind(this);

        initView();
        modeCheck();



    }//oc


    @OnClick({R.id.btn_finish, R.id.btn_edit, R.id.bt_takePhoto, R.id.btn_delete})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.btn_finish:
                done();
                break;
            case R.id.btn_edit:
                editMode();
                break;
            case R.id.bt_takePhoto:
                takePhoto();
                break;
            case R.id.btn_delete:
                deleteDialog();
                break;
            default:
                break;
        }
    }//



    private void initView(){

        tinyDB = new TinyDB(this);
        mSharedPrefs = SharedPrefManager.getInstance(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.info_student);

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
        id = mSharedPrefs.getLoginId();

    }//init


    private void done(){
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
    }//


    private void editMode(){

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


    private void takePhoto(){

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

    private void deleteDialog(){

        customTwoBtnDialog = new CustomTwoBtnDialog(this, getResources().getString(R.string.delete_sutudent), "delete_student", null);
        customTwoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customTwoBtnDialog.setCancelable(false);
        customTwoBtnDialog.show();


    }


    static public void executeDelete(){

        Log.e(TAG, id+"   "+cvid+"실행");
        deleteListPresenter.deleteList(id, cvid);
        Integer userId = Integer.parseInt(cvid);
        CVServiceConnectionManager.deleteUser(wonderfulCV.serverAddress, wonderfulCV.token, userId);

    }


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

        //cvid를 못받으면 종료
        if (cvid.equals("-1")){

            finish();
            Toast.makeText(this, R.string.error_cvid, Toast.LENGTH_SHORT).show();
        }

        //이미지를 받아오면 버튼 투명하게
        if (image != null){

//            bt_takePhoto.setVisibility(View.INVISIBLE);
            bt_takePhoto.setAlpha((float) 0.1);
        }else{
            bt_takePhoto.setAlpha((float) 1.0);
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
        Intent intent =  new Intent(DetailViewActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void success() {


        Toast.makeText(this, getResources().getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        Intent intent =  new Intent(DetailViewActivity.this, MainActivity.class);

        mode = 1;
        intent.putExtra("mode", mode);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    public void error() {
        Toast.makeText(this, getResources().getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void deleteSuccess() {

        Toast.makeText(this, getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DetailViewActivity.this, MainActivity.class);
        mode = 3;
        intent.putExtra("mode", 3);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteError() {

        Toast.makeText(this, getResources().getString(R.string.delete_fail), Toast.LENGTH_SHORT).show();
    }
}//class

