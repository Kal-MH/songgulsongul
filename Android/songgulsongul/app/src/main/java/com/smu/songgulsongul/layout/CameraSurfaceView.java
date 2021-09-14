package com.smu.songgulsongul.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    static public Camera camera = null;
    public List<Camera.Size> listPreviewSizes;


    public CameraSurfaceView(Context context) {
        super(context);
        if (camera == null)
            camera = Camera.open();

        listPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try {
            // 카메라 객체를 사용할 수 있게 연결한다.
            if (camera == null)
                camera = Camera.open();


            // 카메라 설정
            Camera.Parameters parameters = camera.getParameters();

            // 카메라의 회전이 가로/세로일때 화면을 설정한다.
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                parameters.set("orientation", "landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            camera.setParameters(parameters);

            camera.setPreviewDisplay(surfaceHolder);

            // 카메라 미리보기를 시작한다.
            camera.startPreview();

            // 자동포커스 설정
            camera.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {

                    }
                }
            });
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public boolean capture(Camera.PictureCallback callback) {
        if (camera != null) {
            camera.takePicture(null, null, callback);
            return true;
        } else {
            return false;
        }
    }

}