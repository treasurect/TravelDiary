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
import com.treasure.traveldiary.activity.diary_center.DiaryDetailActivity;
import com.treasure.traveldiary.adapter.TravellerDiaryListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class MineDiaryImageFragment extends BaseFragment implements TravellerDiaryListAdapter.DiaryTextClick, View.OnClickListener, CustomRefreshListView.OnRefreshListener {
    private CustomRefreshListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPrepared;
    private int maxLength;
    private BmobQuery<DiaryBean> query = new BmobQuery<>();
    private int skip = 0;
    private boolean isLoadEndFlag;
    private SharedPreferences mPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_image, container, false);
        isPrepared = true;
        initFindId(view);
        isLoadEndFlag = false;
        skip = 0;
        mPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        initListView();
        initClick();
        getDiaryList();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;

    }

    private void initFindId(View view) {
        listView = (CustomRefreshListView) view.findViewById(R.id.diary_list_image);
        loading = (FrameLayout) view.findViewById(R.id.loading_layout);
    }

    private void initListView() {
        diaryList = new ArrayList<>();
        adapter = new TravellerDiaryListAdapter(getContext(), diaryList);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
    }

    private void initClick() {
        adapter.setDiaryTextClick(this);
        loading.setOnClickListener(this);
    }

    private void getDiaryList() {
        loading.setVisibility(View.VISIBLE);
        query.addWhereEqualTo("diary_type", 1)
                .addWhereEqualTo("user_name",mPreferences.getString("user_name",""));
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
                        .addWhereEqualTo("user_name",mPreferences.getString("user_name",""))
                        .setSkip(skip)
                        .order("-publish_time").addWhereEqualTo("diary_type", 1).findObjects(new FindListener<DiaryBean>() {
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
                            Toast.makeText(getContext(), "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void textClick(DiaryBean diaryBean) {
        Intent intent = new Intent(getContext(), DiaryDetailActivity.class);
        intent.putExtra("user_name",diaryBean.getUser_name());
        intent.putExtra("user_time",diaryBean.getPublish_time());
        startActivity(intent);
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
                    .addWhereEqualTo("user_name",mPreferences.getString("user_name",""))
                    .order("-publish_time")
                    .addWhereEqualTo("diary_type",1)
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
}
