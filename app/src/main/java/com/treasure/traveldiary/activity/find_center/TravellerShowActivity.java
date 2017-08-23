package com.treasure.traveldiary.activity.find_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.StackView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.TravellerShowAdapter;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class TravellerShowActivity extends BaseActivity implements View.OnClickListener, TravellerShowAdapter.TShowClick {

    private StackView stackView;
    private List<UserInfoBean> list;
    private TravellerShowAdapter adapter;
    private boolean isPageDestroy;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_show);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("驴友风采");
        preferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        initStackView();
        initClick();
        getUserList();
    }

    private void initFindId() {
        stackView = (StackView) findViewById(R.id.traveller_show_stackView);
    }

    private void initStackView() {
        list = new ArrayList<>();
        adapter = new TravellerShowAdapter(list, this);
        stackView.setAdapter(adapter);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        adapter.settShowClick(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                TravellerShowActivity.this.finish();
                break;
        }
    }

    private void getUserList() {
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfoBean>() {
            @Override
            public void done(List<UserInfoBean> list2, BmobException e) {
                if (e == null) {
                    list.clear();
                    list.addAll(list2);
                    adapter.notifyDataSetChanged();
                } else {
                    if (!isPageDestroy) {
                        Toast.makeText(TravellerShowActivity.this, "用户列表获取失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void detailClick(UserInfoBean infoBean, int type) {
        if (type == 0) {//+关注
            toFans(infoBean);
        } else {
            Intent intent = new Intent(TravellerShowActivity.this, TravellerDetailActivity.class);
            intent.putExtra("user_name",infoBean.getUser_name());
            startActivity(intent);
        }
    }

    private void toFans(final UserInfoBean infoBean) {
        //成为粉丝
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", infoBean.getUser_name())
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                            if (!isPageDestroy) {
                                String objectId = list.get(0).getObjectId();
                                List<SUserBean> fans = list.get(0).getFans();
                                for (int i = 0; i < fans.size(); i++) {
                                    if (fans.get(i).getLeave_name().equals(preferences.getString("user_name",""))){
                                        Toast.makeText(TravellerShowActivity.this, "已经关注过了", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                LogUtil.d("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                SUserBean SUserBean = new SUserBean();
                                SUserBean.setLeave_name(preferences.getString("user_name", ""));
                                SUserBean.setLeave_nick(preferences.getString("user_nick", ""));
                                SUserBean.setLeave_icon(preferences.getString("user_icon", ""));
                                SUserBean.setLeave_time(Tools.getNowTime());
                                SUserBean.setLeave_content(preferences.getString("user_desc", ""));
                                fans.add(SUserBean);

                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setFans(fans);
                                userInfoBean.update(objectId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            if (!isPageDestroy) {
                                                toAttention(infoBean);
                                            }
                                        } else {
                                            if (!isPageDestroy) {
                                                Toast.makeText(TravellerShowActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                        } else {
                            if (!isPageDestroy) {
                                Toast.makeText(TravellerShowActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void toAttention(final UserInfoBean infoBean) {
        //添加关注
        BmobQuery<UserInfoBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("user_name",preferences.getString("user_name",""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null){
                            if (!isPageDestroy){
                                String objectId = list.get(0).getObjectId();
                                List<SUserBean> attention = list.get(0).getAttention();
                                SUserBean SUserBean = new SUserBean();
                                SUserBean.setLeave_name(infoBean.getUser_name());
                                SUserBean.setLeave_nick(infoBean.getNick_name());
                                SUserBean.setLeave_icon(infoBean.getUser_icon());
                                SUserBean.setLeave_time(Tools.getNowTime());
                                SUserBean.setLeave_content(infoBean.getUser_desc());
                                attention.add(SUserBean);

                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setAttention(attention);
                                userInfoBean.update(objectId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            if (!isPageDestroy){
                                                Toast.makeText(TravellerShowActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            if (!isPageDestroy){
                                                Toast.makeText(TravellerShowActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                        }else {
                            if (!isPageDestroy){
                                Toast.makeText(TravellerShowActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
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
