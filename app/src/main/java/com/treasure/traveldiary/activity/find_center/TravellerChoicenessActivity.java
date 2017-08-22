package com.treasure.traveldiary.activity.find_center;
import android.os.Bundle;
import android.view.View;

import com.loopeer.cardstack.CardStackView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.Tools;

public class TravellerChoicenessActivity extends BaseActivity implements View.OnClickListener {

    private CardStackView cardStackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_choiceness);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("旅游精选");

        initFindId();
        initCardStackView();
        initClick();
    }

    private void initFindId() {
        cardStackView = (CardStackView) findViewById(R.id.traveller_choiceness_cardStackView);
    }
    private void initCardStackView() {
//        mTestStackAdapter = new TestStackAdapter(this);
//        mStackView.setAdapter(mTestStackAdapter);
//        mTestStackAdapter.updateData(Arrays.asList(TEST_DATAS));
    }
    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                TravellerChoicenessActivity.this.finish();
                break;
        }
    }
}
