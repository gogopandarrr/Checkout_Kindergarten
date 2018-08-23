package com.onethefull.attendmobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.onethefull.attendmobile.account.setchildren.DetailViewActivity;
import com.onethefull.attendmobile.cv.DetectionBasedTracker;
import com.onethefull.attendmobile.cv.DetectionView;
import com.onethefull.wonderful_cv_library.CV_Package.CreateNewUserAsyncTask;
import com.onethefull.wonderful_cv_library.CV_Package.Crypto;
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

    int mode;
    int addCVid;
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

        modeCheck();


        mDetectionView.setCvCameraViewListener(this);
//        mDetectionView.setAlpha(0);

        mDetectionView.setCameraIndex(1);

        mDetectionView.enableView();
        int maxCameraViewWidth = 640;
        int maxCameraViewHeight = 480;
        mDetectionView.setMaxFrameSize(maxCameraViewWidth, maxCameraViewHeight);
        setDetectorType(JAVA_DETECTOR);
        setMinFaceSize(0.3f);
    }

    @OnClick({R.id.btn_take_picture})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_picture:
                cvCreateNewUser();
                finishCamera();
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


                if (facesArray.length == 1 && (System.currentTimeMillis() - timestamp) > 1000) {
                    try {
                        timestamp = System.currentTimeMillis();
                        if (mRgba.cols() > 0) {
                            bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(mRgba, bmp);
                            if (facePics.size() < 5) {
                                Log.d(TAG, "Bitmap Size: " + bmp.getByteCount());
                                facePics.add(bmp);
                                Log.d(TAG, "facePics Size: " + facePics.size());
                            } else {
                                Log.d(TAG, "Reached face pics threshold");
                                faceDetectionFinished = true;
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





    // TODO: 2018. 8. 23.  
    private void cvCreateNewUser() {
        wonderfulCV = new WonderfulCV();

        Crypto.deleteExistingTokenFromStorage();
//        wonderfulCV.initiateServerConnection(getApplicationContext(), "1thefull.ml", 5000,
//                "onni.caffe@1thefull.com", "1thefull322");
//        wonderfulCV.initiateServerConnection(getApplicationContext(), "1thefull.ml", 5000,
//                "jwseo2698@1thefull.com", "password");
        wonderfulCV.initiateServerConnection(getApplicationContext(), "1thefull.ml", 5000,
                "panda@1thefull.com", "zkfmak85");


        Log.d(TAG, "Registering new user with face");
        CreateNewUserAsyncTask createNewUserTask = new CreateNewUserAsyncTask(
                new CreateNewUserAsyncTask.AsyncResponse() {
                    @Override
                    public void processFinish(int cvId) {
                        Log.d(TAG,"cv등록 완료");
                        Toast.makeText(getApplicationContext(),
                                "cv등록 완료", Toast.LENGTH_SHORT).show();
                    }
                });

        if (wonderfulCV.checkIfServerConnectionInitialized()) {
            Log.e("aaaaa",facePics.size()+"");
            createNewUserTask.setUserInfo(wonderfulCV.serverAddress + "/api/user",
                    "jiwoo","seo", "01097892698", "jwseo2698@1thefull.com",
                    wonderfulCV.token, facePics);
            createNewUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }



    }
    
    
    ////////////////


    private void modeCheck(){

        mode = getIntent().getIntExtra("mode", -1);
    }


    private void finishCamera(){

        Intent intent = new Intent(FRActivity.this, DetailViewActivity.class);


//        bitmap을 바이트로 바꿔서 전송.
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        Log.e("size", facePics.size()+"");
        facePics.get(0).compress(Bitmap.CompressFormat.JPEG, 100, bs);
        try {
            bs.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        intent.putExtra("image",bs.toByteArray());

        if(mode == 4){
            mode = 4;//편집모드에서 사진 다시 찍을 때 모드
            intent.putExtra("name", getIntent().getStringExtra("name"));
            intent.putExtra("tel", getIntent().getStringExtra("tel"));
            intent.putExtra("email", getIntent().getStringExtra("email"));
            intent.putExtra("mode", mode);

        }else{
            mode = 1; //처음 원생 등록 모드
            intent.putExtra("mode", mode);
        }

        startActivity(intent);


        finish();

    }//


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

}
