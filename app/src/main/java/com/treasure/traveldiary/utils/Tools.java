/**
 *
 */
package com.treasure.traveldiary.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.treasure.traveldiary.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 各种工具类
 */
public class Tools {

    /**
     * 精确小数点后两位
     *
     * @param str;
     * @return String
     */
    public static String stringByFormat(double str) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        return df.format(str);
    }

    /**
     * 限制字数 ，超出+...
     *
     * @param str;
     * @return String
     */
    public static String getText(String str) {
        if (str == null) return "";
        if (str.length() > 15) {
            str = str.substring(0, 15);
            return str + "...";
        } else {
            return str;
        }
    }

    /**
     * 限制字数 ，超出+...
     *
     * @param str;
     * @return String
     */
    public static String getText1(String str) {
        if (str == null) return "";
        if (str.length() > 8) {
            str = str.substring(0, 8);
            return str + "...";
        } else {
            return str;
        }
    }

    /**
     * 限制字数 ，超出+...
     *
     * @param str;
     * @return String
     */
    public static String getText2(String str) {
        if (str == null) return "";
        if (str.length() > 8) {
            str = str.substring(0, 6);
            return str + "...";
        } else {
            return str;
        }
    }

    /**
     * 沉浸模式使用在其他页面
     *
     * @param context;
     */
    public static void setTranslucentStatus(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = context.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorOrange);// 状态栏无背景
    }

    /**
     * 沉浸模式使用在主页面
     *
     * @param context;
     */
    public static void setTranslucentStatusMainPager(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = context.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorOrange);// 状态栏无背景
    }

    /**
     * 获状态栏高度
     *
     * @param context 上下文
     * @return 通知栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int temp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 弹出日期选择器
     */
    public static void showDateDialog(Context context,
                                      DatePickerDialog.OnDateSetListener listener, Calendar mycalendar) {
        DatePickerDialog dialog = new DatePickerDialog(context, listener,
                mycalendar.get(Calendar.YEAR), mycalendar.get(Calendar.MONTH),
                mycalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 弹出吐司
     */
    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出吐司
     */
    public static void showToast(Context context, int content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 验证输入的邮箱格式是否符合
     *
     * @param email;
     * @return 是否合法
     */
    public static boolean emailFormat(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 通过字符串获取date对象
     */
    public static Date getDateByStr(String str) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 通过long转成年月日小时分钟星期时间格式字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateByStrYMDHME(String str) {
        try {
            Date date = new Date(Long.parseLong(str));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm EEEE", Locale.CHINA);
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过long转成年月日时间格式
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateStrYMD(String str) {
        try {
            Date date = new Date(Long.parseLong(str));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过long转成小时分钟时间格式
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateStrHM(String str) {
        try {
            Date date = new Date(Long.parseLong(str));
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过long转成月日时间格式
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateStrMD(String str) {
        try {
            Date date = new Date(Long.parseLong(str));
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", Locale.CHINA);
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过long转成年月日时分秒时间格式
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateStrYMDHMS(String str) {
        Date date = new Date(Long.parseLong(str));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过long转成月日时间格式
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateStrmd(String str) {
        Date date = new Date(Long.parseLong(str));
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", Locale.CHINA);
        try {
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过字符串获取date对象（包含年月日时分秒）
     */
    public static String obtainDateNowYMDHMS() {
        String dataString = null;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        try {
            dataString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataString;
    }

    /**
     * 通过字符串获取date对象（包含年月日时分）
     */
    public static String obtainDateNowYMDHM() {
        String dataString = null;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA);
        try {
            dataString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataString;
    }

    /**
     * 通过Date获取日期字符串对象
     */
    public static String getDateStr(Date date, String formateStr) {
        String dateStr = null;
        SimpleDateFormat formatter = new SimpleDateFormat(/*"yyyy-MM-dd HH:mm:ss"*/formateStr, Locale.CHINA);
        try {
            dateStr = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 显示时间
     * 60分钟以内
     * 24小时以内
     * 超过24小时显示日期
     */
    public static String showTime(String str) {
        if (isNull(str)) return "";
        long issueTime = Long.parseLong(str);
        long now = System.currentTimeMillis();
        if (now - issueTime < 1000 * 60 * 60) {
            if (((now - issueTime) / 1000 / 60) == 0) {
                return "刚刚";
            }
            return ((now - issueTime) / 1000 / 60) + "分钟前";
        }
        if (now - issueTime < 1000 * 60 * 60 * 24) {
            return ((now - issueTime) / 1000 / 60 / 60) + "小时前";
        }
        return getDateStrYMD(str);
    }

    /**
     * 将int转为16进制,如果数字在byte或short范围内通过补0方式使其成为双字节16进制
     */
    public static String int2Short16Str(int i) {
        String str16 = Integer.toHexString(i);
        if (str16.length() == 1) {
            str16 = "000" + str16;
        } else if (str16.length() == 2) {
            str16 = "00" + str16;
        } else if (str16.length() == 3) {
            str16 = "0" + str16;
        }
        return str16;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static File getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 1280f;//这里设置高度
        float ww = 800f;//这里设置宽度
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static File compressImage(Bitmap image) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.jpg");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 2048) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;//每次都减少10  
        }
        try {
            OutputStream out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.close();
            baos.close();
            image.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File compressImage(Bitmap image, int number) {
        File file = new File(Environment.getExternalStorageDirectory() + "/temp" + number + ".jpg");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 2048) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;//每次都减少10
        }
        try {
            OutputStream out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.close();
            baos.close();
            image.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String saveBitmapToSD(Context context, Bitmap bitmap, String name) {
        String absolutePath = context.getExternalFilesDir(null).getAbsolutePath();
        File file = new File(absolutePath + "/" + name + ".png");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            LogUtil.e("~~~~~~~~~~error~~~~~~~~~" + e.getMessage());
        }
        return file.getAbsolutePath();
    }

    /**
     * 判断是否为空
     */
    public static boolean isNull(String str) {
        return str == null || str.equals("") || str.equals("null") || str.equals("NULL");
    }

    /**
     * 获取SD卡路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            return null;
        }
        return sdDir.getAbsolutePath();
    }

    /**
     * 判断是否有SD卡
     *
     * @return true为有SDcard，false则表示没有
     */
    public static boolean hasSdcard() {
        boolean hasCard = false;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            hasCard = true;
        }
        return hasCard;
    }

    //获取联系人电话
    public static String getContactPhone(Context context, Cursor cursor) {
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            // int phoneCount = phones.getCount();
            // allPhoneNum = new ArrayList<String>(phoneCount);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            phoneResult = phoneNumber;
                            break;
                    }
                    // allPhoneNum.add(phoneNumber);
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }

    public static File save(JSONObject json) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.txt");
        try {
            if (!file.exists())
                file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            out.write(json.toString().getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getVersionName(Context context) {
        // 获取PackageManager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }

    public static String TransNumbersATC(int d) {
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String ss[] = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿"};
        String s = String.valueOf(d);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        String sss = String.valueOf(sb);
        int i = 0;
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, ss[i++]);
        }
        return sb.toString();
    }

    /**
     * 获取IMEI/MEID/ESN码 仅限于手机
     *
     * @param context
     * @return
     */
    public static String getDeviceMark(Context context) {
        String deviceid = ((TelephonyManager) context.getApplicationContext().getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        return deviceid;
    }

    /**
     * 获取设备的MAC地址 只有在wifi联网的状态才能获取到手机网卡的mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    //下载图片转化成bitmap
    public static Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 动画
     */
    public static void setAnimation(View view, int trans_x, int trans_y, int startAlpha, int endAlpha, int startRotation, int endRotation, float scaleMax, float scaleEnd, int duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", trans_x);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", trans_y);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scaleMax, scaleEnd);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scaleMax, scaleEnd);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translationX).with(translationY).with(alpha).with(rotation).with(scaleX).with(scaleY);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new AnticipateOvershootInterpolator());
        animatorSet.start();
    }
}
