package com.treasure.traveldiary.activity.user_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.EvaluatedDetailActivity;
import com.treasure.traveldiary.adapter.TravellerEvaluatedListAdapter;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class UserEvaluatedListActivity extends BaseActivity implements View.OnClickListener, CustomRefreshListView.OnRefreshListener, TravellerEvaluatedListAdapter.EvaluatedListClick {

    private CustomRefreshListView listView;
    private List<EvaluatedBean> evaluated_list;
    private TravellerEvaluatedListAdapter adapter;
    private SharedPreferences mPreferences;
    private FrameLayout loading;
    private BmobQuery<EvaluatedBean> query = new BmobQuery<>();
    private int skip = 0;
    private boolean isLoadEndFlag;
    private int maxLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_evaluated_list);
        initTitle();
        Tools.setTranslucentStatus(this);
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        title.setText("我的点评/吐槽");
        btn_back.setVisibility(View.VISIBLE);
        isLoadEndFlag = false;
        skip = 0;
        iniFindId();
        initListView();
        initClick();
        getEvaluatedList();
    }

    private void iniFindId() {
        listView = (CustomRefreshListView) findViewById(R.id.user_evaluated_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
    }

    private void initListView() {
        evaluated_list = new ArrayList<>();
        adapter = new TravellerEvaluatedListAdapter(this, evaluated_list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        loading.setOnClickListener(this);
        adapter.setEvaluatedListClick(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserEvaluatedListActivity.this.finish();
                break;
        }
    }

    private void getEvaluatedList() {
        loading.setVisibility(View.VISIBLE);
        query.count(EvaluatedBean.class, new CountListener() {
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
                        .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                        .order("-publish_time").findObjects(new FindListener<EvaluatedBean>() {
                    @Override
                    public void done(List<EvaluatedBean> list2, BmobException e) {
                        if (e == null) {
                            if (list2 != null) {
                                loading.setVisibility(View.GONE);
                                evaluated_list.clear();
                                evaluated_list.addAll(list2);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(UserEvaluatedListActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
                    .order("-publish_time")
                    .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                    .findObjects(new FindListener<EvaluatedBean>() {
                        @Override
                        public void done(List<EvaluatedBean> list, BmobException e) {
                            if (e == null) {
                                if (list != null) {
                                    evaluated_list.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    loading.setVisibility(View.GONE);
                                    listView.completeRefresh();
                                }
                            } else {
                                loading.setVisibility(View.GONE);
                                listView.completeRefresh();
                                Toast.makeText(UserEvaluatedListActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(UserEvaluatedListActivity.this, "数据加载完成", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.GONE);
            listView.completeRefresh();
        }
    }

    @Override
    public void ListClick(EvaluatedBean evaluatedBean) {
        Intent intent = new Intent(UserEvaluatedListActivity.this, EvaluatedDetailActivity.class);
        intent.putExtra("user_name",evaluatedBean.getUser_name());
        intent.putExtra("user_time",evaluatedBean.getPublish_time());
        startActivity(intent);
    }
}
