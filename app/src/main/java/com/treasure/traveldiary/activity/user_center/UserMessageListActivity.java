package com.treasure.traveldiary.activity.user_center;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.MineMessageListAdapter;
import com.treasure.traveldiary.bean.PushBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserMessageListActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private List<PushBean> list;
    private MineMessageListAdapter adapter;
    private FrameLayout loading;
    private SharedPreferences mPreferences;
    private LinearLayout nodata_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_list);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("我的消息");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        initListView();
        initClick();
        getMessageList();
    }

    private void initFindId() {
        listView = (ListView) findViewById(R.id.user_message_list);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        nodata_layout = (LinearLayout) findViewById(R.id.no_data_layout);
    }

    private void initListView() {
        list = new ArrayList<>();
        adapter = new MineMessageListAdapter(this, list);
        listView.setAdapter(adapter);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserMessageListActivity.this.finish();
                break;
        }
    }

    private void getMessageList() {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<PushBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                .findObjects(new FindListener<PushBean>() {
                    @Override
                    public void done(List<PushBean> list1, BmobException e) {
                        if (e == null) {
                            list.clear();
                            Collections.reverse(list1);
                            list.addAll(list1);
                            adapter.notifyDataSetChanged();
                            if (list1.size() == 0){
                                nodata_layout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(UserMessageListActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        loading.setVisibility(View.GONE);
                    }
                });
    }
}
