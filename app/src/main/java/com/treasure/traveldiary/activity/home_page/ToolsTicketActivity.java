package com.treasure.traveldiary.activity.home_page;

import android.os.Bundle;
import android.view.View;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.Tools;

public class ToolsTicketActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_ticket);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("票务");

        initFindId();
        initClick();
    }

    private void initFindId() {

    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                ToolsTicketActivity.this.finish();
                break;
        }
    }
}
