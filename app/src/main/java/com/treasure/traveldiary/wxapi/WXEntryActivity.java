package com.treasure.traveldiary.wxapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.TravelApplication;
import com.treasure.traveldiary.utils.HttpHelper;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.StringContents;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        //微信分享
        TravelApplication.iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.getType()) {
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                switch (baseResp.errCode) {
                    //发送成功(用户同意)
                    case BaseResp.ErrCode.ERR_OK:
                        Toast.makeText(this, "恭喜你，微信分享成功", Toast.LENGTH_SHORT).show();
                        this.finish();
                        break;
                    //发送取消(用户取消)
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        Toast.makeText(this, "微信分享取消", Toast.LENGTH_SHORT).show();
                        this.finish();
                        break;
                    //发送取消(用户拒绝授权，用户取消)
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        Toast.makeText(this, "微信分享取消", Toast.LENGTH_SHORT).show();
                        this.finish();
                        break;
                }
                break;
        }
    }
}
