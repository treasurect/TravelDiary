package com.treasure.traveldiary.activity.home_page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.ToolsWeatherFutureAdapter;
import com.treasure.traveldiary.bean.ToolsWeatherFutureBean;
import com.treasure.traveldiary.bean.ToolsWeatherResultBean;
import com.treasure.traveldiary.utils.HttpHelper;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ToolsWeatherActivity extends BaseActivity implements View.OnClickListener {
    private TextView now_temp, now_fine, now_range, now_air;
    private TextView publish_time, publish_humidity, publish_pollution, publish_wind, publish_cold, publish_dress, publish_exercise;
    private CustomScrollListView listView;
    private List<ToolsWeatherFutureBean> list;
    private ToolsWeatherFutureAdapter adapter;
    private ImageView btn_return;
    private TextView now_city;
    private ImageView btn_cityList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    now_temp.setText(resultBean.getResult().get(0).getTemperature());
                    now_fine.setText(resultBean.getResult().get(0).getWeather());
                    now_range.setText(resultBean.getResult().get(0).getFuture().get(0).getTemperature());
                    now_air.setText("空气" + resultBean.getResult().get(0).getAirCondition());
                    publish_time.setText("更新时间：" + resultBean.getResult().get(0).getTime());
                    publish_humidity.setText(resultBean.getResult().get(0).getHumidity().substring(3));
                    publish_pollution.setText(resultBean.getResult().get(0).getPollutionIndex());
                    publish_wind.setText(resultBean.getResult().get(0).getWind());
                    publish_cold.setText(resultBean.getResult().get(0).getColdIndex());
                    publish_dress.setText(resultBean.getResult().get(0).getDressingIndex());
                    publish_exercise.setText(resultBean.getResult().get(0).getExerciseIndex());
                    List<ToolsWeatherResultBean.ResultBean.FutureBean> futureList = resultBean.getResult().get(0).getFuture();
                    list.clear();
                    for (int i = 1; i < futureList.size(); i++) {
                        ToolsWeatherFutureBean futureBean = new ToolsWeatherFutureBean();
                        futureBean.setDate(futureList.get(i).getDate());
                        futureBean.setWeather(futureList.get(i).getDayTime());
                        futureBean.setTemp_range(futureList.get(i).getTemperature());
                        list.add(futureBean);
                    }
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    break;
                case 400:
                    Toast.makeText(ToolsWeatherActivity.this, "获取天气详情失败", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private ToolsWeatherResultBean resultBean;
    private NestedScrollView scrollView;
    private String province;
    private String city;
    private FrameLayout loading;
    private FloatingActionButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_weather);
        Tools.setTranslucentStatus(this);

        initFindId();
        receiveIntent();
        initListView();
        initScrollView();
        initClick();
        getWeatherDetail(StringContents.SMSSDK_APPKEY, city, province);
    }

    private void initFindId() {
        now_temp = (TextView) findViewById(R.id.tools_weather_now_temperature);
        now_fine = (TextView) findViewById(R.id.tools_weather_now_fine);
        now_range = (TextView) findViewById(R.id.tools_weather_now_range);
        now_air = (TextView) findViewById(R.id.tools_weather_now_air);
        publish_time = (TextView) findViewById(R.id.tools_weather_publish_time);
        publish_humidity = (TextView) findViewById(R.id.tools_weather_publish_humidity);
        publish_pollution = (TextView) findViewById(R.id.tools_weather_publish_pollution);
        publish_wind = (TextView) findViewById(R.id.tools_weather_publish_wind);
        publish_cold = (TextView) findViewById(R.id.tools_weather_publish_cold);
        publish_dress = (TextView) findViewById(R.id.tools_weather_publish_dress);
        publish_exercise = (TextView) findViewById(R.id.tools_weather_publish_exercise);
        listView = (CustomScrollListView) findViewById(R.id.tools_weather_listView);
        btn_return = (ImageView) findViewById(R.id.tools_weather_return);
        now_city = (TextView) findViewById(R.id.tools_weather_title);
        btn_cityList = (ImageView) findViewById(R.id.tools_weather_cityList);
        scrollView = (NestedScrollView) findViewById(R.id.tools_weather_scrollView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        refresh = (FloatingActionButton) findViewById(R.id.tools_weather_refresh);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        province = intent.getStringExtra("user_province");
        city = intent.getStringExtra("user_city");
        now_city.setText(city);
    }

    private void initListView() {
        list = new ArrayList<>();
        adapter = new ToolsWeatherFutureAdapter(this, list);
        listView.setAdapter(adapter);
    }

    private void initScrollView() {
        scrollView.smoothScrollTo(0, 20);
        listView.setFocusable(false);
    }

    private void initClick() {
        btn_return.setOnClickListener(this);
        btn_cityList.setOnClickListener(this);
        refresh.setOnClickListener(this);
        loading.setOnClickListener(this);
    }

    private void getWeatherDetail(String key, String city, String province) {
        loading.setVisibility(View.VISIBLE);
        String url = "http://apicloud.mob.com/v1/weather/query?key=" + key + "&city="+city+"&province="+province;
        HttpHelper.doGetCall(url, this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendMessage(mHandler.obtainMessage(400));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    resultBean = gson.fromJson(response.body().string(), ToolsWeatherResultBean.class);
                    if (resultBean != null) {
                        if (resultBean.getResult() != null) {
                            mHandler.sendMessage(mHandler.obtainMessage(200));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tools_weather_return:
                ToolsWeatherActivity.this.finish();
                break;
            case R.id.tools_weather_cityList:
                startActivityForResult(new Intent(ToolsWeatherActivity.this,ToolsWeatherListActivity.class),201);
                break;
            case R.id.tools_weather_refresh:
                Tools.setAnimation(refresh,0,0,1,1,0,-720,1,1,2000);
                getWeatherDetail(StringContents.SMSSDK_APPKEY,city,province);
                Toast.makeText(this, "最新数据已更新", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK){
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            getWeatherDetail(StringContents.SMSSDK_APPKEY, city, province);
            now_city.setText(city);
        }
    }
}
