package com.onethefull.attend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.onethefull.attend.api.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {

    CameraView cameraView;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        cameraView = findViewById(R.id.cameraView);

        modeCheck();

        permissionCheck();


    }//oc


    private void modeCheck(){

       mode = getIntent().getIntExtra("mode", -1);

    }

    private void permissionCheck(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permission = checkSelfPermission(Manifest.permission.CAMERA);
            if (permission == PackageManager.PERMISSION_DENIED){
                String[] arr = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(arr, 10);
            }
        }
    }//permissionCheck


    public void capture(View v){

        cameraView.camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {



                if (bytes != null){
                    int screenWidth = 480;
                    int screenHeigh = 640;

                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

                        Bitmap scaled  = Bitmap.createScaledBitmap(bm, screenHeigh, screenWidth, true);
                        int w = scaled.getWidth();
                        int h = scaled.getHeight();

                        Matrix mtx = new Matrix();
                        mtx.postRotate(90);

                        bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                    }else{
                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeigh, true);
                        bm = scaled;
                    }

                    try {

                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, bs);

                        bs.close();
                        bm.recycle();


                        Intent intent = new Intent(PhotoActivity.this, DetailViewActivity.class);
                        intent.putExtra("image",bs.toByteArray());


                        if(mode == 4){
                            mode = 4;
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("name", getIntent().getStringExtra("name"));
                            intent.putExtra("tel", getIntent().getStringExtra("tel"));
                            intent.putExtra("email", getIntent().getStringExtra("email"));

                        }
                        else mode = 1; //처음시작

                        intent.putExtra("mode", mode);
                        startActivity(intent);


                        finish();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }//if

            }
        });


    }//




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED|| grantResults[1] == PackageManager.PERMISSION_DENIED) {

                    Toast.makeText(this, "카메라 사용불가", Toast.LENGTH_SHORT).show();
                }
                else{

                }
        }

    }//




}//class
