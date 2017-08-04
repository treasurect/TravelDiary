package com.treasure.traveldiary.activity.user;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class UserForgetPassActivity extends BaseActivity implements View.OnClickListener {
    private EditText editPhone;
    private EditText editCode;
    private TextView btnSendMes;
    private TextView btnNext;
    private String phoneNumber, codeNumber;
    private String dataCode;
    private Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //请求验证码
                case 200:
                    Toast.makeText(UserForgetPassActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                    openCountTimer();
                    break;
                //提交验证码
                case 201:
                    btnNext.setVisibility(View.GONE);
                    next_layout.setVisibility(View.VISIBLE);
                    break;
                //失败原因
                case 400:
                    Toast.makeText(UserForgetPassActivity.this, dataCode, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private CountDownTimer timer = null;
    private LinearLayout next_layout;
    private EditText editPass1;
    private EditText editPass2;
    private TextView btnSure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forget_pass);
        initTitle();
        btn_back.setVisibility(View.VISIBLE);
        title.setText("重置密码");
        initFindId();
        initClick();
        registerEvent();
    }
    private void initFindId() {
        editPhone = (EditText) findViewById(R.id.mine_forgetPass_phone);
        editCode = (EditText) findViewById(R.id.mine_forgetPass_verification_code);
        btnSendMes = (TextView) findViewById(R.id.user_forgetPass_send_message);
        btnNext = (TextView) findViewById(R.id.btn_user_forgetPass_next);
        next_layout = (LinearLayout) findViewById(R.id.mine_forgetPass_next_layout);
        editPass1 = (EditText) findViewById(R.id.mine_forgetPass_pass1);
        editPass2 = (EditText) findViewById(R.id.mine_forgetPass_pass2);
        btnSure = (TextView) findViewById(R.id.btn_user_forgetPass);
    }

    private void initClick() {
        btnNext.setOnClickListener(this);
        btnSendMes.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btnSure.setOnClickListener(this);
    }

    private void registerEvent() {
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                LogUtil.d("~~~~~~~~~~~~~~~~~~~~~~~~~~~1111", data.toString());
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        handler.sendMessage(handler.obtainMessage(201));

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                        handler.sendMessage(handler.obtainMessage(200));

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    String data1 = data.toString();
                    dataCode = data1.substring(45, data1.length() - 2);
                    handler.sendMessage(handler.obtainMessage(400));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_user_forgetPass_next:
                phoneNumber = editPhone.getText().toString().trim();
                codeNumber = editCode.getText().toString().trim();
                if (!phoneNumber.equals("") && !codeNumber.equals("")) {
                    verifySMSCode();
                } else {
                    Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.user_forgetPass_send_message:
                phoneNumber = editPhone.getText().toString().trim();
                if (isRequestSMSCode()) {
                    requestSMSCode();
                }
                break;
            case R.id.btn_user_forgetPass:
                String pass1 = editPass1.getText().toString().trim();
                String pass2 = editPass2.getText().toString().trim();
                if (Tools.isNull(pass1) || Tools.isNull(pass2)){
                    Toast.makeText(this, "请补全信息", Toast.LENGTH_SHORT).show();
                }else {
                    if (pass1.equals(pass2)){
                        updateAccount();
                    }else {
                        Toast.makeText(this, "您输入的两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean isRequestSMSCode() {
        Matcher matcher = p.matcher(phoneNumber);
        if (matcher.matches()) {
            return true;
        } else {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void requestSMSCode() {
        SMSSDK.getVerificationCode("86", phoneNumber);
    }

    private void openCountTimer() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSendMes.setClickable(false);
                btnSendMes.setText("获取（" + (millisUntilFinished / 1000) + "s）");
                btnSendMes.setTextSize(15.0f);
                btnSendMes.setTextColor(getResources().getColor(R.color.colorGray2));
            }

            @Override
            public void onFinish() {
                btnSendMes.setClickable(true);
                btnSendMes.setText("获取验证码");
                btnSendMes.setTextSize(18.0f);
                btnSendMes.setTextColor(getResources().getColor(R.color.colorBlock));
            }
        };
        timer.start();
    }

    private void verifySMSCode() {
        SMSSDK.submitVerificationCode("86", phoneNumber, codeNumber);
    }

    private void updateAccount() {
        BmobQuery<UserInfoBean> query = new BmobQuery<UserInfoBean>();
        query.addWhereEqualTo("user_name", editPhone.getText().toString());
        query.findObjects(new FindListener<UserInfoBean>() {
            @Override
            public void done(List<UserInfoBean> object, BmobException e) {
                if(e==null){
                    for (UserInfoBean gameScore : object) {
                        toUpdate(gameScore.getObjectId());
                    }
                }else {
                    Toast.makeText(UserForgetPassActivity.this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toUpdate(String objectId) {
        final UserInfoBean infoBean = new UserInfoBean();
        infoBean.setUser_pwd(editPass1.getText().toString().trim());
        infoBean.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(UserForgetPassActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
                    UserForgetPassActivity.this.finish();
                }else {
                    Toast.makeText(UserForgetPassActivity.this, "重置失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
