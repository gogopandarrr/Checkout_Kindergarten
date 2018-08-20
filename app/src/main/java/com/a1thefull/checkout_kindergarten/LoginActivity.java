package com.a1thefull.checkout_kindergarten;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a1thefull.checkout_kindergarten.api.ApiService;
import com.a1thefull.checkout_kindergarten.api.SaveSharedPreference;
import com.a1thefull.checkout_kindergarten.api.ServiceGenerator;
import com.a1thefull.checkout_kindergarten.api.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_password;
    private Button bt_go;
    private TextView bt_forgot, bt_register;
    CheckBox autoCheck;

    //자동로그인
    SharedPreferences autoUser;
    Boolean autoChecked = false;
    private String loginEmail, loginPwd;
    String name = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initView();
        autoLogin();
        setListener();
    }//oc


    private void initView(){

        autoCheck = findViewById(R.id.autoCheck);
        et_email = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        bt_go = findViewById(R.id.bt_go);
        bt_register = findViewById(R.id.bt_register);
        bt_forgot = findViewById(R.id.bt_forgot);

        ServiceGenerator serviceGenerator = new ServiceGenerator();



    }


    private void autoLogin(){

        autoUser = getSharedPreferences("autoUser", MODE_PRIVATE);
        loginEmail = autoUser.getString("user_email","");
        loginPwd = autoUser.getString("user_pwd","");
        Log.d("저장된 데이터",loginEmail.toString()+" "+ loginPwd.toString());


        autoChecked = autoUser.getBoolean("AUTO_LOGIN", false);
        if (autoChecked){
            et_email.setText(loginEmail);
            et_password.setText(loginPwd);
            autoCheck.setChecked(true);

        }


    }

    //자동로그인 저장
    private void autoSave(int i){
        Log.d("버튼", "클릭" + et_password.getText().toString().trim());
        SharedPreferences user = getSharedPreferences("autoUser", MODE_PRIVATE);
        SharedPreferences.Editor userEditor = user.edit();

        if (i == 0){
            userEditor.putString("user_email", et_email.getText().toString().trim());
            userEditor.putString("user_pwd", et_password.getText().toString().trim());
            userEditor.putBoolean("EMAIL_CHECK", false);
            userEditor.putBoolean("AUTO_LOGIN", true);
            userEditor.commit();
        }else if (i == 1){
            userEditor.putString("user_email",et_email.getText().toString().trim());
            userEditor.putString("user_pwd","");
            userEditor.putBoolean("EMAIL_CHECK",true);
            userEditor.putBoolean("AUTO_LOGIN", false);
            userEditor.commit();

        }
    }

    // 자동 로그 체크 확인
    private void autoLoginCheck(){

        if (autoCheck.isChecked()) {
            Log.d("버튼", "클릭 + autoCheck.isChecked");
            autoSave(0);
        }
    }




    //서버통신
    private void loginService(){
        String email = et_email.getText().toString();
        String pwd = et_password.getText().toString();

        HashMap<String, String> map = new HashMap<>();

        map.put("USER_EMAIL", Base64.encodeToString(email.getBytes(), Base64.NO_WRAP));
        map.put("USER_PW", Base64.encodeToString(pwd.getBytes(), Base64.NO_WRAP));
        ApiService loginService = ServiceGenerator.createService(ApiService.class, email, pwd);

        Call<User> call = loginService.requestLogin(map);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Log.i("통신","메세지 : " + response.body().getAccessToken());

                    if (response.body().getStatus_code().equals("0")){

                        SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                        name = response.body().getName();
                        Log.d("통신","성공" + response.body().getStatus_code() + name);
                        SharedPreferences user = getSharedPreferences("autoUser", MODE_PRIVATE);
                        SharedPreferences.Editor userEditor = user.edit();
                        userEditor.putString("user_email", et_email.getText().toString().trim());
                        userEditor.putString("user_pwd", et_password.getText().toString().trim());
                        userEditor.putString("user_name", name);
                        userEditor.putString("user_accessToken", response.body().getAccessToken());

                        userEditor.commit();
                        Intent intent = new Intent(getApplicationContext(), PeopleListActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        name = response.body().getName();
                        Log.d("통신","성공" + response.body().getStatus_code());

                    }
                } else {
                    int statusCode = response.code();
                    Log.i("통신","성공 응답코드 : " + statusCode);
                }
            }//onRes

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.i("통신","실패 : " + t.toString());
                Toast.makeText(LoginActivity.this, "서버와 통신 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }//loginservice




    //리스너들
    private void setListener(){
        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Explode explode= new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);


                autoLoginCheck(); //자동로그인확인
                loginService(); //서버 요청


                startActivity(new Intent(LoginActivity.this, PeopleListActivity.class));


            }
        });




        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setEnterTransition(null);
                getWindow().setExitTransition(null);
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                int register= 1;
                intent.putExtra("mode", register);

                startActivity(intent);
            }
        });


        bt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setEnterTransition(null);
                getWindow().setExitTransition(null);
                ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, bt_register, bt_register.getTransitionName());
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                int find= 2;
                intent.putExtra("mode", find);

                startActivity(intent,options.toBundle());
            }
        });

    }//listener



    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


}//end
