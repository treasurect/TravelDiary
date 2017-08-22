package com.treasure.traveldiary;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobSDK;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.treasure.traveldiary.utils.StringContents;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by treasure on 2017/7/30.
 */

public class TravelApplication extends Application {
    private boolean isNight;
    private List<String> diary_image;
    public static IWXAPI iwxapi;

    @Override
    public void onCreate() {
        super.onCreate();
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        //Bmob后端云初始化
        Bmob.initialize(this, StringContents.Bmob_APPKEY);
        //fresco的初始化
        Fresco.initialize(this);
        //MobSMS初始化
        MobSDK.init(getApplicationContext(), StringContents.SMSSDK_APPKEY, StringContents.SMSSDK_APPSECRET);
        //JPUSH
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        // 微信
        iwxapi = WXAPIFactory.createWXAPI(this, StringContents.WeChat_APP_ID, false);
        iwxapi.registerApp(StringContents.WeChat_APP_ID);

        setNight(false);
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public List<String> getDiary_image() {
        return diary_image;
    }

    public void setDiary_image(List<String> diary_image) {
        this.diary_image = diary_image;
    }
}
