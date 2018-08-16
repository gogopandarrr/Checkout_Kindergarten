package com.a1thefull.checkout_kindergarten;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.onethefull.wonderful_cv_android.CV_Package.camera.CameraSourcePreview;
import com.onethefull.wonderful_cv_android.CV_Package.camera.GraphicOverlay;
import com.onethefull.wonderful_cv_library.CV_Package.FaceDetectionAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.FaceGraphic;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CameraActivity extends AppCompatActivity {

    CameraSourcePreview mPreview;
    GraphicOverlay mGraphicOverlay;

    WonderfulDetector boxDetector;
    WonderfulCV wonderfulCV;
    Bitmap currentFaceBitmap;
    Boolean gotResult = false;


    private    static final String TAG = "Face Detection";
    private String faceX, faceY, faceLeftEyeOpenProb, faceRightEyeOpenProb, faceSmilingProb;
    private CameraSource mCameraSource = null;
    private final float FACE_SIZE_THRESHOLD = 275.0f;
    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private Boolean gotResponse = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initView();
        permission();

    }//oc


    private void initView(){

        mPreview = findViewById(R.id.preview);
        mGraphicOverlay = findViewById(R.id.faceOverlay);
        wonderfulCV = new WonderfulCV();
        wonderfulCV.initiateServerConnection(this, "1thefull.ml", 5000,
                "michael.vogl@1thefull.com", "password");
    }


    private void permission(){

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }

    }//permission


    private void createCameraSource(){
        Context context = getApplicationContext();
        com.google.android.gms.vision.face.FaceDetector detector =
                new com.google.android.gms.vision.face.FaceDetector.Builder(context)
                        .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                        .build();

        boxDetector = new WonderfulDetector(detector, 300, 450);
        boxDetector.setProcessor(
                new MultiProcessor.Builder<>(new CameraActivity.GraphicFaceTrackerFactory()).build());

        if (!boxDetector.isOperational()){
            Log.w("Face Detection", "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, boxDetector)
                            .setRequestedPreviewSize(300, 450)
                            .setFacing(CameraSource.CAMERA_FACING_FRONT)
                            .setRequestedFps(30.0f)
                            .setAutoFocusEnabled(true)
                            .build();
    }





    private void startCameraSource(){

        if (mCameraSource != null){

            if (mCameraSource != null){
                try {
                    mPreview.start(mCameraSource, mGraphicOverlay);
                } catch (IOException e) {
                    Log.e(TAG, "Unable to start camera source.", e);
                    mCameraSource.release();
                    mCameraSource = null;
                }

            }

        }
    }




    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face>{

        @Override
        public Tracker<Face> create(Face face) {
            return new CameraActivity.GraphicFaceTracker(mGraphicOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face>{
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay){
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);

        }

        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {
            wonderfulCV.faceFound = true;

            Boolean faceSizeThresholdPassed = false;

            if (face.getWidth() > FACE_SIZE_THRESHOLD) faceSizeThresholdPassed = true;

            Bitmap bmp32 = boxDetector.currentBitmap.copy(Bitmap.Config.ARGB_8888, true);

            if (gotResponse && faceSizeThresholdPassed){

                FaceDetectionAsyncTask faceDetectionTask = new FaceDetectionAsyncTask(new FaceDetectionAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(ArrayList<Identity> facesList) {
                      if (facesList.size() > 0){
                          wonderfulCV.currentFace = facesList.get(0);
                      }
                      gotResponse = true;
                    }
                });

                if (wonderfulCV.checkIfServerConnectionInitialized()){
                    faceDetectionTask.setConnectionInfo(wonderfulCV.token, wonderfulCV.serverAddress +
                                                    "/api/user/detection", bmp32);
                    faceDetectionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    gotResponse = false;
                }
            }

            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);

            int x = (int) face.getPosition().x;
            int y = (int) face.getPosition().y;
            int width = (int) face.getWidth();
            int height = (int) face.getHeight();

            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;


            if (x + width <= boxDetector.currentBitmap.getWidth() && y + height <= boxDetector.currentBitmap.getHeight()) {
                Bitmap tempBitmap = Bitmap.createBitmap(boxDetector.currentBitmap, x, y, width, height);

                currentFaceBitmap = tempBitmap;
            }
            faceX = Integer.toString(x);
            faceY = Integer.toString(y);
            faceLeftEyeOpenProb = Integer.toString((int) (face.getIsLeftEyeOpenProbability() * 100)) + "%";
            faceRightEyeOpenProb = Integer.toString((int) (face.getIsRightEyeOpenProbability() * 100)) + "%";
            faceSmilingProb = Integer.toString((int) (face.getIsSmilingProbability() * 100)) + "%";



        }//onUpdate

        @Override
        public void onMissing(Detector.Detections<Face> detections) {
            mOverlay.remove(mFaceGraphic);
        }

        @Override
        public void onDone() {
            wonderfulCV.faceFound = false;
            mOverlay.remove(mFaceGraphic);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();



    }//onRequest


    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
        Log.d(TAG, "CV_Info :  On Resume");
    }

    public void capture(View v){


        mCameraSource.takePicture(null, callbackPicture);

    }

    CameraSource.PictureCallback callbackPicture = new CameraSource.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes) {
            Bitmap bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

            Intent intent = new Intent(CameraActivity.this, DetailViewActivity.class);
            intent.putExtra("image",bm);
            startActivity(intent);



//            File path = Environment.getExternalStorageDirectory();
//            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            File file = new File(path,"IMG_"+time+".jpg");
//
//            try {
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(bytes);
//                fos.flush();
//                fos.close();
//
//                Toast.makeText(CameraActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
//
//                Intent scan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                scan.setData(Uri.parse("files://"+file.getPath()));
//                sendBroadcast(scan);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }

    };


    private void requestCameraPermission(){
        Log.w(TAG, "Camera permission is not granted. Requesting permission");
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };
    }


}//class
