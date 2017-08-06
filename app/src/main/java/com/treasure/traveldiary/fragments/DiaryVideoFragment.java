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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DiaryVideoFragment extends BaseFragment implements TravellerDiaryListAdapter.DiaryTextClick, View.OnClickListener {
    private ListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_video, container, false);
        isPrepared = true;
        initFindId(view);
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;
        loading.setVisibility(View.VISIBLE);
        initListView();
        initClick();
        getDiaryList();
    }

    private void initFindId(View view) {
        listView = (ListView) view.findViewById(R.id.diary_list_video);
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
    }

    private void getDiaryList() {
        BmobQuery<DiaryBean> query = new BmobQuery<>();
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
}
