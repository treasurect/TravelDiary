package com.treasure.traveldiary.activity.find_center;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.treasure.traveldiary.BaseActivity;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.GameH5Activity;
import com.treasure.traveldiary.activity.find_center.qr_scan.camera.CameraManager;
import com.treasure.traveldiary.activity.find_center.qr_scan.decoding.CaptureActivityHandler;
import com.treasure.traveldiary.activity.find_center.qr_scan.decoding.InactivityTimer;
import com.treasure.traveldiary.activity.find_center.qr_scan.decoding.RGBLuminanceSource;
import com.treasure.traveldiary.activity.find_center.qr_scan.encoding.EncodingHandler;
import com.treasure.traveldiary.activity.find_center.qr_scan.view.ViewfinderView;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class TravellerQRScanActivity extends BaseActivity implements Callback, View.OnClickListener {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    private SharedPreferences preferences;
    private Bitmap user_QR_bitmap;
    private boolean isPageDestroy;
    private String user_QR_str;
    private TextView showUserQR;
    private PopupWindow mPopupWindow;
    private PopupWindow mPopupWindow2;
    private Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
    private Pattern p2 = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?", Pattern.CASE_INSENSITIVE);
    private int qr_type;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller_qrscan);
        initTitle();
        Tools.setTranslucentStatus(this);
        btn_back.setVisibility(View.VISIBLE);
        title.setText("扫描");
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        user_QR_str = "ct:"+preferences.getString("user_name","")+":"+preferences.getString("user_nick","");
        getUserQR();

        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        initFindId();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        initClick();
    }

    private void getUserQR() {
        try {
            user_QR_bitmap = EncodingHandler.createQRCode(user_QR_str, 300);//300表示宽高
        } catch (WriterException e) {
            if (!isPageDestroy) {
                Toast.makeText(this, "生成用户二维码失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initFindId() {
        viewfinderView = (ViewfinderView) findViewById(R.id.qrscan_viewfinder_content);
        surfaceView = (SurfaceView) findViewById(R.id.qrscan_scanner_view);
        showUserQR = (TextView) findViewById(R.id.qrscan_show_userQR);
    }

    private void initClick() {
        btn_back.setOnClickListener(this);
        showUserQR.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                TravellerQRScanActivity.this.finish();
                break;
            case R.id.qrscan_show_userQR:
                showUserQRWindow();
                break;
        }
    }

    private void showUserQRWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.popupwindow_user_qrscan, null);
        mPopupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.leaveMesPopupWindow);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x66000000));

        SimpleDraweeView qrscan_icon = (SimpleDraweeView) convertView.findViewById(R.id.popup_user_qrscan_icon);
        TextView qrscan_nick = (TextView) convertView.findViewById(R.id.popup_user_qrscan_nick);
        TextView qrscan_desc = (TextView) convertView.findViewById(R.id.popup_user_qrscan_desc);
        ImageView qrscan_sex = (ImageView) convertView.findViewById(R.id.popup_user_qrscan_sex);
        ImageView qrscan_image = (ImageView) convertView.findViewById(R.id.popup_user_qrscan_image);
        ImageView qrscan_close = (ImageView) convertView.findViewById(R.id.popup_user_qrscan_close);

        qrscan_icon.setImageURI(Uri.parse(preferences.getString("user_icon", "")));
        qrscan_nick.setText(preferences.getString("user_nick", ""));
        qrscan_desc.setText(preferences.getString("user_desc", ""));
        if (preferences.getString("user_sex", "").equals("男")) {
            qrscan_sex.setImageResource(R.mipmap.ic_sex_man);
        } else {
            qrscan_sex.setImageResource(R.mipmap.ic_sex_women);
        }
        if (user_QR_bitmap != null) {
            qrscan_image.setImageBitmap(user_QR_bitmap);
        }
        qrscan_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_traveller_qrscan, null);
        mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    private void showQRResultWindow(final String resultString) {
        View convertView = LayoutInflater.from(this).inflate(R.layout.popupwindow_qrscan_result, null);
        mPopupWindow2 = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow2.setAnimationStyle(R.style.leaveMesPopupWindow);
        mPopupWindow2.setOutsideTouchable(true);
        mPopupWindow2.setBackgroundDrawable(new ColorDrawable(0x66000000));

        TextView content = (TextView) convertView.findViewById(R.id.qrscan_result_content);
        TextView cancel = (TextView) convertView.findViewById(R.id.qrscan_result_cancel);
        TextView enter = (TextView) convertView.findViewById(R.id.qrscan_result_enter);
        if(resultString.length() > 2){
            if (resultString.substring(0,2).equals("ct")){
                String[] split = resultString.split(":");
                content.setText("是否添加关注："+split[1]);
                qr_type = 0;
            }else if (p.matcher(resultString).matches()){
                content.setText("是否call："+resultString);
                qr_type = 1;
            }else if (p2.matcher(resultString).matches()){
                content.setText("是否打开链接："+resultString);
                qr_type = 2;
            }else{
                content.setText("结果：" + resultString);
                qr_type = 3;
            }
        }else {
            content.setText("结果：" + resultString);
            qr_type = 3;
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow2.dismiss();
                resetQRScan();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qr_type == 0){
                    //添加粉丝
                    String[] split = resultString.split(":");
                    getUserInfo(split[1]);
                }else if (qr_type == 1){
                    //call
                    toCall(resultString);
                }else if (qr_type == 2){
                    toWeb(resultString);
                }else {

                }
            }
        });

        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_traveller_qrscan, null);
        mPopupWindow2.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    private void toWeb(String resultString) {
        if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
            mPopupWindow2.dismiss();
        }
        Intent intent = new Intent(TravellerQRScanActivity.this, GameH5Activity.class);
        intent.putExtra("game_type","扫描详情");
        intent.putExtra("web_url",resultString);
        startActivity(intent);
    }

    private void toCall(String resultString) {
        if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
            mPopupWindow2.dismiss();
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resultString));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void resetQRScan() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }
    private void getUserInfo(String user_name){
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name",user_name)
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null){
                            UserInfoBean userInfoBean = list.get(0);
                            toFans(userInfoBean);
                        }else {
                            if (!isPageDestroy){
                                if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
                                    mPopupWindow2.dismiss();
                                    resetQRScan();
                                }
                                Toast.makeText(TravellerQRScanActivity.this, "获取目标用户失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void toFans(final UserInfoBean infoBean) {
        //成为粉丝
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", infoBean.getUser_name())
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                            if (!isPageDestroy) {
                                String objectId = list.get(0).getObjectId();
                                List<SUserBean> fans = list.get(0).getFans();
                                for (int i = 0; i < fans.size(); i++) {
                                    if (fans.get(i).getLeave_name().equals(preferences.getString("user_name",""))){
                                        if (!isPageDestroy) {
                                            Toast.makeText(TravellerQRScanActivity.this, "已经关注过了", Toast.LENGTH_SHORT).show();
                                            if (mPopupWindow2 != null && mPopupWindow2.isShowing()) {
                                                mPopupWindow2.dismiss();
                                                resetQRScan();
                                            }
                                        }
                                        return;
                                    }
                                }
                                SUserBean SUserBean = new SUserBean();
                                SUserBean.setLeave_name(preferences.getString("user_name", ""));
                                SUserBean.setLeave_nick(preferences.getString("user_nick", ""));
                                SUserBean.setLeave_icon(preferences.getString("user_icon", ""));
                                SUserBean.setLeave_time(Tools.getNowTime());
                                SUserBean.setLeave_content(preferences.getString("user_desc", ""));
                                fans.add(SUserBean);

                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setFans(fans);
                                userInfoBean.update(objectId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            if (!isPageDestroy) {
                                                toAttention(infoBean);
                                            }
                                        } else {
                                            if (!isPageDestroy) {
                                                if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
                                                    mPopupWindow2.dismiss();
                                                    resetQRScan();
                                                }
                                                Toast.makeText(TravellerQRScanActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                        } else {
                            if (!isPageDestroy) {
                                if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
                                    mPopupWindow2.dismiss();
                                    resetQRScan();
                                }
                                Toast.makeText(TravellerQRScanActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void toAttention(final UserInfoBean infoBean) {
        //添加关注
        BmobQuery<UserInfoBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("user_name",preferences.getString("user_name",""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null){
                            if (!isPageDestroy){
                                String objectId = list.get(0).getObjectId();
                                List<SUserBean> attention = list.get(0).getAttention();
                                SUserBean SUserBean = new SUserBean();
                                SUserBean.setLeave_name(infoBean.getUser_name());
                                SUserBean.setLeave_nick(infoBean.getNick_name());
                                SUserBean.setLeave_icon(infoBean.getUser_icon());
                                SUserBean.setLeave_time(Tools.getNowTime());
                                SUserBean.setLeave_content(infoBean.getUser_desc());
                                attention.add(SUserBean);

                                UserInfoBean userInfoBean = new UserInfoBean();
                                userInfoBean.setAttention(attention);
                                userInfoBean.update(objectId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            if (!isPageDestroy){
                                                if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
                                                    mPopupWindow2.dismiss();
                                                    resetQRScan();
                                                }
                                                Toast.makeText(TravellerQRScanActivity.this, "恭喜你，关注成功", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            if (!isPageDestroy){
                                                if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
                                                    mPopupWindow2.dismiss();
                                                    resetQRScan();
                                                }
                                                Toast.makeText(TravellerQRScanActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                        }else {
                            if (!isPageDestroy){
                                if (mPopupWindow2 != null && mPopupWindow2.isShowing()){
                                    mPopupWindow2.dismiss();
                                    resetQRScan();
                                }
                                Toast.makeText(TravellerQRScanActivity.this, "关注失败，请重新关注", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.scanner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.scan_local:
//                //打开手机中的相册
//                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
//                innerIntent.setType("image/*");
//                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//                this.startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    //获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    mProgress = new ProgressDialog(TravellerQRScanActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
//                                Message m = handler.obtainMessage();
//                                m.what = R.id.decode_succeeded;
//                                m.obj = result.getText();
//                                handler.sendMessage(m);
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("qr_result_str", result.getText());
//                                Logger.d("saomiao",result.getText());
//                                bundle.putParcelable("bitmap",result.get);
                                resultIntent.putExtras(bundle);
                                TravellerQRScanActivity.this.setResult(RESULT_OK, resultIntent);

                            } else {
                                Message m = handler.obtainMessage();
                                m.what = R.id.decode_failed;
                                m.obj = "Scan failed!";
                                handler.sendMessage(m);
                            }
                        }
                    }).start();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            if (!isPageDestroy) {
                Toast.makeText(TravellerQRScanActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            showQRResultWindow(resultString);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        isPageDestroy = true;
        inactivityTimer.shutdown();
        super.onDestroy();
    }
}