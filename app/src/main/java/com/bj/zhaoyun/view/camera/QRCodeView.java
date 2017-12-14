package com.bj.zhaoyun.view.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.bj.zhaoyun.R;

import java.io.IOException;
import java.util.List;

/**
 * Description：QRCodeView
 * 相机判断是否存在
 * 判断摄像头数量,选择前置摄像头
 * 判断闪光灯是否存在.
 * 开启/关闭闪光灯(根据光传感器数据显示手电筒图标)
 * 根据手势控制相机的放大缩小
 * Created by Buuu on 2017/12/7.
 */

public class QRCodeView extends FrameLayout implements View.OnClickListener {
    private SurfaceView sv_display;
    private Camera camera;
    private Camera.Parameters parameters;
    private QRCodeCenterRectangleView center_react;
    private MyPreviewCallback myPreviewCallback;
    private int mMinPreviewCameraSize = 480 * 320;


    public QRCodeView(@NonNull Context context) {
        this(context, null);
    }

    public QRCodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QRCodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.qr_code_view, this);
        initData();
    }

    private void initData() {
        sv_display = findViewById(R.id.sv_display);
        center_react = findViewById(R.id.center_react);
        myPreviewCallback = new MyPreviewCallback();
    }

    /**
     * 启动相机
     *
     * @param mListener 用于显示相机启动失败或者成功的回调
     */
    public void start(OnCameraStatusListener mListener) {
        //获取摄像头数量，调用前置摄像头
        int cameraNumber = Camera.getNumberOfCameras();
        if (cameraNumber > 0) {
            try {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                for (int i = 0; i < cameraNumber; i++) {
                    Camera.getCameraInfo(i, cameraInfo);
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//调用后置摄像头
                        camera = Camera.open(i);
                        break;
                    }
                }
                parameters = camera.getParameters();
//                parameters.setPreviewFpsRange(4, 10);
                List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
                int width = 0;
                int height = 0;
                if (mSupportedPreviewSizes.size() > 1) {
                    if (mSupportedPreviewSizes.get(0).width > mSupportedPreviewSizes.get(mSupportedPreviewSizes.size() - 1).width) {
                        //规格由大到小
                        for (int x = mSupportedPreviewSizes.size() - 1; x >= 0; x--) {
                            if (mSupportedPreviewSizes.get(x).width * mSupportedPreviewSizes.get(x).height >= mMinPreviewCameraSize) {
                                width = mSupportedPreviewSizes.get(x).width;
                                height = mSupportedPreviewSizes.get(x).height;
                                break;
                            }
                        }
                    } else {//规格由小到大
                        for (int x = 0; x < mSupportedPreviewSizes.size(); x--) {
                            if (mSupportedPreviewSizes.get(x).width * mSupportedPreviewSizes.get(x).height >= mMinPreviewCameraSize) {
                                width = mSupportedPreviewSizes.get(x).width;
                                height = mSupportedPreviewSizes.get(x).height;
                                break;
                            }
                        }
                    }
                } else {
                    if (mSupportedPreviewSizes.size() >= 1) {
                        width = mSupportedPreviewSizes.get(0).width;
                        height = mSupportedPreviewSizes.get(0).height;
                    }
                }
                parameters.setPreviewSize(width, height);//设置预览的数据
                camera.setParameters(parameters);
                myPreviewCallback.setPreviewWidthHeight(width, height);
                camera.setPreviewCallback(myPreviewCallback);
            } catch (RuntimeException e) {
                e.printStackTrace(); //相机是否被占用
                if (mListener != null) {
                    mListener.onFail();
                }
            }
        }
        if (parameters != null) {
            camera.setDisplayOrientation(90);
        }
        sv_display.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //todo 设置方向
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    //自动聚焦
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    public void release() {
        if (camera != null) {
            camera.stopPreview();
        }
        if (camera != null) {
            camera.release();
        }
    }


    //处理多只触控效果
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.center_react:
                camera.setAutoFocusMoveCallback(new Camera.AutoFocusMoveCallback() {
                    @Override
                    public void onAutoFocusMoving(boolean start, Camera camera) {

                    }
                });
                break;
            default:
                break;
        }

    }


    public interface OnCameraStatusListener {
        void onSuccess();

        void onFail();
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

}
