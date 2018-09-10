package com.onethefull.attendmobile.account.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.onethefull.attendmobile.MainActivity;
import com.onethefull.attendmobile.R;
import com.onethefull.attendmobile.account.join.RegisterActivity;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;


public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText et_email, et_password;
    private Button bt_go;
    private TextView bt_forgot, bt_register;
    private CircularProgressBar circularProgressBar;
    CheckBox autoCheck;

    private LoginPresenter mLoginPresenter;



    //자동로그인
    SharedPreferences autoUser;
    Boolean autoChecked = false;
    private String loginEmail, loginPwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        initView();
        autoLogin();
        setListener();
    }//oc


    private void initView(){

        mLoginPresenter = new PresenterImpl(LoginActivity.this, getApplicationContext());
        autoCheck = findViewById(R.id.autoCheck);
        et_email = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        bt_go = findViewById(R.id.bt_go);
        bt_register = findViewById(R.id.bt_register);
        bt_forgot = findViewById(R.id.bt_forgot);
        circularProgressBar = findViewById(R.id.progress_circular);

        updateValues();



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


    // 자동 로그인 체크 확인
    private void autoLoginCheck(){

        if (autoCheck.isChecked()) {
            Log.d("버튼", "클릭 + autoCheck.isChecked");
            Log.d("버튼", "클릭" + et_password.getText().toString().trim());
            SharedPreferences user = getSharedPreferences("autoUser", MODE_PRIVATE);
            SharedPreferences.Editor userEditor = user.edit();

            userEditor.putString("user_email", et_email.getText().toString().trim());
            userEditor.putString("user_pwd", et_password.getText().toString().trim());
            userEditor.putBoolean("AUTO_LOGIN", true);
            userEditor.commit();
        }
    }



    //리스너들
    private void setListener(){
        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoLoginCheck(); //자동로그인확인
                permission();

//                loginSuccess();
            }
        });




        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                int register = 1;
                intent.putExtra("mode", register);
                startActivity(intent);
            }
        });


        bt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                int forgot = 2;
                intent.putExtra("mode", forgot);
                startActivity(intent);
            }
        });

    }//listener



    private void permission(){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            int permission = checkSelfPermission(Manifest.permission.CAMERA);
            if (permission == PackageManager.PERMISSION_DENIED) {
                String[] arr = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(arr, 10);
            }else{
                login();
            }
        }


        }//



    private void login(){

        et_email.setEnabled(false);
        et_password.setEnabled(false);
        circularProgressBar.setVisibility(View.VISIBLE);

        final String id = et_email.getText().toString();
        String pwd = et_password.getText().toString();
        mLoginPresenter.performLogin(id, pwd);
    }//



    private void setBacktoNormal(){

        et_email.setEnabled(true);
        et_password.setEnabled(true);
        bt_go.setEnabled(true);
        circularProgressBar.setVisibility(View.GONE);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void loginValidations(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        setBacktoNormal();
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();

    }

    @Override
    public void loginError() {
        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
        setBacktoNormal();

    }

    @Override
    public void launch(Class cls) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {

                    Toast.makeText(this, R.string.error_permission, Toast.LENGTH_SHORT).show();
                } else {
                    login();
                }
        }
    }//


    private void updateValues(){
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_colors))
                .sweepSpeed(2f)
                .rotationSpeed(1f)
                .strokeWidth(dpToPx(10))
                .style(CircularProgressDrawable.STYLE_ROUNDED);

        circularProgressBar.setIndeterminateDrawable(b.build());


    }

    public int dpToPx(int dp){
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

}//end
