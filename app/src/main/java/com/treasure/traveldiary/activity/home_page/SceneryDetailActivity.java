package com.treasure.traveldiary.activity.home_page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.DiaryLeavemesListAdapter;
import com.treasure.traveldiary.bean.LeaveMesBean;
import com.treasure.traveldiary.bean.SceneryBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class SceneryDetailActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ScrollView scrollView;
    private FrameLayout show_leave_mes;
    private CustomScrollListView mes_listView;
    private TextView scenery_level;
    private TextView scenery_open_time;
    private TextView scenery_addr;
    private TextView scenery_way;
    private TextView scenery_desc;
    private List<LeaveMesBean> mesBeanList;
    private DiaryLeavemesListAdapter adapter;
    private SimpleDraweeView scenery_image;
    private PopupWindow mPopupWindow;
    private EditText editLeaveMes;
    private TextView sendLeaveMes;
    private String scenery_name;
    private boolean isPageDestroy;
    private SharedPreferences mPreferences;
    private FrameLayout loading;
    private FloatingActionButton refresh;
    private List<LeaveMesBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenery_detail);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("详情");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        initListView();
        initScrollView();
        getIntentReceiver();
        initClick();
    }

    private void initFindId() {
        scrollView = (ScrollView) findViewById(R.id.scenery_detail_leaveMes_layout);
        show_leave_mes = (FrameLayout) findViewById(R.id.scenery_detail_show_leaveMes);
        mes_listView = (CustomScrollListView) findViewById(R.id.scenery_detail_leaveMes_list);
        scenery_image = (SimpleDraweeView) findViewById(R.id.scenery_detail_image);
        scenery_level = (TextView) findViewById(R.id.scenery_detail_level);
        scenery_open_time = (TextView) findViewById(R.id.scenery_detail_open_time);
        scenery_addr = (TextView) findViewById(R.id.scenery_detail_addr);
        scenery_way = (TextView) findViewById(R.id.scenery_detail_way);
        scenery_desc = (TextView) findViewById(R.id.scenery_detail_desc);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        refresh = (FloatingActionButton) findViewById(R.id.scenery_detail_leaveMes_refresh);
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
                scenery_image.setImageURI(Uri.parse(sceneryBean.getScenery_image().get(0)));
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
        final LeaveMesBean leaveMesBean = new LeaveMesBean();
        leaveMesBean.setLeave_name(mPreferences.getString("user_name", ""));
        leaveMesBean.setLeave_nick(mPreferences.getString("user_nick", ""));
        leaveMesBean.setLeave_icon(mPreferences.getString("user_icon", ""));
        String nowTime = Tools.getNowTime();
        leaveMesBean.setLeave_time(nowTime);
        leaveMesBean.setLeave_content(editLeaveMes.getText().toString().trim());
        list.add(leaveMesBean);
        sceneryBean.setScenery_comments(list);

        Collections.reverse(mesBeanList);
        mesBeanList.add(leaveMesBean);

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
                                List<LeaveMesBean> scenery_comments = list.get(0).getScenery_comments();
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
