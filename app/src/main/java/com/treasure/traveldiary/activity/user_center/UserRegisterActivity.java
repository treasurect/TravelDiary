package com.treasure.traveldiary.activity.user_center;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText editPhone;
    private EditText editCode;
    private TextView btnSendMes;
    private TextView btnLogin;
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
                    Toast.makeText(UserRegisterActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                    openCountTimer();
                    break;
                //提交验证码
                case 201:
                    Toast.makeText(UserRegisterActivity.this, "恭喜你，验证成功,请完善你的信息", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserRegisterActivity.this, UserEditUserInfoActivity.class);
                    intent.putExtra("edit_type","register");
                    if (!Tools.isNull(editPhone.getText().toString().trim())) {
                        intent.putExtra("UserPhone", phoneNumber);
                    }
                    startActivity(intent);
                    UserRegisterActivity.this.finish();
                    break;
                //失败原因
                case 400:
                    Toast.makeText(UserRegisterActivity.this, dataCode, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private CountDownTimer timer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initTitle();//基础activity里初始化标题栏
        Tools.setTranslucentStatus(this);//沉浸模式
        btn_back.setVisibility(View.VISIBLE);
        title.setText("注册");
        initFindId();
        initClick();
        registerEvent();
    }
    private void initFindId() {
        editPhone = (EditText) findViewById(R.id.mine_register_phone);
        editCode = (EditText) findViewById(R.id.mine_register_verification_code);
        btnSendMes = (TextView) findViewById(R.id.user_send_message);
        btnLogin = (TextView) findViewById(R.id.btn_user_login);
    }

    private void initClick() {
        btnLogin.setOnClickListener(this);
        btnSendMes.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void registerEvent() {
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                LogUtil.d("~~~~~~~~~~~~~~~~~~~~~~~~~~~1111", data.toString());
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Message message = new Message();
                        message.what = 201;
                        handler.sendMessage(message);

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                        Message message = new Message();
                        message.what = 200;
                        handler.sendMessage(message);

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    String data1 = data.toString();
                    dataCode = data1.substring(45, data1.length() - 2);
                    Message message = new Message();
                    message.what = 400;
                    handler.sendMessage(message);
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
            case R.id.btn_user_login:
                phoneNumber = editPhone.getText().toString().trim();
                codeNumber = editCode.getText().toString().trim();
                if (!phoneNumber.equals("") && !codeNumber.equals("")) {
                    verifySMSCode();
                } else {
                    Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.user_send_message:
                phoneNumber = editPhone.getText().toString().trim();
                if (isRequestSMSCode()) {
                    requestSMSCode();
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


    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
