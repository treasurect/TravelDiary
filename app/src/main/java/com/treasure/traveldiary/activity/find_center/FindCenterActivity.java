package com.treasure.traveldiary.activity.find_center;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.Tools;

public class FindCenterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout traveller_circle;
    private LinearLayout traveller_choiceness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_center);
        initTitle();
        Tools.setTranslucentStatus(this);
        title.setText("发现");

        initFindId();
        initClick();
    }

    private void initFindId() {
        traveller_circle = (LinearLayout) findViewById(R.id.find_traveller_circle_layout);
        traveller_choiceness = (LinearLayout) findViewById(R.id.find_traveller_choiceness_layout);
    }

    private void initClick() {
        traveller_circle.setOnClickListener(this);
        traveller_choiceness.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_traveller_circle_layout:
                Intent intent = new Intent(FindCenterActivity.this, TravellerCircleActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, traveller_circle, "travellercircle").toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.find_traveller_choiceness_layout:
                Intent intent1 = new Intent(FindCenterActivity.this, TravellerChoicenessActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(this, traveller_choiceness, "travellerchioceness").toBundle());
                } else {
                    startActivity(intent1);
                }
                break;
        }
    }
}
