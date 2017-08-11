package com.treasure.traveldiary.activity.user_center;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.home_page.DiaryImageCameraActivity;
import com.treasure.traveldiary.bean.LeaveMesBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserEditUserInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView editPhone;
    private EditText editNick;
    private EditText editPwd;
    private EditText editAge;
    private EditText editDesc;
    private RadioButton sex_man;
    private RadioButton sex_woman;
    private TextView btnEnter;
    private int sex;
    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    private Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
    private String edit_type;
    private ImageView pass_visible;
    private boolean isHind = true;
    private SimpleDraweeView editIcon;
    private String mFileUrl = "暂无头像";
    private String userPhone;
    private String mObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_user_info);
        initTitle();//基础activity里初始化标题栏
        Tools.setTranslucentStatus(this);//沉浸模式
        title.setText("信息修改");
        initFindId();
        receiveIntentData();
        initClick();
    }

    private void initFindId() {
        editPhone = (TextView) findViewById(R.id.mine_edit_phone);
        editNick = (EditText) findViewById(R.id.mine_edit_nickname);
        editPwd = (EditText) findViewById(R.id.mine_edit_password);
        editAge = (EditText) findViewById(R.id.mine_edit_age);
        editDesc = (EditText) findViewById(R.id.mine_edit_userDesc);
        sex_man = (RadioButton) findViewById(R.id.mine_edit_sex_man);
        sex_woman = (RadioButton) findViewById(R.id.mine_edit_sex_woman);
        btnEnter = (TextView) findViewById(R.id.btn_user_edit_enter);
        pass_visible = (ImageView) findViewById(R.id.mine_edit_pass_visible);
        editIcon = (SimpleDraweeView) findViewById(R.id.mine_edit_icon);
    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("UserPhone"))) {
            userPhone = intent.getStringExtra("UserPhone");
            editPhone.setText(userPhone);
            editNick.setText(userPhone);
        } else {
            editNick.setText("小王");
        }
        if (!Tools.isNull(intent.getStringExtra("edit_type"))) {
            edit_type = intent.getStringExtra("edit_type");
            if (edit_type.equals("normal")) {
                btn_back.setVisibility(View.VISIBLE);
                btn_back.setOnClickListener(this);
                btnEnter.setText("修改资料");
                BmobQuery<UserInfoBean> query = new BmobQuery<>();
                query.addWhereEqualTo("user_name", userPhone);
                query.findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                            mFileUrl = list.get(0).getUser_icon();
                            editIcon.setImageURI(Uri.parse(mFileUrl));
                        } else {
                            Toast.makeText(UserEditUserInfoActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        editAge.setText("0");
        sex_man.setChecked(true);
    }

    private void initClick() {
        btnEnter.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        pass_visible.setOnClickListener(this);
        editIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_edit_enter:
                if (Tools.isNull(editPwd.getText().toString().trim())) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Tools.isNull(editNick.getText().toString().trim())) {
                    Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Tools.isNull(editAge.getText().toString().trim())) {
                    Toast.makeText(this, "请输入年龄", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Tools.isNull(editDesc.getText().toString().trim())) {
                    Toast.makeText(this, "让更多人认识你", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Tools.isNull(edit_type)) {
                    if (edit_type.equals("normal")) {
                        updateAccount();
                    } else {
                        editRegister();
                    }
                }
                break;
            case R.id.btn_back:
                UserEditUserInfoActivity.this.finish();
                break;
            case R.id.mine_edit_pass_visible:
                if (isHind) {
                    isHind = false;
                    initNoHindPassInput();
                } else {
                    initHindPassInput();
                    isHind = true;
                }
                break;
            case R.id.mine_edit_icon:
                Intent intent = new Intent(UserEditUserInfoActivity.this, DiaryImageCameraActivity.class);
                startActivityForResult(intent, 200);
                break;
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

    private void updateAccount() {
        BmobQuery<UserInfoBean> query = new BmobQuery<UserInfoBean>();
        query.addWhereEqualTo("user_name", editPhone.getText().toString());
        query.findObjects(new FindListener<UserInfoBean>() {
            @Override
            public void done(List<UserInfoBean> object, BmobException e) {
                if (e == null) {
                    for (UserInfoBean gameScore : object) {
                        toUpdate(gameScore.getObjectId());
                    }
                }
            }
        });
    }

    private void toUpdate(String objectId) {
        if (sex_man.isChecked()) {
            sex = 0;
        } else if (sex_woman.isChecked()) {
            sex = 1;
        }
        final UserInfoBean infoBean = new UserInfoBean();
        infoBean.setUser_name(editPhone.getText().toString().trim());
        infoBean.setNick_name(editNick.getText().toString().trim() + "");
        infoBean.setUser_pwd(editPwd.getText().toString().trim());
        infoBean.setAge(Integer.parseInt(editAge.getText().toString().trim()));
        infoBean.setSex(sex);
        infoBean.setUser_desc(editDesc.getText().toString().trim() + "");
        infoBean.setUser_icon(mFileUrl);
        infoBean.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(UserEditUserInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    //存入SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", "login");
                    editor.putString("user_icon", infoBean.getUser_icon());
                    editor.putString("user_name", infoBean.getUser_name());
                    editor.putString("user_nick", infoBean.getNick_name());
                    editor.putString("user_pwd", infoBean.getUser_pwd());
                    editor.putString("user_age", String.valueOf(infoBean.getAge()));
                    editor.putString("user_sex", String.valueOf(infoBean.getSex()));
                    editor.putString("user_desc", infoBean.getUser_desc());
                    editor.apply();
                    //发送登录成功 广播
                    Intent intent = new Intent();
                    intent.setAction(StringContents.ACTION_COMMENTDATA);
                    intent.putExtra("label", "login");
                    sendBroadcast(intent);
                    UserEditUserInfoActivity.this.finish();
                } else {
                    Toast.makeText(UserEditUserInfoActivity.this, "更新失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editRegister() {
        /**
         * 查询是否重复
         */
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", editPhone.getText().toString().trim());
        query.count(UserInfoBean.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    if (integer > 0) {
                        Toast.makeText(UserEditUserInfoActivity.this, "手机号已被注册", Toast.LENGTH_SHORT).show();
                        UserEditUserInfoActivity.this.finish();
                    } else {
                        toRegister();
                    }
                } else {
                    Toast.makeText(UserEditUserInfoActivity.this, "原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toRegister() {
        /**
         * 注册
         */
        if (sex_man.isChecked()) {
            sex = 0;
        } else if (sex_woman.isChecked()) {
            sex = 1;
        }
        final UserInfoBean infoBean = new UserInfoBean();
        infoBean.setUser_name(editPhone.getText().toString().trim());
        infoBean.setNick_name(editNick.getText().toString().trim() + "");
        infoBean.setUser_pwd(editPwd.getText().toString().trim());
        infoBean.setAge(Integer.parseInt(editAge.getText().toString().trim()));
        infoBean.setSex(sex);
        infoBean.setUser_desc(editDesc.getText().toString().trim() + "");
        infoBean.setUser_icon(mFileUrl);
        infoBean.setIntegral_count(0);
        List<String> list = new ArrayList<>();
        list.add("0");
        infoBean.setSigning_date(list);
        LeaveMesBean leaveMesBean = new LeaveMesBean();
        leaveMesBean.setLeave_name(editPhone.getText().toString().trim());
        List<LeaveMesBean> leaveMesBeen = new ArrayList<>();
        leaveMesBeen.add(leaveMesBean);
        infoBean.setLeaveMesList(leaveMesBeen);
        infoBean.save(                                                                 new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(UserEditUserInfoActivity.this, "恭喜你，注册成功", Toast.LENGTH_SHORT).show();
                    //存入SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", "login");
                    editor.putString("user_icon", infoBean.getUser_icon());
                    editor.putString("user_name", infoBean.getUser_name());
                    editor.putString("user_nick", infoBean.getNick_name());
                    editor.putString("user_pwd", infoBean.getUser_pwd());
                    editor.putString("user_age", String.valueOf(infoBean.getAge()));
                    editor.putString("user_sex", String.valueOf(infoBean.getSex()));
                    editor.putString("user_desc", infoBean.getUser_desc());
                    editor.putInt("user_integral", infoBean.getIntegral_count());
                    editor.apply();
                    //发送登录成功 广播
                    Intent intent = new Intent();
                    intent.setAction(StringContents.ACTION_COMMENTDATA);
                    intent.putExtra("label", "login");
                    sendBroadcast(intent);
                    UserEditUserInfoActivity.this.finish();
                } else {
                    Toast.makeText(UserEditUserInfoActivity.this, "很遗憾，注册失败\n原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!Tools.isNull(edit_type)) {
            if (edit_type.equals("normal")) {
                super.onBackPressed();
            } else if (edit_type.equals("register")) {
                Toast.makeText(this, "请提交自己的信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {

                byte[] bitmaps = (byte[]) data.getExtras().get("camera_data");
                Bitmap bm_camera1 = BitmapFactory.decodeByteArray(bitmaps, 0, bitmaps.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                bm_camera1 = Bitmap.createBitmap(bm_camera1, 0, 0, bm_camera1.getWidth(), bm_camera1.getHeight(), matrix, true);

                final BmobFile bmobFile = new BmobFile(new File(Tools.saveBitmapToSD(UserEditUserInfoActivity.this, bm_camera1, "user_icon")));
                bmobFile.uploadblock(new UploadFileListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            mFileUrl = bmobFile.getFileUrl();
                            editIcon.setImageURI(Uri.parse(mFileUrl));
                            BmobQuery<UserInfoBean> query = new BmobQuery<>();
                            query.addWhereEqualTo("user_name", userPhone);
                            query.findObjects(new FindListener<UserInfoBean>() {

                                @Override
                                public void done(List<UserInfoBean> list, BmobException e) {
                                    mObjectId = list.get(0).getObjectId();
                                    UserInfoBean userInfoBean = new UserInfoBean();
                                    userInfoBean.setUser_icon(mFileUrl);
                                    userInfoBean.update(mObjectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                                editor.putString("user_icon", mFileUrl);
                                                editor.apply();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    }
                });
            }
        }
    }
}
