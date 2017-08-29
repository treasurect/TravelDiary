package com.treasure.traveldiary.activity.find_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
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
import com.treasure.traveldiary.adapter.TravellerDiaryListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class TravellerDetailActivity extends BaseActivity implements View.OnClickListener, TravellerDiaryListAdapter.DiaryTextClick {
    private ImageView btnReturn, user_sex;
    private TextView text_title, user_nick, attention_num, fans_num, user_desc;
    private SimpleDraweeView user_icon;
    private NestedScrollView scrollView;
    private CustomScrollListView listView;
    private List<DiaryBean> list;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private LinearLayout nodata_layout;
    private boolean isPageDestroy;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_detail);
        Tools.setTranslucentStatus(this);

        initFindId();
        getIntentReceiver();
        initListView();
        initScrollView();
        initClick();
        getUserInfo(name);
        getDiaryList(name);
    }

    private void initFindId() {
        btnReturn = (ImageView) findViewById(R.id.traveller_detail_return);
        text_title = (TextView) findViewById(R.id.traveller_detail_title);
        user_icon = (SimpleDraweeView) findViewById(R.id.traveller_detail_icon);
        user_nick = (TextView) findViewById(R.id.traveller_detail_nick);
        user_sex = (ImageView) findViewById(R.id.traveller_detail_sex);
        attention_num = (TextView) findViewById(R.id.traveller_detail_attention);
        fans_num = (TextView) findViewById(R.id.traveller_detail_fans);
        user_desc = (TextView) findViewById(R.id.traveller_detail_desc);

        scrollView = (NestedScrollView) findViewById(R.id.traveller_detail_scrollView);
        listView = (CustomScrollListView) findViewById(R.id.traveller_detail_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        nodata_layout = (LinearLayout) findViewById(R.id.no_data_layout);
    }

    private void getIntentReceiver() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("user_name"))) {
            name = intent.getStringExtra("user_name");
        }
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
        adapter.setDiaryTextClick(this);
        loading.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.traveller_detail_return:
                TravellerDetailActivity.this.finish();
                break;
        }
    }

    private void getUserInfo(String name) {
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", name)
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                            if (!isPageDestroy) {
                                if (list.size() > 0) {
                                    user_icon.setImageURI(Uri.parse(list.get(0).getUser_icon()));
                                    text_title.setText(list.get(0).getNick_name() + "的首页");
                                    user_nick.setText(list.get(0).getNick_name());
                                    if (list.get(0).getSex().equals("男")) {
                                        user_sex.setImageResource(R.mipmap.ic_sex_man);
                                    } else {
                                        user_sex.setImageResource(R.mipmap.ic_sex_women);
                                    }
                                    attention_num.setText("关注：" + (list.get(0).getAttention().size() - 1));
                                    fans_num.setText("粉丝：" + (list.get(0).getFans().size() - 1));
                                    user_desc.setText(list.get(0).getUser_desc());
                                }
                            }
                        } else {
                            if (!isPageDestroy) {
                                Toast.makeText(TravellerDetailActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void getDiaryList(String name) {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<DiaryBean> query = new BmobQuery<>();
        query.setSkip(0)
                .setLimit(50)
                .order("-publish_time")
                .addWhereEqualTo("state", "公开")
                .addWhereEqualTo("user_name", name)
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
                                Toast.makeText(TravellerDetailActivity.this, "获取日记列表失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void layoutClick(DiaryBean diaryBean) {
        Intent intent = new Intent(TravellerDetailActivity.this, DiaryDetailActivity.class);
        intent.putExtra("user_name", diaryBean.getUser_name());
        intent.putExtra("user_time", diaryBean.getPublish_time());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }
}
