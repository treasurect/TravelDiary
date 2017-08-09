package com.treasure.traveldiary.activity.user;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserSigningActivity extends BaseActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {

    private CalendarView calendar_view;
    private TextView mon,tue,wed,thr,fri,sat,sun;
    private String nowTime;
    private SharedPreferences mPreferences;
    private int integral_count1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signing);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("签到");
        mPreferences = getSharedPreferences("user",MODE_PRIVATE);
        text_integral.setVisibility(View.VISIBLE);
        text_integral.setText("积分："+mPreferences.getInt("user_integral",0));

        initFindId();
        nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Long(System.currentTimeMillis()));
        initClick();
    }

    private void initFindId() {
        calendar_view = (CalendarView) findViewById(R.id.user_signing_view);
        sun = (TextView) findViewById(R.id.user_signing_Sun);
        mon = (TextView) findViewById(R.id.user_signing_Mon);
        tue = (TextView) findViewById(R.id.user_signing_Tue);
        wed = (TextView) findViewById(R.id.user_signing_Wed);
        thr = (TextView) findViewById(R.id.user_signing_Thr);
        fri = (TextView) findViewById(R.id.user_signing_Fri);
        sat = (TextView) findViewById(R.id.user_signing_Sat);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        calendar_view.setOnDateChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserSigningActivity.this.finish();
                break;
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(calendarView.getDate());
        int nowYear = Integer.parseInt(nowTime.substring(0, 4));
        int nowMonth = Integer.parseInt(nowTime.substring(5, 7));
        int nowDay = Integer.parseInt(nowTime.substring(8, 10));
        if (year == nowYear && (month+1) == nowMonth && dayOfMonth == nowDay){
            BmobQuery<UserInfoBean> query = new BmobQuery<>();
            query.addWhereEqualTo("user_name",mPreferences.getString("user_name",""));
            query.findObjects(new FindListener<UserInfoBean>() {
                @Override
                public void done(List<UserInfoBean> list, BmobException e) {
                    if (e == null){
                        if (list != null){
                            int integral_count = list.get(0).getIntegral_count();
                            List<String> signing_date = list.get(0).getSigning_date();
                            String objectId = list.get(0).getObjectId();
                            boolean isRetry = false;
                            for (int i = 1; i < signing_date.size(); i++) {
                                if (nowTime.substring(0,10).equals(signing_date.get(i).substring(0,10))){
                                    isRetry = true;
                                }
                            }
                            if (isRetry){
                                Toast.makeText(UserSigningActivity.this, "您已经签过到了！", Toast.LENGTH_SHORT).show();
                            }else{
                                updateUserInfo(objectId,integral_count,signing_date);
                            }
                        }
                    }else {
                        Toast.makeText(UserSigningActivity.this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateUserInfo(String objectId,  int integral_count, List<String> signing_date) {
        integral_count1 = integral_count + 10;
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setIntegral_count(integral_count1);
        signing_date.add(nowTime);
        userInfoBean.setSigning_date(signing_date);
        userInfoBean.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(UserSigningActivity.this, "签到成功，积分+10", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt("user_integral", integral_count1);
                    editor.apply();
                    text_integral.setText("积分："+integral_count1);
                }else {
                    Toast.makeText(UserSigningActivity.this, "签到失败，请重新签到", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
