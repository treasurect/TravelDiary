package com.treasure.traveldiary.activity.add;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class DiaryTextPublishActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText diaryDesc;
    private SharedPreferences mPreferences;
    private String user_addr;
    private float user_lat;
    private float user_long;
    private EditText diaryTitle;
    private TextView diaryLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_text_publish);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_send.setVisibility(View.VISIBLE);
        title.setText("记录生活");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        receiveData();
        initClick();

        btn_send.setClickable(false);
        btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
    }

    private void receiveData() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("user_addr"))){
            user_addr = intent.getStringExtra("user_addr");
            diaryLoc.setText(user_addr);
        }
        if (!Tools.isNull(intent.getStringExtra("user_lat"))){
            user_lat = Float.parseFloat(intent.getStringExtra("user_lat"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_long"))){
            user_long = Float.parseFloat(intent.getStringExtra("user_long"));
        }
    }

    private void initFindId() {
        diaryDesc = (EditText) findViewById(R.id.diary_text_desc);
        diaryTitle = (EditText) findViewById(R.id.diary_text_title);
        diaryLoc = (TextView) findViewById(R.id.diary_text_loc);
    }

    private void initClick() {
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        diaryDesc.addTextChangedListener(this);
        diaryTitle.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                DiaryTextPublishActivity.this.finish();
                break;
            case R.id.btn_send:
                sendDiary();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!Tools.isNull(diaryDesc.getText().toString().trim()) && !Tools.isNull(diaryTitle.getText().toString().trim())){
            btn_send.setClickable(true);
            btn_send.setTextColor(getResources().getColor(R.color.colorWhite));
        }else {
            btn_send.setClickable(false);
            btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void sendDiary() {
        String textDesc = diaryDesc.getText().toString().trim();
        String textTitle = diaryTitle.getText().toString().trim();
        DiaryBean diaryBean = new DiaryBean();
        diaryBean.setUser_name(mPreferences.getString("user_name",""));
        diaryBean.setUser_nick(mPreferences.getString("user_nick",""));
        diaryBean.setUser_addr(user_addr);
        diaryBean.setUser_lat(user_lat);
        diaryBean.setUser_long(user_long);
        diaryBean.setDiary_type(0);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Long(System.currentTimeMillis()));
        diaryBean.setPublish_time(time.substring(5,7)+"月"+time.substring(8,10)+"日"+time.substring(11,16));
        diaryBean.setUser_desc(textDesc);
        diaryBean.setUser_title(textTitle);
        diaryBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(DiaryTextPublishActivity.this, "恭喜你，日记发表成功", Toast.LENGTH_SHORT).show();
                    DiaryTextPublishActivity.this.finish();
                } else {
                    Toast.makeText(DiaryTextPublishActivity.this, "很遗憾，发表失败\n原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
