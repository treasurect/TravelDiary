package com.treasure.traveldiary.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.user.UserEvaluatedListActivity;
import com.treasure.traveldiary.adapter.TravellerEvaluatedListAdapter;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

import static android.R.id.list;

public class EvaluatedFragment extends BaseFragment implements CustomRefreshListView.OnRefreshListener {
    private boolean isPrepare;
    private CustomRefreshListView listView;
    private List<EvaluatedBean>list;
    private TravellerEvaluatedListAdapter adapter;
    private FrameLayout loading;
    private BmobQuery<EvaluatedBean> query = new BmobQuery<>();
    private int maxLendth = 0;
    private int isLoadEndFlag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluated, container, false);
        isPrepare = true;
        iniFindId(view);
        isLoadEndFlag = 0;
        initListView();
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
        list = new ArrayList<>();
        adapter = new TravellerEvaluatedListAdapter(getContext(),list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
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
