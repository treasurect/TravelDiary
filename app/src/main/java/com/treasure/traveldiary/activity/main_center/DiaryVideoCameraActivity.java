package com.treasure.traveldiary.activity.main_center;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;

import java.io.File;

public class DiaryVideoCameraActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private boolean mIsSurfaceCreated = false;
    private int CAMERA_ID = 0; //后置摄像头
    private ImageView scan_btn, scan2_btn;
    private MediaRecorder mMediaRecorder;
    private CamcorderProfile mProfile;
    private ImageView btnSave;
    private ImageView btnCancel;
    private TextView textCountTimer;
    private Thread thread;
    private boolean isPageDestory;
    private int count;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    textCountTimer.setText(count + "");
                    if (count == 0) {
                        stopVideoRecoding();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_video_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initFindId();
        initClick();
        initDiaLog();
        try {
            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        } catch (Exception e) {
            Toast.makeText(this, "开启相机失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFindId() {
        mSurfaceView = (SurfaceView) findViewById(R.id.diary_video_camera_surfaceView);
        scan_btn = (ImageView) findViewById(R.id.diary_video_camera_scan);
        scan2_btn = (ImageView) findViewById(R.id.diary_video_camera_scan2);
        btnSave = (ImageView) findViewById(R.id.diary_video_camera_save);
        btnCancel = (ImageView) findViewById(R.id.diary_video_camera_cancel);
        textCountTimer = (TextView) findViewById(R.id.diary_video_camera_countTimer);
        textCountTimer.setRotation(90);
    }

    private void initClick() {
        scan_btn.setOnClickListener(this);
        scan2_btn.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initDiaLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请横屏录制");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diary_video_camera_scan:
                startVideoRecoding();
                break;
            case R.id.diary_video_camera_scan2:
                stopVideoRecoding();
                break;
            case R.id.diary_video_camera_save:
                scan_btn.setClickable(true);
                scan2_btn.setClickable(true);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                DiaryVideoCameraActivity.this.finish();
                break;
            case R.id.diary_video_camera_cancel:
                scan_btn.setClickable(true);
                scan2_btn.setClickable(true);
                btnSave.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                break;
        }
    }

    private void startVideoRecoding() {
        mCamera.unlock();
        try {
            mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF);
        }catch (Exception e){
            Toast.makeText(this, "您的设备支持的像素过低，特为您选择低画质录制", Toast.LENGTH_SHORT).show();
        mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA);
        }
        File file = new File(getExternalFilesDir(null).getAbsolutePath() + "/diary_video.mp4");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            //1st. Initial state
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mCamera);
            //2st. Initialized state
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //3st. config
            mMediaRecorder.setOutputFormat(mProfile.fileFormat);
            mMediaRecorder.setAudioEncoder(mProfile.audioCodec);
            mMediaRecorder.setVideoEncoder(mProfile.videoCodec);
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            mMediaRecorder.setVideoSize(mProfile.videoFrameWidth, mProfile.videoFrameHeight);
            mMediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
            mMediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
            mMediaRecorder.setAudioEncodingBitRate(mProfile.audioBitRate);
            mMediaRecorder.setAudioChannels(mProfile.audioChannels);
            mMediaRecorder.setAudioSamplingRate(mProfile.audioSampleRate);
            mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            scan_btn.setVisibility(View.GONE);
            openTimer();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

        private void stopVideoRecoding() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scan_btn.setVisibility(View.VISIBLE);
        scan_btn.setClickable(false);
        scan2_btn.setClickable(false);
        textCountTimer.setText("");
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
    }

    private void openTimer() {
        count = 10;
        textCountTimer.setText(count + "");
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(1000);
                            if (!isPageDestory) {
                                count--;
                                handler.sendMessage(handler.obtainMessage(200));
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        thread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceCreated = true;
        mHolder = holder;
        startPreview(CAMERA_ID);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mHolder = holder;

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
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Toast.makeText(this, "开启相机失败", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "关闭相机失败" ,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopPreview();
        isPageDestory = true;
        super.onDestroy();
    }
}