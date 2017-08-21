package com.treasure.traveldiary.activity.home_page;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.ToolsWeatherCityListBean;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ToolsWeatherListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private List<String> pro_list;
    private List<String> city_list;
    private ArrayAdapter<String> pro_adapter;
    private ArrayAdapter<String> city_adapter;
    private ListView pro_listView;
    private ListView city_listView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    pro_list.clear();
                    for (int i = 0; i < mResult.size(); i++) {
                        pro_list.add(mResult.get(i).getProvince());
                    }
                    pro_adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    break;
                case 201:
                    city_list.clear();
                    province = msg.getData().getString("province");
                    for (int i = 0; i < mResult.size(); i++) {
                        if (mResult.get(i).getProvince().equals(province)){
                            List<ToolsWeatherCityListBean.ResultBean.CityBean> cityBeanList = mResult.get(i).getCity();
                            for (int j = 0; j < cityBeanList.size(); j++) {
                                city_list.add(cityBeanList.get(j).getCity());
                            }
                        }
                    }
                    city_adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    break;
                case 400:
                    Toast.makeText(ToolsWeatherListActivity.this, "获取城市列表失败", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private List<ToolsWeatherCityListBean.ResultBean> mResult;
    private ToolsWeatherCityListBean cityListBean;
    private String province;
    private int item_num = 0;
    private FrameLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_weather_list);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("城市列表");

        initFindId();
        initListView();
        initClick();
        getCityList();
    }
    private void initFindId() {
        pro_listView = (ListView) findViewById(R.id.weather_cityList_province_listView);
        city_listView = (ListView) findViewById(R.id.weather_cityList_city_listView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
    }

    private void initListView() {
        pro_list = new ArrayList<>();
        pro_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pro_list);
        pro_listView.setAdapter(pro_adapter);
        city_list = new ArrayList<>();
        city_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, city_list);
        city_listView.setAdapter(city_adapter);

    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        pro_listView.setOnItemClickListener(this);
        city_listView.setOnItemClickListener(this);
        loading.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ToolsWeatherListActivity.this.finish();
                break;
        }
    }

    private void getCityList() {
        loading.setVisibility(View.VISIBLE);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url("http://apicloud.mob.com/v1/weather/citys?key="+ StringContents.SMSSDK_APPKEY)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendMessage(mHandler.obtainMessage(400));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                cityListBean = gson.fromJson(string, ToolsWeatherCityListBean.class);
                if (cityListBean !=null){
                    if (cityListBean.getResult() != null){
                        mResult = cityListBean.getResult();
                        mHandler.sendMessage(mHandler.obtainMessage(200));
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.weather_cityList_province_listView:
                pro_listView.setVisibility(View.GONE);
                city_listView.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("province",pro_list.get(position));
                Message message = mHandler.obtainMessage(201);
                message.setData(bundle);
                mHandler.sendMessage(message);
                item_num++;
                break;
            case R.id.weather_cityList_city_listView:
                pro_listView.setVisibility(View.GONE);
                city_listView.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.putExtra("province",province);
                intent.putExtra("city",city_list.get(position));
                setResult(RESULT_OK,intent);
                ToolsWeatherListActivity.this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (item_num == 1){
            pro_listView.setVisibility(View.VISIBLE);
            city_listView.setVisibility(View.GONE);
            item_num--;
        }else if (item_num == 0){
            super.onBackPressed();
        }
    }
}
