package com.treasure.traveldiary.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.traveller.TravellerDiaryDetailActivity;
import com.treasure.traveldiary.adapter.TravellerDiaryListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class DiaryVideoFragment extends BaseFragment implements TravellerDiaryListAdapter.DiaryTextClick, View.OnClickListener, CustomRefreshListView.OnRefreshListener {
    private CustomRefreshListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPrepared;
    private BmobQuery<DiaryBean> query = new BmobQuery<>();
    private int maxLendth = 0;
    private int isLoadEndFlag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_video, container, false);
        isPrepared = true;
        initFindId(view);
        isLoadEndFlag = 0;
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
        listView = (CustomRefreshListView) view.findViewById(R.id.diary_list_video);
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
        query.addWhereEqualTo("diary_type",2);
        query.count(DiaryBean.class, new CountListener() {
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
                query.addWhereEqualTo("diary_type",2);
                query.findObjects(new FindListener<DiaryBean>() {
                    @Override
                    public void done(List<DiaryBean> list, BmobException e) {
                        if (e == null) {
                            if (list != null) {
                                loading.setVisibility(View.GONE);
                                diaryList.clear();
                                Collections.reverse(list);
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
        if (diaryBean.getDiary_type() == 0) {
            Intent intent = new Intent(getContext(), TravellerDiaryDetailActivity.class);
            intent.putExtra("diaryBean", diaryBean);
            intent.putExtra("type", "text");
            startActivity(intent);
        } else if (diaryBean.getDiary_type() == 1) {
            Intent intent = new Intent(getContext(), TravellerDiaryDetailActivity.class);
            intent.putExtra("diaryBean", diaryBean);
            intent.putExtra("type", "image");
            startActivity(intent);
        } else if (diaryBean.getDiary_type() == 2) {
            Intent intent = new Intent(getContext(), TravellerDiaryDetailActivity.class);
            intent.putExtra("diaryBean", diaryBean);
            intent.putExtra("type", "video");
            startActivity(intent);
        }
    }

    @Override
    public void onPullRefresh() {
        listView.completeRefresh();
    }

    @Override
    public void onLoadingMore() {
        LogUtil.e("~~~~~~~~~~~~~~onLoadingMore~~~~");
        query.setLimit(10);
        maxLendth = maxLendth - 10;
        if (maxLendth <0){
            query.setSkip(0);
            isLoadEndFlag ++;
        }else {
            query.setSkip(maxLendth);
        }
        loading.setVisibility(View.VISIBLE);
        query.addWhereEqualTo("diary_type",2);
        query.findObjects(new FindListener<DiaryBean>() {
            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (isLoadEndFlag < 2){
                    if (e == null) {
                        if (list != null) {
                            Collections.reverse(list);
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
                }else {
                    Toast.makeText(getContext(), "数据加载完成", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    listView.completeRefresh();
                }
            }
        });
    }
}
