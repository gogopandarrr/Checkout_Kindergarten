package com.onethefull.attendmobile.api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    public android.hardware.Camera camera;


    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder= getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera= android.hardware.Camera.open();

        try {

            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        camera.startPreview();
        camera.release();
        camera=null;


    }
}
