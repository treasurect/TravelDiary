package com.treasure.traveldiary.activity.find_center;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.Tools;

public class FindCenterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout traveller_circle;
    private LinearLayout traveller_choiceness;
    private LinearLayout traveller_detail;
    private LinearLayout traveller_forum;
    private LinearLayout traveller_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_center);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("发现");

        initFindId();
        initClick();
    }

    private void initFindId() {
        traveller_circle = (LinearLayout) findViewById(R.id.find_traveller_circle_layout);
        traveller_choiceness = (LinearLayout) findViewById(R.id.find_traveller_choice_layout);
        traveller_detail = (LinearLayout) findViewById(R.id.find_traveller_detail_layout);
        traveller_forum = (LinearLayout) findViewById(R.id.find_traveller_forum_layout);
        traveller_scan = (LinearLayout) findViewById(R.id.find_traveller_scan_layout);
    }

    private void initClick() {
        traveller_circle.setOnClickListener(this);
        traveller_choiceness.setOnClickListener(this);
        traveller_detail.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        traveller_forum.setOnClickListener(this);
        traveller_scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                FindCenterActivity.this.finish();
                break;
            case R.id.find_traveller_circle_layout:
                Intent intent = new Intent(FindCenterActivity.this, TravellerCircleActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, traveller_circle, "traveller_circle").toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.find_traveller_choice_layout:
                Intent intent1 = new Intent(FindCenterActivity.this, TravellerChoiceActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(this, traveller_choiceness, "traveller_choice").toBundle());
                } else {
                    startActivity(intent1);
                }
                break;
            case R.id.find_traveller_detail_layout:
                Intent intent2 = new Intent(FindCenterActivity.this, TravellerShowActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(this, traveller_detail, "traveller_detail").toBundle());
                } else {
                    startActivity(intent2);
                }
                break;
            case R.id.find_traveller_forum_layout:
                Intent intent3 = new Intent(FindCenterActivity.this, TravellerForumActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(this, traveller_forum, "traveller_forum").toBundle());
                } else {
                    startActivity(intent3);
                }
                break;
            case R.id.find_traveller_scan_layout:
                Intent intent4 = new Intent(FindCenterActivity.this, TravellerQRScanActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent4, ActivityOptions.makeSceneTransitionAnimation(this, traveller_scan, "traveller_scan").toBundle());
                } else {
                    startActivity(intent4);
                }
                break;
        }
    }
}
