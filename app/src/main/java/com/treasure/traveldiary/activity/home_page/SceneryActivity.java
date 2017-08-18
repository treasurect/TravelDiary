package com.treasure.traveldiary.activity.home_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.SceneryListAdapter;
import com.treasure.traveldiary.bean.SceneryBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SceneryActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, SceneryListAdapter.SceneryTextClick {

    private LinearLayout nodata_layout;
    private List<String> list;
    private boolean isPageDestroy;
    private FrameLayout loading;
    private GridView listView;
    private FloatingActionButton refresh;
    private String city;
    private List<SceneryBean> scenery_list;
    private SceneryListAdapter sceneryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenery);
        initTitle();
        Tools.setTranslucentStatus(this);
        title.setText("景点");
        btn_back.setVisibility(View.VISIBLE);
        list_city.setVisibility(View.VISIBLE);

        initFindId();
        initSpinner();
        initListView();
        initClick();
    }

    private void initFindId() {
        nodata_layout = (LinearLayout) findViewById(R.id.no_data_layout);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        listView = (GridView) findViewById(R.id.scenery_listView);
        refresh = (FloatingActionButton) findViewById(R.id.scenery_refresh);
    }

    private void initSpinner() {
         list = new ArrayList<>();
        list.add("北京");
        list.add("上海");
        list.add("山东");
        list.add("浙江");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        list_city.setAdapter(adapter);
    }

    private void initListView() {
        scenery_list = new ArrayList<>();
        sceneryListAdapter = new SceneryListAdapter(this,scenery_list);
        listView.setAdapter(sceneryListAdapter);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        list_city.setOnItemSelectedListener(this);
        refresh.setOnClickListener(this);
        sceneryListAdapter.setSceneryTextClick(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                SceneryActivity.this.finish();
                break;
            case R.id.scenery_refresh:
                Tools.setAnimation(refresh, 0, 0, 1, 1, 0, -720, 1, 1, 2000);
                getSceneryList();
                if (!isPageDestroy) {
                    Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getSceneryList() {
        loading.setVisibility(View.VISIBLE);
        title.setText(city+"景点");
        BmobQuery<SceneryBean> query = new BmobQuery<>();
        query.addWhereEqualTo("scenery_city",city)
                .order("createdAt")
                .findObjects(new FindListener<SceneryBean>() {
                    @Override
                    public void done(List<SceneryBean> list, BmobException e) {
                        if (e== null){
                            if (list.size() == 0){
                                nodata_layout.setVisibility(View.VISIBLE);
                            }
                            scenery_list.clear();
                            scenery_list.addAll(list);
                            sceneryListAdapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);
                        }else {
                            if (!isPageDestroy){
                                Toast.makeText(SceneryActivity.this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        city = list.get(i);
        getSceneryList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        getSceneryList();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        super.onDestroy();
    }

    @Override
    public void layoutClick(SceneryBean sceneryBean) {
        Intent intent = new Intent(SceneryActivity.this, SceneryDetailActivity.class);
        intent.putExtra("sceneryBean",sceneryBean);
        startActivity(intent);
    }
}
