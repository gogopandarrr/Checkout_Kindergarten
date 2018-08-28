package com.onethefull.attendmobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.onethefull.attendmobile.api.TinyDB;
import com.onethefull.attendmobile.cv.DetectionBasedTracker;
import com.onethefull.attendmobile.cv.DetectionView;
import com.onethefull.wonderful_cv_library.CV_Package.RequestUserImagesAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.CreateNewUserAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.Crypto;
import com.onethefull.wonderful_cv_library.CV_Package.Identity;
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FRActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = FRActivity.class.getSimpleName();


    @Nullable
    @BindView(R.id.activity_surface_view)
    DetectionView mDetectionView;
    @BindView(R.id.btn_take_picture)
    Button mBtnPicture;

    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    public static final int JAVA_DETECTOR = 0;
    public static final int NATIVE_DETECTOR = 1;
    private Mat mRgba;
    private Mat mGray;
    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker mNativeDetector;
    private int mDetectorType = JAVA_DETECTOR;
    private float mRelativeFaceSize = 0.2f;
    private int mAbsoluteFaceSize = 0;
    private WonderfulCV wonderfulCV;
    private Bitmap bmp;
    private String name, tel, email;
    private ImageView[] iv = new ImageView[5];
    private TinyDB tinyDB;
    private ArrayList<Object> stp;
    String urlString;
    View viewOverlay;
    int mode;
    String addCVid;
    int currentCycle = 0;
    Boolean faceDetectionFinished = false;
    private ArrayList<Bitmap> facePics = new ArrayList<Bitmap>();

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    System.loadLibrary("detectionBasedTracker");
                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(com.onethefull.attendmobile.R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);
                        cascadeDir.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }
                    mDetectionView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fr);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);

        modeCheck();
        getDatas();




        mDetectionView.setCvCameraViewListener(this);
//        mDetectionView.setAlpha(0);
        mDetectionView.setCameraIndex(0);
        mDetectionView.enableView();
        int maxCameraViewWidth = 640;
        int maxCameraViewHeight = 480;
        mDetectionView.setMaxFrameSize(maxCameraViewWidth, maxCameraViewHeight);
        setDetectorType(JAVA_DETECTOR);
        setMinFaceSize(0.3f);


        //프리뷰 작업
        for(int i=0;i<5;i++){

            iv[i] = findViewById(R.id.img_pic_01+i);

        }



        //카메라 오버레이 작업
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        viewOverlay =inflater.inflate(R.layout.overlay_camera, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addContentView(viewOverlay, layoutParams);



    }

    @OnClick({R.id.btn_take_picture})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_picture:
                cvCreateNewUser();
                break;

            default:
                break;
        }
    }

    @OnClick({R.id.btn_reShot})
    public void reShot(View view){
        switch (view.getId()) {
            case R.id.btn_reShot:
                finish();
                startActivity(getIntent());
                break;

            default:
                break;
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mDetectionView != null)
            mDetectionView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV8719 Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        Log.d(TAG, "CV_Info :  On Resume");
        faceDetectionFinished = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "CV_Info :  On Destroy");
        if (mDetectionView != null)
            mDetectionView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        long timestamp = 0;
        if (currentCycle % 1 == 0) {
            mRgba = inputFrame.rgba();
            mGray = inputFrame.gray();
            if (!faceDetectionFinished) {
                if (mAbsoluteFaceSize == 0) {
                    int height = mGray.rows();
                    if (Math.round(height * mRelativeFaceSize) > 0) {
                        mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
                    }
                    mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
                }

                MatOfRect faces = new MatOfRect();

                if (mDetectorType == JAVA_DETECTOR) {
                    if (mJavaDetector != null)
                        mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2,
                                2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
                } else if (mDetectorType == NATIVE_DETECTOR) {
                    if (mNativeDetector != null)
                        mNativeDetector.detect(mGray, faces);
                } else {
                    Log.e(TAG, "Detection method is not selected!");
                }

                Rect[] facesArray = faces.toArray();




                ////////////////////////////////////


                if (facesArray.length == 1 && (System.currentTimeMillis() - timestamp) > 1000) {
                    try {
                        timestamp = System.currentTimeMillis();
                        if (mRgba.cols() > 0) {
                            bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(mRgba, bmp);
                            if (facePics.size() < 5) {
                                Log.d(TAG, "Bitmap Size: " + bmp.getByteCount());

                                facePics.add(RotateBitmap(bmp,0));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iv[facePics.size()-1].setImageBitmap(bmp);
                                    }
                                });

                                Log.d(TAG, "facePics Size: " + facePics.size());
                            } else {
                                Log.d(TAG, "Reached face pics threshold");
                                faceDetectionFinished = true;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        viewOverlay.setVisibility(View.INVISIBLE);
                                        mBtnPicture.setVisibility(View.VISIBLE);
                                    }
                                });



                            }
                        }
                    } catch (CvException e) {
                        Log.d("Exception", e.getMessage());
                    }
                }


            }


        }
        currentCycle++;
        return mRgba;
    }

    private Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



    // TODO: 2018. 8. 23.  
    private void cvCreateNewUser() {
        wonderfulCV = new WonderfulCV();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBtnPicture.setVisibility(View.GONE);
            }
        });


        Toast.makeText(this, "등록 중 입니다. 잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
        Crypto.deleteExistingTokenFromStorage();
        wonderfulCV.initiateServerConnection(getApplicationContext(), "1thefull.ml", 5000,
                "panda@1thefull.com", "zkfmak85");



        Log.d(TAG, "Registering new user with face");
        CreateNewUserAsyncTask createNewUserTask = new CreateNewUserAsyncTask(
                new CreateNewUserAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(int cvId) {
                        addCVid = Integer.toString(cvId);

                        Toast.makeText(getApplicationContext(),
                                cvId+"", Toast.LENGTH_SHORT).show();

                        if (addCVid != null){
                            finishCamera();
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "다시 찍으세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (wonderfulCV.checkIfServerConnectionInitialized()) {



            createNewUserTask.setUserInfo(wonderfulCV.serverAddress + "/api/user",
                    "원","김", "1111111", "temp@aaa.com", wonderfulCV.token, facePics);
            createNewUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            Log.d(TAG,"cv유저 등록 완료");


        }




    }
    
    
    ////////////////


    private void modeCheck(){

        mode = getIntent().getIntExtra("mode", -1);

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                int permission = checkSelfPermission(Manifest.permission.CAMERA);
                if (permission == PackageManager.PERMISSION_DENIED){
                    String[] arr = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(arr, 10);
                }

                 }
    }


    private void finishCamera(){

        Intent intent = new Intent(FRActivity.this, DetailViewActivity.class);

//        bitmap을 바이트로 바꿔서 전송.
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        facePics.get(3).compress(Bitmap.CompressFormat.JPEG, 100, bs);
        try {
            bs.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
            getImagesFromServer();

            intent.putExtra("image",bs.toByteArray());
//////////////

            intent.putExtra("cvid", addCVid);
            Log.d(TAG, addCVid+"<------------");
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


    }//

    public void getImagesFromServer() {

        RequestUserImagesAsyncTask requestImagesTask = new RequestUserImagesAsyncTask(
                new RequestUserImagesAsyncTask.AsyncResponse() {

                    @Override
                    public void processFinish(final ArrayList<Identity> userList) {
                        if (userList.size() > 0) {

                            stp = new ArrayList<>();
                            for (Identity a : userList){
                                stp.add(a);
                            }

                            tinyDB.putListObject("userList", stp);

//                            Identity user = userList.get(0);
//                            urlString  =  "http://1thefull.ml:5000/faceimages/"+user.imageName;


                            }


//                            Identity user1 = userList.get(0);
//                            Log.d("CV_Info", "User info");
//                            Log.d("CV_Info", "First name:" + user1.firstName);
//                            Log.d("CV_Info", "Last  name:" + user1.lastName);
//                            Log.d("CV_Info", "cv_id:" + user1.id);
//                            Log.d("CV_Info", "company name:" + user1.companyName);
//                            Log.d("CV_Info", "full image url: http://1thefull.ml:5000/faceimages/" + user1.imageName);
//
//                            textOutput.setText("Retrieved " + userList.size() + "users \n" +
//                                    "Sample Output: User 1 \n" +
//                                    "First Name: " + user1.firstName + "\n" +
//                                    "Last Name: " + user1.lastName + "\n" +
//                                    "User ID (CV_ID):" + user1.id + "\n" +
//                                    "Company Name: " + user1.companyName + "\n" +
//                                    "Image URL:  http://1thefull.ml:5000/faceimages/" + user1.imageName
//                            );



                    }
                });

        if (wonderfulCV.checkIfServerConnectionInitialized()) {
            requestImagesTask.setRequestParameters(wonderfulCV.serverAddress +
                    "/api/users/", wonderfulCV.token, 9999);
            requestImagesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }


    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    private  void getDatas(){

           Intent intent = getIntent();

           name = intent.getStringExtra("name");
           tel = intent.getStringExtra("tel");
           email = intent.getStringExtra("email");

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
    }
}//class
