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
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    CameraView cameraView;

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
                    int screenWidth =  getResources().getDisplayMetrics().widthPixels;
                    int screenHeigh = getResources().getDisplayMetrics().heightPixels;

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
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);

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

    public  static  void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera){

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degreses = 0;
        switch (rotation){

            case Surface.ROTATION_0:
                degreses = 0;
                break;

                case Surface.ROTATION_90:
                    degreses = 90;
                    break;

                    case Surface.ROTATION_180:
                        degreses = 180;
                        break;

                        case Surface.ROTATION_270:
                            degreses = 270;
                            break;

        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degreses) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degreses + 360) % 360;
        }

        camera.setDisplayOrientation(result);


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
