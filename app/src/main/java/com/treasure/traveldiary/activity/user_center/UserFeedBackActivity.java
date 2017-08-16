package com.treasure.traveldiary.activity.user_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.FeedBackBean;
import com.treasure.traveldiary.utils.Tools;

import java.text.SimpleDateFormat;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class UserFeedBackActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText edit_Content, edit_Contacts;
    private TextView history_feedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_back);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        btn_send.setVisibility(View.VISIBLE);
        title.setText("反馈");
        btn_send.setClickable(false);
        btn_send.setTextColor(getResources().getColor(R.color.colorGray2));

        initFindId();
        history_feedBack.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initClick();
    }

    private void initFindId() {
        edit_Content = (EditText) findViewById(R.id.user_feedBack_content);
        edit_Contacts = (EditText) findViewById(R.id.user_feedBack_contacts);
        history_feedBack = (TextView) findViewById(R.id.mine_history_feedBack);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        history_feedBack.setOnClickListener(this);
        edit_Content.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                UserFeedBackActivity.this.finish();
                break;
            case R.id.btn_send:
                sendFeedBack();
                break;
            case R.id.mine_history_feedBack:
                startActivity(new Intent(UserFeedBackActivity.this, UserFeedBckHistoryActivity.class));
                break;
        }
    }

    private void sendFeedBack() {
        FeedBackBean feedBackBean = new FeedBackBean();
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if (!Tools.isNull(sharedPreferences.getString("token", ""))) {
            feedBackBean.setUser_name(sharedPreferences.getString("user_name", ""));
        }
        feedBackBean.setUser_content(edit_Content.getText().toString().trim());
        feedBackBean.setUser_phone(edit_Contacts.getText().toString().trim() + "");
        String nowTime = Tools.getNowTime();
        feedBackBean.setPublish_time(nowTime);
        feedBackBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(UserFeedBackActivity.this, "我们已收到你的反馈，谢谢支持！", Toast.LENGTH_SHORT).show();
                    UserFeedBackActivity.this.finish();
                } else {
                    Toast.makeText(UserFeedBackActivity.this, "提交失败，原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!Tools.isNull(s.toString().trim())) {
            btn_send.setClickable(true);
            btn_send.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            btn_send.setClickable(false);
            btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
