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

public class MineDiaryAllFragment extends BaseFragment implements TravellerDiaryListAdapter.DiaryTextClick, View.OnClickListener, CustomRefreshListView.OnRefreshListener {
    private com.treasure.traveldiary.widget.CustomRefreshListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPrepared;
    private int skip = 0;
    private boolean isLoadEndFlag;
    private int maxLength;
    private SharedPreferences mPreferences;
    private BmobQuery<DiaryBean> query = new BmobQuery<>();
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
    private boolean isClickLike;
    private LinearLayout nodata_layout;
    private boolean isPageDestroy;
    private TextView nodata_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_all, container, false);
        isPrepared = true;
        isLoadEndFlag = false;
        isPageDestroy = false;
        skip = 0;
        mPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
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
        listView = (CustomRefreshListView) view.findViewById(R.id.diary_list_all);
        loading = (FrameLayout) view.findViewById(R.id.loading_layout);
        nodata_layout = (LinearLayout) view.findViewById(R.id.no_data_layout);
        nodata_text = (TextView) view.findViewById(R.id.diary_list_all_nodata_text);
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

    private void getDiaryList(final int type) {
            loading.setVisibility(View.VISIBLE);
            query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                    .count(DiaryBean.class, new CountListener() {
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
                                    .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                                    .setSkip(skip)
                                    .order("-publish_time")
                                    .findObjects(new FindListener<DiaryBean>() {
                                        @Override
                                        public void done(List<DiaryBean> list, BmobException e) {
                                            if (e == null) {

                                                diaryList.clear();
                                                diaryList.addAll(list);
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
                                                    Toast.makeText(getContext(), "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void likeClick(final DiaryBean diaryBean) {
        BmobQuery<DiaryBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user_name", diaryBean.getUser_name());
        BmobQuery<DiaryBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("publish_time", diaryBean.getPublish_time());
        List<BmobQuery<DiaryBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<DiaryBean> bmobQuery = new BmobQuery<>();
        bmobQuery.and(queries);
        bmobQuery.findObjects(new FindListener<DiaryBean>() {
            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (e == null) {
                    String objectId = list.get(0).getObjectId();
                    toUpdateLikeBean(objectId, diaryBean.getLikeBean());
                }
            }
        });
    }

    private void toUpdateLikeBean(String objectId, List<String> likeBean) {
        for (int i = 0; i < likeBean.size(); i++) {
            if (likeBean.get(i).equals(mPreferences.getString("user_name", ""))) {
                isClickLike = true;
            }
        }
        if (isClickLike) {
            if (!isPageDestroy){
                Toast.makeText(getContext(), "您已经点过赞了", Toast.LENGTH_SHORT).show();
            }
        } else {
            DiaryBean diaryBean = new DiaryBean();
            List<String> strings = new ArrayList<>();
            strings.addAll(likeBean);
            strings.add(mPreferences.getString("user_name", ""));
            diaryBean.setLikeBean(strings);
            diaryBean.update(objectId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        if (!isPageDestroy){
                            Toast.makeText(getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!isPageDestroy){
                            Toast.makeText(getActivity(), "点赞失败\n原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
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
                    .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                    .setSkip(skip)
                    .order("-publish_time")
                    .findObjects(new FindListener<DiaryBean>() {
                        @Override
                        public void done(List<DiaryBean> list, BmobException e) {
                            if (e == null) {
                                if (!isPageDestroy) {
                                    diaryList.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    loading.setVisibility(View.GONE);
                                    listView.completeRefresh();
                                }
                            } else {
                                if (!isPageDestroy){
                                    loading.setVisibility(View.GONE);
                                    listView.completeRefresh();
                                    Toast.makeText(getContext(), "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
