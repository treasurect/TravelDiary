package com.treasure.traveldiary.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.EvaluatedDetailActivity;
import com.treasure.traveldiary.adapter.TravellerEvaluatedListAdapter;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class MineEvaluatedFragment extends BaseFragment implements CustomRefreshListView.OnRefreshListener, TravellerEvaluatedListAdapter.EvaluatedListClick, View.OnClickListener {
    private boolean isPrepare;
    private CustomRefreshListView listView;
    private List<EvaluatedBean> evaluated_list;
    private TravellerEvaluatedListAdapter adapter;
    private FrameLayout loading;
    private BmobQuery<EvaluatedBean> query = new BmobQuery<>();
    private int skip = 0;
    private boolean isLoadEndFlag;
    private int maxLength;
    private SharedPreferences mPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluated, container, false);
        isPrepare = true;
        iniFindId(view);
        isLoadEndFlag = false;
        skip = 0;
        mPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        initListView();
        initClick();
        getEvaluatedList();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepare || !isVisible)
            return;

    }

    private void iniFindId(View view) {
        listView = (CustomRefreshListView) view.findViewById(R.id.evaluated_listView);
        loading = ((FrameLayout) view.findViewById(R.id.loading_layout));
    }

    private void initListView() {
        evaluated_list = new ArrayList<>();
        adapter = new TravellerEvaluatedListAdapter(getContext(), evaluated_list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
    }

    private void initClick() {
        adapter.setEvaluatedListClick(this);
        loading.setOnClickListener(this);
    }

    private void getEvaluatedList() {
        loading.setVisibility(View.VISIBLE);
        query.addWhereEqualTo("user_name",mPreferences.getString("user_name",""))
                .count(EvaluatedBean.class, new CountListener() {
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
                        .addWhereEqualTo("user_name",mPreferences.getString("user_name",""))
                        .findObjects(new FindListener<EvaluatedBean>() {
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
                            Toast.makeText(getContext(), "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    .addWhereEqualTo("user_name",mPreferences.getString("user_name",""))
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
                                Toast.makeText(getContext(), "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "数据加载完成", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.GONE);
            listView.completeRefresh();
        }
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void ListClick(EvaluatedBean evaluatedBean) {
        Intent intent = new Intent(getContext(), EvaluatedDetailActivity.class);
        intent.putExtra("user_name",evaluatedBean.getUser_name());
        intent.putExtra("user_time",evaluatedBean.getPublish_time());
        startActivity(intent);
    }
}
