package com.treasure.traveldiary.activity.add;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.LeaveMesBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class DiaryVideoPublishActivity extends BaseActivity implements View.OnClickListener, TextWatcher, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private String user_addr;
    private float user_lat, user_long;
    private EditText diaryDesc, diaryTitle;
    private TextView diaryLoc, surfacePlay;
    private SurfaceView surfaceView;
    private FrameLayout loading;
    private ImageView video_play;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private ImageView first_image;
    private SharedPreferences mPreferences;
    private String video_first = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_video_publish);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("记录生活");
        btn_send.setVisibility(View.VISIBLE);
        btn_send.setClickable(false);
        btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        receiverIntent();
        initMediaPlayer();
        initClick();
    }


    private void initFindId() {
        diaryDesc = (EditText) findViewById(R.id.diary_video_desc);
        diaryTitle = (EditText) findViewById(R.id.diary_video_title);
        diaryLoc = (TextView) findViewById(R.id.diary_video_loc);
        surfaceView = (SurfaceView) findViewById(R.id.diary_video_view);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        video_play = (ImageView) findViewById(R.id.diary_video_play);
        first_image = (ImageView) findViewById(R.id.diary_video_first_view);
    }

    private void receiverIntent() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("user_addr"))) {
            user_addr = intent.getStringExtra("user_addr");
            diaryLoc.setText(user_addr);
        }
        if (!Tools.isNull(intent.getStringExtra("user_lat"))) {
            user_lat = Float.parseFloat(intent.getStringExtra("user_lat"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_long"))) {
            user_long = Float.parseFloat(intent.getStringExtra("user_long"));
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            String path = getExternalFilesDir(null).getAbsolutePath() + "/diary_video.mp4";
            mediaPlayer.setDataSource(this, Uri.parse(path));
            holder = surfaceView.getHolder();
            holder.addCallback(this);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            final Bitmap bitmap = mmr.getFrameAtTime();//获取第一帧图片
            first_image.setImageBitmap(bitmap);
            mmr.release();//释放资源
            new Thread(new Runnable() {
                @Override
                public void run() {
                    video_first = Tools.saveBitmapToSD(DiaryVideoPublishActivity.this, bitmap, "diary_video_first");
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        diaryDesc.addTextChangedListener(this);
        diaryTitle.addTextChangedListener(this);
        loading.setOnClickListener(this);
        video_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                DiaryVideoPublishActivity.this.finish();
                break;
            case R.id.btn_send:
                upLoadVideo();
                break;
            case R.id.diary_video_play:
                video_play.setVisibility(View.GONE);
                first_image.setVisibility(View.GONE);
                mediaPlayer.start();
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!Tools.isNull(diaryDesc.getText().toString().trim()) && !Tools.isNull(diaryTitle.getText().toString().trim())) {
            btn_send.setClickable(true);
            btn_send.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            btn_send.setClickable(false);
            btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void upLoadVideo() {
        loading.setVisibility(View.VISIBLE);
        final String[] path = new String[2];
        path[0] = video_first;
        path[1] = getExternalFilesDir(null).getAbsolutePath()+"/diary_video.mp4";
        BmobFile.uploadBatch(path, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == path.length){
                    LogUtil.d("~~~~~~~~~~~~~~~~~~~~`"+list1.get(0).contains("mp4")+"........"+list1.get(1).contains("mp4"));
                    if (list1.get(0).contains("mp4")){
                        sendDiary(list1.get(0),list1.get(1));
                    }else {
                        sendDiary(list1.get(1),list1.get(0));
                    }
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private void sendDiary(String fileUrl,String imageUrl) {
        String textDesc = diaryDesc.getText().toString().trim();
        String textTitle = diaryTitle.getText().toString().trim();
        DiaryBean diaryBean = new DiaryBean();
        diaryBean.setUser_name(mPreferences.getString("user_name", ""));
        diaryBean.setUser_nick(mPreferences.getString("user_nick", ""));
        diaryBean.setUser_addr(user_addr);
        diaryBean.setUser_lat(user_lat);
        diaryBean.setUser_long(user_long);
        diaryBean.setDiary_type(2);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Long(System.currentTimeMillis()));
        diaryBean.setPublish_time(time.substring(5, 7) + "月" + time.substring(8, 10) + "日" + time.substring(11, 16));
        diaryBean.setUser_desc(textDesc);
        diaryBean.setUser_title(textTitle);
        diaryBean.setDiary_video(fileUrl);
        diaryBean.setDiary_video_first(imageUrl);
        //生成一个空的数据占位，以方便更新
        LeaveMesBean leaveMesBean = new LeaveMesBean();
        leaveMesBean.setLeave_name(mPreferences.getString("user_name", ""));
        List<LeaveMesBean> leaveMesBeen = new ArrayList<>();
        leaveMesBeen.add(leaveMesBean);
        diaryBean.setMesBeanList(leaveMesBeen);
        diaryBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(DiaryVideoPublishActivity.this, "恭喜你，短视频发布成功", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    DiaryVideoPublishActivity.this.finish();
                } else {
                    Toast.makeText(DiaryVideoPublishActivity.this, "很遗憾，短视频发布失败\n原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
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
        video_play.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        video_play.setVisibility(View.VISIBLE);
    }
}
