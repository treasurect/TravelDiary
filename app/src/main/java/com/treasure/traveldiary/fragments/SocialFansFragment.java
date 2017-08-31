package com.treasure.traveldiary.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.find_center.TravellerDetailActivity;
import com.treasure.traveldiary.activity.social_center.SocialCenterActivity;
import com.treasure.traveldiary.adapter.DiaryLeavemesListAdapter;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SocialFansFragment extends BaseFragment implements CustomRefreshListView.OnRefreshListener, DiaryLeavemesListAdapter.LayoutClick {

    private CustomRefreshListView listView;
    private List<SUserBean>list;
    private DiaryLeavemesListAdapter adapter;
    private boolean isPageDestroy;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    if (!isPageDestroy){
                        loading.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    private FrameLayout loading;
    private LinearLayout nodata_layout;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_fans, container, false);
        isPageDestroy = false;
        preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        initFindId(view);
        initListView();
        getFansList(0);
        return view;
    }

    @Override
    protected void lazyLoad() {

    }
    private void initFindId(View view){
        listView = ((CustomRefreshListView) view.findViewById(R.id.social_fans_listView));
        loading = (FrameLayout) view.findViewById(R.id.loading_layout);
        nodata_layout = (LinearLayout) view.findViewById(R.id.no_data_layout);
    }
    @Override
    public void onResume() {
        super.onResume();
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
    }

    private void initListView() {
        list = new ArrayList<>();
        adapter = new DiaryLeavemesListAdapter(getContext(),list);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        adapter.setLayoutClick(this);
    }
    private void getFansList(final int type){
        loading.setVisibility(View.VISIBLE);
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name",preferences.getString("user_name",""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list2, BmobException e) {
                        if (e == null){
                            if (!isPageDestroy){
                                loading.setVisibility(View.GONE);
                                if (list2.size() > 0){
                                    List<SUserBean> fans = list2.get(0).getFans();
                                    fans.remove(0);
                                    list.clear();
                                    list.addAll(fans);
                                    adapter.notifyDataSetChanged();
                                    if (fans.size() == 0){
                                        nodata_layout.setVisibility(View.VISIBLE);
                                    }else {
                                        nodata_layout.setVisibility(View.GONE);
                                    }
                                }
                                if (type == 1){
                                    listView.completeRefresh();
                                }
                            }
                        }else {
                            if (!isPageDestroy){
                                loading.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "获取关注列表失败", Toast.LENGTH_SHORT).show();
                                if (type == 1){
                                    listView.completeRefresh();
                                }
                            }
                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        isPageDestroy = true;
        super.onDestroyView();
    }

    @Override
    public void onPullRefresh() {
        getFansList(1);
    }

    @Override
    public void onLoadingMore() {

    }

    @Override
    public void leave_mesClick(String name) {
        Intent intent = new Intent(getContext(), TravellerDetailActivity.class);
        intent.putExtra("user_name",name);
        startActivity(intent);
    }
}

