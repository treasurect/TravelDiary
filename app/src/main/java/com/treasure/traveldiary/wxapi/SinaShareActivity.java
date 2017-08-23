package com.treasure.traveldiary.wxapi;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.StringContents;

public class SinaShareActivity extends BaseActivity implements IWeiboHandler.Response {

    private IWeiboShareAPI weiboAPI;
    private String title = "旅游diary（第四版）  已经出炉了\n";//标题
    private String description = "旅游 途中会遇到很多美好的瞬间，倘若未记录下来到日后回忆会是一种很大的遗憾，为了不留下遗憾，记得用APP记录下你的美。";//描述
    private String url = "http://sj.qq.com/myapp/detail.htm?apkName=com.treasure.traveldiary";//分享链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sina_share);
        weiboAPI = WeiboShareSDK.createWeiboAPI(this, StringContents.Sina_APP_KEY);
        weiboAPI.registerApp();
        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            weiboAPI.handleWeiboResponse(getIntent(),
                    SinaShareActivity.this);
        }

        sendMultiMessage(title, description, url);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        weiboAPI.handleWeiboResponse(intent, SinaShareActivity.this);
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param -hasText    分享的内容是否有文本
     * @param -hasImage   分享的内容是否有图片
     * @param -hasWebpage 分享的内容是否有网页
     * @param -hasMusic   分享的内容是否有音乐
     * @param -hasVideo   分享的内容是否有视频
     * @param -hasVoice   分享的内容是否有声音
     */

    private void sendMultiMessage(String ShareTitle, String ShareDescription, String Url) {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj(ShareTitle + ShareDescription + Url);
        weiboMessage.imageObject = getImageObj();

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        //      weiboMessage.mediaObject = getWebpageObj(ShareTitle,ShareDescription, Url);

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;


        weiboAPI.sendRequest(request);//唤起微博客户端回调接口


        //这个也是唤起回调接口，可以监听唤起事件，没有微博客户端的时候调用网页登录
//            AuthInfo authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
//            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
//            String token = "";
//            if (accessToken != null) {
//                token = accessToken.getToken();
//            }
//            mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {
//
//                @Override
//                public void onWeiboException( WeiboException arg0 ) {
//                }
//
//                @Override
//                public void onComplete( Bundle bundle ) {
//                    // TODO Auto-generated method stub
//                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
//                    AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
//                    Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
//                }
//
//                @Override
//                public void onCancel() {
//                }
//            });
    }


    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String SharedText) {
        TextObject textObject = new TextObject();
        textObject.text = SharedText;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        //BitmapDrawable bitmapDrawable = drawable;
        Resources resources = getResources();
        Drawable draw = resources.getDrawable(R.mipmap.ic_travel_logo);

        imageObject.setImageObject(((BitmapDrawable) draw).getBitmap());

        return imageObject;
    }
    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(SinaShareActivity.this, "恭喜你，微博分享成功！",
                        Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(SinaShareActivity.this, "微博分享取消",
                        Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(SinaShareActivity.this, "错误", Toast.LENGTH_LONG).show();
                break;
        }
        this.finish();
    }
}
