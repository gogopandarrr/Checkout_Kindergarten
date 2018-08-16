package com.a1thefull.checkout_kindergarten;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FloatingActionButton bt_close;
    private TextView tv_title;
    private EditText et_email, et_password, et_password_confirm, et_username;
    private CardView cardView_register;
    private Button bt_signUp;
    private int mode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        showEnterAnimation();
        initView();
        modeCheck();
        setListener();


    }//dc





    private void initView(){


        cardView_register= findViewById(R.id.cardView_legister);
        bt_close= findViewById(R.id.bt_close);
        et_email= findViewById(R.id.et_email);
        tv_title= findViewById(R.id.title_register);
        et_username= findViewById(R.id.et_username);
        et_password= findViewById(R.id.et_password);
        et_password_confirm= findViewById(R.id.et_password_confirm);
        bt_signUp= findViewById(R.id.bt_signUp);


    }//init


    private void modeCheck(){
        Intent data= getIntent();
        mode= data.getIntExtra("mode",0);
        LinearLayout layout= findViewById(R.id.linear_register);


        if(mode==2){

            layout.setVisibility(View.GONE);
            tv_title.setText("비밀번호 찾기");
            bt_signUp.setText("비밀번호 전송");

        }

    }//modecheck



    private void setListener(){

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateRevealClose();
            }
        });

        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email= et_email.getText().toString();
                String username= et_username.getText().toString();
                String password= et_password.getText().toString();
                String password_confirm= et_password_confirm.getText().toString();


               switch (mode){


                   case 1:
                        if (validateForm(email,password,password_confirm, username))
                            Toast.makeText(RegisterActivity.this, getResources().getText(R.string.success_registeration), Toast.LENGTH_SHORT).show();
                        else
//                            Toast.makeText(RegisterActivity.this, getResources().getText(R.string.error_registration), Toast.LENGTH_SHORT).show();
                       break;


                   case 2:

                       if(checkEmail(email))
                            Toast.makeText(RegisterActivity.this, getResources().getText(R.string.success_find_password), Toast.LENGTH_SHORT).show();
                       else
                           Toast.makeText(RegisterActivity.this, getResources().getText(R.string.error_email), Toast.LENGTH_SHORT).show();

                       break;


               }//switch


            }//onclick
        });


    }//listener

    //todo
    private void signUp(){





    }//signUP


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

    private void showEnterAnimation(){
        Transition transition= TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cardView_register.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {

                transition.removeListener(this);
                animateRevealShow();

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });


    }//showEnter


    private void animateRevealShow(){
        Animator animator= ViewAnimationUtils.createCircularReveal(
                cardView_register, cardView_register.getWidth()/2, 0, bt_close.getWidth()/2, cardView_register.getHeight());

        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
               cardView_register.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });

        animator.start();

    }//revealshow



    private void animateRevealClose(){
        Animator animator= ViewAnimationUtils.createCircularReveal(
                cardView_register, cardView_register.getWidth()/2, 0, cardView_register.getHeight(), bt_close.getHeight()/2);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cardView_register.setVisibility(View.INVISIBLE);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

        animator.start();

    }//revealclose



    @Override
    public void onBackPressed() {
        animateRevealClose();
    }


}//class
