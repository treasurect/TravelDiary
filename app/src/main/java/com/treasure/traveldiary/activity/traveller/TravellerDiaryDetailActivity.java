package com.treasure.traveldiary.activity.traveller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.TravelApplication;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.io.IOException;

public class TravellerDiaryDetailActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private SimpleDraweeView user_icon;
    private TextView user_name, user_time, user_title, user_desc;
    private CustomScrollListView leaveMes_listView;
    private SimpleDraweeView detail_image1, detail_image2, detail_image3;
    private LinearLayout image_layout;
    private TravelApplication application;
    private SurfaceView surfaceView;
    private ImageView video_play;
    private FrameLayout noText_layout;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private FrameLayout video_layout;
    private ImageView video_first;
    private ProgressBar video_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_diary_detail);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("详情");

        application = (TravelApplication) getApplication();
        initFindId();
        receiveIntent();
        initClick();
    }

    private void initFindId() {
        user_icon = (SimpleDraweeView) findViewById(R.id.diary_detail_user_icon);
        user_name = (TextView) findViewById(R.id.diary_detail_user_name);
        user_time = (TextView) findViewById(R.id.diary_detail_user_time);
        user_title = (TextView) findViewById(R.id.diary_detail_user_title);
        user_desc = (TextView) findViewById(R.id.diary_detail_user_desc);
        leaveMes_listView = (CustomScrollListView) findViewById(R.id.diary_detail_leaveMes_list);
        detail_image1 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image1);
        detail_image2 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image2);
        detail_image3 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image3);
        image_layout = (LinearLayout) findViewById(R.id.diary_detail_user_image_layout);
        surfaceView = (SurfaceView) findViewById(R.id.diary_detail_user_video_view);
        video_play = (ImageView) findViewById(R.id.diary_detail_user_video_play);
        noText_layout = (FrameLayout) findViewById(R.id.diary_detail_user_noText_layout);
        video_layout = (FrameLayout) findViewById(R.id.diary_detail_user_video_layout);
        video_first = (ImageView) findViewById(R.id.diary_detail_user_video_first);
        video_loading = (ProgressBar) findViewById(R.id.diary_detail_user_video_loading);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        final DiaryBean diaryBean = (DiaryBean) intent.getExtras().get("diaryBean");
        if (diaryBean != null) {
            if (!Tools.isNull(diaryBean.getUser_icon())) {
                user_icon.setImageURI(Uri.parse(diaryBean.getUser_icon()));
            }
            if (!Tools.isNull(diaryBean.getUser_nick())) {
                user_name.setText(diaryBean.getUser_nick());
            }
            if (!Tools.isNull(diaryBean.getPublish_time())) {
                user_time.setText(diaryBean.getPublish_time());
            }
            if (!Tools.isNull(diaryBean.getUser_title())) {
                user_title.setText(diaryBean.getUser_title());
            }
            if (!Tools.isNull(diaryBean.getUser_desc())) {
                user_desc.setText(diaryBean.getUser_desc());
            }
            if (!Tools.isNull(intent.getStringExtra("type"))) {
                if (intent.getStringExtra("type").equals("text")) {
                    noText_layout.setVisibility(View.GONE);
                } else if (intent.getStringExtra("type").equals("image")) {
                    video_layout.setVisibility(View.GONE);
                    if (diaryBean.getDiary_image() != null) {
                        if (diaryBean.getDiary_image().size() == 1) {
                            detail_image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0).toString()));
                            detail_image2.setVisibility(View.GONE);
                            detail_image3.setVisibility(View.GONE);
                        } else if (diaryBean.getDiary_image().size() == 2) {
                            detail_image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0).toString()));
                            detail_image2.setImageURI(Uri.parse(diaryBean.getDiary_image().get(1).toString()));
                            detail_image3.setVisibility(View.GONE);
                        } else if (diaryBean.getDiary_image().size() == 3) {
                            detail_image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0).toString()));
                            detail_image2.setImageURI(Uri.parse(diaryBean.getDiary_image().get(1).toString()));
                            detail_image3.setImageURI(Uri.parse(diaryBean.getDiary_image().get(2).toString()));
                        }
                    }
                } else {
                    if (!Tools.isNull(diaryBean.getDiary_video())) {
                        image_layout.setVisibility(View.GONE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initMediaPlayer(diaryBean.getDiary_video());
                            }
                        }).start();
                    }
                }
            }
        }
        if (!Tools.isNull(intent.getStringExtra("user_icon"))) {
            user_icon.setImageURI(Uri.parse(intent.getStringExtra("user_nick")));
        }
        if (!Tools.isNull(intent.getStringExtra("user_nick"))) {
            user_name.setText(intent.getStringExtra("user_nick"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_time"))) {
            user_time.setText(intent.getStringExtra("user_time"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_title"))) {
            user_title.setText(intent.getStringExtra("user_title"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_desc"))) {
            user_desc.setText(intent.getStringExtra("user_desc"));
        }
        if (!Tools.isNull(intent.getStringExtra("type"))) {
            if (intent.getStringExtra("type").equals("text")) {
                noText_layout.setVisibility(View.GONE);
            } else if (intent.getStringExtra("type").equals("image")) {
                video_layout.setVisibility(View.GONE);
                if (application.getDiary_image() != null) {
                    if (application.getDiary_image().size() == 1) {
                        detail_image1.setImageURI(Uri.parse(application.getDiary_image().get(0).toString()));
                        detail_image2.setVisibility(View.GONE);
                        detail_image3.setVisibility(View.GONE);
                    } else if (application.getDiary_image().size() == 2) {
                        detail_image1.setImageURI(Uri.parse(application.getDiary_image().get(0).toString()));
                        detail_image2.setImageURI(Uri.parse(application.getDiary_image().get(1).toString()));
                        detail_image3.setVisibility(View.GONE);
                    } else if (application.getDiary_image().size() == 3) {
                        detail_image1.setImageURI(Uri.parse(application.getDiary_image().get(0).toString()));
                        detail_image2.setImageURI(Uri.parse(application.getDiary_image().get(1).toString()));
                        detail_image3.setImageURI(Uri.parse(application.getDiary_image().get(2).toString()));
                    }
                }
            } else {
                image_layout.setVisibility(View.GONE);
                if (!Tools.isNull(intent.getStringExtra("path"))) {
                    final String path = intent.getStringExtra("path");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initMediaPlayer(path);
                        }
                    }).start();
                }
            }
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
            e.printStackTrace();
        }
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        video_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                TravellerDiaryDetailActivity.this.finish();
                break;
            case R.id.diary_detail_user_video_play:
                video_play.setVisibility(View.GONE);
                video_first.setVisibility(View.GONE);
                mediaPlayer.start();
                break;
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
        video_play.setVisibility(View.VISIBLE);
        video_loading.setVisibility(View.GONE);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        video_play.setVisibility(View.VISIBLE);
    }
}
