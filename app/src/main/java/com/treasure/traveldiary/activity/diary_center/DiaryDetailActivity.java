package com.treasure.traveldiary.activity.diary_center;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.TravelApplication;
import com.treasure.traveldiary.adapter.DiaryLeavemesListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class DiaryDetailActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, TextWatcher {

    private SimpleDraweeView user_icon;
    private TextView user_name, user_time, user_title, user_desc;
    private CustomScrollListView leaveMes_listView;
    private TravelApplication application;
    private SurfaceView surfaceView;
    private ImageView video_play;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private FrameLayout video_layout;
    private SimpleDraweeView video_first;
    private ProgressBar video_loading;
    private SimpleDraweeView image1, image01, image02, image001, image002, image003;
    private LinearLayout image_layout2, image_layout3;
    private FrameLayout leaveMes_send;
    private PopupWindow mPopupWindow;
    private EditText editLeaveMes;
    private TextView sendLeaveMes;
    private SharedPreferences mPreferences;
    private String diary_publisher, diary_time;
    private String objectId;
    private List<SUserBean> mesBeanList;
    private DiaryLeavemesListAdapter adapter;
    private FrameLayout loading;
    private ScrollView scrollView;
    private FloatingActionButton btnRefresh;
    private DiaryBean diaryBean;
    private ImageView video_full;
    private String image_url1, image_url2, image_url3;
    private boolean isPageDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("详情");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        application = (TravelApplication) getApplication();
        initFindId();
        initListView();
        initScrollView();
        receiveIntent();
        if (!Tools.isNull(diary_publisher) && !Tools.isNull(diary_time)) {
            getDiaryDetail(diary_publisher, diary_time);
        }
        initClick();
    }

    private void initFindId() {
        user_icon = (SimpleDraweeView) findViewById(R.id.diary_detail_user_icon);
        user_name = (TextView) findViewById(R.id.diary_detail_user_name);
        user_time = (TextView) findViewById(R.id.diary_detail_user_time);
        user_title = (TextView) findViewById(R.id.diary_detail_user_title);
        user_desc = (TextView) findViewById(R.id.diary_detail_user_desc);
        leaveMes_listView = (CustomScrollListView) findViewById(R.id.diary_detail_leaveMes_list);
        leaveMes_send = (FrameLayout) findViewById(R.id.diary_detail_leaveMes_send);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        scrollView = (ScrollView) findViewById(R.id.diary_detail_leaveMes_layout);
        btnRefresh = (FloatingActionButton) findViewById(R.id.diary_detail_leaveMes_refresh);
        //Video
        video_layout = (FrameLayout) findViewById(R.id.diary_detail_user_video_layout);
        surfaceView = (SurfaceView) findViewById(R.id.diary_detail_user_video_view);
        video_play = (ImageView) findViewById(R.id.diary_detail_user_video_play);
        video_first = (SimpleDraweeView) findViewById(R.id.diary_detail_user_video_first);
        video_loading = (ProgressBar) findViewById(R.id.diary_detail_user_video_loading);
        video_full = (ImageView) findViewById(R.id.diary_detail_user_video_full);
        //Image  onePic
        image1 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image1);
        //Image twoPic
        image_layout2 = (LinearLayout) findViewById(R.id.diary_detail_user_image_layout2);
        image01 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image01);
        image02 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image02);
        //Image threePic
        image_layout3 = (LinearLayout) findViewById(R.id.diary_detail_user_image_layout3);
        image001 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image001);
        image002 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image002);
        image003 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image003);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("user_name"))) {
            diary_publisher = intent.getStringExtra("user_name");
        }
        if (!Tools.isNull(intent.getStringExtra("user_time"))) {
            diary_time = intent.getStringExtra("user_time");
        }
    }

    private void initListView() {
        mesBeanList = new ArrayList<>();
        adapter = new DiaryLeavemesListAdapter(this, mesBeanList);
        leaveMes_listView.setAdapter(adapter);
    }

    private void initScrollView() {
        scrollView.smoothScrollTo(0, 20);
        leaveMes_listView.setFocusable(false);
    }

    private void getDiaryDetail(String diary_publisher, String diary_time) {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<DiaryBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user_name", diary_publisher);
        BmobQuery<DiaryBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("publish_time", diary_time);
        List<BmobQuery<DiaryBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<DiaryBean> bmobQuery = new BmobQuery<>();
        bmobQuery.and(queries);
        bmobQuery.findObjects(new FindListener<DiaryBean>() {

            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (e == null) {
                    diaryBean = list.get(0);
                    objectId = list.get(0).getObjectId();
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
                        if (!Tools.isNull(String.valueOf(diaryBean.getDiary_type()))) {
                            if (diaryBean.getDiary_type().equals("1")) {
                                if (diaryBean.getDiary_image().size() == 1) {
                                    image1.setVisibility(View.VISIBLE);
                                    image_url1 = diaryBean.getDiary_image().get(0).toString();
                                    image1.setImageURI(Uri.parse(image_url1));
                                } else if (diaryBean.getDiary_image().size() == 2) {
                                    image_layout2.setVisibility(View.VISIBLE);
                                    image_url1 = diaryBean.getDiary_image().get(0).toString();
                                    image_url2 = diaryBean.getDiary_image().get(1).toString();
                                    image01.setImageURI(Uri.parse(image_url1));
                                    image02.setImageURI(Uri.parse(image_url2));
                                } else if (diaryBean.getDiary_image().size() == 3) {
                                    image_layout3.setVisibility(View.VISIBLE);
                                    image_url1 = diaryBean.getDiary_image().get(0).toString();
                                    image_url2 = diaryBean.getDiary_image().get(1).toString();
                                    image_url3 = diaryBean.getDiary_image().get(2).toString();
                                    image001.setImageURI(Uri.parse(image_url1));
                                    image002.setImageURI(Uri.parse(image_url2));
                                    image003.setImageURI(Uri.parse(image_url3));
                                }
                            } else if (diaryBean.getDiary_type().equals("2")) {
                                video_layout.setVisibility(View.VISIBLE);
                                video_first.setImageURI(Uri.parse(diaryBean.getDiary_video_first()));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initMediaPlayer(diaryBean.getDiary_video());
                                    }
                                }).start();
                            }
                        }
                        if (diaryBean.getMesBeanList() != null) {
                            List<SUserBean> leaveMesList1 = diaryBean.getMesBeanList();
                            //显示留言  从外部进入
                            mesBeanList.clear();
                            leaveMesList1.remove(0);
                            Collections.reverse(leaveMesList1);
                            mesBeanList.addAll(leaveMesList1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    loading.setVisibility(View.GONE);
                } else {
                    Toast.makeText(DiaryDetailActivity.this, "获取日记详情失败", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getLeaveMesList(String objectId) {
        BmobQuery<DiaryBean> diaryBeanBmobQuery = new BmobQuery<>();
        diaryBeanBmobQuery.addWhereEqualTo("objectId", objectId)
                .findObjects(new FindListener<DiaryBean>() {
                    @Override
                    public void done(List<DiaryBean> list, BmobException e) {
                        if (e == null) {
                            List<SUserBean> leaveMesList2 = list.get(0).getMesBeanList();
                            //显示留言   刷新
                            mesBeanList.clear();
                            leaveMesList2.remove(0);
                            Collections.reverse(leaveMesList2);
                            mesBeanList.addAll(leaveMesList2);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(DiaryDetailActivity.this, "很遗憾，获取评论列表失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initMediaPlayer(String path) {
        mediaPlayer = new MediaPlayer();
        try {
            video_full.setClickable(false);
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
            if (!isPageDestroy) {
                Toast.makeText(this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        video_play.setOnClickListener(this);
        leaveMes_send.setOnClickListener(this);
        loading.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        image1.setOnClickListener(this);
        image01.setOnClickListener(this);
        image02.setOnClickListener(this);
        image001.setOnClickListener(this);
        image002.setOnClickListener(this);
        image003.setOnClickListener(this);
        video_full.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                DiaryDetailActivity.this.finish();
                break;
            case R.id.diary_detail_user_video_play:
                video_play.setVisibility(View.GONE);
                video_first.setVisibility(View.GONE);
                mediaPlayer.start();
                break;
            case R.id.diary_detail_leaveMes_send:
                showPopupWindow();
                leaveMes_send.setVisibility(View.GONE);
                break;
            case R.id.leaveMes_send:
                mPopupWindow.dismiss();
                leaveMes_send.setVisibility(View.VISIBLE);
                if (!Tools.isNull(editLeaveMes.getText().toString().trim())) {
                    sendLeaveMes();
                }
                break;
            case R.id.diary_detail_leaveMes_refresh:
                Tools.setAnimation(btnRefresh, 0, 0, 1, 1, 0, -760, 1, 1, 2000);
                getLeaveMesList(objectId);
                Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.diary_detail_user_image1:
                Intent intent00 = new Intent(DiaryDetailActivity.this, DiaryImageFullActivity.class);
//                Bitmap bitmap = ((BitmapDrawable) ((SimpleDraweeView) image1).getDrawable()).getBitmap();
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                byte[] bytes = bos.toByteArray();
//                intent00.putExtra("image", bytes);
                intent00.putExtra("image_url", image_url1);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent00, ActivityOptions.makeSceneTransitionAnimation(this, image1, "image1").toBundle());
                } else {
                    startActivity(intent00);
                }
                break;
            case R.id.diary_detail_user_image01:
                Intent intent01 = new Intent(DiaryDetailActivity.this, DiaryImageFullActivity.class);
                intent01.putExtra("image_url", image_url1);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent01, ActivityOptions.makeSceneTransitionAnimation(this, image01, "image01").toBundle());
                } else {
                    startActivity(intent01);
                }
                break;
            case R.id.diary_detail_user_image02:
                Intent intent02 = new Intent(DiaryDetailActivity.this, DiaryImageFullActivity.class);
                intent02.putExtra("image_url", image_url2);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent02, ActivityOptions.makeSceneTransitionAnimation(this, image02, "image02").toBundle());
                } else {
                    startActivity(intent02);
                }
                break;
            case R.id.diary_detail_user_image001:
                Intent intent03 = new Intent(DiaryDetailActivity.this, DiaryImageFullActivity.class);
                intent03.putExtra("image_url", image_url1);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent03, ActivityOptions.makeSceneTransitionAnimation(this, image001, "image001").toBundle());
                } else {
                    startActivity(intent03);
                }
                break;
            case R.id.diary_detail_user_image002:
                Intent intent04 = new Intent(DiaryDetailActivity.this, DiaryImageFullActivity.class);
                intent04.putExtra("image_url", image_url2);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent04, ActivityOptions.makeSceneTransitionAnimation(this, image002, "image002").toBundle());
                } else {
                    startActivity(intent04);
                }
                break;
            case R.id.diary_detail_user_image003:
                Intent intent05 = new Intent(DiaryDetailActivity.this, DiaryImageFullActivity.class);
                intent05.putExtra("image_url", image_url3);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent05, ActivityOptions.makeSceneTransitionAnimation(this, image003, "image003").toBundle());
                } else {
                    startActivity(intent05);
                }
                break;
            case R.id.diary_detail_user_video_full:
                Intent intent06 = new Intent(DiaryDetailActivity.this, DiaryVideoFullActivity.class);
                intent06.putExtra("video_duration", String.valueOf(mediaPlayer.getDuration()));
                intent06.putExtra("video_position", String.valueOf(mediaPlayer.getCurrentPosition()));
                intent06.putExtra("video_url", diaryBean.getDiary_video());
                intent06.putExtra("image_url", diaryBean.getDiary_video_first());
                startActivityForResult(intent06, 200);
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
        video_full.setClickable(true);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        video_play.setVisibility(View.VISIBLE);
        video_first.setVisibility(View.VISIBLE);
        mediaPlayer.seekTo(0);
    }

    /**
     * 显示 关闭 popupWindow
     */
    public void showPopupWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.popupwindow_leavemes_send, null);
        mPopupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.leaveMesPopupWindow);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x66000000));
        editLeaveMes = ((EditText) convertView.findViewById(R.id.leaveMes_content));
        sendLeaveMes = ((TextView) convertView.findViewById(R.id.leaveMes_send));

        sendLeaveMes.setOnClickListener(this);
        editLeaveMes.addTextChangedListener(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_diary_detail, null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (Tools.isNull(charSequence.toString())) {
            sendLeaveMes.setText("关闭");
            sendLeaveMes.setTextColor(getResources().getColor(R.color.colorGray2));
        } else {
            sendLeaveMes.setText("发送");
            sendLeaveMes.setTextColor(getResources().getColor(R.color.colorOrange));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void sendLeaveMes() {
        BmobQuery<DiaryBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user_name", diary_publisher);
        BmobQuery<DiaryBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("publish_time", diary_time);
        List<BmobQuery<DiaryBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<DiaryBean> bmobQuery = new BmobQuery<>();
        bmobQuery.and(queries);
        bmobQuery.findObjects(new FindListener<DiaryBean>() {
            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (e == null) {
                    objectId = list.get(0).getObjectId();
                    List<SUserBean> leaveMesList3 = list.get(0).getMesBeanList();
                    String diary_type = list.get(0).getDiary_type();
                    String user_lat = list.get(0).getUser_lat();
                    String user_long = list.get(0).getUser_long();
                    saveLeaveMes(objectId, leaveMesList3, diary_type, user_lat, user_long);
                } else {
                    Toast.makeText(DiaryDetailActivity.this, "很遗憾，评论发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveLeaveMes(final String objectId, List<SUserBean> leaveMesList4, String diary_type, String user_lat, String user_long) {
        SUserBean bean = new SUserBean();
        bean.setLeave_content(editLeaveMes.getText().toString().trim());
        bean.setLeave_icon(mPreferences.getString("user_icon", ""));
        bean.setLeave_nick(mPreferences.getString("user_nick", ""));
        bean.setLeave_name(mPreferences.getString("user_name", ""));
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Long(System.currentTimeMillis()));
        bean.setLeave_time(time.substring(5, 7) + "月" + time.substring(8, 10) + "日" + time.substring(11, 16));

        List<SUserBean> leaveMesBeen = new ArrayList<>();
        leaveMesBeen.addAll(leaveMesList4);
        leaveMesBeen.add(bean);
        //上传服务器
        DiaryBean diaryBean = new DiaryBean();
        diaryBean.setMesBeanList(leaveMesBeen);
        diaryBean.setDiary_type(diary_type);
        diaryBean.setUser_lat(user_lat);
        diaryBean.setUser_long(user_long);
        diaryBean.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(DiaryDetailActivity.this, "评论成功，你够意思", Toast.LENGTH_SHORT).show();
                    getLeaveMesList(objectId);
                } else {
                    Toast.makeText(DiaryDetailActivity.this, "很遗憾，评论发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            if (data != null) {
                int video_position = Integer.parseInt(data.getStringExtra("video_position"));
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {//此时未播放完视频
                        mediaPlayer.seekTo(video_position);
                        video_play.setVisibility(View.GONE);
                        video_first.setVisibility(View.GONE);
                    } else {//已播放完或者未播放
                        video_play.setVisibility(View.GONE);
                        video_first.setVisibility(View.GONE);
                        mediaPlayer.start();
                        mediaPlayer.seekTo(video_position);
                    }
                }
            }
        }
    }
}
