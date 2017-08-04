package com.treasure.traveldiary.activity.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.TravelApplication;
import com.treasure.traveldiary.activity.traveller.TravellerCenterActivity;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.receiver.CommonDataReceiver;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserCenterActivity extends AppCompatActivity implements View.OnClickListener {
    private PopupWindow mPopupWindow;
    private ImageView imageNight, mine_login_icon, imageHistory, imageSettings;
    private TextView mine_login_username;
    private String user_name;
    private EditText editPwd, editPhone;
    private FrameLayout signIn_layout, messagePush_layout, feedBack_layout, turing_layout;
    private TravelApplication application;
    private LinearLayout layoutNight, layoutHistory, layoutSettings;
    private ImageView pass_visible;
    private boolean isHind = true;
    private SharedPreferences mPreferences;
    private FrameLayout diary_layout;
    private IntentFilter filter;
    private CommonDataReceiver commonDataReceiver;
    private boolean pageLoaded;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:

                    break;
            }
        }
    };
    private FrameLayout evaluated_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        application = (TravelApplication) getApplication();
        Tools.setTranslucentStatus(this);

        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        receiveBoradCast();
        initFindId();
        initClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            if (!pageLoaded){
                receiveIntent();
                pageLoaded = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void receiveBoradCast() {
        filter = new IntentFilter();
        filter.addAction(StringContents.ACTION_COMMENTDATA);
        commonDataReceiver = new CommonDataReceiver();
        commonDataReceiver.setDoUIReceiver(new CommonDataReceiver.DoUIReceiver() {
            @Override
            public void doUI(Context context, Intent intent) {
                if (intent.getExtras().getString("label").equals("login")) {
                    String user_nick = mPreferences.getString("user_nick", "");
                    String user_icon = mPreferences.getString("user_icon", "");
                    if (!Tools.isNull(user_nick)) {
                        mine_login_username.setText(user_nick);
                    }
                    if (!Tools.isNull(user_icon)) {
                        if (user_icon.equals("暂无头像")){
                            mine_login_icon.setImageResource(R.mipmap.ic_travel_logo);
                        }else {
                            mine_login_icon.setImageURI(Uri.parse(user_icon));
                        }
                    }
                }
            }
        });
        registerReceiver(commonDataReceiver, filter);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("type"))){
            if (intent.getStringExtra("type").equals("toLogin")){
                LogUtil.d("~~~~~~~~~~~~~加载完毕");
                showPopupWindow();
            }
        }
    }
    private void initFindId() {
        mine_login_icon = (SimpleDraweeView) findViewById(R.id.mine_login_icon);
        mine_login_username = (TextView) findViewById(R.id.mine_login_username);
        imageNight = (ImageView) findViewById(R.id.mine_night_icon);
        layoutNight = (LinearLayout) findViewById(R.id.mine_night_layout);
        imageHistory = (ImageView) findViewById(R.id.mine_history_icon);
        layoutHistory = (LinearLayout) findViewById(R.id.mine_history_layout);
        imageSettings = (ImageView) findViewById(R.id.mine_settings_icon);
        layoutSettings = (LinearLayout) findViewById(R.id.mine_settings_layout);
        signIn_layout = (FrameLayout) findViewById(R.id.mine_signIn_layout);
        messagePush_layout = (FrameLayout) findViewById(R.id.mine_messagePush_layout);
        feedBack_layout = (FrameLayout) findViewById(R.id.mine_feedBack_layout);
        turing_layout = (FrameLayout) findViewById(R.id.mine_turing_layout);
        diary_layout = (FrameLayout) findViewById(R.id.mine_diary_layout);
        evaluated_layout = (FrameLayout) findViewById(R.id.mine_evaluated_layout);
    }

    private void initClick() {
        mine_login_icon.setOnClickListener(this);
        layoutNight.setOnClickListener(this);
        layoutHistory.setOnClickListener(this);
        layoutSettings.setOnClickListener(this);
        signIn_layout.setOnClickListener(this);
        messagePush_layout.setOnClickListener(this);
        feedBack_layout.setOnClickListener(this);
        turing_layout.setOnClickListener(this);
        diary_layout.setOnClickListener(this);
        evaluated_layout.setOnClickListener(this);
    }
    private void initView() {
        //用户名 头像
        String token = mPreferences.getString("token", "");
        if (!Tools.isNull(token)) {
            mine_login_icon.setImageResource(R.mipmap.ic_travel_logo);
            mine_login_username.setText(mPreferences.getString("user_nick", ""));
            BmobQuery<UserInfoBean> query = new BmobQuery<>();
            query.addWhereEqualTo("user_name", mPreferences.getString("user_name",""));
            query.findObjects(new FindListener<UserInfoBean>() {
                @Override
                public void done(List<UserInfoBean> list, BmobException e) {
                    if (e == null){
                        if (list.get(0).getUser_icon().equals("暂无头像")){
                            mine_login_icon.setImageResource(R.mipmap.ic_travel_logo);
                        }else {
                            mine_login_icon.setImageURI(Uri.parse(list.get(0).getUser_icon()));
                        }
                    }else {
                        Toast.makeText(UserCenterActivity.this, "原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            mine_login_icon.setImageResource(R.mipmap.ic_launcher_round);
            mine_login_username.setText("登录让内容更精彩");
        }
        //夜间模式
        if (application.isNight()) {
            imageNight.setImageResource(R.mipmap.ic_night);
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = 0.001f;
            window.setAttributes(layoutParams);
        } else {
            imageNight.setImageResource(R.mipmap.ic_daytime);
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = -1;
            window.setAttributes(layoutParams);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_login_icon:
                if (Tools.isNull(mPreferences.getString("token",""))){
                    //Login
                    showPopupWindow();
                }else {
                    //edit
                    Intent intent = new Intent(UserCenterActivity.this, UserEditUserInfoActivity.class);
                    intent.putExtra("edit_type","normal");
                    String user_name = mPreferences.getString("user_name", "");
                    intent.putExtra("UserPhone", user_name);
                    startActivity(intent);
                }
                break;
            case R.id.mine_history_layout:
                break;
            case R.id.mine_night_layout:
                nightSwitch();
                break;
            case R.id.mine_settings_layout:
                startActivity(new Intent(UserCenterActivity.this,UserSettingsActivity.class));
                break;
            case R.id.mine_signIn_layout:
                break;
            case R.id.mine_messagePush_layout:
                if (Tools.isNull(mPreferences.getString("token",""))){
                    showPopupWindow();
                }else {
                    Intent intent = new Intent(UserCenterActivity.this, UserMessageListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mine_feedBack_layout:

                break;
            case R.id.mine_diary_layout:
                if (Tools.isNull(mPreferences.getString("token",""))){
                    showPopupWindow();
                }else {
                    Intent intent = new Intent(UserCenterActivity.this, TravellerCenterActivity.class);
                    intent.putExtra("type","mine");
                    startActivity(intent);
                }
                break;
            case R.id.mine_evaluated_layout:
                if (Tools.isNull(mPreferences.getString("token",""))){
                    showPopupWindow();
                }else {
                    Intent intent = new Intent(UserCenterActivity.this, UserEvaluatedListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mine_turing_layout:
                break;
            case R.id.mine_popup_quit:
                quitpopupWindow();
                break;
            case R.id.mine_popup_loginin:
                Loginin();
                break;
            case R.id.mine_popup_register:
                mPopupWindow.dismiss();
                startActivity(new Intent(UserCenterActivity.this, UserRegisterActivity.class));
                break;
            case R.id.mine_popup_forget_password:
                mPopupWindow.dismiss();
                startActivity(new Intent(UserCenterActivity.this, UserForgetPassActivity.class));
                break;
            case R.id.mine_login_password_visible:
                if (isHind) {
                    initNoHindPassInput();
                    isHind = false;
                } else {
                    initHindPassInput();
                    isHind = true;
                }
                break;
        }
    }
    /**
     * 显示 关闭 popupWindow
     */
    public void showPopupWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.popupwindow_mine_login, null);
        mPopupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.loginPopupWindow);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x66000000));

        ImageView quit = (ImageView) convertView.findViewById(R.id.mine_popup_quit);
        editPhone = (EditText) convertView.findViewById(R.id.mine_login_phone);
        editPwd = (EditText) convertView.findViewById(R.id.mine_login_password);
        TextView login = (TextView) convertView.findViewById(R.id.mine_popup_loginin);
        TextView register = (TextView) convertView.findViewById(R.id.mine_popup_register);
        TextView forget = (TextView) convertView.findViewById(R.id.mine_popup_forget_password);
        pass_visible = (ImageView) convertView.findViewById(R.id.mine_login_password_visible);

        quit.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
        pass_visible.setOnClickListener(this);

        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_user_center, null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    private void quitpopupWindow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您确认放弃登录吗？");
        builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPopupWindow.dismiss();
            }
        });
        builder.setNegativeButton("继续登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
    /**
     * 夜间模式的切换
     */
    private void nightSwitch() {
        if (!application.isNight()) {
            imageNight.setImageResource(R.mipmap.ic_night);
            application.setNight(true);
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = 0.001f;
            window.setAttributes(layoutParams);
            application.setNight(true);
        } else {
            imageNight.setImageResource(R.mipmap.ic_daytime);
            application.setNight(false);
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = -1;
            window.setAttributes(layoutParams);
            application.setNight(false);
        }
    }
    //不隐藏密码
    private void initNoHindPassInput() {
        pass_visible.setImageResource(R.mipmap.ic_eye_open);
        editPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        editPwd.setSelection(editPwd.getText().length());
    }

    //隐藏密码
    private void initHindPassInput() {
        pass_visible.setImageResource(R.mipmap.ic_eye_close);
        editPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editPwd.setSelection(editPwd.getText().length());
    }

    /**
     * 登录操作
     */
    private void Loginin() {
        if (Tools.isNull(editPhone.getText().toString().trim())) {
            Toast.makeText(UserCenterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Tools.isNull(editPwd.getText().toString().trim())) {
            Toast.makeText(UserCenterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", editPhone.getText().toString().trim());
        query.findObjects(new FindListener<UserInfoBean>() {
            @Override
            public void done(List<UserInfoBean> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        Toast.makeText(UserCenterActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        if (editPwd.getText().toString().trim().equals(list.get(0).getUser_pwd())) {
                            Toast.makeText(UserCenterActivity.this, "恭喜你，登陆成功", Toast.LENGTH_SHORT).show();
                            //存入SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", "login");
                            editor.putString("user_icon", "暂无头像");
                            editor.putString("user_name", list.get(0).getUser_name());
                            editor.putString("user_nick", list.get(0).getNick_name());
                            editor.putString("user_pwd", list.get(0).getUser_pwd());
                            editor.putString("user_age", String.valueOf(list.get(0).getAge()));
                            editor.putString("user_sex", String.valueOf(list.get(0).getSex()));
                            editor.putString("user_desc", list.get(0).getUser_desc());
                            editor.apply();

                            //发送登录成功 广播
                            Intent intent = new Intent();
                            intent.setAction(StringContents.ACTION_COMMENTDATA);
                            intent.putExtra("label", "login");
                            sendBroadcast(intent);
                            mPopupWindow.dismiss();
                        } else {
                            Toast.makeText(UserCenterActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            editPwd.setText("");
                        }
                    }
                } else {
                    Toast.makeText(UserCenterActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(commonDataReceiver);
        super.onDestroy();
    }
}
