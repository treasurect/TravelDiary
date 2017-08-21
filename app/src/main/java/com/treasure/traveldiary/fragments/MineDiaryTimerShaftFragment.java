package com.treasure.traveldiary.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.TravellerTimerShaftListAdapter;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.widget.CustomRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MineDiaryTimerShaftFragment extends BaseFragment {

    private ListView listView;
    private List<String> list;
    private TravellerTimerShaftListAdapter adapter;
    private SharedPreferences mPreferences;
    private FrameLayout loading;
    private boolean isPageDestroy;
    private LinearLayout nodata_layout;
    private TextView nodata_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_mine_diary_timer_shaft, container, false);
        isPageDestroy = false;
        mPreferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        initFindId(view);
        initListView();
        getTimerShaftList();
        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    private void initFindId(View view) {
        listView = ((ListView) view.findViewById(R.id.mine_diary_timer_shaft_listView));
        loading = ((FrameLayout) view.findViewById(R.id.loading_layout));
        nodata_layout = (LinearLayout) view.findViewById(R.id.no_data_layout);
        nodata_text = (TextView) view.findViewById(R.id.timer_shaft_nodata_text);
    }

    private void initListView(){
        list = new ArrayList<>();
        adapter = new TravellerTimerShaftListAdapter(list,getContext());
        listView.setAdapter(adapter);
    }
    private void getTimerShaftList() {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name",mPreferences.getString("user_name",""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list2, BmobException e) {
                        if (e == null){
                            list.clear();
                            List<String> timer_shaft = list2.get(0).getTimer_shaft();
                            Collections.reverse(timer_shaft);
                            list.addAll(timer_shaft);
                            adapter.notifyDataSetChanged();
                            if (!isPageDestroy){
                                loading.setVisibility(View.GONE);
                                nodata_text.setVisibility(View.GONE);
                                if (list.size() == 0) {
                                    nodata_layout.setVisibility(View.VISIBLE);
                                }
                            }
                        }else {
                            if (!isPageDestroy){
                                loading.setVisibility(View.GONE);
                                nodata_text.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "获取时间轴列表失败", Toast.LENGTH_SHORT).show();
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
}
