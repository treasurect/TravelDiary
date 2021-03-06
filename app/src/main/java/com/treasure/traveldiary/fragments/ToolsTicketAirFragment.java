package com.treasure.traveldiary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.main_center.ToolsWeatherListActivity;
import com.treasure.traveldiary.adapter.ToolsTicketAirAdapter;
import com.treasure.traveldiary.bean.ToolsTicketAirBean;
import com.treasure.traveldiary.utils.HttpHelper;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ToolsTicketAirFragment extends BaseFragment implements View.OnClickListener {
    private TextView station_start, station_end, station_query;
    private ImageView station_exchange;
    private ListView station_result;
    private List<ToolsTicketAirBean.ResultBean> list;
    private ToolsTicketAirAdapter adapter;
    private ToolsTicketAirBean airBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    list.addAll(airBean.getResult());
                    adapter.notifyDataSetChanged();
                    result_progress.setVisibility(View.GONE);
                    break;
                case 201:
                    Toast.makeText(getContext(), "未查询到航班信息", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    result_progress.setVisibility(View.GONE);
                    break;
                case 202:
                    String start1 = station_start.getText().toString().trim();
                    String end1 = station_end.getText().toString().trim();
                    station_start.setText(end1);
                    station_end.setText(start1);
                    break;
                case 400:
                    Toast.makeText(getContext(), "获取飞机票信息失败", Toast.LENGTH_SHORT).show();
                    result_progress.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private int screen_width;
    private int screen_height;
    private FrameLayout result_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools_ticket_air, container, false);
        initFindId(view);
        initClick();
        initListView();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;
        return view;
    }

    private void initFindId(View view) {
        station_start = (TextView) view.findViewById(R.id.air_station_start);
        station_end = (TextView) view.findViewById(R.id.air_station_end);
        station_exchange = (ImageView) view.findViewById(R.id.air_station_exchange);
        station_query = (TextView) view.findViewById(R.id.air_station_query);
        station_result = (ListView) view.findViewById(R.id.air_station_result);
        result_progress = (FrameLayout) view.findViewById(R.id.loading_layout);
    }

    private void initClick() {
        station_exchange.setOnClickListener(this);
        station_query.setOnClickListener(this);
        station_start.setOnClickListener(this);
        station_end.setOnClickListener(this);
    }

    private void initListView() {
        list = new ArrayList<>();
        adapter = new ToolsTicketAirAdapter(getContext(), list);
        station_result.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.air_station_start:
                Intent intent = new Intent(getContext(), ToolsWeatherListActivity.class);
                startActivityForResult(intent, 201);
                break;
            case R.id.air_station_end:
                Intent intent2 = new Intent(getContext(), ToolsWeatherListActivity.class);
                startActivityForResult(intent2, 202);
                break;
            case R.id.air_station_exchange:
                Tools.setTransAnimation(station_start,0,screen_width/2,0,0,1500);
                Tools.setTransAnimation(station_end,0,-screen_width/2,0,0,1500);
                Tools.setAnimation(station_exchange,0,0,1,1,0,-180,1,1,1000);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            handler.sendMessage(handler.obtainMessage(202));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.air_station_query:
                String start = station_start.getText().toString().trim();
                String end = station_end.getText().toString();
                getTrainInfo(start, end);
                result_progress.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getTrainInfo(String start, String end) {
        String url = "http://apicloud.mob.com/flight/line/query?key="+ StringContents.SMSSDK_APPKEY+"&start="+start+"&end="+end;
        HttpHelper.doGetCall(url, getContext(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendMessage(handler.obtainMessage(400));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                Gson gson = new Gson();
                 airBean = gson.fromJson(resp, ToolsTicketAirBean.class);
                list.clear();
                if (airBean != null) {
                    if (airBean.getResult() != null) {
                        handler.sendMessage(handler.obtainMessage(200));
                    } else {
                        handler.sendMessage(handler.obtainMessage(201));
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 201:
                if (resultCode == getActivity().RESULT_OK) {
                    String city = data.getExtras().getString("city");
                    if (!Tools.isNull(city)) {
                        station_start.setText(city);
                    }
                }
                break;
            case 202:
                if (resultCode == getActivity().RESULT_OK) {
                    String district = data.getExtras().getString("district");
                    if (!Tools.isNull(district)) {
                        station_end.setText(district);
                    }
                }
                break;
        }
    }

    @Override
    protected void lazyLoad() {

    }
}
