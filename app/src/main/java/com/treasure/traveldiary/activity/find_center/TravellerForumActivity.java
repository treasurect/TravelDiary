package com.treasure.traveldiary.activity.find_center;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.HomeFragmentPagerAdapter;
import com.treasure.traveldiary.bean.ForumBean;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.fragments.BaseFragment;
import com.treasure.traveldiary.fragments.ForumAllFragment;
import com.treasure.traveldiary.fragments.ForumHotFragment;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class TravellerForumActivity extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener, TextWatcher {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton add_forum;
    private PopupWindow mPopupWindow;
    private EditText content;
    private TextView send;
    private SharedPreferences preferences;
    private boolean isPageDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_forum);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("用户论坛");
        preferences = getSharedPreferences("user", MODE_PRIVATE);

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
        tabLayout = (TabLayout) findViewById(R.id.traveller_forum_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.traveller_forum_viewPager);
        add_forum = (FloatingActionButton) findViewById(R.id.add_forum_btn);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        add_forum.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                TravellerForumActivity.this.finish();
                break;
            case R.id.add_forum_btn:
                Tools.setAnimation(add_forum, 0, 0, 1, 1, 0, -45, 1, 1, 500);
                showAddForumWindow();
                break;
            case R.id.leaveMes_send:
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    Tools.setAnimation(add_forum, 0, 0, 1, 1, -45, 0, 1, 1, 500);
                }
                if (send.getText().toString().trim().equals("发送")) {
                    saveForum();
                }
                break;
        }
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("热点"));
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
    }

    private void initViewPager() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new ForumHotFragment());
        list.add(new ForumAllFragment());
        HomeFragmentPagerAdapter pagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(), false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void showAddForumWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.popupwindow_leavemes_send, null);
        mPopupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.leaveMesPopupWindow);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x66000000));

        content = (EditText) convertView.findViewById(R.id.leaveMes_content);
        send = (TextView) convertView.findViewById(R.id.leaveMes_send);
        content.setHint("发表您的观点");
        send.setOnClickListener(this);
        content.addTextChangedListener(this);

        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_traveller_forum, null);
        mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (Tools.isNull(charSequence.toString().trim())) {
            send.setText("关闭");
            send.setTextColor(getResources().getColor(R.color.colorGray));
        } else {
            send.setText("发送");
            send.setTextColor(getResources().getColor(R.color.colorBlock));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void saveForum() {
        ForumBean forumBean = new ForumBean();
        forumBean.setUser_name(preferences.getString("user_name", ""));
        forumBean.setUser_nick(preferences.getString("user_nick", ""));
        forumBean.setUser_icon(preferences.getString("user_icon", ""));
        forumBean.setPublish_content(content.getText().toString().trim());
        forumBean.setPublish_from("自制");
        forumBean.setPublish_time(Tools.getNowTime());
        //加一个空占位
        List<SUserBean> sUserBeen = new ArrayList<>();
        SUserBean sUserBean = new SUserBean();
        sUserBean.setLeave_name(preferences.getString("user_name", ""));
        sUserBeen.add(sUserBean);
        forumBean.setLeaveMes_list(sUserBeen);
        forumBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (!isPageDestroy) {
                        Toast.makeText(TravellerForumActivity.this, "恭喜你，发表成功！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!isPageDestroy) {
                        Toast.makeText(TravellerForumActivity.this, "很遗憾，发表失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }
}
