package com.onethefull.attendmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onethefull.wonderful_cv_library.CV_Package.RegisterNewAccountAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class CVServiceRegistration extends Activity {


    @BindView(R.id.et_password)
    EditText editTextPassword;
    @BindView(R.id.et_email)
    EditText editTextEmail;
    @BindView(R.id.et_kindergarten)
    EditText editTextName;



    WonderfulCV wonderfulCV = new WonderfulCV();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv_service_registeration);
        ButterKnife.bind(this);
        wonderfulCV = new WonderfulCV();
        wonderfulCV.getFullServerAddress("1thefull.ml", 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }




    @Optional
    @OnClick(R.id.bt_next)
    public void register() {

        String userEmail = editTextEmail.getText().toString().trim();
        String userPassword = editTextPassword.getText().toString().trim();
        String userOrganizationName = editTextName.getText().toString().trim();


        Log.d("CV_Info", "Registering new user");
        RegisterNewAccountAsyncTask registerNewAccountTask = new RegisterNewAccountAsyncTask(
                new RegisterNewAccountAsyncTask.AsyncResponse() {

                    @Override
                    public void processFinish(Boolean success) {
                        if (success) {
                            Log.d("CV_Info", "New Account Creation successful");
                            CharSequence text = "Account Created";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                        } else {
                            Log.d("CV_Info", "New Account Creation failed");
                            CharSequence text = "Error during Account Creation";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                        }
                    }
                });


        if (wonderfulCV.checkIfServerConnectionInitialized()) {
            registerNewAccountTask.setNewUserInfo(wonderfulCV.serverAddress + "/api/company",
                    userEmail, userOrganizationName, userPassword);

            registerNewAccountTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }



    public static Intent createIntent(Context context) {
        return new Intent(context, CVServiceRegistration.class);
    }
}