package com.treasure.traveldiary.activity.user;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.TravellerEvaluatedListAdapter;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class UserEvaluatedListActivity extends BaseActivity implements View.OnClickListener, CustomRefreshListView.OnRefreshListener {

    private CustomRefreshListView listView;
    private List<EvaluatedBean> list;
    private TravellerEvaluatedListAdapter adapter;
    private SharedPreferences mPreferences;
    private FrameLayout loading;
    private BmobQuery<EvaluatedBean> query = new BmobQuery<>();
    private int maxLendth = 0;
    private int isLoadEndFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_evaluated_list);
        initTitle();
        Tools.setTranslucentStatus(this);
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        title.setText("我的点评/吐槽");
        btn_back.setVisibility(View.VISIBLE);

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
        list = new ArrayList<>();
        adapter = new TravellerEvaluatedListAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
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
                    maxLendth = integer.intValue();
                }catch (Exception error){

                }
                query.setLimit(10);
                maxLendth = maxLendth - 10;
                if (maxLendth < 10){
                    query.setSkip(0);
                }else {
                    query.setSkip(maxLendth);
                }
                query.findObjects(new FindListener<EvaluatedBean>() {
                    @Override
                    public void done(List<EvaluatedBean> list2, BmobException e) {
                        if (e == null) {
                            if (list2 != null) {
                                loading.setVisibility(View.GONE);
                                list.clear();
                                Collections.reverse(list2);
                                list.addAll(list2);
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
        query.setLimit(10);
        maxLendth = maxLendth - 10;
        if (maxLendth <0){
            query.setSkip(0);
            isLoadEndFlag ++;
        }else {
            query.setSkip(maxLendth);
        }
        loading.setVisibility(View.VISIBLE);
        query.findObjects(new FindListener<EvaluatedBean>() {
            @Override
            public void done(List<EvaluatedBean> list2, BmobException e) {
                if (isLoadEndFlag < 2){
                    if (e == null) {
                        if (list2 != null) {
                            Collections.reverse(list2);
                            list.addAll(list2);
                            adapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);
                            listView.completeRefresh();
                        }
                    } else {
                        loading.setVisibility(View.GONE);
                        listView.completeRefresh();
                        Toast.makeText(UserEvaluatedListActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserEvaluatedListActivity.this, "数据加载完成", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    listView.completeRefresh();
                }
            }
        });
    }
}
