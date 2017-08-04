package com.treasure.traveldiary.activity.add;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.LogUtil;

import java.io.ByteArrayOutputStream;

public class DiaryCameraActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PictureCallback, View.OnClickListener {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private boolean mIsSurfaceCreated = false;
    private boolean mIsTimerRunning = false;
    private static final int CAMERA_ID = 0; //后置摄像头
    private ImageView scan_btn;
    private FrameLayout loading;
    private boolean isPageDestroy;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
//                    byte[] bitmapByte = baos.toByteArray();

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_camera);

        initFindId();
        checkCameraPermission();//检查相机权限
        scan_btn.setOnClickListener(this);
    }

    private void initFindId() {
        mSurfaceView = (SurfaceView) findViewById(R.id.diary_camera_surfaceView);
        scan_btn = (ImageView) findViewById(R.id.diary_camera_scan);
        loading = (FrameLayout) findViewById(R.id.diary_camera_loading);
    }


    //请求相机权限
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= 21 && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            //有权限后，进行操作
            try {
                mHolder = mSurfaceView.getHolder();
                mHolder.addCallback(this);
            } catch (Exception e) {
                LogUtil.e("~~~~~~~~~~~~~~~~~~~~", e.getMessage());
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsSurfaceCreated = false;
    }

    //开启预览
    private void startPreview() {
        if (mCamera != null || !mIsSurfaceCreated) {
            return;
        }

        try {
            mCamera = Camera.open(CAMERA_ID);
            Camera.Parameters parameters = mCamera.getParameters();
//            int width = getResources().getDisplayMetrics().widthPixels;
//            int height = getResources().getDisplayMetrics().heightPixels;
//            Camera.Size size = getBestPreviewSize(width, height, parameters);
//            if (size != null) {
//                //设置预览分辨率
//                parameters.setPreviewSize(size.width, size.height);
//                //设置保存图片的大小
//                parameters.setPictureSize(size.width, size.height);
//            }

            //自动对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setPreviewFrameRate(20);

            //设置相机预览方向
            mCamera.setDisplayOrientation(90);

            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera.takePicture(null,null,this);
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
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    //动态权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkCameraPermission();
            } else {
                Toast.makeText(this, "请在应用管理中打开相机权限", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diary_camera_scan:
                loading.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            if (!isPageDestroy){
                                handler.sendMessage(handler.obtainMessage(200));
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
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