package com.treasure.traveldiary.activity.main_center;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.HomeFragmentPagerAdapter;
import com.treasure.traveldiary.fragments.BaseFragment;
import com.treasure.traveldiary.fragments.ToolsTicketAirFragment;
import com.treasure.traveldiary.fragments.ToolsTicketTrainFragment;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class ToolsTicketActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_ticket);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("票务");

        initFindId();
        initClick();
        tabLayout.addOnTabSelectedListener(this);
        initTabLayout();
        initViewPager();
        //联动
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0, false);
    }

    private void initFindId() {
        tabLayout = (TabLayout) findViewById(R.id.tools_ticket_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.tools_ticket_viewPager);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                ToolsTicketActivity.this.finish();
                break;
        }
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("火车票"));
        tabLayout.addTab(tabLayout.newTab().setText("机票"));
    }

    private void initViewPager() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new ToolsTicketTrainFragment());
        list.add(new ToolsTicketAirFragment());
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
