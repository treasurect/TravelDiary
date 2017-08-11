package com.treasure.traveldiary.activity.home_page;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.LeaveMesBean;
import com.treasure.traveldiary.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class DiaryImagePublishActivity extends BaseActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {
    private EditText diaryDesc;
    private SharedPreferences mPreferences;
    private String user_addr;
    private float user_lat;
    private float user_long;
    private EditText diaryTitle;
    private TextView diaryLoc;
    private ImageView image1, image2, image3;
    private FrameLayout loading;
    private Bitmap bm_camera1, bm_camera2, bm_camera3;
    private ImageView image_add;
    private List<String> list = new ArrayList<>();
    private boolean isSendDiary;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    sendDiary();
                    break;
                case 400:
                    Toast.makeText(DiaryImagePublishActivity.this, "上传失败：" + error, Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private String error;
    private ImageView state_img;
    private Spinner state_text;
    private ArrayAdapter<String> adapter;
    private String image_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_image_publish);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_send.setVisibility(View.VISIBLE);
        title.setText("记录生活");
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);

        initFindId();
        receiveData();
        initSpinner();
        initClick();
        btn_send.setClickable(false);
        image3.setClickable(false);
        btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
    }

    private void initFindId() {
        diaryDesc = (EditText) findViewById(R.id.diary_image_desc);
        diaryTitle = (EditText) findViewById(R.id.diary_image_title);
        diaryLoc = (TextView) findViewById(R.id.diary_image_loc);
        image1 = (ImageView) findViewById(R.id.diary_image_camera1);
        image2 = (ImageView) findViewById(R.id.diary_image_camera2);
        image3 = (ImageView) findViewById(R.id.diary_image_camera3);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        image_add = (ImageView) findViewById(R.id.diary_image_add);
        state_img = (ImageView) findViewById(R.id.diary_image_state_img);
        state_text = (Spinner) findViewById(R.id.diary_image_state_text);
    }

    private void receiveData() {
        Intent intent = getIntent();
        if (!Tools.isNull(intent.getStringExtra("user_addr"))) {
            user_addr = intent.getStringExtra("user_addr");
            diaryLoc.setText(user_addr);
        }
        if (!Tools.isNull(intent.getStringExtra("user_lat"))) {
            user_lat = Float.parseFloat(intent.getStringExtra("user_lat"));
        }
        if (!Tools.isNull(intent.getStringExtra("user_long"))) {
            user_long = Float.parseFloat(intent.getStringExtra("user_long"));
        }
        if (!Tools.isNull(intent.getByteArrayExtra("camera_data").toString())) {
            byte[] bitmaps = intent.getByteArrayExtra("camera_data");
            bm_camera1 = BitmapFactory.decodeByteArray(bitmaps, 0, bitmaps.length);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bm_camera1 = Bitmap.createBitmap(bm_camera1, 0, 0, bm_camera1.getWidth(), bm_camera1.getHeight(), matrix, true);
            image1.setImageBitmap(bm_camera1);

        }
    }

    private void initSpinner() {
        List<String> list = new ArrayList<>();
        list.add("公开");
        list.add("私密");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_text.setAdapter(adapter);
    }

    private void initClick() {
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        diaryDesc.addTextChangedListener(this);
        diaryTitle.addTextChangedListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        loading.setOnClickListener(this);
        state_text.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                DiaryImagePublishActivity.this.finish();
                break;
            case R.id.btn_send:
                uploadImage();
                break;
            case R.id.diary_image_camera1:
                Intent intent1 = new Intent(DiaryImagePublishActivity.this, DiaryImageCameraActivity.class);
                startActivityForResult(intent1, 201);
                break;
            case R.id.diary_image_camera2:
                Intent intent2 = new Intent(DiaryImagePublishActivity.this, DiaryImageCameraActivity.class);
                startActivityForResult(intent2, 202);
                break;
            case R.id.diary_image_camera3:
                Intent intent3 = new Intent(DiaryImagePublishActivity.this, DiaryImageCameraActivity.class);
                startActivityForResult(intent3, 203);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        image_state = adapter.getItem(i);
        if (image_state.equals("公开")) {
            state_img.setImageResource(R.mipmap.ic_lock_open);
        } else {
            state_img.setImageResource(R.mipmap.ic_lock_close);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        image_state = "公开";
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!Tools.isNull(diaryDesc.getText().toString().trim()) && !Tools.isNull(diaryTitle.getText().toString().trim())) {
            btn_send.setClickable(true);
            btn_send.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            btn_send.setClickable(false);
            btn_send.setTextColor(getResources().getColor(R.color.colorGray2));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void uploadImage() {
        loading.setVisibility(View.VISIBLE);
        //上传图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                int length;
                if (bm_camera2 == null) {
                    length = 1;
                } else if (bm_camera3 == null) {
                    length = 2;
                } else {
                    length = 3;
                }
                final String[] image_list = new String[length];
                if (bm_camera1 != null) {
                    String path1 = Tools.saveBitmapToSD(DiaryImagePublishActivity.this, bm_camera1, "bm_camera1");
                    image_list[0] = path1;
                }
                if (bm_camera2 != null) {
                    String path2 = Tools.saveBitmapToSD(DiaryImagePublishActivity.this, bm_camera2, "bm_camera2");
                    image_list[1] = path2;
                }
                if (bm_camera3 != null) {
                    String path3 = Tools.saveBitmapToSD(DiaryImagePublishActivity.this, bm_camera3, "bm_camera3");
                    image_list[2] = path3;
                }
                BmobFile.uploadBatch(image_list, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list1, List<String> list2) {
                        if (list2.size() == image_list.length) {
                            list.addAll(list2);
                            handler.sendMessage(handler.obtainMessage(200));
                        }
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {

                    }

                    @Override
                    public void onError(int i, String s) {
                        error = s;
                        handler.sendMessage(handler.obtainMessage(400));
                    }
                });
            }
        }).start();

    }

    private void sendDiary() {
        String textDesc = diaryDesc.getText().toString().trim();
        String textTitle = diaryTitle.getText().toString().trim();
        DiaryBean diaryBean = new DiaryBean();
        diaryBean.setUser_name(mPreferences.getString("user_name", ""));
        diaryBean.setUser_nick(mPreferences.getString("user_nick", ""));
        diaryBean.setUser_icon(mPreferences.getString("user_icon", ""));
        diaryBean.setUser_addr(user_addr);
        diaryBean.setUser_lat(user_lat);
        diaryBean.setUser_long(user_long);
        diaryBean.setDiary_type(1);
        diaryBean.setState(image_state);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Long(System.currentTimeMillis()));
        diaryBean.setPublish_time(time.substring(5, 7) + "月" + time.substring(8, 10) + "日" + time.substring(11, 16));
        diaryBean.setUser_desc(textDesc);
        diaryBean.setUser_title(textTitle);
        diaryBean.setDiary_image(list);
        //生成一个空的数据占位，以方便更新
        LeaveMesBean leaveMesBean = new LeaveMesBean();
        leaveMesBean.setLeave_name(mPreferences.getString("user_name", ""));
        List<LeaveMesBean> leaveMesBeen = new ArrayList<>();
        leaveMesBeen.add(leaveMesBean);
        diaryBean.setMesBeanList(leaveMesBeen);
        diaryBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(DiaryImagePublishActivity.this, "恭喜你，日记发表成功", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    DiaryImagePublishActivity.this.finish();
                } else {
                    Toast.makeText(DiaryImagePublishActivity.this, "很遗憾，发表失败\n原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            byte[] bitmaps = (byte[]) data.getExtras().get("camera_data");
            if (bitmaps != null) {
                bm_camera1 = BitmapFactory.decodeByteArray(bitmaps, 0, bitmaps.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                bm_camera1 = Bitmap.createBitmap(bm_camera1, 0, 0, bm_camera1.getWidth(), bm_camera1.getHeight(), matrix, true);
                image1.setImageBitmap(bm_camera1);
            }
        } else if (requestCode == 202 && resultCode == Activity.RESULT_OK) {
            byte[] bitmaps = (byte[]) data.getExtras().get("camera_data");
            if (bitmaps != null) {
                bm_camera2 = BitmapFactory.decodeByteArray(bitmaps, 0, bitmaps.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                bm_camera2 = Bitmap.createBitmap(bm_camera2, 0, 0, bm_camera2.getWidth(), bm_camera2.getHeight(), matrix, true);
                image2.setImageBitmap(bm_camera2);
                image_add.setVisibility(View.VISIBLE);
                image3.setClickable(true);
            }
        } else if (requestCode == 203 && resultCode == Activity.RESULT_OK) {
            byte[] bitmaps = (byte[]) data.getExtras().get("camera_data");
            if (bitmaps != null) {
                bm_camera3 = BitmapFactory.decodeByteArray(bitmaps, 0, bitmaps.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                bm_camera3 = Bitmap.createBitmap(bm_camera3, 0, 0, bm_camera3.getWidth(), bm_camera3.getHeight(), matrix, true);
                image3.setImageBitmap(bm_camera3);
            }
        }
    }
}
