package com.treasure.traveldiary.activity.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.Tools;

public class UserEvaluatedListActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_evaluated_list);
        initTitle();
        Tools.setTranslucentStatus(this);
        title.setText("我的点评/吐槽");
        btn_back.setVisibility(View.VISIBLE);
        iniFindId();
        initListView();
        initClick();
    }

    private void iniFindId() {

    }

    private void initListView() {

    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserEvaluatedListActivity.this.finish();
                break;
        }
    }
}
