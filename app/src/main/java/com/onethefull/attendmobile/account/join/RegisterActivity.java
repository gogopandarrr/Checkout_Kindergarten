package com.onethefull.attendmobile.account.join;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onethefull.attendmobile.account.login.LoginActivity;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.account.resetpwd.ResetPwdPresenter;
import com.onethefull.attendmobile.account.resetpwd.ResetPwdPresenterImpl;
import com.onethefull.attendmobile.account.resetpwd.ResetPwdView;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements JoinView, ResetPwdView{

    private LinearLayout layout_register, layout_forgot;
    private TextView tv_title, tv_forgot;
    private EditText et_email, et_password, et_password_confirm, et_kindergarten, et_email_find;
    private Button bt_signUp, bt_close;
    private JoinPresenter mJoinPresenter;
    private ResetPwdPresenter resetPwdPresenter;
    int mode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        modeCheck();
        setListener();


    }//dc





    private void initView(){

        layout_forgot = findViewById(R.id.layout_forgot);
        layout_register = findViewById(R.id.layout_register);
        tv_title = findViewById(R.id.tv_title);
        tv_forgot = findViewById(R.id.tv_forgot);
        et_email= findViewById(R.id.et_email);
        et_email_find = findViewById(R.id.et_email_find);
        et_kindergarten = findViewById(R.id.et_kindergarten);
        et_password= findViewById(R.id.et_password);
        et_password_confirm= findViewById(R.id.et_password_confirm);
        bt_signUp = findViewById(R.id.bt_next);
        bt_close = findViewById(R.id.bt_cancel);


        mJoinPresenter = new JoinPresenterImpl(RegisterActivity.this, getApplicationContext());
        resetPwdPresenter = new ResetPwdPresenterImpl(RegisterActivity.this, getApplicationContext());

    }//init


    private void modeCheck(){
        Intent data= getIntent();
        mode= data.getIntExtra("mode",0);


        switch (mode){

            case 1:
                tv_title.setText("회원가입");
                layout_register.setVisibility(View.VISIBLE);
                layout_forgot.setVisibility(View.GONE);
                break;


            case 2:

                tv_title.setText("비밀번호 찾기");
                layout_register.setVisibility(View.GONE);
                layout_forgot.setVisibility(View.VISIBLE);

                break;

        }





    }//modecheck



    private void setListener(){


        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString();
                String email_find = et_email_find.getText().toString();
                String kindergarten = et_kindergarten.getText().toString();
                String password = et_password.getText().toString();
                String password_confirm = et_password_confirm.getText().toString();


               switch (mode){


                   case 1:
                        if (validateForm(email,password,password_confirm, kindergarten)){

                            mJoinPresenter.performJoin(email.trim(), password.trim(), kindergarten.trim());
                            mJoinPresenter.performJoinCVservice(email.trim(), password.trim(), kindergarten.trim());

                        }
                        else{

                            Toast.makeText(RegisterActivity.this, getResources().getText(R.string.error_registration), Toast.LENGTH_SHORT).show();
                        }

                       break;


                   case 2: //비밀번호 찾기 모드

                       if(checkEmail(email_find)){

                           String findEmail = et_email_find.getText().toString();
                           resetPwdPresenter.performResetPwd(findEmail);

                       }
                       else{
                           tv_forgot.setText(R.string.error_email);
                           tv_forgot.setTextColor(Color.RED);
                       }

                       break;


               }//switch
            }
        });



        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

    }//listener




    //-------------------------- 검증 ----------------------------

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    private boolean validateForm(String email, String password, String password_confirm, String username){

        boolean valid= true;

        if(TextUtils.isEmpty(email)){
            et_email.setError(getResources().getString(R.string.error_void));
            valid= false;
        }else if (!checkEmail(email)){
             et_email.setError(getResources().getString(R.string.error_email));
             valid= false;
        }else{
            et_email.setError(null);
        }


        if(TextUtils.isEmpty(password)){
            et_password.setError(getResources().getString(R.string.error_void));
            valid= false;
        }else if(!password.equals(password_confirm)){
            et_password.setError(getResources().getString(R.string.error_password_comfirm));
            valid= false;
            et_password.setText(null);
            et_password_confirm.setText(null);
        }else if(password.length()<6){
            et_password.setError(getResources().getString(R.string.error_password_length));
            valid= false;

        }else{
            et_password.setError(null);
        }


        if(TextUtils.isEmpty(username)){
            et_kindergarten.setError(getResources().getString(R.string.error_void));
            valid= false;
        }else{
            et_kindergarten.setError(null);
        }

        return valid;
    }

    @Override
    public void validation(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resetSuccess() {
        Toast.makeText(this, R.string.reset_success, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void success() {
        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
        launch(LoginActivity.class);
    }

    @Override
    public void error() {
        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void launch(Class cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    //------------------------------------------------------








}//class
