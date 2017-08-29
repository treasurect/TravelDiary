package com.treasure.traveldiary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.utils.ACache;

public class BaseActivity extends FragmentActivity {

    public ImageView btn_back;
    public TextView title;
    public TextView btn_send;
    public static ACache aCache;
    public TextView btn_cancel;
    public LinearLayout mapLocLayout;
    public TextView text_map_loc;
    public TextView text_integral;
    public SimpleDraweeView user_icon;
    public Spinner list_city;
    public ImageView btn_more;
    public EditText edit_search;
    public ImageView btn_search_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化缓存
        if (aCache == null) {
            aCache = ACache.get(this);
        }
    }

    public void initTitle() {
        title = (TextView) findViewById(R.id.text_title);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_send = (TextView) findViewById(R.id.btn_send);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        mapLocLayout = (LinearLayout) findViewById(R.id.layout_map_loc);
        text_map_loc = (TextView) findViewById(R.id.text_map_loc);
        text_integral = (TextView) findViewById(R.id.text_integral);
        user_icon = (SimpleDraweeView) findViewById(R.id.image_user_icon);
        list_city = (Spinner) findViewById(R.id.list_city);
        btn_more = (ImageView) findViewById(R.id.btn_more);
        edit_search = (EditText)findViewById(R.id.edit_search);
        btn_search_cancel = (ImageView)findViewById(R.id.btn_search_cancel);
    }

}
