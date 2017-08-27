package com.treasure.traveldiary.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.DiaryDetailActivity;
import com.treasure.traveldiary.adapter.TravellerForumListAdapter;
import com.treasure.traveldiary.bean.ForumBean;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class ForumHotFragment extends BaseFragment implements TravellerForumListAdapter.ForumClick, View.OnClickListener, CustomRefreshListView.OnRefreshListener {
    private CustomRefreshListView listView;
    private List<ForumBean> forumList;
    private TravellerForumListAdapter adapter;
    private FrameLayout loading;
    private boolean isPrepared;
    private int skip = 0;
    private boolean isLoadEndFlag;
    private int maxLength;
    private BmobQuery<ForumBean> query = new BmobQuery<>();
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
    private LinearLayout nodata_layout;
    private boolean isPageDestroy;
    private TextView nodata_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_hot, container, false);
        isPrepared = true;
        isLoadEndFlag = false;
        isPageDestroy = false;
        skip = 0;
        initFindId(view);
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

    @Override
    public void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (!isPageDestroy){
                        handler.sendMessage(handler.obtainMessage(200));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onResume();
    }

    private void initFindId(View view) {
        listView = (CustomRefreshListView) view.findViewById(R.id.forum_list_hot_listView);
        loading = (FrameLayout) view.findViewById(R.id.loading_layout);
        nodata_layout = (LinearLayout) view.findViewById(R.id.no_data_layout);
        nodata_text = (TextView) view.findViewById(R.id.forum_list_hot_nodata_text);
    }

    private void initListView() {
        forumList = new ArrayList<>();
        adapter = new TravellerForumListAdapter(getContext(), forumList);
        listView.setAdapter(adapter);
    }

    private void initClick() {
        adapter.setForumClick(this);
        loading.setOnClickListener(this);
        listView.setOnRefreshListener(this);
    }

    private void getDiaryList(final int type) {
        loading.setVisibility(View.VISIBLE);
        query.count(ForumBean.class, new CountListener() {
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
                        .findObjects(new FindListener<ForumBean>() {
                            @Override
                            public void done(List<ForumBean> list, BmobException e) {
                                if (e == null) {
                                    forumList.clear();
                                    forumList.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    if (!isPageDestroy){
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
                                    if (!isPageDestroy){
                                        if (type == 1) {
                                            listView.completeRefresh();
                                        }
                                        nodata_text.setVisibility(View.GONE);
                                        loading.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "获取论坛列表失败", Toast.LENGTH_SHORT).show();
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
    public void layoutClick(ForumBean forumBean) {
        Intent intent = new Intent(getContext(), DiaryDetailActivity.class);
        intent.putExtra("user_name", forumBean.getUser_name());
        intent.putExtra("user_time", forumBean.getPublish_time());
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
                    .findObjects(new FindListener<ForumBean>() {
                        @Override
                        public void done(List<ForumBean> list, BmobException e) {
                            if (e == null) {
                                if (!isPageDestroy) {
                                    forumList.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    loading.setVisibility(View.GONE);
                                    listView.completeRefresh();
                                }
                            } else {
                                if (!isPageDestroy){
                                    loading.setVisibility(View.GONE);
                                    listView.completeRefresh();
                                    Toast.makeText(getContext(), "加载更多论坛列表失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            if (!isPageDestroy){
                Toast.makeText(getContext(), "数据加载完成", Toast.LENGTH_SHORT).show();
            }
            loading.setVisibility(View.GONE);
            listView.completeRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        isPageDestroy = true;
        super.onDestroyView();
    }
}
