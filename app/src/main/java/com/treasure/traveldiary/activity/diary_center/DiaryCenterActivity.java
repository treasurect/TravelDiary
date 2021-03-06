package com.treasure.traveldiary.activity.diary_center;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.HomeFragmentPagerAdapter;
import com.treasure.traveldiary.fragments.BaseFragment;
import com.treasure.traveldiary.fragments.MineDiaryAllFragment;
import com.treasure.traveldiary.fragments.MineDiaryImageFragment;
import com.treasure.traveldiary.fragments.MineDiaryTextFragment;
import com.treasure.traveldiary.fragments.MineDiaryTimerShaftFragment;
import com.treasure.traveldiary.fragments.MineDiaryVideoFragment;
import com.treasure.traveldiary.fragments.MineEvaluatedFragment;
import com.treasure.traveldiary.fragments.MineSmallGameFragment;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class DiaryCenterActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_center);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("我的日记");

        initFindId();
        tabLayout.addOnTabSelectedListener(this);
        initTabLayout();
        initViewPager();
        initClick();
        //联动
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(2, false);
    }
    private void initFindId() {
        tabLayout = (TabLayout) findViewById(R.id.diary_center_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.diary_center_viewPager);
    }
    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("小游戏"));
        tabLayout.addTab(tabLayout.newTab().setText("时间轴"));
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setText("图片"));
        tabLayout.addTab(tabLayout.newTab().setText("日记"));
        tabLayout.addTab(tabLayout.newTab().setText("短视频"));
        tabLayout.addTab(tabLayout.newTab().setText("点评/吐槽"));
    }

    private void initViewPager() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new MineSmallGameFragment());
        list.add(new MineDiaryTimerShaftFragment());
        list.add(new MineDiaryAllFragment());
        list.add(new MineDiaryImageFragment());
        list.add(new MineDiaryTextFragment());
        list.add(new MineDiaryVideoFragment());
        list.add(new MineEvaluatedFragment());
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
    private void initClick(){
        btn_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                DiaryCenterActivity.this.finish();
                break;
        }
    }
}
