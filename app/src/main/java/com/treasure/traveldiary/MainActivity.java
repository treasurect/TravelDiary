package com.treasure.traveldiary;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.activity.add.DiaryImageCameraActivity;
import com.treasure.traveldiary.activity.add.DiaryImagePublishActivity;
import com.treasure.traveldiary.activity.add.DiaryEvaluatedActivity;
import com.treasure.traveldiary.activity.add.DiaryVideoCameraActivity;
import com.treasure.traveldiary.activity.add.DiaryVideoPublishActivity;
import com.treasure.traveldiary.activity.traveller.DiaryCenterActivity;
import com.treasure.traveldiary.activity.traveller.TravellerDiaryDetailActivity;
import com.treasure.traveldiary.activity.add.DiaryTextPublishActivity;
import com.treasure.traveldiary.activity.user.UserDiaryActivity;
import com.treasure.traveldiary.activity.user.UserCenterActivity;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.MapMarkerInfoBean;
import com.treasure.traveldiary.listener.MapOrientationListener;
import com.treasure.traveldiary.utils.Tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity implements View.OnClickListener, BDLocationListener, BaiduMap.OnMapLoadedCallback, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener, RadioGroup.OnCheckedChangeListener {

    private LinearLayout travellerLayout;
    private LinearLayout settingsLayout;
    private MapView mapView;
    private BaiduMap map;
    private double user_latitude = 39.915168, user_longitude = 116.403875;
    private double destination_latitude, destination_longitude;
    private float radius = 0;
    public LocationClient mLocationClient = null;
    private float mCurrentX;
    private LatLng p1, p2;
    private List<LatLng> point_list;
    private OverlayOptions ooPolyline;
    private final static double DEF_PI = 3.14159265359; // PI
    private final static double DEF_2PI = 6.28318530712; // 2*PI
    private final static double DEF_PI180 = 0.01745329252; // PI/180.0
    private final static double DEF_R = 6370693.5; // 地球半径，m
    private MapOrientationListener orientationListener;
    private BitmapDescriptor marker_bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker);
    private String mapMes_error;
    private boolean mapMes_error_send;
    private boolean loading_gone_send;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    MyLocationData data = new MyLocationData.Builder()
                            .accuracy(radius)
                            .direction(mCurrentX)
                            .latitude(user_latitude)
                            .longitude(user_longitude)
                            .build();
                    map.setMyLocationData(data);
                    MyLocationConfiguration config = new MyLocationConfiguration(mode, true, null);
                    map.setMyLocationConfigeration(config);
                    text_map_loc.setText(user_addr);
                    break;
                case 400:
                    Toast.makeText(MainActivity.this, mapMes_error, Toast.LENGTH_SHORT).show();
                    break;
                case 201:
                    loading.setVisibility(View.VISIBLE);
                    break;
                case 401:
                    loading.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private boolean isPageDestroy;
    private FrameLayout loading;
    private FloatingActionButton btnAdd,btnDiaryText,btnDiaryImage,btnDiaryEvaluated,btnDiaryVideo;
    private boolean addShow;
    private int widthPixels;
    private String user_city;
    private String user_addr = "北京市";
    private SharedPreferences mPreferences;
    private InfoWindow mInfoWindow;
    private ImageView addLayout;
    private RadioGroup map_type;
    private RadioButton map_type_normal;
    private RadioButton map_type_satellite;
    private RadioButton map_type_compass;
    private MyLocationConfiguration.LocationMode mode = MyLocationConfiguration.LocationMode.NORMAL;
    private ImageView show_location;
    private TravelApplication application;
    private TextView text_t,image_t, video_t,evaluated_t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tools.setTranslucentStatus(this);
        initTitle();
        mapLocLayout.setVisibility(View.VISIBLE);

        widthPixels = getResources().getDisplayMetrics().widthPixels;
        application = (TravelApplication) getApplication();
        initFindId();
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        useLocationOrientationListener();
        initMap();
        initLocation();
        initClick();
    }

    private void initFindId() {
        travellerLayout = (LinearLayout) findViewById(R.id.main_travel_layout);
        settingsLayout = (LinearLayout) findViewById(R.id.main_settings_layout);
        mapView = (MapView) findViewById(R.id.home_mapView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        btnAdd = (FloatingActionButton) findViewById(R.id.main_add_image);
        btnDiaryText = (FloatingActionButton) findViewById(R.id.add_diary_text);
        btnDiaryImage = (FloatingActionButton) findViewById(R.id.add_diary_image);
        btnDiaryVideo = (FloatingActionButton) findViewById(R.id.add_diary_video);
        btnDiaryEvaluated = (FloatingActionButton) findViewById(R.id.add_diary_evaluated);
        addLayout = (ImageView) findViewById(R.id.add_diary_layout);
        map_type = (RadioGroup) findViewById(R.id.layout_map_type);
        map_type_normal = (RadioButton) findViewById(R.id.btn_map_normal);
        map_type_satellite = (RadioButton) findViewById(R.id.btn_map_satellite);
        map_type_compass = (RadioButton) findViewById(R.id.btn_map_compass);
        show_location = (ImageView) findViewById(R.id.user_map_location);
        text_t = (TextView) findViewById(R.id.add_diary_text_t);
        image_t = (TextView) findViewById(R.id.add_diary_image_t);
        video_t = (TextView) findViewById(R.id.add_diary_video_t);
        evaluated_t = (TextView) findViewById(R.id.add_diary_evaluated_t);
    }

    private void useLocationOrientationListener() {
        orientationListener = new MapOrientationListener(this);
        orientationListener.start();
        orientationListener.setMapOrientationListener(new MapOrientationListener.onOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {//监听方向的改变，方向改变时，需要得到地图上方向图标的位置
                mCurrentX = x;
            }
        });
    }

    private void initMap() {
        // 隐藏百度的LOGO
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
//        mapView.showScaleControl(false);// 不显示地图上比例尺
//        mapView.showZoomControls(false);// 不显示地图缩放控件（按钮控制栏）
        map = mapView.getMap();
        map.setIndoorEnable(true);//开启室内图

        map_type_normal.setChecked(true);
        map_type_normal.setTextColor(getResources().getColor(R.color.colorWhite));
        map_type_satellite.setTextColor(getResources().getColor(R.color.colorBlock));
        map_type_compass.setTextColor(getResources().getColor(R.color.colorBlock));
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //定位初始化
        mLocationClient.registerLocationListener(this);//定位注册

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setNeedDeviceDirect(true);
        mLocationClient.setLocOption(option);
        map.setOnMapLoadedCallback(this);
    }

    private void initClick() {
        travellerLayout.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        settingsLayout.setOnClickListener(this);
        loading.setOnClickListener(this);
        btnDiaryText.setOnClickListener(this);
        btnDiaryImage.setOnClickListener(this);
        btnDiaryVideo.setOnClickListener(this);
        btnDiaryEvaluated.setOnClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
        addLayout.setOnClickListener(this);
        map_type.setOnCheckedChangeListener(this);
        show_location.setOnClickListener(this);
        text_t.setOnClickListener(this);
        image_t.setOnClickListener(this);
        video_t.setOnClickListener(this);
        evaluated_t.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_travel_layout:
                Intent intent1 = new Intent(MainActivity.this, DiaryCenterActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(this, travellerLayout, "traveller").toBundle());
                } else {
                    startActivity(intent1);
                }
                break;
            case R.id.main_add_image:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    Intent intent = new Intent(MainActivity.this, UserCenterActivity.class);
                    intent.putExtra("type", "toLogin");
                    startActivity(intent);
                } else {
                    if (!addShow) {
                        showFloatActionButton();
                    } else {
                        hindFloatActionButton();
                    }
                }
                break;
            case R.id.add_diary_text:
            case R.id.add_diary_text_t:
                Intent intent2 = new Intent(MainActivity.this, DiaryTextPublishActivity.class);
                intent2.putExtra("user_addr", user_addr);
                intent2.putExtra("user_lat", String.valueOf(user_latitude));
                intent2.putExtra("user_long", String.valueOf(user_longitude));
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(this, btnDiaryText, "diary_text").toBundle());
                } else {
                    startActivity(intent2);
                }
                hindFloatActionButton();
                break;
            case R.id.add_diary_image:
            case R.id.add_diary_image_t:
                Intent intent3 = new Intent(MainActivity.this, DiaryImageCameraActivity.class);
                startActivityForResult(intent3, 200);
                hindFloatActionButton();
                break;
            case R.id.add_diary_video:
            case R.id.add_diary_video_t:
                Intent intent6 = new Intent(MainActivity.this, DiaryVideoCameraActivity.class);
                startActivityForResult(intent6, 201);
                hindFloatActionButton();
                break;
            case R.id.add_diary_evaluated:
            case R.id.add_diary_evaluated_t:
                Intent intent5 = new Intent(MainActivity.this, DiaryEvaluatedActivity.class);
                intent5.putExtra("user_lat",String.valueOf(user_latitude));
                intent5.putExtra("user_lon",String.valueOf(user_longitude));
                intent5.putExtra("user_addr",user_addr);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent5, ActivityOptions.makeSceneTransitionAnimation(this, btnDiaryEvaluated, "diary_evaluated").toBundle());
                } else {
                    startActivity(intent5);
                }
                hindFloatActionButton();
                break;
            case R.id.add_diary_layout:
                if (addShow) {
                    hindFloatActionButton();
                }
                break;
            case R.id.user_map_location:
                showUserLocation();
                break;
            case R.id.main_settings_layout:
                Intent intent = new Intent(MainActivity.this, UserCenterActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, settingsLayout, "settings").toBundle());
                } else {
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if (radioButton.getText().equals("普通")) {
            map_type_normal.setTextColor(getResources().getColor(R.color.colorWhite));
            map_type_satellite.setTextColor(getResources().getColor(R.color.colorBlock));
            map_type_compass.setTextColor(getResources().getColor(R.color.colorBlock));
            map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            mode = MyLocationConfiguration.LocationMode.NORMAL;
        } else if (radioButton.getText().equals("卫星")) {
            map_type_normal.setTextColor(getResources().getColor(R.color.colorBlock));
            map_type_satellite.setTextColor(getResources().getColor(R.color.colorWhite));
            map_type_compass.setTextColor(getResources().getColor(R.color.colorBlock));
            map.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            mode = MyLocationConfiguration.LocationMode.NORMAL;
        } else if (radioButton.getText().equals("罗盘")) {
            map_type_normal.setTextColor(getResources().getColor(R.color.colorBlock));
            map_type_satellite.setTextColor(getResources().getColor(R.color.colorBlock));
            map_type_compass.setTextColor(getResources().getColor(R.color.colorWhite));
            mode = MyLocationConfiguration.LocationMode.COMPASS;
        }
    }

    private void showFloatActionButton() {
        Tools.setAnimation(btnDiaryText, -widthPixels / 3, -330, 0, 1,0,360,1,1,500);
        Tools.setAnimation(btnDiaryImage, -widthPixels/9, -330, 0, 1,0,360,1,1,500);
        Tools.setAnimation(btnDiaryVideo, widthPixels / 9, -330, 0, 1,0,-360,1,1,500);
        Tools.setAnimation(btnDiaryEvaluated, widthPixels / 3, -330, 0, 1,0,-360,1,1,500);
        Tools.setAnimation(text_t, -widthPixels / 3, -250, 0, 1,0,360,1,1,500);
        Tools.setAnimation(image_t, -widthPixels/9, -250, 0, 1,0,360,1,1,500);
        Tools.setAnimation(video_t, widthPixels / 9, -250, 0, 1,0,-360,1,1,500);
        Tools.setAnimation(evaluated_t, widthPixels / 3, -250, 0, 1,0,-360,1,1,500);
        Tools.setAnimation(btnAdd, 0,0,1,1,0, -45,1,1,500);
        addLayout.setVisibility(View.VISIBLE);
        addLayout.setClickable(true);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(addLayout, "alpha", 0, 1);
        alpha.setDuration(500);
        alpha.start();
        addShow = true;
    }

    private void hindFloatActionButton() {
        Tools.setAnimation(btnDiaryText, 0, 0, 1, 0,-360,0,1,1,500);
        Tools.setAnimation(btnDiaryImage, 0, 0, 1, 0,-360,0,1,1,500);
        Tools.setAnimation(btnDiaryVideo, 0, 0, 1, 0,360,0,1,1,500);
        Tools.setAnimation(btnDiaryEvaluated, 0, 0, 1, 0,360,0,1,1,500);
        Tools.setAnimation(text_t, 0, 0, 1, 0,-360,0,1,1,500);
        Tools.setAnimation(image_t, 0, 0, 1, 0,-360,0,1,1,500);
        Tools.setAnimation(video_t, 0, 0, 1, 0,360,0,1,1,500);
        Tools.setAnimation(evaluated_t, 0, 0, 1, 0,360,0,1,1,500);
        Tools.setAnimation(btnAdd,0,0,1,1, -45, 0,1,1,500);
        addLayout.setClickable(false);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(addLayout, "alpha", 1, 0);
        alpha.setDuration(500);
        alpha.start();
        addShow = false;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
// 定位接口可能返回错误码,要根据结果错误码,来判断是否是正确的地址;
        int locType = bdLocation.getLocType();
        switch (locType) {
            case BDLocation.TypeCacheLocation:
            case BDLocation.TypeOffLineLocation:
            case BDLocation.TypeGpsLocation:
            case BDLocation.TypeNetWorkLocation:

                radius = bdLocation.getRadius();
                user_latitude = bdLocation.getLatitude();
                user_longitude = bdLocation.getLongitude();
                user_city = bdLocation.getCity().substring(0, bdLocation.getCity().length() - 1);
                user_addr = bdLocation.getAddrStr().replace(bdLocation.getCountry(), "").replace(bdLocation.getCity(), "");
                handler.sendMessage(handler.obtainMessage(200));
                if (!loading_gone_send) {
                    handler.sendMessage(handler.obtainMessage(401));
                    loading_gone_send = true;
                }
                break;
            default:
                mapMes_error = bdLocation.getLocTypeDescription();
                if (!mapMes_error_send) {
                    handler.sendMessage(handler.obtainMessage(400));
                    handler.sendMessage(handler.obtainMessage(401));
                    mapMes_error_send = true;
                }
                break;
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public void onMapLoaded() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.sendMessage(handler.obtainMessage(201));
                    Thread.sleep(3000);
                    if (!isPageDestroy) {
                        handler.sendMessage(handler.obtainMessage(401));
                        LatLng latLng = new LatLng(user_latitude, user_longitude);
                        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(19);// 设置地图放大比例
                        map.setMapStatus(msu);
                        msu = MapStatusUpdateFactory.newLatLng(latLng);
                        map.animateMapStatus(msu);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //线段绘制
    public void drawLines() {
//        double sub_latitude = destination_latitude - latitude;
//        double sub_longitude = destination_longitude - longitude;
//        overlay.remove();
        point_list = new ArrayList<>();
        p1 = new LatLng(user_latitude, user_longitude);
        p2 = new LatLng(destination_latitude, destination_longitude);
//        int j = 0;
//        for (int i = 0; i < 20; i++) {
//            p1 = new LatLng(latitude + sub_latitude * j / 40, longitude + sub_longitude * j / 40);
//            p2 = new LatLng(latitude + sub_latitude * (j + 1) / 40, longitude + sub_longitude * (j + 1) / 40);
//            point_list.clear();
        point_list.add(p1);
        point_list.add(p2);
        ooPolyline = new PolylineOptions().dottedLine(true).width(8).color(0xAA666666).points(point_list).visible(true);
        map.addOverlay(ooPolyline);
//            j = j + 2;
//        }
    }

    //根据球面距离计算两点直接的距离
    public String getLongDistance(double lat1, double lon1, double lat2, double lon2) {
        double ew1, ns1, ew2, ns2;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0) {
            distance = 1.0;
        } else if (distance < -1.0) {
            distance = -1.0;
        }
        // 求大圆劣弧长度
        distance = DEF_R * Math.acos(distance);
        if (distance > 1000.0) {
            DecimalFormat df = new DecimalFormat("0.000");
            String dis = df.format(distance / 1000);
            return dis + "km";
        } else {
            DecimalFormat df = new DecimalFormat("0.000");
            String dis = df.format(distance);
            return dis + "m";
        }
    }

    //定位到用户当前位置
    private void showUserLocation() {
        LatLng latLng = new LatLng(user_latitude, user_longitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        map.animateMapStatus(msu);
    }

    //添加标注
    public void addOverlay(MapMarkerInfoBean Info) {
        OverlayOptions overlayoptions = null;
        Marker marker = null;

        overlayoptions = new MarkerOptions()//
                .position(Info.getLatLng())// 设置marker的位置
                .icon(marker_bitmap)// 设置marker的图标
                .animateType(MarkerOptions.MarkerAnimateType.grow)
                .zIndex(9);// 設置marker的所在層級
        marker = (Marker) map.addOverlay(overlayoptions);

        Bundle bundle = new Bundle();
        bundle.putSerializable("marker", Info);
        marker.setExtraInfo(bundle);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final MapMarkerInfoBean markerInfo = (MapMarkerInfoBean) marker.getExtraInfo().get("marker");
        View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.map_marker_info_layout, null);
        inflate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        SimpleDraweeView marker_icon = (SimpleDraweeView) inflate.findViewById(R.id.map_marker_icon);
        TextView marker_addr = (TextView) inflate.findViewById(R.id.map_marker_addr);
        TextView marker_title = (TextView) inflate.findViewById(R.id.map_marker_title);
        TextView marker_desc = (TextView) inflate.findViewById(R.id.map_marker_desc);
        TextView marker_join = (TextView) inflate.findViewById(R.id.map_marker_join);
        TextView marker_time = (TextView) inflate.findViewById(R.id.map_marker_time);

        marker_icon.setImageURI(Uri.parse(markerInfo.getUser_icon()+""));
        marker_addr.setText(markerInfo.getUser_addr() + "");
        marker_title.setText(markerInfo.getUser_title() + "");
        marker_desc.setText(markerInfo.getUser_desc() + "");
        marker_time.setText(markerInfo.getUser_time() + "");
        application.setDiary_image(markerInfo.getUser_image());
        marker_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.hideInfoWindow();
                Intent intent = new Intent(MainActivity.this, TravellerDiaryDetailActivity.class);
                intent.putExtra("user_nick",markerInfo.getUser_nick());
                intent.putExtra("user_icon",markerInfo.getUser_icon());
                intent.putExtra("user_time",markerInfo.getUser_time());
                intent.putExtra("user_title",markerInfo.getUser_title());
                intent.putExtra("user_desc",markerInfo.getUser_desc());
                if (markerInfo.getDiary_type() == 0){
                    intent.putExtra("type","text");
                }else if (markerInfo.getDiary_type() == 1){
                    intent.putExtra("type","image");
                }else if (markerInfo.getDiary_type() == 2){
                    intent.putExtra("type","video");
                    intent.putExtra("path",markerInfo.getVideo_path());
                    intent.putExtra("path_first",markerInfo.getVideo_path_first());
                }
                startActivity(intent);
            }
        });
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        mInfoWindow = new InfoWindow(inflate, markerInfo.getLatLng(), -100);
        //显示InfoWindow
        map.showInfoWindow(mInfoWindow);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        map.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    private void requestDiaryList() {

        BmobQuery<DiaryBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""));
        query.findObjects(new FindListener<DiaryBean>() {
            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        LatLng latLng = new LatLng(list.get(i).getUser_lat(), list.get(i).getUser_long());
                        MapMarkerInfoBean mapMarkerInfoBean = new MapMarkerInfoBean();
                        mapMarkerInfoBean.setLatLng(latLng);
                        mapMarkerInfoBean.setUser_icon(list.get(i).getUser_icon());
                        mapMarkerInfoBean.setUser_addr(list.get(i).getUser_addr());
                        mapMarkerInfoBean.setUser_title(list.get(i).getUser_title());
                        mapMarkerInfoBean.setUser_desc(list.get(i).getUser_desc());
                        mapMarkerInfoBean.setUser_time(list.get(i).getPublish_time());
                        mapMarkerInfoBean.setUser_nick(list.get(i).getUser_nick());
                        mapMarkerInfoBean.setUser_image(list.get(i).getDiary_image());
                        mapMarkerInfoBean.setDiary_type(list.get(i).getDiary_type());
                        mapMarkerInfoBean.setVideo_path(list.get(i).getDiary_video()+"");
                        mapMarkerInfoBean.setVideo_path_first(list.get(i).getDiary_video_first()+"");
                        addOverlay(mapMarkerInfoBean);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK){
            byte[] camera_data = (byte[]) data.getExtras().get("camera_data");
            if (camera_data != null){
                Intent intent2 = new Intent(MainActivity.this, DiaryImagePublishActivity.class);
                intent2.putExtra("camera_data",camera_data);
                intent2.putExtra("user_addr", user_addr);
                intent2.putExtra("user_lat", String.valueOf(user_latitude));
                intent2.putExtra("user_long", String.valueOf(user_longitude));
                startActivity(intent2);
            }
        }
        if (requestCode == 201 && resultCode == Activity.RESULT_OK){
                Intent intent2 = new Intent(MainActivity.this, DiaryVideoPublishActivity.class);
                intent2.putExtra("user_addr", user_addr);
                intent2.putExtra("user_lat", String.valueOf(user_latitude));
                intent2.putExtra("user_long", String.valueOf(user_longitude));
                startActivity(intent2);
            }
    }

    /**
     * 生命周期的处理
     */
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mapView.onResume();
        mLocationClient.start();
        map.setMyLocationEnabled(true);
        requestDiaryList();
        if (!Tools.isNull(mPreferences.getString("token", ""))) {
            requestDiaryList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mapView.onPause();
        mLocationClient.stop();
        map.clear();
        map.setMyLocationEnabled(false);
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mLocationClient.unRegisterLocationListener(this);
        orientationListener.stop();
        isPageDestroy = true;
        super.onDestroy();
    }
}
