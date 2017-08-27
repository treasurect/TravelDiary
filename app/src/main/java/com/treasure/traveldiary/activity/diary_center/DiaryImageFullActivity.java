package com.treasure.traveldiary.activity.diary_center;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.Tools;

public class DiaryImageFullActivity extends BaseActivity {

    private SimpleDraweeView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_image_full);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initFindId();
        initIntentReceiver();
    }

    private void initFindId() {
        img = (SimpleDraweeView) findViewById(R.id.detail_show_big_img);
    }

    private void initIntentReceiver() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("image_url"))){
            img.setImageURI(Uri.parse(intent.getStringExtra("image_url")));
        }
    }
}
