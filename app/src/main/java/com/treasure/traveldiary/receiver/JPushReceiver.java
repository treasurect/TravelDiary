package com.treasure.traveldiary.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.treasure.traveldiary.activity.user_center.UserMessageListActivity;
import com.treasure.traveldiary.bean.PushBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.text.SimpleDateFormat;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by treasure on 2017/8/3.
 */

public class JPushReceiver extends BroadcastReceiver{
    private static final String TAG = "JPushReceiver";
    private NotificationManager nm;
    private SharedPreferences mPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        mPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);

        /**
         * t通知
         */
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            receivingNotification(context,bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            openNotification(context,bundle);

        } else {
            LogUtil.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(final Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        PushBean pushBean = new PushBean();
        pushBean.setTitle(title);
        pushBean.setMessage(message);
        String nowTime = Tools.getNowTime();
        pushBean.setTime(nowTime);
        if (!Tools.isNull(mPreferences.getString("token",""))){
            pushBean.setUser_name(mPreferences.getString("user_name",""));
        }
        pushBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    Toast.makeText(context, "收到了一条通知", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openNotification(Context context, Bundle bundle){
        context.startActivity(new Intent(context, UserMessageListActivity.class));
    }
}
