package com.treasure.traveldiary.activity.diary_center;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.io.IOException;

public class DiaryVideoFullActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SurfaceView surfaceView;
    private SimpleDraweeView full_image;
    private ImageView full_play;
    private ProgressBar full_loading;
    private String video_url;
    private int video_duration, video_position;
    private String image_url = "";
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private boolean isPageDestroy;
    private boolean isVideoPlay;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    if (mediaPlayer != null){
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                    break;
                case 300:
                    full_play.setVisibility(View.VISIBLE);
                    full_loading.setVisibility(View.GONE);
                    if (isVideoPlay){
                        mediaPlayer.seekTo(video_position);
                        full_play.setVisibility(View.GONE);
                        full_image.setVisibility(View.GONE);
                        mediaPlayer.start();
                        if (thread != null){
                            thread.start();
                        }
                    }
                    break;
                case 301:
                    full_play.setVisibility(View.VISIBLE);
                    full_image.setVisibility(View.VISIBLE);
                    seekBar.setProgress(0);
                    break;
            }
        }
    };
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_video_full);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initFindId();
        initIntent();
        initClick();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initMediaPlayer(video_url);
            }
        }).start();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                        if (!isPageDestroy && mediaPlayer != null && mediaPlayer.isPlaying()){
                            try {
                                Thread.sleep(16);
                                if (!isPageDestroy && mediaPlayer != null&&mediaPlayer.isPlaying()){
                                    handler.sendMessage(handler.obtainMessage(200));
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            LogUtil.d("~~~~~~~~~~~~~~~~~~~~break");
                            break;
                        }
                    }
            }
        });
    }

    private void initFindId() {
        surfaceView = (SurfaceView) findViewById(R.id.video_full_surfaceView);
        full_image = (SimpleDraweeView) findViewById(R.id.video_full_image);
        full_play = (ImageView) findViewById(R.id.video_full_play);
        full_loading = (ProgressBar) findViewById(R.id.video_full_loading);
        seekBar = (SeekBar) findViewById(R.id.video_full_seekBar);
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("video_url"))) {
            video_url = intent.getStringExtra("video_url");
            video_duration = Integer.parseInt(intent.getStringExtra("video_duration"));
            video_position = Integer.parseInt(intent.getStringExtra("video_position"));
            image_url = intent.getStringExtra("image_url");
            full_image.setImageURI(Uri.parse(image_url));
            seekBar.setMax(video_duration);
            seekBar.setProgress(video_position);
            if (video_position > 0){
                isVideoPlay = true;
            }
        }
    }

    private void initClick() {
        full_play.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_full_play:
                full_play.setVisibility(View.GONE);
                full_image.setVisibility(View.GONE);
                mediaPlayer.start();
                if (thread != null){
                    thread.start();
                }
                break;
        }
    }

    private void initMediaPlayer(String path) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(path));
            holder = surfaceView.getHolder();
            holder.addCallback(this);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);

            //            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            //            mmr.setDataSource(path);
            //            Bitmap bitmap = mmr.getFrameAtTime();//获取第一帧图片
            //            video_first.setImageBitmap(bitmap);
            //            mmr.release();//释放资源

        } catch (IOException e) {
            if (!isPageDestroy){
                Toast.makeText(this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        handler.sendMessage(handler.obtainMessage(300));
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        handler.sendMessage(handler.obtainMessage(301));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        seekBar.get
//        if (mediaPlayer != null && mediaPlayer.isPlaying()){
//            mediaPlayer.seekTo();
//        }
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            Intent intent = new Intent();
            intent.putExtra("video_position",String.valueOf(mediaPlayer.getCurrentPosition()));
            setResult(RESULT_OK,intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }
}
