package com.treasure.traveldiary.activity.traveller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.TravelApplication;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.widget.CustomScrollListView;

public class TravellerDiaryDetailActivity extends BaseActivity implements View.OnClickListener {

    private SimpleDraweeView user_icon;
    private TextView user_name, user_time, user_title, user_desc;
    private CustomScrollListView leaveMes_listView;
    private SimpleDraweeView detail_image1,detail_image2,detail_image3;
    private LinearLayout image_layout;
    private TravelApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_diary_detail);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("详情");

        application = (TravelApplication) getApplication();
        initFindId();
        receiveIntent();
        initClick();
    }

    private void initFindId() {
        user_icon = (SimpleDraweeView) findViewById(R.id.diary_detail_user_icon);
        user_name = (TextView) findViewById(R.id.diary_detail_user_name);
        user_time = (TextView) findViewById(R.id.diary_detail_user_time);
        user_title = (TextView) findViewById(R.id.diary_detail_user_title);
        user_desc = (TextView) findViewById(R.id.diary_detail_user_desc);
        leaveMes_listView = (CustomScrollListView) findViewById(R.id.diary_detail_leaveMes_list);
        detail_image1 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image1);
        detail_image2 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image2);
        detail_image3 = (SimpleDraweeView) findViewById(R.id.diary_detail_user_image3);
        image_layout = (LinearLayout) findViewById(R.id.diary_detail_user_image_layout);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        DiaryBean diaryBean = (DiaryBean) intent.getExtras().get("diaryBean");
        if (diaryBean !=null){
            if (!Tools.isNull(diaryBean.getUser_icon())){
                user_icon.setImageURI(Uri.parse(diaryBean.getUser_icon()));
            }
            if (!Tools.isNull(diaryBean.getUser_nick())){
                user_name.setText(diaryBean.getUser_nick());
            }
            if (!Tools.isNull(diaryBean.getPublish_time())){
                user_time.setText(diaryBean.getPublish_time());
            }
            if (!Tools.isNull(diaryBean.getUser_title())){
                user_title.setText(diaryBean.getUser_title());
            }
            if (!Tools.isNull(diaryBean.getUser_desc())){
                user_desc.setText(diaryBean.getUser_desc());
            }
            if (!Tools.isNull(intent.getStringExtra("type"))){
                if (intent.getStringExtra("type").equals("text")){
                    image_layout.setVisibility(View.GONE);
                }else {
                    if (diaryBean.getDiary_image() !=null){
                        if (diaryBean.getDiary_image().size() == 1){
                            detail_image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0).toString()));
                            detail_image2.setVisibility(View.GONE);
                            detail_image3.setVisibility(View.GONE);
                        }else if (diaryBean.getDiary_image().size() == 2){
                            detail_image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0).toString()));
                            detail_image2.setImageURI(Uri.parse(diaryBean.getDiary_image().get(1).toString()));
                            detail_image3.setVisibility(View.GONE);
                        }else if (diaryBean.getDiary_image().size() == 3){
                            detail_image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0).toString()));
                            detail_image2.setImageURI(Uri.parse(diaryBean.getDiary_image().get(1).toString()));
                            detail_image3.setImageURI(Uri.parse(diaryBean.getDiary_image().get(2).toString()));
                        }
                    }
                }
            }
        }
        if (!Tools.isNull(intent.getStringExtra("user_icon"))){
            user_icon.setImageURI(Uri.parse(intent.getStringExtra("user_nick")));
        }
        if (!Tools.isNull(intent.getStringExtra("user_nick"))){
            user_name.setText(intent.getStringExtra("user_nick"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_time"))){
            user_time.setText(intent.getStringExtra("user_time"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_title"))){
            user_title.setText(intent.getStringExtra("user_title"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_desc"))){
            user_desc.setText(intent.getStringExtra("user_desc"));
        }
        if (!Tools.isNull(intent.getStringExtra("type"))){
            if (intent.getStringExtra("type").equals("text")){
                image_layout.setVisibility(View.GONE);
            }else {
                if (application.getDiary_image() !=null){
                    if (application.getDiary_image().size() == 1){
                        detail_image1.setImageURI(Uri.parse(application.getDiary_image().get(0).toString()));
                        detail_image2.setVisibility(View.GONE);
                        detail_image3.setVisibility(View.GONE);
                    }else if (application.getDiary_image().size() == 2){
                        detail_image1.setImageURI(Uri.parse(application.getDiary_image().get(0).toString()));
                        detail_image2.setImageURI(Uri.parse(application.getDiary_image().get(1).toString()));
                        detail_image3.setVisibility(View.GONE);
                    }else if (application.getDiary_image().size() == 3){
                        detail_image1.setImageURI(Uri.parse(application.getDiary_image().get(0).toString()));
                        detail_image2.setImageURI(Uri.parse(application.getDiary_image().get(1).toString()));
                        detail_image3.setImageURI(Uri.parse(application.getDiary_image().get(2).toString()));
                    }
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
                TravellerDiaryDetailActivity.this.finish();
                break;
        }
    }
}
