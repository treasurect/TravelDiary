package com.treasure.traveldiary.activity.user_center;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;

public class UserSettingsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout cache_clear;
    private LinearLayout check_versions;
    private TextView logout;
    private ProgressDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    Toast.makeText(UserSettingsActivity.this, msg.getData().getString("hint"), Toast.LENGTH_SHORT).show();
                    requestLogout();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        initTitle();//基础activity里初始化标题栏
        Tools.setTranslucentStatus(this);//沉浸模式
        btn_back.setVisibility(View.VISIBLE);
        title.setText("设置");

        initFindViewById();
        if (Tools.isNull(getSharedPreferences("user",MODE_PRIVATE).getString("token",""))){
            logout.setVisibility(View.GONE);
        }
        initClick();
    }
    private void initFindViewById() {
        cache_clear = (LinearLayout) findViewById(R.id.user_settings_cache_clear);
        check_versions = (LinearLayout) findViewById(R.id.user_settings_check_versions);
        logout = (TextView) findViewById(R.id.user_personal_settings_logout);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        cache_clear.setOnClickListener(this);
        check_versions.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                UserSettingsActivity.this.finish();
                break;
            case R.id.user_settings_cache_clear:
                showClearDialog();
                break;
            case R.id.user_personal_settings_logout:
                requestLogout();
                break;
            case R.id.user_settings_check_versions:

                break;
        }
    }

    private void showClearDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要删除所有缓存吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog = new ProgressDialog(UserSettingsActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setTitle("提示");
                dialog.setMessage("正在清除缓存。。。");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            dialog.dismiss();
                            Message message = mHandler.obtainMessage(200);
                            Bundle bundle = new Bundle();
                            bundle.putString("hint", "缓存已清理");
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * 请求退出
     */
    private void requestLogout() {
        BaseActivity.aCache.clear();
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("token","");
        editor.apply();
        Intent intent = new Intent();
        intent.setAction(StringContents.ACTION_COMMENTDATA);
        intent.putExtra("label","login");
        sendBroadcast(intent);
        UserSettingsActivity.this.finish();
    }
}
