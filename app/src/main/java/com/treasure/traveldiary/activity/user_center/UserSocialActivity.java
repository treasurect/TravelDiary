package com.treasure.traveldiary.activity.user_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.find_center.TravellerDetailActivity;
import com.treasure.traveldiary.adapter.DiaryLeavemesListAdapter;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserSocialActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener, AdapterView.OnItemClickListener {
    private TabLayout tabLayout;
    private ListView listView;
    private List<SUserBean> list;
    private DiaryLeavemesListAdapter adapter;
    private SharedPreferences preferences;
    private boolean isPageDestroy;
    private FrameLayout loading;
    private LinearLayout nodata_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_social);
        initTitle();
        Tools.setTranslucentStatus(this);
        title.setText("社交");
        btn_back.setVisibility(View.VISIBLE);
        preferences = getSharedPreferences("user",MODE_PRIVATE);

        initFindId();
        tabLayout.addOnTabSelectedListener(this);
        initTabLayout();
        initListView();
        initClick();
    }

    private void initFindId() {
        tabLayout = (TabLayout) findViewById(R.id.user_social_tabLayout);
        listView = (ListView) findViewById(R.id.user_social_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        nodata_layout = (LinearLayout) findViewById(R.id.no_data_layout);
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("关注"));
        tabLayout.addTab(tabLayout.newTab().setText("粉丝"));
    }

    private void initListView() {
        list = new ArrayList<>();
        adapter = new DiaryLeavemesListAdapter(this,list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (position == 0){
            getAttentionList();
        }else {
            getFansList();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserSocialActivity.this.finish();
                break;
        }
    }
    private void getAttentionList(){
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
                                    List<SUserBean> attention = list2.get(0).getAttention();
                                    list.clear();
                                    attention.remove(0);
                                    list.addAll(attention);
                                    adapter.notifyDataSetChanged();
                                    if (attention.size() == 0){
                                        nodata_layout.setVisibility(View.VISIBLE);
                                    }else {
                                        nodata_layout.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }else {
                            if (!isPageDestroy){
                                loading.setVisibility(View.GONE);
                                Toast.makeText(UserSocialActivity.this, "获取关注列表失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void getFansList(){
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
                            }
                        }else {
                            if (!isPageDestroy){
                                loading.setVisibility(View.GONE);
                                Toast.makeText(UserSocialActivity.this, "获取关注列表失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String leave_name = list.get(i).getLeave_name();
        Intent intent = new Intent(UserSocialActivity.this, TravellerDetailActivity.class);
        intent.putExtra("user_name",leave_name);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }
}