package com.onethefull.attendmobile.cv;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Michael Vogl on 24-Nov-17.
 */


public class DetectionView extends JavaCameraView implements Camera.PictureCallback {

    private static final String TAG = "Sample::Tutorial3View";
    private String mPictureFileName;

    public DetectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public List<String> getFocusList() {
        return mCamera.getParameters().getSupportedFocusModes();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);

    }

    public void setFocus(String focusMode) {
        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(focusMode);
        mCamera.setParameters(params);
    }



    public void setPreviewFPS(double min, double max){
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewFpsRange((int)(min*1000), (int)(max*1000));
        mCamera.setParameters(params);
    }

    public List<Camera.Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Camera.Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }
    private Camera.Parameters params;

    public void setExposure(int exposure) {
        params =  mCamera.getParameters();
        float minEx = params.getMinExposureCompensation();
        float maxEx = params.getMaxExposureCompensation();

        exposure = Math.round((maxEx - minEx) / 100 * exposure + minEx);

        params.setExposureCompensation(exposure);
        Log.d("JavaCameraViewSettings", "Exposure Compensation " + String.valueOf(exposure));
        mCamera.setParameters(params);
    }

    public Camera.Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }

    public void takePicture(final String fileName) {
        Log.i(TAG, "Taking picture");
        this.mPictureFileName = fileName;
        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(null);

        // PictureCallback is implemented by the current class
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG, "Saving a bitmap to file");
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        // Write the image in a file (in jpeg format)
        try {
            FileOutputStream fos = new FileOutputStream(mPictureFileName);

            fos.write(data);
            fos.close();

        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

    }
}