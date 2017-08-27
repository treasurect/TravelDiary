package com.treasure.traveldiary.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import cn.bmob.v3.listener.UpdateListener;

public class MineDiaryTextFragment extends BaseFragment implements TravellerDiaryListAdapter.DiaryTextClick, View.OnClickListener, CustomRefreshListView.OnRefreshListener {
    private CustomRefreshListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPrepared;
    private BmobQuery<DiaryBean> query = new BmobQuery<>();
    private int skip = 0;
    private boolean isLoadEndFlag;
    private int maxLength;
    private SharedPreferences mPreferences;
    private LinearLayout nodata_layout;
    private boolean isPageDestroy;
    private Handler handler = new Handler() {
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
    private TextView nodata_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_text, container, false);
        isPrepared = true;
        initFindId(view);
        isLoadEndFlag = false;
        isPageDestroy = false;
        skip = 0;
        mPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        initListView();
        initClick();
        getDiaryList(0);
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;

    }

    private void initFindId(View view) {
        listView = (CustomRefreshListView) view.findViewById(R.id.diary_list_text);
        loading = (FrameLayout) view.findViewById(R.id.loading_layout);
        nodata_layout = (LinearLayout) view.findViewById(R.id.no_data_layout);
        nodata_text = (TextView) view.findViewById(R.id.diary_list_text_nodata_text);
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

    private void getDiaryList(final int type) {
        loading.setVisibility(View.VISIBLE);
        query.addWhereEqualTo("diary_type", "0")
                .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""));
        query.count(DiaryBean.class, new CountListener() {
            @Override
            public void done(final Integer integer, final BmobException e) {
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
                        .addWhereEqualTo("diary_type", "0")
                        .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                        .findObjects(new FindListener<DiaryBean>() {
                            @Override
                            public void done(List<DiaryBean> list, BmobException e) {
                                if (e == null) {

                                    diaryList.clear();
                                    diaryList.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    if (!isPageDestroy) {
                                        loading.setVisibility(View.GONE);
                                        nodata_text.setVisibility(View.GONE);
                                        if (list.size() == 0) {
                                            nodata_layout.setVisibility(View.VISIBLE);
                                        }
                                        if (type == 1) {
                                            listView.completeRefresh();
                                        }
                                    }
                                } else {
                                    if (!isPageDestroy) {
                                        if (type == 1) {
                                            listView.completeRefresh();
                                        }
                                        loading.setVisibility(View.GONE);
                                        nodata_text.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "获取日记列表失败", Toast.LENGTH_SHORT).show();
                                    }
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
    public void layoutClick(DiaryBean diaryBean) {
        Intent intent = new Intent(getContext(), DiaryDetailActivity.class);
        intent.putExtra("user_name", diaryBean.getUser_name());
        intent.putExtra("user_time", diaryBean.getPublish_time());
        startActivity(intent);
    }

    @Override
    public void onPullRefresh() {
        getDiaryList(1);
    }

    @Override
    public void onLoadingMore() {
        skip += 10;
        if (skip >= maxLength) {
            isLoadEndFlag = true;
        }
        if (!isLoadEndFlag) {
            loading.setVisibility(View.VISIBLE);
            query.setLimit(10)
                    .setSkip(skip)
                    .order("-publish_time")
                    .addWhereEqualTo("diary_type", "0")
                    .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
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
                                if (!isPageDestroy) {
                                    Toast.makeText(getContext(), "加载更多日记列表失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            if (!isPageDestroy) {
                Toast.makeText(getContext(), "数据加载完成", Toast.LENGTH_SHORT).show();
            }
            loading.setVisibility(View.GONE);
            listView.completeRefresh();
        }
    }

    @Override
    public void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (!isPageDestroy) {
                        handler.sendMessage(handler.obtainMessage(200));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        isPageDestroy = true;
        super.onDestroyView();
    }
}
