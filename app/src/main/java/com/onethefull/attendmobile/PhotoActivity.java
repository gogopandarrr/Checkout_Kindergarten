package com.onethefull.attendmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onethefull.attendmobile.account.setchildren.DetailViewActivity;
import com.onethefull.wonderful_cv_library.CV_Package.CreateNewUserAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.Crypto;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;
import com.otaliastudios.cameraview.AspectRatio;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.SizeSelector;
import com.otaliastudios.cameraview.SizeSelectors;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {


    private static final String TAG = FRActivity.class.getSimpleName();
    com.otaliastudios.cameraview.CameraView cameraView;
    private ArrayList<Bitmap> facePics = new ArrayList<Bitmap>();
    private WonderfulCV wonderfulCV;
    private String name, tel, email;
    static int mode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);



        modeCheck();
        permissionCheck();
        cameraSetting();



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


    private void cameraSetting(){

        cameraView = findViewById(R.id.camera);
        cameraView.start();
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] bytes) {
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


                    facePics.add(bm);

                    cvCreateNewUser();
                    finishCamera(bytes);


                }//if

            }
        });


        SizeSelector width = SizeSelectors.minWidth(500);
        SizeSelector height = SizeSelectors.minHeight(500);
        SizeSelector dimensions = SizeSelectors.and(width, height);
        SizeSelector ratio = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0);
        SizeSelector result = SizeSelectors.or(
                SizeSelectors.and(ratio, dimensions), ratio, SizeSelectors.biggest());
        cameraView.setPictureSize(result);



        //카메라 오버레이 작업
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View viewOverlay =inflater.inflate(R.layout.overlay_camera, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        this.addContentView(viewOverlay, layoutParams);


    }///





    private void cvCreateNewUser() {
        wonderfulCV = new WonderfulCV();

        Crypto.deleteExistingTokenFromStorage();
        wonderfulCV.initiateServerConnection(getApplicationContext(), "1thefull.ml", 5000,
                "panda@1thefull.com", "zkfmak85");
        Log.d(TAG, "Registering new user with face");

        CreateNewUserAsyncTask createNewUserTask = new CreateNewUserAsyncTask(
                new CreateNewUserAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(int cvId) {
                        Log.d(TAG,"cv등록 완료");
                        Toast.makeText(getApplicationContext(),
                                "cv등록 완료"+cvId, Toast.LENGTH_SHORT).show();
                    }
                });

        if (wonderfulCV.checkIfServerConnectionInitialized()) {



            Log.e("facepic",facePics.size()+"");
            createNewUserTask.setUserInfo(wonderfulCV.serverAddress + "/api/user",
                    "jiwoo","seo", "01097892698", "jwseo2698@1thefull.com",
                    wonderfulCV.token, facePics);
            createNewUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }


    private void finishCamera(byte[] bytes){

        Intent intent = new Intent(PhotoActivity.this, DetailViewActivity.class);
        intent.putExtra("image",bytes);


            intent.putExtra("name", getIntent().getStringExtra("name"));
            intent.putExtra("tel", getIntent().getStringExtra("tel"));
            intent.putExtra("email", getIntent().getStringExtra("email"));
            intent.putExtra("mode", mode);
            startActivity(intent);
            finish();

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

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }


    public void capture(View v){

        cameraView.captureSnapshot();

//        cameraView.camera.takePicture(null, null, new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] bytes, Camera camera) {
//
//
//
//                if (bytes != null){
//                    int screenWidth = 480;
//                    int screenHeigh = 640;
//
//                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//
//                        Bitmap scaled  = Bitmap.createScaledBitmap(bm, screenHeigh, screenWidth, true);
//                        int w = scaled.getWidth();
//                        int h = scaled.getHeight();
//
//                        Matrix mtx = new Matrix();
//                        mtx.postRotate(90);
//
//                        bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
//                    }else{
//                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeigh, true);
//                        bm = scaled;
//                    }
//
//                    try {
//
//                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                        bm.compress(Bitmap.CompressFormat.JPEG, 85, bs);
//
//                        bs.close();
//                        bm.recycle();
//
//
//                        Intent intent = new Intent(PhotoActivity.this, DetailViewActivity.class);
//                        intent.putExtra("image",bs.toByteArray());
//
//
//                        if(mode == 4){
//                            mode = 4;
//                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.putExtra("name", getIntent().getStringExtra("name"));
//                            intent.putExtra("tel", getIntent().getStringExtra("tel"));
//                            intent.putExtra("email", getIntent().getStringExtra("email"));
//
//                        }
//                        else mode = 1; //처음시작
//
//                        intent.putExtra("mode", mode);
//                        startActivity(intent);
//
//
//                        finish();
//
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }//if
//
//            }
//        });


    }//
}//class
