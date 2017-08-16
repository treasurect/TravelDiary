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
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserSettingsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout cache_clear;
    private LinearLayout btnCheck;
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
    private TextView version_num, version_state;
    private SharedPreferences mPreferences;
    private String version_name_online, version_name_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        initTitle();//基础activity里初始化标题栏
        Tools.setTranslucentStatus(this);//沉浸模式
        btn_back.setVisibility(View.VISIBLE);
        title.setText("设置");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        version_name_phone = Tools.getVersionName(this);

        initFindId();
        initClick();

        if (Tools.isNull(mPreferences.getString("token", ""))) {
            logout.setVisibility(View.GONE);
        }else {
            checkVersion();
        }
        version_num.setText("V " + version_name_phone);
    }

    private void initFindId() {
        cache_clear = (LinearLayout) findViewById(R.id.user_settings_cache_clear);
        btnCheck = (LinearLayout) findViewById(R.id.user_settings_check_versions);
        logout = (TextView) findViewById(R.id.user_personal_settings_logout);
        version_num = (TextView) findViewById(R.id.user_settings_versions_num);
        version_state = (TextView) findViewById(R.id.user_settings_versions_state);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        cache_clear.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
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
                if (!Tools.isNull(version_name_online) && !Tools.isNull(version_name_phone)){
                    if (version_name_online.equals(version_name_phone)){
                        Toast.makeText(this, "已是最新版本", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "请到应用商店更新应用", Toast.LENGTH_SHORT).show();
                    }
                }
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
        editor.putString("token", "");
        editor.apply();
        Intent intent = new Intent();
        intent.setAction(StringContents.ACTION_COMMENTDATA);
        intent.putExtra("label", "login");
        sendBroadcast(intent);
        UserSettingsActivity.this.finish();
    }

    private void checkVersion() {
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                             version_name_online = list.get(0).getVersion_name();
                            if (version_name_phone.equals(version_name_online)) {
                                version_state.setText("已是最新版本");
                            } else {
                                version_state.setText("有新版本更新");
                            }
                        } else {
                            Toast.makeText(UserSettingsActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
