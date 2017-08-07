package com.treasure.traveldiary.activity.traveller;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.HomeFragmentPagerAdapter;
import com.treasure.traveldiary.fragments.BaseFragment;
import com.treasure.traveldiary.fragments.DiaryAllFragment;
import com.treasure.traveldiary.fragments.DiaryImageFragment;
import com.treasure.traveldiary.fragments.DiaryTextFragment;
import com.treasure.traveldiary.fragments.DiaryVideoFragment;
import com.treasure.traveldiary.fragments.EvaluatedFragment;
import com.treasure.traveldiary.fragments.SmallGameFragment;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class DiaryCenterActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_center);
        initTitle();
        Tools.setTranslucentStatus(this);
        title.setText("驴友记");

        initFindId();
        tabLayout.addOnTabSelectedListener(this);
        initTabLayout();
        initViewPager();
        //联动
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0, false);
    }
    private void initFindId() {
        tabLayout = (TabLayout) findViewById(R.id.diary_center_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.diary_center_viewPager);
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setText("文字"));
        tabLayout.addTab(tabLayout.newTab().setText("图片"));
        tabLayout.addTab(tabLayout.newTab().setText("短视频"));
        tabLayout.addTab(tabLayout.newTab().setText("点评/吐槽"));
        tabLayout.addTab(tabLayout.newTab().setText("小游戏"));
    }

    private void initViewPager() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new DiaryAllFragment());
        list.add(new DiaryTextFragment());
        list.add(new DiaryImageFragment());
        list.add(new DiaryVideoFragment());
        list.add(new EvaluatedFragment());
        list.add(new SmallGameFragment());
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
}
