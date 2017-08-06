package com.treasure.traveldiary.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.traveller.TravellerDiaryDetailActivity;
import com.treasure.traveldiary.adapter.TravellerDiaryListAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserDiaryActivity extends BaseActivity implements View.OnClickListener, TravellerDiaryListAdapter.DiaryTextClick {
    private ListView listView;
    private List<DiaryBean> diaryList;
    private TravellerDiaryListAdapter adapter;
    private FrameLayout loading;
    private boolean isPageDestroy;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_diary);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("我的日记");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        initListView();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDiaryList();
    }

    private void initFindId() {
        listView = (ListView) findViewById(R.id.traveller_diary_list);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
    }

    private void initListView() {
        diaryList = new ArrayList<>();
        adapter = new TravellerDiaryListAdapter(this, diaryList);
        listView.setAdapter(adapter);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        adapter.setDiaryTextClick(this);
        loading.setOnClickListener(this);
    }

    private void getDiaryList() {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<DiaryBean> query = new BmobQuery<>();
            query.addWhereEqualTo("user_name",mPreferences.getString("user_name",""));
        query.findObjects(new FindListener<DiaryBean>() {
            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (e == null) {
                    if (list != null) {
                        if (!isPageDestroy){
                            loading.setVisibility(View.GONE);
                        }
                        diaryList.clear();
                        Collections.reverse(list);
                        diaryList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (!isPageDestroy){
                        loading.setVisibility(View.GONE);
                    }
                    Toast.makeText(UserDiaryActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserDiaryActivity.this.finish();
                break;
        }
    }

    @Override
    public void textClick(DiaryBean diaryBean) {
        if (diaryBean.getDiary_type() == 0){
            Intent intent = new Intent(UserDiaryActivity.this, TravellerDiaryDetailActivity.class);
            intent.putExtra("diaryBean",diaryBean);
            intent.putExtra("type","text");
            startActivity(intent);
        }else if (diaryBean.getDiary_type() == 1){
            Intent intent = new Intent(UserDiaryActivity.this, TravellerDiaryDetailActivity.class);
            intent.putExtra("diaryBean",diaryBean);
            intent.putExtra("type","image");
            startActivity(intent);
        }else if (diaryBean.getDiary_type() == 2){
            Intent intent = new Intent(UserDiaryActivity.this, TravellerDiaryDetailActivity.class);
            intent.putExtra("diaryBean",diaryBean);
            intent.putExtra("type","video");
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }
}
