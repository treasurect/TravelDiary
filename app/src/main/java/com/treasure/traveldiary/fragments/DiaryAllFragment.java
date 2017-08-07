package com.treasure.traveldiary.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import cn.bmob.v3.listener.LogInListener;

public class DiaryAllFragment extends BaseFragment implements TravellerDiaryListAdapter.DiaryTextClick, View.OnClickListener, CustomRefreshListView.OnRefreshListener {
    private com.treasure.traveldiary.widget.CustomRefreshListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPrepared;
    private int maxLendth = 0;
    private int isLoadEndFlag = 0;
    private BmobQuery<DiaryBean> query = new BmobQuery<>();
    private Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    loading.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_all, container, false);
        isPrepared = true;
        isLoadEndFlag = 0;
        initFindId(view);
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

    @Override
    public void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    handler.sendMessage(handler.obtainMessage(200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onResume();
    }

    private void initFindId(View view) {
        listView = (com.treasure.traveldiary.widget.CustomRefreshListView) view.findViewById(R.id.diary_list_all);
        loading = (FrameLayout) view.findViewById(R.id.loading_layout);
    }

    private void initListView() {
        diaryList = new ArrayList<>();
        adapter = new TravellerDiaryListAdapter(getContext(), diaryList);
        listView.setAdapter(adapter);
    }

    private void initClick() {
        adapter.setDiaryTextClick(this);
        loading.setOnClickListener(this);
        listView.setOnRefreshListener(this);
    }

    private void getDiaryList() {
        loading.setVisibility(View.VISIBLE);
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
