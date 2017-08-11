package com.treasure.traveldiary.activity.traveller_circle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.DiaryDetailActivity;
import com.treasure.traveldiary.adapter.TravellerDiaryListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_circle);
        Tools.setTranslucentStatus(this);
        mPreferences = getSharedPreferences("user",MODE_PRIVATE);

        initFindid();
        user_icon.setImageURI(Uri.parse(mPreferences.getString("user_icon","")));
        initListView();
        initScrollView();
        initClick();
        getTravellerCircleList();
    }

    private void initFindid() {
        btnReturn = (ImageView) findViewById(R.id.traveller_circle_return);
        btnSettings = (ImageView) findViewById(R.id.traveller_circle_settings);
        text_title = (TextView) findViewById(R.id.traveller_circle_title);
        user_bg = (SimpleDraweeView) findViewById(R.id.traveller_circle_user_bg);
        user_icon = (SimpleDraweeView) findViewById(R.id.traveller_circle_user_icon);
        scrollView = (NestedScrollView) findViewById(R.id.traveller_circle_scrollView);
        listView = (CustomScrollListView) findViewById(R.id.traveller_circle_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        btnRefresh = (FloatingActionButton) findViewById(R.id.traveller_circle_refresh);
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

                break;
            case R.id.traveller_circle_refresh:
                Tools.setAnimation(btnRefresh,0,0,1,1,0,-720,1,1,2000);
                getTravellerCircleList();
                break;
        }
    }

    private void getTravellerCircleList() {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<DiaryBean> query = new BmobQuery<>();
        query.setSkip(0)
                .setLimit(50)
                .order("-publish_time")
                .findObjects(new FindListener<DiaryBean>() {
                    @Override
                    public void done(List<DiaryBean> list2, BmobException e) {
                        if (e == null){
                            list.addAll(list2);
                            adapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(TravellerCircleActivity.this, "原因："  +e.getMessage(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void textClick(DiaryBean diaryBean) {
        Intent intent = new Intent(TravellerCircleActivity.this, DiaryDetailActivity.class);
        intent.putExtra("user_name",diaryBean.getUser_name());
        intent.putExtra("user_time",diaryBean.getPublish_time());
        startActivity(intent);
    }
}
