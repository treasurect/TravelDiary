package com.treasure.traveldiary.activity.find_center;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.DiaryDetailActivity;
import com.treasure.traveldiary.activity.main_center.DiaryImageCameraActivity;
import com.treasure.traveldiary.adapter.TravellerDiaryListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class TravellerCircleActivity extends BaseActivity implements View.OnClickListener, TravellerDiaryListAdapter.DiaryTextClick {

    private ImageView btnReturn;
    private ImageView btnSettings;
    private TextView text_title;
    private SimpleDraweeView user_bg;
    private SimpleDraweeView user_icon;
    private NestedScrollView scrollView;
    private CustomScrollListView listView;
    private List<DiaryBean> list;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private SharedPreferences mPreferences;
    private FloatingActionButton btnRefresh;
    private String circle_bg_img = "暂无图片";
    private boolean isClickLike;
    private LinearLayout nodata_layout;
    private boolean isPageDestroy;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    loading.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_circle);
        Tools.setTranslucentStatus(this);
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        initView();
        initListView();
        initScrollView();
        initClick();
        getTravellerCircleList();
    }

    private void initFindId() {
        btnReturn = (ImageView) findViewById(R.id.traveller_circle_return);
        btnSettings = (ImageView) findViewById(R.id.traveller_circle_settings);
        text_title = (TextView) findViewById(R.id.traveller_circle_title);
        user_bg = (SimpleDraweeView) findViewById(R.id.traveller_circle_user_bg);
        user_icon = (SimpleDraweeView) findViewById(R.id.traveller_circle_user_icon);
        scrollView = (NestedScrollView) findViewById(R.id.traveller_circle_scrollView);
        listView = (CustomScrollListView) findViewById(R.id.traveller_circle_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        btnRefresh = (FloatingActionButton) findViewById(R.id.traveller_circle_refresh);
        nodata_layout = (LinearLayout) findViewById(R.id.no_data_layout);
    }

    private void initView() {
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                            if (list != null) {
                                user_bg.setImageURI(Uri.parse(list.get(0).getTraveller_circle_bg()));
                                user_icon.setImageURI(Uri.parse(list.get(0).getUser_icon()));
                            }
                        } else {
                            if (!isPageDestroy) {
                                Toast.makeText(TravellerCircleActivity.this, "更新背景失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void initListView() {
        list = new ArrayList<>();
        adapter = new TravellerDiaryListAdapter(this, list);
        listView.setAdapter(adapter);
    }

    private void initScrollView() {
        scrollView.smoothScrollTo(0, 20);
        listView.setFocusable(false);
    }

    private void initClick() {
        btnReturn.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        user_bg.setOnClickListener(this);
        adapter.setDiaryTextClick(this);
        btnRefresh.setOnClickListener(this);
        loading.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.traveller_circle_return:
                TravellerCircleActivity.this.finish();
                break;
            case R.id.traveller_circle_settings:

                break;
            case R.id.traveller_circle_user_bg:
                showCameraSelectDialog();
                break;
            case R.id.traveller_circle_refresh:
                Tools.setAnimation(btnRefresh, 0, 0, 1, 1, 0, -720, 1, 1, 2000);
                getTravellerCircleList();
                if (!isPageDestroy) {
                    Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showCameraSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] item_list = {"拍照", "从相册上中选取"};
        builder.setSingleChoiceItems(item_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    startActivityForResult(new Intent(TravellerCircleActivity.this, DiaryImageCameraActivity.class), 200);
                } else if (which == 1) {
                    startActivityForResult(new Intent(TravellerCircleActivity.this, DiaryImageCameraActivity.class), 200);
                }
                dialog.dismiss();
            }
        }).create();
        builder.show();
    }

    private void getTravellerCircleList() {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<DiaryBean> query = new BmobQuery<>();
        query.setSkip(0)
                .setLimit(50)
                .order("-publish_time")
                .addWhereEqualTo("state", "公开")
                .findObjects(new FindListener<DiaryBean>() {
                    @Override
                    public void done(List<DiaryBean> list2, BmobException e) {
                        if (e == null) {
                            list.clear();
                            list.addAll(list2);
                            adapter.notifyDataSetChanged();
                            if (!isPageDestroy) {
                                loading.setVisibility(View.GONE);
                                if (list2.size() == 0) {
                                    nodata_layout.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            if (!isPageDestroy) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(TravellerCircleActivity.this, "获取日记列表失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void layoutClick(DiaryBean diaryBean) {
        Intent intent = new Intent(TravellerCircleActivity.this, DiaryDetailActivity.class);
        intent.putExtra("user_name", diaryBean.getUser_name());
        intent.putExtra("user_time", diaryBean.getPublish_time());
        startActivity(intent);
    }

    @Override
    public void likeClick(final DiaryBean diaryBean) {
        BmobQuery<DiaryBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user_name", diaryBean.getUser_name());
        BmobQuery<DiaryBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("publish_time", diaryBean.getPublish_time());
        List<BmobQuery<DiaryBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<DiaryBean> bmobQuery = new BmobQuery<>();
        bmobQuery.and(queries);
        bmobQuery.findObjects(new FindListener<DiaryBean>() {
            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (e == null) {
                    String objectId = list.get(0).getObjectId();
                    toUpdateLikeBean(objectId, diaryBean.getLikeBean());
                }
            }
        });
    }

    private void toUpdateLikeBean(String objectId, List<String> likeBean) {
        for (int i = 0; i < likeBean.size(); i++) {
            if (likeBean.get(i).equals(mPreferences.getString("user_name", ""))) {
                isClickLike = true;
            }
        }
        if (isClickLike) {
            if (!isPageDestroy) {
                Toast.makeText(this, "您已经点过赞了", Toast.LENGTH_SHORT).show();
            }
        } else {
            DiaryBean diaryBean = new DiaryBean();
            List<String> strings = new ArrayList<>();
            strings.addAll(likeBean);
            strings.add(mPreferences.getString("user_name", ""));
            diaryBean.setLikeBean(strings);
            diaryBean.update(objectId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        if (!isPageDestroy) {
                            Toast.makeText(TravellerCircleActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!isPageDestroy) {
                            Toast.makeText(TravellerCircleActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            byte[] camera_data = (byte[]) data.getExtras().get("camera_data");
            if (camera_data != null) {
                final Bitmap bm_camera1 = BitmapFactory.decodeByteArray(camera_data, 0, camera_data.length);
                user_bg.setImageBitmap(bm_camera1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String circle_bg = Tools.saveBitmapToSD(TravellerCircleActivity.this, bm_camera1, "traveller_circle_bg");
                        final BmobFile bmobFile = new BmobFile(new File(circle_bg));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    circle_bg_img = bmobFile.getFileUrl();
                                    //存到本地
                                    SharedPreferences.Editor editor = mPreferences.edit();
                                    editor.putString("user_circle_bg", circle_bg_img);
                                    editor.apply();
                                    toGetObjectId();
                                } else {
                                    if (!isPageDestroy) {
                                        Toast.makeText(TravellerCircleActivity.this, "背景更新失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }).start();
            }
        }
    }

    private void toGetObjectId() {
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                            if (list != null) {
                                String objectId = list.get(0).getObjectId();
                                toUpdateUserInfo(objectId);
                            }
                        } else {
                            if (!isPageDestroy) {
                                Toast.makeText(TravellerCircleActivity.this, "背景更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void toUpdateUserInfo(String objectId) {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setTraveller_circle_bg(circle_bg_img);
        userInfoBean.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (!isPageDestroy) {
                        Toast.makeText(TravellerCircleActivity.this, "背景更新成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!isPageDestroy) {
                        Toast.makeText(TravellerCircleActivity.this, "背景更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (!isPageDestroy) {
                        handler.sendMessage(handler.obtainMessage(200));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }
}
