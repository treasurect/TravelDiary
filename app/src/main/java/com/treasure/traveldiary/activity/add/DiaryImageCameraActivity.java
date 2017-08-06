package com.treasure.traveldiary.activity.add;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.LogUtil;

import java.util.List;

public class DiaryImageCameraActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PictureCallback, View.OnClickListener {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private boolean mIsSurfaceCreated = false;
    private boolean mIsTimerRunning = false;
    private  int CAMERA_ID = 0; //后置摄像头
    private ImageView scan_btn;
    private boolean isPageDestroy;
    private ImageView btnTrue;
    private ImageView btnFalse;
    private byte[] camera_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_image_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initFindId();
        initClick();
        try {
            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(this);
        } catch (Exception e) {
            LogUtil.e("~~~~~~~~~~~~~~~~~~~~", e.getMessage());
        }
    }

    private void initFindId() {
        mSurfaceView = (SurfaceView) findViewById(R.id.diary_image_camera_surfaceView);
        scan_btn = (ImageView) findViewById(R.id.diary_image_camera_scan);
        btnTrue = (ImageView) findViewById(R.id.diary_image_true);
        btnFalse = (ImageView) findViewById(R.id.diary_image_false);
    }

    private void initClick() {
        scan_btn.setOnClickListener(this);
        btnTrue.setOnClickListener(this);
        btnFalse.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diary_image_camera_scan:
                mCamera.takePicture(null, null, DiaryImageCameraActivity.this);
                scan_btn.setClickable(false);
                break;
            case R.id.diary_image_true:
                Intent intent = new Intent();
                intent.putExtra("camera_data",camera_data);
                setResult(RESULT_OK, intent);
                btnFalse.setVisibility(View.GONE);
                btnTrue.setVisibility(View.GONE);
                scan_btn.setClickable(true);
                DiaryImageCameraActivity.this.finish();
                break;
            case R.id.diary_image_false:
                btnFalse.setVisibility(View.GONE);
                btnTrue.setVisibility(View.GONE);
                scan_btn.setClickable(true);
                stopPreview();
                startPreview(CAMERA_ID);
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceCreated = true;
        mHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview(CAMERA_ID);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsSurfaceCreated = false;
        mSurfaceView = null;
        mHolder = null;
    }

    //开启预览
    private void startPreview(int CAMERA_ID) {
        if (mCamera != null || !mIsSurfaceCreated) {
            return;
        }
        try {
            mCamera = Camera.open(CAMERA_ID);
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
            int width = previewSizes.get(0).width;
            int height = previewSizes.get(0).height;
            if (pictureSizes.get(pictureSizes.size() - 1).width >= width) {
                parameters.setPictureSize(width, height);
            }

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setPreviewFrameRate(20);

            //设置相机预览方向
            mCamera.setDisplayOrientation(90);

            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            LogUtil.e("~~~~~~~~~~~~~~~~~", e.getMessage());
        }

    }

    private void stopPreview() {
        //释放Camera对象
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                LogUtil.e("~~~~~~~~~~~~~~~~~", e.getMessage());
            }
        }
    }


    //相机结束
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        btnTrue.setVisibility(View.VISIBLE);
        btnFalse.setVisibility(View.VISIBLE);
        camera_data = data;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopPreview();
        mIsTimerRunning = false;
        isPageDestroy = true;
        super.onDestroy();
    }
}