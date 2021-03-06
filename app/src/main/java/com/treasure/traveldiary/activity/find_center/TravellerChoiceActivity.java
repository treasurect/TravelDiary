package com.treasure.traveldiary.activity.find_center;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.loopeer.cardstack.CardStackView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.adapter.ChoiceStackAdapter;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TravellerChoiceActivity extends BaseActivity implements View.OnClickListener, CardStackView.ItemExpendListener {

    private CardStackView cardStackView;
    private ChoiceStackAdapter choiceStackAdapter;
    private List<DiaryBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_choice);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("驴友精选");

        initFindId();
        initCardStackView();
        initClick();
        getTravellerCircleList();
    }

    private void initFindId() {
        cardStackView = (CardStackView) findViewById(R.id.traveller_choice_cardStackView);
    }

    private void initCardStackView() {
        list = new ArrayList<>();
        choiceStackAdapter = new ChoiceStackAdapter(this, list);
        cardStackView.setAdapter(choiceStackAdapter);
        cardStackView.setItemExpendListener(this);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                TravellerChoiceActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemExpend(boolean expend) {

    }

    private void getTravellerCircleList() {
        BmobQuery<DiaryBean> query = new BmobQuery<>();
        query.setSkip(0)
                .setLimit(50)
                .order("-publish_time")
                .addWhereEqualTo("state", "公开")
                .findObjects(new FindListener<DiaryBean>() {
                    @Override
                    public void done(List<DiaryBean> list2, BmobException e) {
                        if (e == null) {
                            list.clear();
                            list.addAll(list2);
                            choiceStackAdapter.updateData(list);
                        } else {

                            Toast.makeText(TravellerChoiceActivity.this, "获取日记列表失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
