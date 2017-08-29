package com.treasure.traveldiary.activity.main_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.find_center.TravellerDetailActivity;
import com.treasure.traveldiary.activity.find_center.TravellerShowActivity;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class TravellerSearchActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView btnSearch;
    private FrameLayout layoutSearch;
    private boolean isPageDestroy;
    private PopupWindow popupWindow;
    private UserInfoBean infoBean;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_search);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        edit_search.setVisibility(View.VISIBLE);
        preferences = getSharedPreferences("user",MODE_PRIVATE);

        initFindId();
        initClick();
    }

    private void initFindId() {
        btnSearch = (TextView) findViewById(R.id.traveller_search_btn);
        layoutSearch = (FrameLayout) findViewById(R.id.traveller_search_btn_layout);
        layoutSearch.setVisibility(View.GONE);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        btn_search_cancel.setOnClickListener(this);
        layoutSearch.setOnClickListener(this);
        edit_search.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                TravellerSearchActivity.this.finish();
                break;
            case R.id.btn_search_cancel:
                edit_search.setText("");
                break;
            case R.id.traveller_search_btn_layout:
                if (!Tools.isNull(edit_search.getText().toString().trim())) {
                    toSearch(edit_search.getText().toString().trim());
                }
                break;
            case R.id.popup_user_intro_close:
                if (popupWindow != null && popupWindow.isShowing())
                    popupWindow.dismiss();
                break;
            case R.id.popup_user_intro_attention:
                toFans(infoBean);
                break;
            case R.id.popup_user_intro_detail:
                if (popupWindow != null && popupWindow.isShowing())
                    popupWindow.dismiss();
                Intent intent = new Intent(TravellerSearchActivity.this, TravellerDetailActivity.class);
                intent.putExtra("user_name",infoBean.getUser_name());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (Tools.isNull(charSequence.toString().trim())) {
            layoutSearch.setVisibility(View.GONE);
            btn_search_cancel.setVisibility(View.GONE);
        } else {
            layoutSearch.setVisibility(View.VISIBLE);
            btnSearch.setText("搜索：" + charSequence.toString().trim());
            btn_search_cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    private void toSearch(String user_name){
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name",user_name)
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null){
                            if (list.size() == 0 && !isPageDestroy){
                                Toast.makeText(TravellerSearchActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (popupWindow != null && popupWindow.isShowing())
                                popupWindow.dismiss();
                            toShowUser(list.get(0));
                        }else {
                            if (!isPageDestroy){
                                Toast.makeText(TravellerSearchActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void toShowUser(UserInfoBean userInfoBean) {
        infoBean = userInfoBean;
        View convertView = LayoutInflater.from(this).inflate(R.layout.popupwindow_user_intro, null);
        popupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.style.leaveMesPopupWindow);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x66000000));

        SimpleDraweeView icon = (SimpleDraweeView) convertView.findViewById(R.id.popup_user_intro_icon);
        TextView nick = (TextView) convertView.findViewById(R.id.popup_user_intro_nick);
        TextView desc = (TextView) convertView.findViewById(R.id.popup_user_intro_desc);
        ImageView sex = (ImageView) convertView.findViewById(R.id.popup_user_intro_sex);
        ImageView close = (ImageView) convertView.findViewById(R.id.popup_user_intro_close);
        TextView attention = (TextView) convertView.findViewById(R.id.popup_user_intro_attention);
        TextView detail = (TextView) convertView.findViewById(R.id.popup_user_intro_detail);
        icon.setImageURI(Uri.parse(userInfoBean.getUser_icon()));
        nick.setText(userInfoBean.getNick_name());
        desc.setText(userInfoBean.getUser_desc());
        if (userInfoBean.getSex().equals("男")){
            sex.setImageResource(R.mipmap.ic_sex_man);
        }else {
            sex.setImageResource(R.mipmap.ic_sex_women);
        }
        close.setOnClickListener(this);
        attention.setOnClickListener(this);
        detail.setOnClickListener(this);

        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_traveller_search, null);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
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
                                        Toast.makeText(TravellerSearchActivity.this, "已经关注过了", Toast.LENGTH_SHORT).show();
                                        if (popupWindow != null && popupWindow.isShowing())
                                            popupWindow.dismiss();
                                        return;
                                    }
                                }
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
                                                Toast.makeText(TravellerSearchActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                                if (popupWindow != null && popupWindow.isShowing())
                                                    popupWindow.dismiss();
                                            }
                                        }
                                    }
                                });
                            }
                        } else {
                            if (!isPageDestroy) {
                                Toast.makeText(TravellerSearchActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                if (popupWindow != null && popupWindow.isShowing())
                                    popupWindow.dismiss();
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
                                                Toast.makeText(TravellerSearchActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                                if (popupWindow != null && popupWindow.isShowing())
                                                    popupWindow.dismiss();
                                            }
                                        }else {
                                            if (!isPageDestroy){
                                                Toast.makeText(TravellerSearchActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                                if (popupWindow != null && popupWindow.isShowing())
                                                    popupWindow.dismiss();
                                            }
                                        }
                                    }
                                });
                            }
                        }else {
                            if (!isPageDestroy){
                                Toast.makeText(TravellerSearchActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                if (popupWindow != null && popupWindow.isShowing())
                                    popupWindow.dismiss();
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
