package com.treasure.traveldiary.activity.traveller;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.utils.Tools;

public class EvaluatedDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView addr;
    private SimpleDraweeView user_icon;
    private TextView user_name,user_time,user_title;
    private ImageView star1,star2,star3,star4,star5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluated_detail);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("详情");
        
        initFindId();
        receiveIntetnt();
        initClick();
    }

    private void initFindId() {
        user_icon = (SimpleDraweeView)findViewById(R.id.evaluated_detail_user_icon);
        user_name = (TextView)findViewById(R.id.evaluated_detail_user_name);
        user_time = (TextView)findViewById(R.id.evaluated_detail_user_time);
        user_title = (TextView)findViewById(R.id.evaluated_detail_user_title);
        star1 = (ImageView) findViewById(R.id.evaluated_detail_user_star1);
        star2 = (ImageView) findViewById(R.id.evaluated_detail_user_star2);
        star3 = (ImageView) findViewById(R.id.evaluated_detail_user_star3);
        star4 = (ImageView) findViewById(R.id.evaluated_detail_user_star4);
        star5 = (ImageView) findViewById(R.id.evaluated_detail_user_star5);
        addr = (TextView) findViewById(R.id.evaluated_detail_addr);
    }

    private void receiveIntetnt() {
        Intent intent = getIntent();
        EvaluatedBean evaluatedBean = (EvaluatedBean) intent.getExtras().get("evaluatedBean");
        if (evaluatedBean != null){
            if (!Tools.isNull(evaluatedBean.getUser_icon())){
                user_icon.setImageURI(Uri.parse(evaluatedBean.getUser_icon()));
            }
            if (!Tools.isNull(evaluatedBean.getUser_name())){
                user_name.setText(evaluatedBean.getUser_nick());
            }
            if (!Tools.isNull(evaluatedBean.getPublish_time())){
                user_time.setText(evaluatedBean.getPublish_time());
            }
            if (!Tools.isNull(evaluatedBean.getUser_evaluated())){
                user_title.setText(evaluatedBean.getUser_evaluated());
            }
            if (!Tools.isNull(evaluatedBean.getUser_addr())){
                addr.setText(evaluatedBean.getUser_addr());
            }
            int star_num = evaluatedBean.getStar_num();
            if (!Tools.isNull(String.valueOf(star_num))){
                if (star_num == 1){
                    star1.setImageResource(R.mipmap.ic_star_click);
                }
                if (star_num == 2){
                    star1.setImageResource(R.mipmap.ic_star_click);
                    star2.setImageResource(R.mipmap.ic_star_click);
                }
                if (star_num == 3){
                    star1.setImageResource(R.mipmap.ic_star_click);
                    star2.setImageResource(R.mipmap.ic_star_click);
                    star3.setImageResource(R.mipmap.ic_star_click);
                }
                if (star_num == 4){
                    star1.setImageResource(R.mipmap.ic_star_click);
                    star2.setImageResource(R.mipmap.ic_star_click);
                    star3.setImageResource(R.mipmap.ic_star_click);
                    star4.setImageResource(R.mipmap.ic_star_click);
                }
                if (star_num == 5){
                    star1.setImageResource(R.mipmap.ic_star_click);
                    star2.setImageResource(R.mipmap.ic_star_click);
                    star3.setImageResource(R.mipmap.ic_star_click);
                    star4.setImageResource(R.mipmap.ic_star_click);
                    star5.setImageResource(R.mipmap.ic_star_click);
                }
            }
        }
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                EvaluatedDetailActivity.this.finish();
                break;
        }
    }
}
