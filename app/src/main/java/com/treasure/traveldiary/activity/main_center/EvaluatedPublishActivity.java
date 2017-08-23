package com.treasure.traveldiary.activity.main_center;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EvaluatedPublishActivity extends BaseActivity implements View.OnClickListener, OnGetPoiSearchResultListener, AdapterView.OnItemClickListener, TextWatcher {
    private LatLng latLng;
    private PoiSearch poiSearch;
    private List<PoiInfo> allPoi;
    private ListView nearby_listView;
    private List<String> nearby_list;
    private ArrayAdapter nearby_adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    nearby_adapter.notifyDataSetChanged();
                    break;
                case 201:
                    nearby_listView.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    title.setText("点评/吐槽");
                    String[] split = user_addr.split("\\(");
                    nearby_loc.setText(split[0]);
                    btn_send.setVisibility(View.VISIBLE);
                    btn_send.setClickable(false);
                    btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
                    break;
            }
        }
    };
    private String user_addr = "china";
    private FrameLayout loading;
    private TextView star_text;
    private ImageView star1, star2, star3, star4, star5;
    private EditText star_edit;
    private TextView nearby_loc;
    private String starNum = "0";
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluated_publish);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("景区选择");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        receiverIntent();
        initListView();
        initClick();
    }

    private void initFindId() {
        nearby_listView = (ListView) findViewById(R.id.signin_nearby_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        star_text = (TextView) findViewById(R.id.signin_nearby_star_text);
        star1 = (ImageView) findViewById(R.id.signin_nearby_star1);
        star2 = (ImageView) findViewById(R.id.signin_nearby_star2);
        star3 = (ImageView) findViewById(R.id.signin_nearby_star3);
        star4 = (ImageView) findViewById(R.id.signin_nearby_star4);
        star5 = (ImageView) findViewById(R.id.signin_nearby_star5);
        star_edit = (EditText) findViewById(R.id.signin_nearby_edit);
        nearby_loc = (TextView) findViewById(R.id.signin_nearby_loc);
    }

    private void receiverIntent() {
        Intent intent = getIntent();
        String user_lat = intent.getStringExtra("user_lat");
        String user_lon = intent.getStringExtra("user_lon");
        user_addr = intent.getStringExtra("user_addr");
        if (!Tools.isNull(user_lat) && !Tools.isNull(user_lon)) {
            latLng = new LatLng(Double.parseDouble(user_lat), Double.parseDouble(user_lon));
            searchNearBy();
        }
    }

    private void initListView() {
        nearby_list = new ArrayList<>();
//        String str = user_addr + "      (我的位置)";
//        SpannableStringBuilder builder = new SpannableStringBuilder(str);
//        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange))
//                ,user_addr.length(),str.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        nearby_list.add(user_addr + "             （我的位置）");
        nearby_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nearby_list);
        nearby_listView.setAdapter(nearby_adapter);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        nearby_listView.setOnItemClickListener(this);
        loading.setOnClickListener(this);
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
        star_edit.addTextChangedListener(this);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                EvaluatedPublishActivity.this.finish();
                break;
            case R.id.signin_nearby_star1:
                initStar();
                star1.setImageResource(R.mipmap.ic_star_click);
                Tools.setAnimation(star1, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                star_text.setText("很差");
                star_text.setTextColor(getResources().getColor(R.color.colorOrange));
                starNum = "1";
                break;
            case R.id.signin_nearby_star2:
                initStar();
                star1.setImageResource(R.mipmap.ic_star_click);
                star2.setImageResource(R.mipmap.ic_star_click);
                Tools.setAnimation(star1, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star2, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                star_text.setText("一般");
                star_text.setTextColor(getResources().getColor(R.color.colorOrange));
                starNum = "2";
                break;
            case R.id.signin_nearby_star3:
                initStar();
                star1.setImageResource(R.mipmap.ic_star_click);
                star2.setImageResource(R.mipmap.ic_star_click);
                star3.setImageResource(R.mipmap.ic_star_click);
                Tools.setAnimation(star1, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star2, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star3, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                star_text.setText("还行");
                star_text.setTextColor(getResources().getColor(R.color.colorOrange));
                starNum = "3";
                break;
            case R.id.signin_nearby_star4:
                initStar();
                star1.setImageResource(R.mipmap.ic_star_click);
                star2.setImageResource(R.mipmap.ic_star_click);
                star3.setImageResource(R.mipmap.ic_star_click);
                star4.setImageResource(R.mipmap.ic_star_click);
                Tools.setAnimation(star1, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star2, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star3, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star4, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                star_text.setText("推荐");
                star_text.setTextColor(getResources().getColor(R.color.colorOrange));
                starNum = "4";
                break;
            case R.id.signin_nearby_star5:
                star1.setImageResource(R.mipmap.ic_star_click);
                star2.setImageResource(R.mipmap.ic_star_click);
                star3.setImageResource(R.mipmap.ic_star_click);
                star4.setImageResource(R.mipmap.ic_star_click);
                star5.setImageResource(R.mipmap.ic_star_click);
                Tools.setAnimation(star1, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star2, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star3, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star4, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                Tools.setAnimation(star5, 0, 0, 1, 1, 0, 0, 2f, 1f, 1000);
                star_text.setText("怒赞");
                star_text.setTextColor(getResources().getColor(R.color.colorOrange));
                starNum = "5";
                break;
            case R.id.btn_send:
                sendEvaluated();
                break;
        }
    }

    private void initStar() {
        star1.setImageResource(R.mipmap.ic_star_unclick);
        star2.setImageResource(R.mipmap.ic_star_unclick);
        star3.setImageResource(R.mipmap.ic_star_unclick);
        star4.setImageResource(R.mipmap.ic_star_unclick);
        star5.setImageResource(R.mipmap.ic_star_unclick);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        user_addr = nearby_list.get(i);
        loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    handler.sendMessage(handler.obtainMessage(201));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!Tools.isNull(star_edit.getText().toString().trim()) && !starNum.equals("0")) {
            btn_send.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_send.setClickable(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void searchNearBy() {
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.location(latLng);
        option.keyword("星级景区");
        option.pageCapacity(30);
        option.radius(2000);
        option.sortType(PoiSortType.distance_from_near_to_far);
        poiSearch.searchNearby(option);
    }

    private void sendEvaluated() {
        EvaluatedBean bean = new EvaluatedBean();
        bean.setUser_name(mPreferences.getString("user_name", ""));
        bean.setUser_nick(mPreferences.getString("user_nick", ""));
        bean.setUser_icon(mPreferences.getString("user_icon", ""));
        bean.setUser_addr(user_addr);
        String nowTime = Tools.getNowTime();
        bean.setPublish_time(nowTime);
        bean.setStar_num(starNum);
        bean.setUser_evaluated(star_edit.getText().toString().trim());
        //生成一个空的数据占位，以方便更新
        SUserBean SUserBean = new SUserBean();
        SUserBean.setLeave_name(mPreferences.getString("user_name", ""));
        List<SUserBean> leaveMesBeen = new ArrayList<>();
        leaveMesBeen.add(SUserBean);
        bean.setMesBeanList(leaveMesBeen);
        bean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(EvaluatedPublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    EvaluatedPublishActivity.this.finish();
                } else {
                    Toast.makeText(EvaluatedPublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult != null) {
            if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
                allPoi = poiResult.getAllPoi();
                for (int i = 0; i < allPoi.size(); i++) {
                    nearby_list.add(allPoi.get(i).name + "(" + allPoi.get(i).address + ")");
                }
                handler.sendMessage(handler.obtainMessage(200));
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    protected void onDestroy() {
        poiSearch.destroy();
        super.onDestroy();
    }
}
