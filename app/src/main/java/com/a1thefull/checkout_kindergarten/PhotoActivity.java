package com.a1thefull.checkout_kindergarten;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PhotoActivity extends AppCompatActivity {

    CameraView cameraView;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        cameraView = findViewById(R.id.cameraView);

        permissionCheck();

    }//oc


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
                        String filename = "bitmap.jpeg";
                        FileOutputStream stream = PhotoActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, stream);


                        stream.close();
                        bm.recycle();

                        Intent intent = new Intent(PhotoActivity.this, DetailViewActivity.class);
                        intent.putExtra("image", filename);
                        startActivity(intent);

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
