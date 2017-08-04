package com.treasure.traveldiary.utils;

import android.content.Context;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络访问帮助类
 * Created by Administrator on 2016/12/8.
 */

public class HttpHelper {

//    /**
//     * 获取OKHttp简单Get请求
//     * @param url：url携带的参数自拼
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @return RequestCall：回调需自定义
//     */
//    public static RequestCall getGetCall(String url, Context context){
//        return OkHttpUtils
//                        .get()
//                        .url(url)
//                        .tag(context)
//                        .build();
//    }
//
//    /**
//     * OKHttp执行简单Get请求，String类型响应的方法
//     * @param url：url携带的参数自拼
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @param stringCallback：string类型的response回调监听器，原始的response后续自行处理
//     */
//    public static void doGetCall(String url,Context context,StringCallback stringCallback){
//                OkHttpUtils
//                .get()
//                .url(url)
//                .tag(context)
//                .build().execute(stringCallback);
//    }
//
//    //项目接口统一采用post 此方法备用
//    public static void doGetCallWithNoBaseurl(String url,Context context,StringCallback stringCallback){
//        OkHttpUtils
//                .get()
//                .url(url)
//                .tag(context)
//                .build().execute(stringCallback);
//    }
//    /**
//     * 获取OKHttp传递Json数据的Post请求
//     * @param url：地址
//     * @param object：json对象
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @return RequestCall：回调需自定义
//     */
//    public static RequestCall getJsonPostCall(String url, Object object, Context context){
//        return OkHttpUtils.postString().url(url)
//                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                        .content(new Gson().toJson(object))
//                        //以页面为单位设置tag，再对应生命周期中取消请求
//                        .tag(context)
//                        .build();
//    }
//
//    /**
//     * OKHttp执行Json格式参数的Post请求，String类型响应的方法
//     * @param url：地址
//     * @param object：json对象
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @param stringCallback：string类型的response回调监听器，原始的response后续自行处理
//     */
//    public static void doJsonPostCall(String url, Object object, Context context,StringCallback stringCallback){
//        OkHttpUtils.postString().url(url)
//                .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                .content(new Gson().toJson(object))
//                //以页面为单位设置tag，再对应生命周期中取消请求
//                .tag(context)
//                .build().execute(stringCallback);
//    }
//
//    /**
//     * 获取OKHttp上传单个文件的post请求
//     * @param url:地址
//     * @param file:文件
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @return RequestCall：回调需自定义
//     */
//    public static RequestCall getupLoadFileCall(String url, File file, Context context){
//        return OkHttpUtils.postFile()
//                .url(url)
//                .file(file)
//                .tag(context)
//                .build();
//    }
//
//    /**
//     * OKHttp执行上传单个文件的post请求
//     * @param url:地址
//     * @param file:文件
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @param stringCallback：string类型的response回调监听器，原始的response后续自行处理
//     */
//    public static void doUpLoadFileCall(String url, File file, Context context,StringCallback stringCallback){
//        OkHttpUtils.postFile()
//                .url(url)
//                .file(file)
//                .tag(context)
//                .build().execute(stringCallback);
//    }
//
//    /**
//     * OKHttp执行下载文件的请求
//     * @param url:地址
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @param fileCallBack：文件类型的response回调监听器
//     */
//    public static void doDownLoadFileCall(String url,Context context, FileCallBack fileCallBack){
//        OkHttpUtils
//                .get()
//                .url(url)
//                .tag(context)
//                .build()
//                .execute(fileCallBack);
//    }
//
//    /**
//     * OKHttp执行下载图片的请求
//     * 特殊情况使用okhttp获取bitmap对象，一般情况建议使用fresco
//     * @param url:地址
//     * @param context:页面--页面关闭时批量处理网络请求 eg：Activity的onDestroy方法中执行：OkHttpUtils.getInstance().cancelTag(this);
//     * @param bitmapCallback：Bitmap类型的response回调监听器
//     */
//    public static void doDownLoadBitmapCall(String url, Context context, BitmapCallback bitmapCallback){
//        OkHttpUtils
//                .get()
//                .url(url)
//                .tag(context)
//                .build()
//                .execute(bitmapCallback);
//    }

    /**
     * okhttp的get请求
     */
    public static void doGetCall(String url, Context context, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .tag(context)
                .build();
        new OkHttpClient().newCall(request).enqueue(callback);
    }

    /**
     * okhttp的post请求
     */
    public static void doPostCall(String url, Context context, FormBody body, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .tag(context)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
