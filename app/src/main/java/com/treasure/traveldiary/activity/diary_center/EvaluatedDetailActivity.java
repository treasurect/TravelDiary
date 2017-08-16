package com.treasure.traveldiary.activity.diary_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.DiaryLeavemesListAdapter;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.bean.LeaveMesBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class EvaluatedDetailActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private TextView addr;
    private SimpleDraweeView user_icon;
    private TextView user_name,user_time,user_title;
    private ImageView star1,star2,star3,star4,star5;
    private ScrollView scrollView;
    private CustomScrollListView listView;
    private FrameLayout loading;
    private FloatingActionButton btnRefresh;
    private String evaluated_publisher,evaluated_time;
    private EvaluatedBean evaluatedBean;
    private String objectId;
    private List<LeaveMesBean> mesBeanList;
    private DiaryLeavemesListAdapter adapter;
    private FrameLayout leaveMes_send;
    private PopupWindow mPopupWindow;
    private EditText editLeaveMes;
    private TextView sendLeaveMes;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluated_detail);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("详情");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        
        initFindId();
        initListView();
        initScrollView();
        receiveIntetnt();
        if (!Tools.isNull(evaluated_publisher) && !Tools.isNull(evaluated_time)){
            getEvaluatedDetail(evaluated_publisher,evaluated_time);
        }
        initClick();
    }

    private void initFindId() {
        user_icon = (SimpleDraweeView)findViewById(R.id.evaluated_detail_user_icon);
        user_name = (TextView)findViewById(R.id.evaluated_detail_user_name);
        user_time = (TextView)findViewById(R.id.evaluated_detail_user_time);
        user_title = (TextView)findViewById(R.id.evaluated_detail_user_title);
        star1 = (ImageView) findViewById(R.id.evaluated_detail_user_star1);
        star2 = (ImageView) findViewById(R.id.evaluated_detail_user_star2);
        star3 = (ImageView) findViewById(R.id.evaluated_detail_user_star3);
        star4 = (ImageView) findViewById(R.id.evaluated_detail_user_star4);
        star5 = (ImageView) findViewById(R.id.evaluated_detail_user_star5);
        addr = (TextView) findViewById(R.id.evaluated_detail_addr);
        scrollView = (ScrollView) findViewById(R.id.evaluated_detail_leaveMes_layout);
        listView = (CustomScrollListView) findViewById(R.id.evaluated_detail_leaveMes_list);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        btnRefresh = (FloatingActionButton) findViewById(R.id.evaluated_detail_leaveMes_refresh);
        leaveMes_send = (FrameLayout) findViewById(R.id.evaluated_detail_leaveMes_send);
    }

    private void initListView() {
        mesBeanList = new ArrayList<>();
        adapter = new DiaryLeavemesListAdapter(this,mesBeanList);
        listView.setAdapter(adapter);
    }
    private void initScrollView() {
        scrollView.smoothScrollTo(0, 20);
        listView.setFocusable(false);
    }
    private void receiveIntetnt() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("user_name"))){
            evaluated_publisher = intent.getStringExtra("user_name");
        }
        if (!Tools.isNull(intent.getStringExtra("user_time"))){
            evaluated_time = intent.getStringExtra("user_time");
        }
    }
    private void getEvaluatedDetail(String evaluated_publisher, String evaluated_time) {
        loading.setVisibility(View.VISIBLE);
        BmobQuery<EvaluatedBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user_name", evaluated_publisher);
        BmobQuery<EvaluatedBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("publish_time", evaluated_time);
        List<BmobQuery<EvaluatedBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<EvaluatedBean> bmobQuery = new BmobQuery<>();
        bmobQuery.and(queries);
        bmobQuery.findObjects(new FindListener<EvaluatedBean>() {
            @Override
            public void done(List<EvaluatedBean> list, BmobException e) {
                if (e == null){
                    evaluatedBean = list.get(0);
                    objectId = list.get(0).getObjectId();
                    if (evaluatedBean != null){
                        if (!Tools.isNull(evaluatedBean.getUser_icon())){
                            user_icon.setImageURI(Uri.parse(evaluatedBean.getUser_icon()));
                        }
                        if (!Tools.isNull(evaluatedBean.getUser_name())){
                            user_name.setText(evaluatedBean.getUser_nick());
                        }
                        if (!Tools.isNull(evaluatedBean.getPublish_time())){
                            user_time.setText(evaluatedBean.getPublish_time());
                        }
                        if (!Tools.isNull(evaluatedBean.getUser_evaluated())){
                            user_title.setText(evaluatedBean.getUser_evaluated());
                        }
                        if (!Tools.isNull(evaluatedBean.getUser_addr())){
                            addr.setText(evaluatedBean.getUser_addr());
                        }
                        String star_num = evaluatedBean.getStar_num();
                        if (!Tools.isNull(String.valueOf(star_num))){
                            if (star_num.equals("1")){
                                star1.setImageResource(R.mipmap.ic_star_click);
                            }
                            if (star_num.equals("2")){
                                star1.setImageResource(R.mipmap.ic_star_click);
                                star2.setImageResource(R.mipmap.ic_star_click);
                            }
                            if (star_num.equals("3")){
                                star1.setImageResource(R.mipmap.ic_star_click);
                                star2.setImageResource(R.mipmap.ic_star_click);
                                star3.setImageResource(R.mipmap.ic_star_click);
                            }
                            if (star_num.equals("4")){
                                star1.setImageResource(R.mipmap.ic_star_click);
                                star2.setImageResource(R.mipmap.ic_star_click);
                                star3.setImageResource(R.mipmap.ic_star_click);
                                star4.setImageResource(R.mipmap.ic_star_click);
                            }
                            if (star_num.equals("5")){
                                star1.setImageResource(R.mipmap.ic_star_click);
                                star2.setImageResource(R.mipmap.ic_star_click);
                                star3.setImageResource(R.mipmap.ic_star_click);
                                star4.setImageResource(R.mipmap.ic_star_click);
                                star5.setImageResource(R.mipmap.ic_star_click);
                            }
                        }
                        if (evaluatedBean.getMesBeanList() != null){
                            List<LeaveMesBean> leaveMesList1 = evaluatedBean.getMesBeanList();
                            //显示留言  从外部进入
                            mesBeanList.clear();
                            leaveMesList1.remove(0);
                            Collections.reverse(leaveMesList1);
                            mesBeanList.addAll(leaveMesList1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    loading.setVisibility(View.GONE);
                }else {
                    Toast.makeText(EvaluatedDetailActivity.this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
    private void getLeaveMesList(String objectId){
        BmobQuery<EvaluatedBean> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId",objectId)
                .findObjects(new FindListener<EvaluatedBean>() {
                    @Override
                    public void done(List<EvaluatedBean> list, BmobException e) {
                        if (e == null){
                            List<LeaveMesBean> leaveMesList2 = list.get(0).getMesBeanList();
                            //显示留言   刷新
                            mesBeanList.clear();
                            leaveMesList2.remove(0);
                            Collections.reverse(leaveMesList2);
                            mesBeanList.addAll(leaveMesList2);
                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(EvaluatedDetailActivity.this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void initClick() {
        btn_back.setOnClickListener(this);
        loading.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        leaveMes_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                EvaluatedDetailActivity.this.finish();
                break;
            case R.id.evaluated_detail_leaveMes_refresh:
                Tools.setAnimation(btnRefresh,0,0,1,1,0,-760,1,1,2000);
                getLeaveMesList(objectId);
                Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.evaluated_detail_leaveMes_send:
                showPopupWindow();
                leaveMes_send.setVisibility(View.GONE);
                break;
            case R.id.leaveMes_send:
                mPopupWindow.dismiss();
                leaveMes_send.setVisibility(View.VISIBLE);
                if (!Tools.isNull(editLeaveMes.getText().toString().trim())) {
                    sendLeaveMes();
                }
                break;
        }
    }
    /**
     * 显示 关闭 popupWindow
     */
    public void showPopupWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.popupwindow_leavemes_send, null);
        mPopupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.leaveMesPopupWindow);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x66000000));
        editLeaveMes = ((EditText) convertView.findViewById(R.id.leaveMes_content));
        sendLeaveMes = ((TextView) convertView.findViewById(R.id.leaveMes_send));

        sendLeaveMes.setOnClickListener(this);
        editLeaveMes.addTextChangedListener(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_diary_detail, null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (Tools.isNull(charSequence.toString())) {
            sendLeaveMes.setText("关闭");
            sendLeaveMes.setTextColor(getResources().getColor(R.color.colorGray2));
        } else {
            sendLeaveMes.setText("发送");
            sendLeaveMes.setTextColor(getResources().getColor(R.color.colorOrange));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    private void sendLeaveMes() {
        BmobQuery<EvaluatedBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user_name", evaluated_publisher);
        BmobQuery<EvaluatedBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("publish_time", evaluated_time);
        List<BmobQuery<EvaluatedBean>> queries = new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        BmobQuery<EvaluatedBean> bmobQuery = new BmobQuery<>();
        bmobQuery.and(queries);
        bmobQuery.findObjects(new FindListener<EvaluatedBean>() {
            @Override
            public void done(List<EvaluatedBean> list, BmobException e) {
                if (e == null) {
                    objectId = list.get(0).getObjectId();
                    List<LeaveMesBean> leaveMesList3 = list.get(0).getMesBeanList();
                    String star_num = list.get(0).getStar_num();
                    saveLeaveMes(objectId, leaveMesList3,star_num);
                } else {
                    Toast.makeText(EvaluatedDetailActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveLeaveMes(final String objectId, List<LeaveMesBean> leaveMesList4, String star_num)  {
        LeaveMesBean bean = new LeaveMesBean();
        bean.setLeave_content(editLeaveMes.getText().toString().trim());
        bean.setLeave_icon(mPreferences.getString("user_icon", ""));
        bean.setLeave_nick(mPreferences.getString("user_nick", ""));
        bean.setLeave_name(mPreferences.getString("user_name", ""));
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Long(System.currentTimeMillis()));
        bean.setLeave_time(time.substring(5, 7) + "月" + time.substring(8, 10) + "日" + time.substring(11, 16));

        List<LeaveMesBean> leaveMesBeen = new ArrayList<>();
        leaveMesBeen.addAll(leaveMesList4);
        leaveMesBeen.add(bean);
        //上传服务器
        EvaluatedBean evaluatedBean = new EvaluatedBean();
        evaluatedBean.setMesBeanList(leaveMesBeen);
        evaluatedBean.setStar_num(star_num);
        evaluatedBean.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(EvaluatedDetailActivity.this, "评论成功，你够意思", Toast.LENGTH_SHORT).show();
                    getLeaveMesList(objectId);
                } else {
                    Toast.makeText(EvaluatedDetailActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
