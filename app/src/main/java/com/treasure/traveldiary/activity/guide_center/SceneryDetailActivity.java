package com.treasure.traveldiary.activity.guide_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.DiaryLeavemesListAdapter;
import com.treasure.traveldiary.adapter.PictureGuideAdapter;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.bean.SceneryBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class SceneryDetailActivity extends BaseActivity implements View.OnClickListener, TextWatcher, ViewPager.OnPageChangeListener {

    private ScrollView scrollView;
    private FrameLayout show_leave_mes;
    private CustomScrollListView mes_listView;
    private TextView scenery_level;
    private TextView scenery_open_time;
    private TextView scenery_addr;
    private TextView scenery_way;
    private TextView scenery_desc;
    private List<SUserBean> mesBeanList;
    private DiaryLeavemesListAdapter adapter;
    private ViewPager scenery_viewPager;
    private PopupWindow mPopupWindow;
    private EditText editLeaveMes;
    private TextView sendLeaveMes;
    private String scenery_name;
    private boolean isPageDestroy;
    private SharedPreferences mPreferences;
    private FrameLayout loading;
    private FloatingActionButton refresh;
    private List<SUserBean> list = new ArrayList<>();
    private List<String> image_url;
    private PictureGuideAdapter guideAdapter;
    private List<ImageView> views;
    private List<Bitmap> bitmap_list = new ArrayList<>();
    private Bitmap bitmap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    for (int i = 0; i < bitmap_list.size(); i++) {
                        ImageView imageView = new ImageView(SceneryDetailActivity.this);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setImageBitmap(bitmap_list.get(i));
                        views.add(imageView);
                        guideAdapter.notifyDataSetChanged();
                    }
                    for (int i = 0; i < bitmap_list.size(); i++) {
                        ImageView imageView = new ImageView(SceneryDetailActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(bitmap.getWidth() / 8, bitmap.getHeight() / 8);
                        layoutParams.setMargins(5, 0, 5, 0);
                        layoutParams.gravity = Gravity.CENTER_VERTICAL;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setBackgroundResource(R.drawable.viewpager_dots_unclick);
                        dots_layout.addView(imageView);
                    }
                    setDots(0);
                    viewPager_loading.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private ProgressBar viewPager_loading;
    private LinearLayout dots_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenery_detail);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("详情");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bots_orange);

        initFindId();
        initListView();
        initScrollView();
        initViewPager();
        getIntentReceiver();
        initClick();
    }

    private void initFindId() {
        scrollView = (ScrollView) findViewById(R.id.scenery_detail_leaveMes_layout);
        show_leave_mes = (FrameLayout) findViewById(R.id.scenery_detail_show_leaveMes);
        mes_listView = (CustomScrollListView) findViewById(R.id.scenery_detail_leaveMes_list);
        scenery_viewPager = (ViewPager) findViewById(R.id.scenery_detail_viewPager);
        scenery_level = (TextView) findViewById(R.id.scenery_detail_level);
        scenery_open_time = (TextView) findViewById(R.id.scenery_detail_open_time);
        scenery_addr = (TextView) findViewById(R.id.scenery_detail_addr);
        scenery_way = (TextView) findViewById(R.id.scenery_detail_way);
        scenery_desc = (TextView) findViewById(R.id.scenery_detail_desc);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        refresh = (FloatingActionButton) findViewById(R.id.scenery_detail_leaveMes_refresh);
        viewPager_loading = (ProgressBar) findViewById(R.id.scenery_detail_loading);
        dots_layout = (LinearLayout) findViewById(R.id.scenery_detail_dots_layout);
    }

    private void initViewPager() {
        views = new ArrayList<>();
        guideAdapter = new PictureGuideAdapter(this, views);
        scenery_viewPager.setAdapter(guideAdapter);
    }

    private void getIntentReceiver() {
        Intent intent = getIntent();
        if (intent.getExtras().get("sceneryBean") != null) {
            SceneryBean sceneryBean = (SceneryBean) intent.getExtras().get("sceneryBean");
            if (!Tools.isNull(sceneryBean.getScenery_name())) {
                scenery_name = sceneryBean.getScenery_name();
                title.setText(scenery_name);
            }
            if (sceneryBean.getScenery_image() != null) {
                image_url = sceneryBean.getScenery_image();
                viewPager_loading.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < image_url.size(); i++) {
                            Bitmap bitmap = Tools.getBitmap(image_url.get(i));
                            bitmap_list.add(bitmap);
                        }
                        handler.sendMessage(handler.obtainMessage(200));
                    }
                }).start();
            }
            if (!Tools.isNull(sceneryBean.getScenery_level())) {
                scenery_level.setText(sceneryBean.getScenery_level());
            }
            if (!Tools.isNull(sceneryBean.getScenery_addr())) {
                scenery_addr.setText(sceneryBean.getScenery_addr());
            }
            if (!Tools.isNull(sceneryBean.getScenery_open_time())) {
                scenery_open_time.setText(sceneryBean.getScenery_open_time());
            }
            if (!Tools.isNull(sceneryBean.getScenery_desc())) {
                scenery_desc.setText(sceneryBean.getScenery_desc());
            }
            if (!Tools.isNull(sceneryBean.getScenery_way())) {
                scenery_way.setText(sceneryBean.getScenery_way());
            }
            if (sceneryBean.getScenery_comments() != null) {
                list.addAll(sceneryBean.getScenery_comments());

                sceneryBean.getScenery_comments().remove(0);
                Collections.reverse(sceneryBean.getScenery_comments());
                mesBeanList.addAll(sceneryBean.getScenery_comments());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initListView() {
        mesBeanList = new ArrayList<>();
        adapter = new DiaryLeavemesListAdapter(this, mesBeanList);
        mes_listView.setAdapter(adapter);
    }

    private void initScrollView() {
        scrollView.smoothScrollTo(0, 20);
        mes_listView.setFocusable(false);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        show_leave_mes.setOnClickListener(this);
        refresh.setOnClickListener(this);
        loading.setOnClickListener(this);
        scenery_viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                SceneryDetailActivity.this.finish();
                break;
            case R.id.scenery_detail_show_leaveMes:
                showPopupWindow();
                show_leave_mes.setVisibility(View.GONE);
                break;
            case R.id.leaveMes_send:
                if (!Tools.isNull(editLeaveMes.getText().toString().trim())) {
                    getLeaveMes();
                }
                mPopupWindow.dismiss();
                show_leave_mes.setVisibility(View.VISIBLE);
                break;
            case R.id.scenery_detail_leaveMes_refresh:
                Tools.setAnimation(refresh, 0, 0, 1, 1, 0, -760, 1, 1, 2000);
                getLeaveMesList();
                Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position >= 0 && position < bitmap_list.size()) {
            initDots();
            setDots(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initDots() {
        for (int i = 0; i < bitmap_list.size(); i++) {
            ImageView imageView = (ImageView) dots_layout.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(bitmap.getWidth() / 8, bitmap.getHeight() / 8);
            layoutParams.setMargins(5, 0, 5, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundResource(R.drawable.viewpager_dots_unclick);

        }
    }

    private void setDots(int position) {
        ImageView imageView = (ImageView) dots_layout.getChildAt(position);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(bitmap.getWidth() / 5, bitmap.getHeight() / 5);
        layoutParams.setMargins(5, 0, 5, 0);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(layoutParams);
        imageView.setBackgroundResource(R.drawable.viewpager_dots_click);
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
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_scenery_detail, null);
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

    private void getLeaveMes() {
        BmobQuery<SceneryBean> query = new BmobQuery<>();
        query.addWhereEqualTo("scenery_name", scenery_name)
                .findObjects(new FindListener<SceneryBean>() {
                    @Override
                    public void done(List<SceneryBean> list, BmobException e) {
                        if (e == null) {
                            String objectId = list.get(0).getObjectId();
                            saveLeaveMes(objectId);
                        } else {
                            if (!isPageDestroy) {
                                Toast.makeText(SceneryDetailActivity.this, "获取日记列表失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void saveLeaveMes(String objectId) {
        SceneryBean sceneryBean = new SceneryBean();
        final SUserBean SUserBean = new SUserBean();
        SUserBean.setLeave_name(mPreferences.getString("user_name", ""));
        SUserBean.setLeave_nick(mPreferences.getString("user_nick", ""));
        SUserBean.setLeave_icon(mPreferences.getString("user_icon", ""));
        String nowTime = Tools.getNowTime();
        SUserBean.setLeave_time(nowTime);
        SUserBean.setLeave_content(editLeaveMes.getText().toString().trim());
        list.add(SUserBean);
        sceneryBean.setScenery_comments(list);

        Collections.reverse(mesBeanList);
        mesBeanList.add(SUserBean);

        sceneryBean.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Collections.reverse(mesBeanList);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SceneryDetailActivity.this, "评论成功，你够意思！", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isPageDestroy) {
                        Toast.makeText(SceneryDetailActivity.this, "很遗憾，评论失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getLeaveMesList() {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<SceneryBean> query = new BmobQuery<>();
        query.addWhereEqualTo("scenery_name", scenery_name)
                .findObjects(new FindListener<SceneryBean>() {
                    @Override
                    public void done(List<SceneryBean> list, BmobException e) {
                        if (e == null) {
                            if (!isPageDestroy) {
                                List<SUserBean> scenery_comments = list.get(0).getScenery_comments();
                                mesBeanList.clear();
                                scenery_comments.remove(0);
                                Collections.reverse(scenery_comments);
                                mesBeanList.addAll(scenery_comments);
                                adapter.notifyDataSetChanged();
                                loading.setVisibility(View.GONE);
                            }
                        } else {
                            if (!isPageDestroy) {
                                Toast.makeText(SceneryDetailActivity.this, "获取评论列表失败", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }
}
