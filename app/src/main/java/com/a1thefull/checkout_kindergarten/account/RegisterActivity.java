package com.a1thefull.checkout_kindergarten.account;

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

import com.a1thefull.checkout_kindergarten.R;
import com.a1thefull.checkout_kindergarten.account.login.LoginActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout layout_register, layout_forgot;
    private TextView tv_title, tv_forgot;
    private EditText et_email, et_password, et_password_confirm, et_username, et_email_find;
    private Button bt_signUp, bt_close;
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
        et_username= findViewById(R.id.et_username);
        et_password= findViewById(R.id.et_password);
        et_password_confirm= findViewById(R.id.et_password_confirm);
        bt_signUp = findViewById(R.id.bt_next);
        bt_close = findViewById(R.id.bt_cancel);



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

                String email= et_email.getText().toString();
                String email_find = et_email_find.getText().toString();
                String username= et_username.getText().toString();
                String password= et_password.getText().toString();
                String password_confirm= et_password_confirm.getText().toString();


               switch (mode){


                   case 1:
                        if (validateForm(email,password,password_confirm, username)){
                            Toast.makeText(RegisterActivity.this, getResources().getText(R.string.success_registeration), Toast.LENGTH_SHORT).show();
                            Intent intent =  new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{

                            Toast.makeText(RegisterActivity.this, getResources().getText(R.string.error_registration), Toast.LENGTH_SHORT).show();
                        }

                       break;


                   case 2:

                       if(checkEmail(email_find)){
                           Toast.makeText(RegisterActivity.this, getResources().getText(R.string.success_find_password), Toast.LENGTH_SHORT).show();
                           Intent intent =  new Intent(RegisterActivity.this, LoginActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           startActivity(intent);
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
        }else{
            et_password.setError(null);
        }


        if(TextUtils.isEmpty(username)){
            et_username.setError(getResources().getString(R.string.error_void));
            valid= false;
        }else{
            et_username.setError(null);
        }

        return valid;
    }


    //------------------------------------------------------








}//class
