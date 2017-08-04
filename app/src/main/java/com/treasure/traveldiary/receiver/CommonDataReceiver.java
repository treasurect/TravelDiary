package com.treasure.traveldiary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by treasure on 2017/7/31.
 */

public class CommonDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        doUIReceiver.doUI(context, intent);
    }

    public interface DoUIReceiver {
        void doUI(Context context, Intent intent);
    }

    public DoUIReceiver doUIReceiver = null;

    public void setDoUIReceiver(DoUIReceiver doUIReceiver) {
        this.doUIReceiver = doUIReceiver;
    }
}
