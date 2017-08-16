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
    private LinearLayout nodata_layout;
    private TextView nodata_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluated, container, false);
        isPrepare = true;
        iniFindId(view);
        isLoadEndFlag = false;
        isPageDestroy = false;
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
        nodata_layout = (LinearLayout) view.findViewById(R.id.no_data_layout);
        nodata_text = (TextView) view.findViewById(R.id.evaluated_list_nodata_text);
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
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
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
                                .addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                                .findObjects(new FindListener<EvaluatedBean>() {
                                    @Override
                                    public void done(List<EvaluatedBean> list2, BmobException e) {
                                        if (e == null) {
                                            evaluated_list.clear();
                                            evaluated_list.addAll(list2);
                                            adapter.notifyDataSetChanged();
                                            if (!isPageDestroy) {
                                                loading.setVisibility(View.GONE);
                                                nodata_text.setVisibility(View.GONE);
                                                if (list2.size() == 0) {
                                                    nodata_layout.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        } else {
                                            if (!isPageDestroy) {
                                                loading.setVisibility(View.GONE);
                                                nodata_text.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
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
        if (skip >= maxLength) {
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
                                if (!isPageDestroy) {
                                    Toast.makeText(getContext(), "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {

    }

    @Override
    public void ListClick(EvaluatedBean evaluatedBean) {
        Intent intent = new Intent(getContext(), EvaluatedDetailActivity.class);
        intent.putExtra("user_name", evaluatedBean.getUser_name());
        intent.putExtra("user_time", evaluatedBean.getPublish_time());
        startActivity(intent);
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
