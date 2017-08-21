package com.treasure.traveldiary.activity.home_page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.LeaveMesBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DiaryTextPublishActivity extends BaseActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    private EditText diaryDesc;
    private SharedPreferences mPreferences;
    private String user_addr;
    private String user_lat;
    private String user_long;
    private EditText diaryTitle;
    private TextView diaryLoc;
    private FrameLayout loading;
    private ImageView state_img;
    private Spinner state_text;
    private ArrayAdapter<String> adapter;
    private String text_state;

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
        initSpinner();
        initClick();

        btn_send.setClickable(false);
        btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
    }

    private void initFindId() {
        diaryDesc = (EditText) findViewById(R.id.diary_text_desc);
        diaryTitle = (EditText) findViewById(R.id.diary_text_title);
        diaryLoc = (TextView) findViewById(R.id.diary_text_loc);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        state_img = (ImageView) findViewById(R.id.diary_text_state_img);
        state_text = (Spinner) findViewById(R.id.diary_text_state_text);
    }

    private void receiveData() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("user_addr"))) {
            user_addr = intent.getStringExtra("user_addr");
            diaryLoc.setText(user_addr);
        }
        if (!Tools.isNull(intent.getStringExtra("user_lat"))) {
            user_lat = intent.getStringExtra("user_lat");
        }
        if (!Tools.isNull(intent.getStringExtra("user_long"))) {
            user_long = intent.getStringExtra("user_long");
        }
    }

    private void initSpinner() {
        List<String> list = new ArrayList<>();
        list.add("公开");
        list.add("私密");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_text.setAdapter(adapter);
    }

    private void initClick() {
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        diaryDesc.addTextChangedListener(this);
        diaryTitle.addTextChangedListener(this);
        loading.setOnClickListener(this);
        state_text.setOnItemSelectedListener(this);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        text_state = adapter.getItem(i);
        if (text_state.equals("公开")) {
            state_img.setImageResource(R.mipmap.ic_lock_open);
        } else {
            state_img.setImageResource(R.mipmap.ic_lock_close);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        text_state = "公开";
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!Tools.isNull(diaryDesc.getText().toString().trim()) && !Tools.isNull(diaryTitle.getText().toString().trim())) {
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

    private void sendDiary() {
        final String nowTime = Tools.getNowTime();
        //保存时间轴
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name",mPreferences.getString("user_name",""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null){
                            String objectId = list.get(0).getObjectId();
                            List<String> timer_shaft = list.get(0).getTimer_shaft();
                            UserInfoBean userInfoBean = new UserInfoBean();
                            timer_shaft.add(nowTime);
                            userInfoBean.setTimer_shaft(timer_shaft);
                            userInfoBean.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {

                                }
                            });
                        }
                    }
                });


        loading.setVisibility(View.VISIBLE);
        String textDesc = diaryDesc.getText().toString().trim();
        String textTitle = diaryTitle.getText().toString().trim();
        DiaryBean diaryBean = new DiaryBean();
        diaryBean.setUser_name(mPreferences.getString("user_name", ""));
        diaryBean.setUser_nick(mPreferences.getString("user_nick", ""));
        diaryBean.setUser_icon(mPreferences.getString("user_icon", ""));
        diaryBean.setUser_addr(user_addr);
        diaryBean.setUser_lat(user_lat);
        diaryBean.setUser_long(user_long);
        diaryBean.setDiary_type("0");
        diaryBean.setState(text_state);
        diaryBean.setPublish_time(nowTime);
        diaryBean.setUser_desc(textDesc);
        diaryBean.setUser_title(textTitle);
        //生成一个空的数据占位，以方便更新
        LeaveMesBean leaveMesBean = new LeaveMesBean();
        leaveMesBean.setLeave_name(mPreferences.getString("user_name", ""));
        List<LeaveMesBean> leaveMesBeen = new ArrayList<>();
        leaveMesBeen.add(leaveMesBean);
        diaryBean.setMesBeanList(leaveMesBeen);
        //生成一个空的数据占位，以方便更新
        List<String> list = new ArrayList<>();
        list.add("0");
        diaryBean.setLikeBean(list);
        diaryBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(DiaryTextPublishActivity.this, "恭喜你，日记发表成功", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    DiaryTextPublishActivity.this.finish();
                } else {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(DiaryTextPublishActivity.this, "很遗憾，发表失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
