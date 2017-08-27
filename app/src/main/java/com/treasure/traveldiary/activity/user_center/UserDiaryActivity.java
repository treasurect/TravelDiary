package com.treasure.traveldiary.activity.user_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.DiaryDetailActivity;
import com.treasure.traveldiary.adapter.TravellerDiaryListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserDiaryActivity extends BaseActivity implements View.OnClickListener, TravellerDiaryListAdapter.DiaryTextClick, CustomRefreshListView.OnRefreshListener {
    private CustomRefreshListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPageDestroy;
    private SharedPreferences mPreferences;
    private int skip = 0;
    private boolean isLoadEndFlag;
    private BmobQuery<DiaryBean> query = new BmobQuery<>();
    private int maxLength;
    private boolean isClickLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_diary);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("我的日记");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        isLoadEndFlag = false;
        skip = 0;
        initFindId();
        initListView();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDiaryList();
    }

    private void initFindId() {
        listView = (CustomRefreshListView) findViewById(R.id.traveller_diary_list);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
    }

    private void initListView() {
        diaryList = new ArrayList<>();
        adapter = new TravellerDiaryListAdapter(this, diaryList);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        adapter.setDiaryTextClick(this);
        loading.setOnClickListener(this);
    }

    private void getDiaryList() {
        loading.setVisibility(View.VISIBLE);
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""));
        query.count(DiaryBean.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                try {
                     maxLength = integer.intValue();
                    if (maxLength <= 10) {
                        isLoadEndFlag = true;
                    }
                } catch (Exception error) {

                }
                query.setLimit(10)
                        .setSkip(skip)
                        .order("-publish_time")
                        .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                        .findObjects(new FindListener<DiaryBean>() {
                            @Override
                            public void done(List<DiaryBean> list, BmobException e) {
                                if (e == null) {
                                    if (list != null) {
                                        loading.setVisibility(View.GONE);
                                        diaryList.clear();
                                        diaryList.addAll(list);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    loading.setVisibility(View.GONE);
                                    Toast.makeText(UserDiaryActivity.this, "获取日记列表失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserDiaryActivity.this.finish();
                break;
        }
    }

    @Override
    public void layoutClick(DiaryBean diaryBean) {
        Intent intent = new Intent(UserDiaryActivity.this, DiaryDetailActivity.class);
        intent.putExtra("user_name",diaryBean.getUser_name());
        intent.putExtra("user_time",diaryBean.getPublish_time());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }

    @Override
    public void onPullRefresh() {
        listView.completeRefresh();
    }

    @Override
    public void onLoadingMore() {
        skip += 10;
        if (skip >= maxLength){
            isLoadEndFlag = true;
        }
        if (!isLoadEndFlag) {
            loading.setVisibility(View.VISIBLE);
            query.setLimit(10)
                    .setSkip(skip)
                    .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                    .order("-publish_time")
                    .findObjects(new FindListener<DiaryBean>() {
                        @Override
                        public void done(List<DiaryBean> list, BmobException e) {
                            if (e == null) {
                                if (list != null) {
                                    diaryList.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    loading.setVisibility(View.GONE);
                                    listView.completeRefresh();
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                listView.completeRefresh();
                                Toast.makeText(UserDiaryActivity.this, "加载更多日记列表失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(UserDiaryActivity.this, "数据加载完成", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.GONE);
            listView.completeRefresh();
        }
    }
}
