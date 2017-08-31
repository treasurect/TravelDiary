package com.treasure.traveldiary.activity.social_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.find_center.TravellerDetailActivity;
import com.treasure.traveldiary.adapter.DiaryLeavemesListAdapter;
import com.treasure.traveldiary.adapter.HomeFragmentPagerAdapter;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.fragments.BaseFragment;
import com.treasure.traveldiary.fragments.SocialAttentionFragment;
import com.treasure.traveldiary.fragments.SocialFansFragment;
import com.treasure.traveldiary.fragments.SocialLeavemesFragment;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class SocialCenterActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_center);
        initTitle();
        Tools.setTranslucentStatus(this);
        title.setText("社交");
        btn_back.setVisibility(View.VISIBLE);

        initFindId();
        tabLayout.addOnTabSelectedListener(this);
        initTabLayout();
        initViewPager();
        initClick();
        //联动
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0, false);
    }

    private void initFindId() {
        tabLayout = (TabLayout) findViewById(R.id.social_center_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.social_center_viewPager);
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("关注"));
        tabLayout.addTab(tabLayout.newTab().setText("粉丝"));
        tabLayout.addTab(tabLayout.newTab().setText("留言"));
    }
    private void initViewPager() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new SocialAttentionFragment());
        list.add(new SocialFansFragment());
        list.add(new SocialLeavemesFragment());
        HomeFragmentPagerAdapter pagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(),false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                SocialCenterActivity.this.finish();
                break;
        }
    }
}