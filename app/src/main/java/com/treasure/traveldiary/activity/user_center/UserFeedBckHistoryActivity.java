package com.treasure.traveldiary.activity.user_center;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.FeedBackBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserFeedBckHistoryActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private List<String> list;
    private ArrayAdapter adapter;
    private FrameLayout loading;
    private LinearLayout nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_bck_history);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("我的历史反馈");

        initFindId();
        initListView();
        initClick();
        getFeedBackList();
    }

    private void initFindId() {
        listView = (ListView) findViewById(R.id.mine_history_feedBack_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        nodata = (LinearLayout) findViewById(R.id.no_data_layout);
    }

    private void initListView() {
        list = new ArrayList<>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserFeedBckHistoryActivity.this.finish();
                break;
        }
    }

    private void getFeedBackList() {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<FeedBackBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name",getSharedPreferences("user",MODE_PRIVATE).getString("user_name",""))
        .findObjects(new FindListener<FeedBackBean>() {
            @Override
            public void done(List<FeedBackBean> list1, BmobException e) {
                if (e == null){
                        for (int i = 0; i < list1.size(); i++) {
                            list.add(list1.get(i).getUser_content());
                        }
                        adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    if (list1.size() == 0){
                        nodata.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(UserFeedBckHistoryActivity.this, "获取反馈列表失败", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}
