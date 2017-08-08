package com.treasure.traveldiary.activity.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_list);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("我的消息");

        initFindId();
        initListView();
        initClick();
        getMessageList();
    }

    private void initFindId() {
        listView = (ListView) findViewById(R.id.user_message_list);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
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
        query.findObjects(new FindListener<PushBean>() {
            @Override
            public void done(List<PushBean> list1, BmobException e) {
                if (e == null) {
                    if (list1 != null) {
                        list.clear();
                        Collections.reverse(list1);
                        list.addAll(list1);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(UserMessageListActivity.this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                loading.setVisibility(View.GONE);
            }
        });
    }
}
