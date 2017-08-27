package com.treasure.traveldiary;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.treasure.traveldiary.activity.find_center.FindCenterActivity;
import com.treasure.traveldiary.activity.main_center.DiaryImageCameraActivity;
import com.treasure.traveldiary.activity.main_center.DiaryImagePublishActivity;
import com.treasure.traveldiary.activity.main_center.EvaluatedPublishActivity;
import com.treasure.traveldiary.activity.main_center.DiaryVideoCameraActivity;
import com.treasure.traveldiary.activity.main_center.DiaryVideoPublishActivity;
import com.treasure.traveldiary.activity.main_center.SceneryActivity;
import com.treasure.traveldiary.activity.main_center.ToolsTicketActivity;
import com.treasure.traveldiary.activity.main_center.ToolsWeatherActivity;
import com.treasure.traveldiary.activity.diary_center.DiaryCenterActivity;
import com.treasure.traveldiary.activity.diary_center.DiaryDetailActivity;
import com.treasure.traveldiary.activity.main_center.DiaryTextPublishActivity;
import com.treasure.traveldiary.activity.user_center.UserEditUserInfoActivity;
import com.treasure.traveldiary.activity.user_center.UserFeedBackActivity;
import com.treasure.traveldiary.activity.user_center.UserForgetPassActivity;
import com.treasure.traveldiary.activity.user_center.UserMessageListActivity;
import com.treasure.traveldiary.activity.user_center.UserRegisterActivity;
import com.treasure.traveldiary.activity.user_center.UserSettingsActivity;
import com.treasure.traveldiary.activity.user_center.UserSigningActivity;
import com.treasure.traveldiary.activity.user_center.UserSocialActivity;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.MapMarkerInfoBean;
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.listener.MapOrientationListener;
import com.treasure.traveldiary.map_overlay.BikingRouteOverlay;
import com.treasure.traveldiary.map_overlay.DrivingRouteOverlay;
import com.treasure.traveldiary.map_overlay.TransitRouteOverlay;
import com.treasure.traveldiary.map_overlay.WalkingRouteOverlay;
import com.treasure.traveldiary.receiver.CommonDataReceiver;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.StringContents;
import com.treasure.traveldiary.utils.Tools;
import com.treasure.traveldiary.wxapi.SinaShareActivity;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity implements View.OnClickListener, BDLocationListener, BaiduMap.OnMapLoadedCallback, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener, RadioGroup.OnCheckedChangeListener, TextWatcher, OnGetRoutePlanResultListener {

    private LinearLayout mineDiaryLayout;
    private LinearLayout findCenterLayout;
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
                case 600:
                    Bundle data1 = msg.getData();
                    mTencent.shareToQQ(MainActivity.this, data1, qqShareListener);
                    break;
                case 601:
                    String nick_name = (String) msg.getData().get("nick_name");
                    binding_qq.setText(nick_name);
                    break;
                case 603:
                    String nick_name2 = (String) msg.getData().get("nick_name");
                    binding_sina.setText(nick_name2);
                    break;
            }
        }
    };
    private boolean isPageDestroy;
    private FrameLayout loading;
    private FloatingActionButton btnAdd, btnDiaryText, btnDiaryImage, btnDiaryEvaluated, btnDiaryVideo;
    private boolean addShow;
    private int widthPixels;
    private int heightPixels;
    private String user_city = "海淀";
    private String user_addr = "北京市";
    private SharedPreferences mPreferences;
    private InfoWindow mInfoWindow;
    private ImageView map_bg2_layout;
    private RadioGroup map_type;
    private RadioButton map_type_normal;
    private RadioButton map_type_satellite;
    private RadioButton map_type_compass;
    private MyLocationConfiguration.LocationMode mode = MyLocationConfiguration.LocationMode.NORMAL;
    private ImageView show_location;
    private TravelApplication application;
    private TextView text_t, image_t, video_t, evaluated_t;
    private FloatingActionButton btnScenic;
    private TextView scenic_t;
    private FloatingActionButton btnLive;
    private TextView live_t;
    private FloatingActionButton btnTools;
    private TextView tools_t;
    private FloatingActionButton btnToolsWeather;
    private TextView weather_t;
    private FloatingActionButton btnToolsTicket;
    private TextView ticket_t;
    private boolean isToolsShow;
    private String user_province = "北京";
    private PopupWindow mPopupWindow;
    private EditText editPhone, editPwd;
    private ImageView pass_visible;
    private boolean isHind = true;
    private IntentFilter filter;
    private CommonDataReceiver commonDataReceiver;
    private SimpleDraweeView left_user_icon;
    private TextView left_user_name;
    private LinearLayout night_layout, signing_layout, social_layout,message_layout, feedback_layout, settings_layout;
    private ImageView night_icon;
    private DrawerLayout drawer_layout;
    private FloatingActionButton search;
    private FrameLayout search_layout;
    private EditText search_edit;
    private TextView search_btn;
    private RoutePlanSearch routePlanSearch;
    private RadioGroup search_rg;
    private int route = 0;
    private PopupWindow shareWindow;
    private Tencent mTencent;
    private IUiListener qqShareListener;
    private TextView binding_qq;
    private TextView binding_wechat;
    private TextView binding_sina;
    private UserInfo userInfo;
    private WeiboAuth weiboAuth;
    private SsoHandler ssoHandler;
    private Oauth2AccessToken mAccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tools.setTranslucentStatus(this);
        initTitle();
        mapLocLayout.setVisibility(View.VISIBLE);
        user_icon.setVisibility(View.VISIBLE);
        btn_share.setVisibility(View.VISIBLE);
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
        application = (TravelApplication) getApplication();
        mTencent = Tencent.createInstance(StringContents.QQ_APP_ID, this.getApplicationContext());

        initFindId();
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        useLocationOrientationListener();
        initMap();
        initLocation();
        initClick();
        receiveBroadCast();
    }

    private void initFindId() {
        mineDiaryLayout = (LinearLayout) findViewById(R.id.mine_diary_layout);
        findCenterLayout = (LinearLayout) findViewById(R.id.find_center_layout);
        mapView = (MapView) findViewById(R.id.home_mapView);
        loading = (FrameLayout) findViewById(R.id.loading_layout);
        btnAdd = (FloatingActionButton) findViewById(R.id.main_add_image);
        btnDiaryText = (FloatingActionButton) findViewById(R.id.add_diary_text);
        btnDiaryImage = (FloatingActionButton) findViewById(R.id.add_diary_image);
        btnDiaryVideo = (FloatingActionButton) findViewById(R.id.add_diary_video);
        btnDiaryEvaluated = (FloatingActionButton) findViewById(R.id.add_evaluated);
        map_bg2_layout = (ImageView) findViewById(R.id.add_map_bg2);
        map_type = (RadioGroup) findViewById(R.id.layout_map_type);
        map_type_normal = (RadioButton) findViewById(R.id.btn_map_normal);
        map_type_satellite = (RadioButton) findViewById(R.id.btn_map_satellite);
        map_type_compass = (RadioButton) findViewById(R.id.btn_map_compass);
        show_location = (ImageView) findViewById(R.id.user_map_location);
        text_t = (TextView) findViewById(R.id.add_diary_text_t);
        image_t = (TextView) findViewById(R.id.add_diary_image_t);
        video_t = (TextView) findViewById(R.id.add_diary_video_t);
        evaluated_t = (TextView) findViewById(R.id.add_evaluated_t);
        btnScenic = (FloatingActionButton) findViewById(R.id.add_scenery);
        scenic_t = (TextView) findViewById(R.id.add_scenery_t);
        btnLive = (FloatingActionButton) findViewById(R.id.add_live);
        live_t = (TextView) findViewById(R.id.add_live_t);
        btnTools = (FloatingActionButton) findViewById(R.id.add_tools);
        tools_t = (TextView) findViewById(R.id.add_tools_t);
        btnToolsWeather = (FloatingActionButton) findViewById(R.id.add_tools_weather);
        weather_t = (TextView) findViewById(R.id.add_tools_weather_t);
        btnToolsTicket = (FloatingActionButton) findViewById(R.id.add_tools_ticket);
        ticket_t = (TextView) findViewById(R.id.add_tools_ticket_t);
        search = (FloatingActionButton) findViewById(R.id.map_search);
        search_layout = (FrameLayout) findViewById(R.id.map_search_layout);
        search_edit = (EditText) findViewById(R.id.map_search_edit);
        search_btn = (TextView) findViewById(R.id.map_search_btn);
        search_rg = (RadioGroup) findViewById(R.id.map_search_rg);
        //侧边栏
        left_user_icon = (SimpleDraweeView) findViewById(R.id.left_login_icon);
        left_user_name = (TextView) findViewById(R.id.left_login_username);
        night_icon = (ImageView) findViewById(R.id.left_night_icon);
        night_layout = (LinearLayout) findViewById(R.id.left_night_layout);
        signing_layout = (LinearLayout) findViewById(R.id.left_signing_layout);
        social_layout = (LinearLayout) findViewById(R.id.left_social_contact_layout);
        message_layout = (LinearLayout) findViewById(R.id.left_message_layout);
        feedback_layout = (LinearLayout) findViewById(R.id.left_feedback_layout);
        settings_layout = (LinearLayout) findViewById(R.id.left_settings_layout);
        drawer_layout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        binding_qq = (TextView) findViewById(R.id.left_binding_qq);
        binding_wechat = (TextView) findViewById(R.id.left_binding_wechat);
        binding_sina = (TextView) findViewById(R.id.left_binding_sina);
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
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);
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
        mineDiaryLayout.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        findCenterLayout.setOnClickListener(this);
        loading.setOnClickListener(this);
        btnDiaryText.setOnClickListener(this);
        btnDiaryImage.setOnClickListener(this);
        btnDiaryVideo.setOnClickListener(this);
        btnDiaryEvaluated.setOnClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
        map_bg2_layout.setOnClickListener(this);
        map_type.setOnCheckedChangeListener(this);
        show_location.setOnClickListener(this);
        text_t.setOnClickListener(this);
        image_t.setOnClickListener(this);
        video_t.setOnClickListener(this);
        evaluated_t.setOnClickListener(this);
        btnScenic.setOnClickListener(this);
        btnLive.setOnClickListener(this);
        btnTools.setOnClickListener(this);
        scenic_t.setOnClickListener(this);
        live_t.setOnClickListener(this);
        tools_t.setOnClickListener(this);
        btnToolsWeather.setOnClickListener(this);
        btnToolsWeather.setOnClickListener(this);
        btnToolsTicket.setOnClickListener(this);
        ticket_t.setOnClickListener(this);
        user_icon.setOnClickListener(this);
        search.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        search_edit.addTextChangedListener(this);
        search_rg.setOnCheckedChangeListener(this);
        btn_share.setOnClickListener(this);

        left_user_icon.setOnClickListener(this);
        left_user_name.setOnClickListener(this);
        night_layout.setOnClickListener(this);
        signing_layout.setOnClickListener(this);
        social_layout.setOnClickListener(this);
        message_layout.setOnClickListener(this);
        feedback_layout.setOnClickListener(this);
        settings_layout.setOnClickListener(this);
        binding_qq.setOnClickListener(this);
        binding_wechat.setOnClickListener(this);
        binding_sina.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                showShareWindow();
                break;
            case R.id.mine_diary_layout:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    showLoginWindow();
                } else {
                    Intent intent1 = new Intent(MainActivity.this, DiaryCenterActivity.class);
                    if (Build.VERSION.SDK_INT >= 21) {
                        startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(this, mineDiaryLayout, "diary").toBundle());
                    } else {
                        startActivity(intent1);
                    }
                }
                break;
            case R.id.main_add_image:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    showLoginWindow();
                } else {
                    //加号    如果主btn显示，点击后隐藏，如果隐藏，点击后显示    如果Tools btn显示，点击后隐藏
                    if (!addShow && !isToolsShow) {
                        showFloatActionButton();
                    } else {
                        hindFloatActionButton();
                    }
                    if (isToolsShow) {
                        isToolsShow = false;
                        Tools.setAnimation(btnTools, 0, 0, 1, 0, 0, -720, 1, 1, 1000);
                        btnToolsWeather.setVisibility(View.GONE);
                        weather_t.setVisibility(View.GONE);
                        btnToolsTicket.setVisibility(View.GONE);
                        ticket_t.setVisibility(View.GONE);
                        Tools.setAnimation(btnToolsWeather, 0, 0, 1, 0, 0, 720, 1, 1, 2500);
                        Tools.setAnimation(weather_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                        Tools.setAnimation(btnToolsTicket, 0, 0, 1, 0, 0, -720, 1, 1, 2500);
                        Tools.setAnimation(ticket_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                        Tools.setAnimation(map_bg2_layout, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                        map_bg2_layout.setClickable(false);
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
            case R.id.add_evaluated:
            case R.id.add_evaluated_t:
                Intent intent5 = new Intent(MainActivity.this, EvaluatedPublishActivity.class);
                intent5.putExtra("user_lat", String.valueOf(user_latitude));
                intent5.putExtra("user_lon", String.valueOf(user_longitude));
                intent5.putExtra("user_addr", user_addr);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent5, ActivityOptions.makeSceneTransitionAnimation(this, btnDiaryEvaluated, "evaluated").toBundle());
                } else {
                    startActivity(intent5);
                }
                hindFloatActionButton();
                break;
            case R.id.add_scenery:
            case R.id.add_scenery_t:
                Intent intent8 = new Intent(MainActivity.this, SceneryActivity.class);
                intent8.putExtra("user_province", user_province);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent8, ActivityOptions.makeSceneTransitionAnimation(this, btnScenic, "scenery").toBundle());
                } else {
                    startActivity(intent8);
                }
                hindFloatActionButton();
                break;
            case R.id.add_live:
            case R.id.add_live_t:
                Toast.makeText(this, "该功能尚未开放！", Toast.LENGTH_SHORT).show();
                hindFloatActionButton();
                break;
            case R.id.add_tools:
            case R.id.add_tools_t:
                //Tools btn    如果Tools显示，点击后隐藏    如果Tools隐藏，点击后显示
                if (!isToolsShow) {
                    hindFloatActionButton();
                    Tools.setAnimation(btnTools, 0, -heightPixels / 2, 0, 1, 0, -720, 1, 1, 1000);
                    btnToolsWeather.setVisibility(View.VISIBLE);
                    weather_t.setVisibility(View.VISIBLE);
                    btnToolsTicket.setVisibility(View.VISIBLE);
                    ticket_t.setVisibility(View.VISIBLE);
                    Tools.setAnimation(btnToolsWeather, 0, 0, 0, 1, 0, 720, 1, 1, 2500);
                    Tools.setAnimation(weather_t, 0, 0, 0, 1, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(btnToolsTicket, 0, 0, 0, 1, 0, -720, 1, 1, 2500);
                    Tools.setAnimation(ticket_t, 0, 0, 0, 1, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(map_bg2_layout, 0, 0, 0, 1, 0, 0, 1, 1, 2000);
                    map_bg2_layout.setClickable(true);
                    isToolsShow = true;
                } else {
                    isToolsShow = false;
                    Tools.setAnimation(btnTools, 0, 0, 1, 0, 0, -720, 1, 1, 1000);
                    btnToolsWeather.setVisibility(View.GONE);
                    weather_t.setVisibility(View.GONE);
                    btnToolsTicket.setVisibility(View.GONE);
                    ticket_t.setVisibility(View.GONE);
                    Tools.setAnimation(btnToolsWeather, 0, 0, 1, 0, 0, 720, 1, 1, 2500);
                    Tools.setAnimation(weather_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(btnToolsTicket, 0, 0, 1, 0, 0, -720, 1, 1, 2500);
                    Tools.setAnimation(ticket_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(map_bg2_layout, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    map_bg2_layout.setClickable(false);
                }

                break;
            case R.id.add_tools_weather:
            case R.id.add_tools_weather_t:
                Intent intent00 = new Intent(MainActivity.this, ToolsWeatherActivity.class);
                intent00.putExtra("user_city", user_city);
                intent00.putExtra("user_province", user_province);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent00, ActivityOptions.makeSceneTransitionAnimation(this, btnAdd, "transition").toBundle());
                } else {
                    startActivity(intent00);
                }
                if (isToolsShow) {
                    isToolsShow = false;
                    Tools.setAnimation(btnTools, 0, 0, 1, 0, 0, -720, 1, 1, 1000);
                    btnToolsWeather.setVisibility(View.GONE);
                    weather_t.setVisibility(View.GONE);
                    btnToolsTicket.setVisibility(View.GONE);
                    ticket_t.setVisibility(View.GONE);
                    Tools.setAnimation(btnToolsWeather, 0, 0, 1, 0, 0, 720, 1, 1, 2500);
                    Tools.setAnimation(weather_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(btnToolsTicket, 0, 0, 1, 0, 0, -720, 1, 1, 2500);
                    Tools.setAnimation(ticket_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(map_bg2_layout, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    map_bg2_layout.setClickable(false);
                }
                break;
            case R.id.add_tools_ticket:
            case R.id.add_tools_ticket_t:
                Intent intent01 = new Intent(MainActivity.this, ToolsTicketActivity.class);
                intent01.putExtra("user_city", user_city);
                if (Build.VERSION.SDK_INT >= 21) {
                    startActivity(intent01, ActivityOptions.makeSceneTransitionAnimation(this, btnAdd, "transition").toBundle());
                } else {
                    startActivity(intent01);
                }
                if (isToolsShow) {
                    isToolsShow = false;
                    Tools.setAnimation(btnTools, 0, 0, 1, 0, 0, -720, 1, 1, 1000);
                    btnToolsWeather.setVisibility(View.GONE);
                    weather_t.setVisibility(View.GONE);
                    btnToolsTicket.setVisibility(View.GONE);
                    ticket_t.setVisibility(View.GONE);
                    Tools.setAnimation(btnToolsWeather, 0, 0, 1, 0, 0, 720, 1, 1, 2500);
                    Tools.setAnimation(weather_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(btnToolsTicket, 0, 0, 1, 0, 0, -720, 1, 1, 2500);
                    Tools.setAnimation(ticket_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(map_bg2_layout, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    map_bg2_layout.setClickable(false);
                }
                break;
            case R.id.add_map_bg2:
                //背景    如果主btn显示，点击后隐藏    如果Tools btn显示，点击后隐藏
                if (addShow) {
                    hindFloatActionButton();
                }
                if (isToolsShow) {
                    isToolsShow = false;
                    Tools.setAnimation(btnTools, 0, 0, 1, 0, 0, -720, 1, 1, 1000);
                    btnToolsWeather.setVisibility(View.GONE);
                    weather_t.setVisibility(View.GONE);
                    btnToolsTicket.setVisibility(View.GONE);
                    ticket_t.setVisibility(View.GONE);
                    Tools.setAnimation(btnToolsWeather, 0, 0, 1, 0, 0, 720, 1, 1, 2500);
                    Tools.setAnimation(weather_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(btnToolsTicket, 0, 0, 1, 0, 0, -720, 1, 1, 2500);
                    Tools.setAnimation(ticket_t, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    Tools.setAnimation(map_bg2_layout, 0, 0, 1, 0, 0, 0, 1, 1, 2000);
                    map_bg2_layout.setClickable(false);
                }
                break;
            case R.id.user_map_location:
                showUserLocation();
                break;
            case R.id.find_center_layout:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    showLoginWindow();
                } else {
                    Intent intent = new Intent(MainActivity.this, FindCenterActivity.class);
                    if (Build.VERSION.SDK_INT >= 21) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, findCenterLayout, "findcenter").toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
                break;
            case R.id.image_user_icon:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    showLoginWindow();
                } else {
                    drawer_layout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.map_search:
                Tools.setAnimation(search, -widthPixels / 2, 0, 1, 1, 0, -720, 1, 0, 1000);
                search_layout.setVisibility(View.VISIBLE);
                Tools.setAnimation(search_layout, 0, 0, 0, 1, 0, 0, 0, 1, 1000);
                break;
            case R.id.map_search_btn:
                if (search_btn.getText().toString().equals("| 关闭")) {
                    Tools.setAnimation(search, 0, 0, 1, 1, 0, 720, 0, 1, 1000);
                    Tools.setAnimation(search_layout, 0, 0, 1, 0, 0, 0, 1, 0, 1000);
                } else {
                    PlanNode stNode = PlanNode.withLocation(new LatLng(user_latitude, user_longitude));
                    PlanNode enNode = PlanNode.withCityNameAndPlaceName(user_city, search_edit.getText().toString().trim());
                    if (route == 0) {
                        routePlanSearch.transitSearch(new TransitRoutePlanOption()
                                .from(stNode)
                                .city(user_city)
                                .to(enNode));
                    } else if (route == 1) {
                        routePlanSearch.drivingSearch(new DrivingRoutePlanOption()
                                .from(stNode)
                                .to(enNode));
                    } else if (route == 2) {
                        routePlanSearch.walkingSearch(new WalkingRoutePlanOption()
                                .from(stNode)
                                .to(enNode));
                    } else if (route == 3) {
                        routePlanSearch.bikingSearch(new BikingRoutePlanOption()
                                .from(stNode)
                                .to(enNode));
                    }
                }
                break;
            case R.id.mine_popup_quit:
                quitLoginWindow();
                break;
            case R.id.mine_popup_loginin:
                LoginIn();
                break;
            case R.id.mine_popup_register:
                mPopupWindow.dismiss();
                startActivity(new Intent(MainActivity.this, UserRegisterActivity.class));
                break;
            case R.id.mine_popup_forget_password:
                mPopupWindow.dismiss();
                startActivity(new Intent(MainActivity.this, UserForgetPassActivity.class));
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
            case R.id.left_login_icon:
            case R.id.left_login_username:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    //Login
                    showLoginWindow();
                } else {
                    //edit
                    Intent intent = new Intent(MainActivity.this, UserEditUserInfoActivity.class);
                    intent.putExtra("edit_type", "normal");
                    String user_name = mPreferences.getString("user_name", "");
                    intent.putExtra("UserPhone", user_name);
                    startActivity(intent);
                }
                break;
            case R.id.left_night_layout:
                nightSwitch();
                break;
            case R.id.left_signing_layout:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    //Login
                    showLoginWindow();
                } else {
                    startActivity(new Intent(MainActivity.this, UserSigningActivity.class));
                }
                break;
            case R.id.left_social_contact_layout:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    showLoginWindow();
                } else {
                    Intent intent = new Intent(MainActivity.this, UserSocialActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.left_message_layout:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    showLoginWindow();
                } else {
                    Intent intent = new Intent(MainActivity.this, UserMessageListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.left_feedback_layout:
                if (Tools.isNull(mPreferences.getString("token", ""))) {
                    showLoginWindow();
                } else {
                    Intent intent = new Intent(MainActivity.this, UserFeedBackActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.left_settings_layout:
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                }
                startActivity(new Intent(MainActivity.this, UserSettingsActivity.class));
                break;
            case R.id.left_binding_qq:
                bindingQQ();
                break;
            case R.id.left_binding_wechat:
                bindingWeChat();
                break;
            case R.id.left_binding_sina:
                bindingSina();
                break;
            case R.id.main_share_block:
                shareWindow.dismiss();
                break;
            case R.id.main_share_qq:
                shareQQ();
                break;
            case R.id.main_share_wechat:
                shareWeChat();
                break;
            case R.id.main_share_wechat_circle:
                shareWeChatCircle();
                break;
            case R.id.main_share_sina:
                shareSina();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (Tools.isNull(search_edit.getText().toString().trim())) {
            search_btn.setText("| 关闭");
            search_rg.setVisibility(View.GONE);
        } else {
            search_btn.setText("| 搜索");
            search_rg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

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
        } else if (radioButton.getText().equals("公交")) {
            route = 0;
        } else if (radioButton.getText().equals("驾车")) {
            route = 1;
        } else if (radioButton.getText().equals("步行")) {
            route = 2;
        } else if (radioButton.getText().equals("骑行")) {
            route = 3;
        }
    }

    private void showFloatActionButton() {
        Tools.setAnimation(btnDiaryText, -widthPixels / 3, -heightPixels / 3, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(btnDiaryImage, -widthPixels / 9, -heightPixels / 3, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(btnDiaryVideo, widthPixels / 9, -heightPixels / 3, 0, 1, 0, -360, 1, 1, 500);
        Tools.setAnimation(btnDiaryEvaluated, widthPixels / 3, -heightPixels / 3, 0, 1, 0, -360, 1, 1, 500);

        Tools.setAnimation(btnScenic, -widthPixels / 3, -heightPixels / 6, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(btnLive, -widthPixels / 9, -heightPixels / 6, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(btnTools, widthPixels / 9, -heightPixels / 6, 0, 1, 0, -360, 1, 1, 500);

        Tools.setAnimation(text_t, -widthPixels / 3, -heightPixels / 3 + 80, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(image_t, -widthPixels / 9, -heightPixels / 3 + 80, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(video_t, widthPixels / 9, -heightPixels / 3 + 80, 0, 1, 0, -360, 1, 1, 500);
        Tools.setAnimation(evaluated_t, widthPixels / 3, -heightPixels / 3 + 80, 0, 1, 0, -360, 1, 1, 500);

        Tools.setAnimation(scenic_t, -widthPixels / 3, -heightPixels / 6 + 80, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(live_t, -widthPixels / 9, -heightPixels / 6 + 80, 0, 1, 0, 360, 1, 1, 500);
        Tools.setAnimation(tools_t, widthPixels / 9, -heightPixels / 6 + 80, 0, 1, 0, -360, 1, 1, 500);

        Tools.setAnimation(btnAdd, 0, 0, 1, 1, 0, -45, 1, 1, 500);
        btnToolsWeather.setVisibility(View.GONE);
        weather_t.setVisibility(View.GONE);
        btnToolsTicket.setVisibility(View.GONE);
        ticket_t.setVisibility(View.GONE);
        map_bg2_layout.setVisibility(View.VISIBLE);
        map_bg2_layout.setClickable(true);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(map_bg2_layout, "alpha", 0, 1);
        alpha.setDuration(500);
        alpha.start();
        addShow = true;
    }

    private void hindFloatActionButton() {
        Tools.setAnimation(btnDiaryText, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(btnDiaryImage, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(btnDiaryVideo, 0, 0, 1, 0, 360, 0, 1, 1, 500);
        Tools.setAnimation(btnDiaryEvaluated, 0, 0, 1, 0, 360, 0, 1, 1, 500);

        Tools.setAnimation(btnScenic, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(btnLive, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(btnTools, 0, 0, 1, 0, 360, 0, 1, 1, 500);

        Tools.setAnimation(text_t, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(image_t, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(video_t, 0, 0, 1, 0, 360, 0, 1, 1, 500);
        Tools.setAnimation(evaluated_t, 0, 0, 1, 0, 360, 0, 1, 1, 500);

        Tools.setAnimation(scenic_t, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(live_t, 0, 0, 1, 0, -360, 0, 1, 1, 500);
        Tools.setAnimation(tools_t, 0, 0, 1, 0, 360, 0, 1, 1, 500);

        Tools.setAnimation(btnAdd, 0, 0, 1, 1, -45, 0, 1, 1, 500);
        btnToolsWeather.setVisibility(View.GONE);
        weather_t.setVisibility(View.GONE);
        btnToolsTicket.setVisibility(View.GONE);
        ticket_t.setVisibility(View.GONE);
        map_bg2_layout.setClickable(false);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(map_bg2_layout, "alpha", 1, 0);
        alpha.setDuration(500);
        alpha.start();
        addShow = false;
    }

    /**
     * 显示 关闭 popupWindow        登陆操作
     */
    public void showLoginWindow() {
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

        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    private void quitLoginWindow() {
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
    private void LoginIn() {
        if (Tools.isNull(editPhone.getText().toString().trim())) {
            Toast.makeText(MainActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Tools.isNull(editPwd.getText().toString().trim())) {
            Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", editPhone.getText().toString().trim());
        query.findObjects(new FindListener<UserInfoBean>() {
            @Override
            public void done(List<UserInfoBean> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        Toast.makeText(MainActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        if (editPwd.getText().toString().trim().equals(list.get(0).getUser_pwd())) {
                            Toast.makeText(MainActivity.this, "恭喜你，登陆成功", Toast.LENGTH_SHORT).show();
                            //存入SharedPreferences
                            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", "login");
                            editor.putString("user_icon", list.get(0).getUser_icon());
                            editor.putString("user_name", list.get(0).getUser_name());
                            editor.putString("user_nick", list.get(0).getNick_name());
                            editor.putString("user_pwd", list.get(0).getUser_pwd());
                            editor.putString("user_age", String.valueOf(list.get(0).getAge()));
                            editor.putString("user_sex", String.valueOf(list.get(0).getSex()));
                            editor.putString("user_desc", list.get(0).getUser_desc());
                            editor.putString("integral_count", list.get(0).getIntegral_count());
                            editor.putString("traveller_circle_bg", list.get(0).getTraveller_circle_bg());
                            editor.apply();

                            //发送登录成功 广播
                            Intent intent = new Intent();
                            intent.setAction(StringContents.ACTION_COMMENTDATA);
                            intent.putExtra("label", "login");
                            sendBroadcast(intent);
                            mPopupWindow.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            editPwd.setText("");
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "查询用户失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void receiveBroadCast() {
        filter = new IntentFilter();
        filter.addAction(StringContents.ACTION_COMMENTDATA);
        commonDataReceiver = new CommonDataReceiver();
        commonDataReceiver.setDoUIReceiver(new CommonDataReceiver.DoUIReceiver() {
            @Override
            public void doUI(Context context, Intent intent) {
                if (intent.getExtras().getString("label").equals("login")) {
                    user_icon.setImageURI(Uri.parse(mPreferences.getString("user_icon", "")));
                    left_user_icon.setImageURI(Uri.parse(mPreferences.getString("user_icon", "")));
                    left_user_name.setText(mPreferences.getString("user_nick", ""));
                }
            }
        });
        registerReceiver(commonDataReceiver, filter);
    }

    /**
     * 夜间模式的切换
     */
    private void nightSwitch() {
        if (!application.isNight()) {
            night_icon.setImageResource(R.mipmap.ic_night);
            application.setNight(true);
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = 0.001f;
            window.setAttributes(layoutParams);
            application.setNight(true);
        } else {
            night_icon.setImageResource(R.mipmap.ic_daytime);
            application.setNight(false);
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = -1;
            window.setAttributes(layoutParams);
            application.setNight(false);
        }
    }

    /**
     * 显示 分享的View
     */
    private void showShareWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.main_share_view, null);
        shareWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        shareWindow.setAnimationStyle(R.style.sharePopupWindow);
        shareWindow.setOutsideTouchable(true);
        shareWindow.setBackgroundDrawable(new ColorDrawable(0x66000000));

        TextView share_block = (TextView) convertView.findViewById(R.id.main_share_block);
        ImageView share_qq = (ImageView) convertView.findViewById(R.id.main_share_qq);
        ImageView share_wechat = (ImageView) convertView.findViewById(R.id.main_share_wechat);
        ImageView share_wechat_circle = (ImageView) convertView.findViewById(R.id.main_share_wechat_circle);
        ImageView share_sina = (ImageView) convertView.findViewById(R.id.main_share_sina);
        share_block.setOnClickListener(this);
        share_qq.setOnClickListener(this);
        share_wechat.setOnClickListener(this);
        share_wechat_circle.setOnClickListener(this);
        share_sina.setOnClickListener(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        shareWindow.showAtLocation(rootView, Gravity.RIGHT, 0, 0);
    }

    /**
     * QQ 微信 微博 的  分享和绑定账号
     */
    private void shareQQ() {
        if (shareWindow != null && shareWindow.isShowing()) {
            shareWindow.dismiss();
        }
        //初始化回调
        qqShareListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(MainActivity.this, "恭喜你，QQ分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(MainActivity.this, "很遗憾，QQ分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "QQ分享已取消", Toast.LENGTH_SHORT).show();
            }
        };

        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "旅游diary（第四版）  已经出炉了");// 标题
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "旅游 途中会遇到很多美好的瞬间，倘若未记录下来到日后回忆会是一种很大的遗憾，为了不留下遗憾，记得用APP记录下你的美。");// 摘要
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://sj.qq.com/myapp/detail.htm?apkName=com.treasure.traveldiary");// 内容地址
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://bmob-cdn-13238.b0.upaiyun.com/2017/08/21/38bdff2c103e49aa838b44bce96e6485.png");// 网络图片地址　　params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "应用名称");// 应用名称
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_INT, "体验");
        Message message = handler.obtainMessage(600);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void bindingQQ() {
        //需要先登录授权，获取到openID和token   用token进行用户信息获取
        if (!mTencent.isSessionValid()) {
            //开始qq授权登录
            mTencent.login(this, "all", new IUiListener() {
                @Override
                public void onComplete(Object arg0) {
                    try {
                        JSONObject jo = (JSONObject) arg0;
                        int ret = jo.getInt("ret");
                        if (ret == 0) {
                            String openID = jo.getString("openid");
                            String accessToken = jo.getString("access_token");
                            String expires = jo.getString("expires_in");
                            mTencent.setOpenId(openID);
                            mTencent.setAccessToken(accessToken, expires);
                            //set token之后
                            userInfo = new UserInfo(MainActivity.this, mTencent.getQQToken());
                            userInfo.getUserInfo(new IUiListener() {
                                @Override
                                public void onComplete(Object o) {
                                    JSONObject jo = (JSONObject) o;
                                    try {
                                        final String nickName = jo.getString("nickname");
                                        String gender = jo.getString("gender");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(1000);
                                                    Message message = handler.obtainMessage(601);
                                                    Bundle bundle1 = new Bundle();
                                                    bundle1.putString("nick_name",nickName);
                                                    message.setData(bundle1);
                                                    handler.sendMessage(message);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                        toUpdateUserInfo(nickName,0);
                                    } catch (Exception e) {

                                    }
                                }

                                @Override
                                public void onError(UiError uiError) {
                                    Toast.makeText(MainActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancel() {
                                }
                            });
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {
                    Toast.makeText(MainActivity.this, "取消了QQ授权", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void shareWeChat() {
        //启动分享页面，分享的结果微信会通过组件启动WXEntryActivity 进行监听
        if (shareWindow != null && shareWindow.isShowing()) {
            shareWindow.dismiss();
        }
        IWXAPI iwxapi = TravelApplication.iwxapi;
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(MainActivity.this, "您还未安装微信客户端",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://sj.qq.com/myapp/detail.htm?apkName=com.treasure.traveldiary";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "旅游diary（第四版）  已经出炉了";
        msg.description = "旅游 途中会遇到很多美好的瞬间，倘若未记录下来到日后回忆会是一种很大的遗憾，为了不留下遗憾，记得用APP记录下你的美。";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_travel_logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    private void shareWeChatCircle() {
        if (shareWindow != null && shareWindow.isShowing()) {
            shareWindow.dismiss();
        }
        IWXAPI iwxapi = TravelApplication.iwxapi;
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(MainActivity.this, "您还未安装微信客户端",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://sj.qq.com/myapp/detail.htm?apkName=com.treasure.traveldiary";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "旅游diary（第四版）  已经出炉了";
        msg.description = "旅游 途中会遇到很多美好的瞬间，倘若未记录下来到日后回忆会是一种很大的遗憾，为了不留下遗憾，记得用APP记录下你的美。";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_travel_logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    private void bindingWeChat() {
        Toast.makeText(this, "此功能暂未开放", Toast.LENGTH_SHORT).show();
    }

    private void shareSina() {
        //进入新页面进行发送微博 和微博状态监听
        startActivity(new Intent(MainActivity.this, SinaShareActivity.class));
        if (shareWindow != null && shareWindow.isShowing()) {
            shareWindow.dismiss();
        }
    }

    private void bindingSina() {
        weiboAuth = new WeiboAuth(this, StringContents.Sina_APP_KEY, StringContents.Sina_REDIRECT_URL, StringContents.Sina_SCOPE);
        ssoHandler = new SsoHandler(MainActivity.this, weiboAuth);
        try {
            ssoHandler.authorize(new WeiboAuthListener() {

                @Override
                public void onComplete(Bundle bundle) {
                    //从 Bundle 中解析 Token
                    mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    if (mAccessToken != null && mAccessToken.isSessionValid()) {

                        final String nickname = bundle.getString("com.sina.weibo.intent.extra.NICK_NAME");
                        String access_token = bundle.getString("access_token");
                        String uid = bundle.getString("uid");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    Message message = handler.obtainMessage(603);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("nick_name",nickname);
                                    message.setData(bundle1);
                                    handler.sendMessage(message);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        toUpdateUserInfo(nickname,2);
                        //GET请求获取用户的信息
//                    try {
//                        URL url = new URL("https://api.weibo.com/2/users/show.json?access_token=" + access_token + "&uid=" + uid);
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setConnectTimeout(5000);
//                        conn.setRequestMethod("GET");
//                        conn.setDoInput(true);
//                        int code = conn.getResponseCode();
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        byte[] buffer = new byte[1024];
//                        int length = 0;
//                        if (code == 200) {
//                            InputStream is = conn.getInputStream();
//                            while ((length = is.read(buffer)) != -1) {
//                                baos.write(buffer, 0, length);
//                            }
//                        }
//                        String jsonString = new String(baos.toByteArray());
//                        JSONObject jsonObject = new JSONObject(jsonString);
//                        String icon = jsonObject.getString("profile_image_url");
//
//                    } catch (Exception e) {
//                        LogUtil.d("chengcj1" + e.getMessage());
//                    }
                    } else {
                        // 以下几种情况，您会收到 Code：
                        // 1. 当您未在平台上注册的应用程序的包名与签名时；
                        // 2. 当您注册的应用程序包名与签名不正确时；
                        // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                        String code = bundle.getString("code");
                        String message = getString(getResources().getIdentifier("authorize_fail", "string", getPackageName()));
                        if (!Tools.isNull(code)) {
                            message = message + "\n Obtained the code: " + code;
                        }
                        LogUtil.e("~~~~~~~~~~~~~~~~~~~~message::" + message);
                    }
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    LogUtil.e("~~~~~~~~~~~~~~~~~~WeiboException" + e.getMessage());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(MainActivity.this, "取消了微博授权", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "请安装微博客户端，并保持登录状态", Toast.LENGTH_SHORT).show();
        }
    }

    private void toUpdateUserInfo(final String nickName, final int type) {
        BmobQuery<UserInfoBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                .findObjects(new FindListener<UserInfoBean>() {
                    @Override
                    public void done(List<UserInfoBean> list, BmobException e) {
                        if (e == null) {
                            String objectId = list.get(0).getObjectId();
                            UserInfoBean userInfoBean = new UserInfoBean();
                            if (type == 0) {
                                userInfoBean.setBinding_qq(nickName);
                            } else if (type == 1) {
                                userInfoBean.setBinding_wechat(nickName);
                            } else {
                                userInfoBean.setBinding_sina(nickName);
                            }
                            userInfoBean.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(MainActivity.this, "信息更新成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "信息更新失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "信息更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                user_province = bdLocation.getProvince().substring(0, bdLocation.getProvince().length() - 1);
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

        marker_icon.setImageURI(Uri.parse(markerInfo.getUser_icon() + ""));
        marker_addr.setText(markerInfo.getUser_addr() + "");
        marker_title.setText(markerInfo.getUser_title() + "");
        marker_desc.setText(markerInfo.getUser_desc() + "");
        marker_time.setText(markerInfo.getUser_time() + "");
        application.setDiary_image(markerInfo.getUser_image());
        marker_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.hideInfoWindow();
                Intent intent = new Intent(MainActivity.this, DiaryDetailActivity.class);
                intent.putExtra("user_name", markerInfo.getUser_name());
                intent.putExtra("user_time", markerInfo.getUser_time());
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

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "未查找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(this, "起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Tools.setAnimation(search, 0, 0, 1, 1, 0, 720, 0, 1, 1000);
            Tools.setAnimation(search_layout, 0, 0, 1, 0, 0, 0, 1, 0, 1000);
            search_edit.setText("");
            map.clear();
            WalkingRouteLine routeLine = walkingRouteResult.getRouteLines().get(0);
            //创建公交路线规划线路覆盖物
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(map);
            //设置公交路线规划数据
            overlay.setData(routeLine);
            //将公交路线规划覆盖物添加到地图中
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "未查找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(this, "起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Tools.setAnimation(search, 0, 0, 1, 1, 0, 720, 0, 1, 1000);
            Tools.setAnimation(search_layout, 0, 0, 1, 0, 0, 0, 1, 0, 1000);
            search_edit.setText("");
            map.clear();
            TransitRouteLine routeLine = transitRouteResult.getRouteLines().get(0);
            //创建公交路线规划线路覆盖物
            TransitRouteOverlay overlay = new TransitRouteOverlay(map);
            //设置公交路线规划数据
            overlay.setData(routeLine);
            //将公交路线规划覆盖物添加到地图中
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "未查找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(this, "起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Tools.setAnimation(search, 0, 0, 1, 1, 0, 720, 0, 1, 1000);
            Tools.setAnimation(search_layout, 0, 0, 1, 0, 0, 0, 1, 0, 1000);
            search_edit.setText("");
            map.clear();
            DrivingRouteLine routeLine = drivingRouteResult.getRouteLines().get(0);
            //创建公交路线规划线路覆盖物
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(map);
            //设置公交路线规划数据
            overlay.setData(routeLine);
            //将公交路线规划覆盖物添加到地图中
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "未查找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(this, "起终点或途经点地址有岐义", Toast.LENGTH_SHORT).show();
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Tools.setAnimation(search, 0, 0, 1, 1, 0, 720, 0, 1, 1000);
            Tools.setAnimation(search_layout, 0, 0, 1, 0, 0, 0, 1, 0, 1000);
            search_edit.setText("");
            map.clear();
            BikingRouteLine routeLine = bikingRouteResult.getRouteLines().get(0);
            //创建公交路线规划线路覆盖物
            BikingRouteOverlay overlay = new BikingRouteOverlay(map);
            //设置公交路线规划数据
            overlay.setData(routeLine);
            //将公交路线规划覆盖物添加到地图中
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    private void requestDiaryList() {

        BmobQuery<DiaryBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""));
        query.findObjects(new FindListener<DiaryBean>() {
            @Override
            public void done(List<DiaryBean> list, BmobException e) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        LatLng latLng = new LatLng(Double.parseDouble(list.get(i).getUser_lat()), Double.parseDouble(list.get(i).getUser_long()));
                        MapMarkerInfoBean mapMarkerInfoBean = new MapMarkerInfoBean();
                        mapMarkerInfoBean.setLatLng(latLng);
                        mapMarkerInfoBean.setUser_name(list.get(i).getUser_name());
                        mapMarkerInfoBean.setUser_icon(list.get(i).getUser_icon());
                        mapMarkerInfoBean.setUser_addr(list.get(i).getUser_addr());
                        mapMarkerInfoBean.setUser_title(list.get(i).getUser_title());
                        mapMarkerInfoBean.setUser_desc(list.get(i).getUser_desc());
                        mapMarkerInfoBean.setUser_time(list.get(i).getPublish_time());
                        mapMarkerInfoBean.setUser_nick(list.get(i).getUser_nick());
                        mapMarkerInfoBean.setUser_image(list.get(i).getDiary_image());
                        mapMarkerInfoBean.setDiary_type(list.get(i).getDiary_type());
                        mapMarkerInfoBean.setVideo_path(list.get(i).getDiary_video() + "");
                        mapMarkerInfoBean.setVideo_path_first(list.get(i).getDiary_video_first() + "");
                        addOverlay(mapMarkerInfoBean);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            byte[] camera_data = (byte[]) data.getExtras().get("camera_data");
            if (camera_data != null) {
                Intent intent2 = new Intent(MainActivity.this, DiaryImagePublishActivity.class);
                intent2.putExtra("camera_data", camera_data);
                intent2.putExtra("user_addr", user_addr);
                intent2.putExtra("user_lat", String.valueOf(user_latitude));
                intent2.putExtra("user_long", String.valueOf(user_longitude));
                startActivity(intent2);
            }
        }
        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            Intent intent2 = new Intent(MainActivity.this, DiaryVideoPublishActivity.class);
            intent2.putExtra("user_addr", user_addr);
            intent2.putExtra("user_lat", String.valueOf(user_latitude));
            intent2.putExtra("user_long", String.valueOf(user_longitude));
            startActivity(intent2);
        }
        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, qqShareListener);
            }
        }
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else if (addShow) {
            hindFloatActionButton();
        } else {
            super.onBackPressed();
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
            BmobQuery<UserInfoBean> query = new BmobQuery<>();
            query.addWhereEqualTo("user_name", mPreferences.getString("user_name", ""))
                    .findObjects(new FindListener<UserInfoBean>() {
                        @Override
                        public void done(List<UserInfoBean> list, BmobException e) {
                            if (e == null) {
                                String icon_url = list.get(0).getUser_icon();
                                user_icon.setImageURI(Uri.parse(icon_url));
                                left_user_icon.setImageURI(Uri.parse(icon_url));
                                left_user_name.setText(list.get(0).getNick_name());
                                binding_qq.setText(list.get(0).getBinding_qq());
                                binding_wechat.setText(list.get(0).getBinding_wechat());
                                binding_sina.setText(list.get(0).getBinding_sina());
                            } else {
                                Toast.makeText(MainActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            requestDiaryList();
        } else {
            user_icon.setImageResource(R.mipmap.ic_no_icon);
            left_user_icon.setImageResource(R.mipmap.ic_no_icon);
            left_user_name.setText("登陆让生活更精彩");
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
        unregisterReceiver(commonDataReceiver);
        super.onDestroy();
    }
}
