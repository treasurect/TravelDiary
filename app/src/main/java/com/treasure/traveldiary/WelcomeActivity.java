package com.treasure.traveldiary;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;
import android.widget.Toast;

public class WelcomeActivity extends BaseActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkNetworkState()) {
                    checkPhonePermission();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            WelcomeActivity.this.finish();
                        }
                    });
                    builder.setMessage("检测到没有网络，请开启网络后，在使用产品");
                    builder.setIcon(R.mipmap.ic_travel_logo);
                    builder.create();
                    builder.show();
                }
            }
        }, 1000);
    }

    private boolean checkNetworkState() {
        ConnectivityManager systemService =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (systemService.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
    //请求手机状态权限
    private void checkPhonePermission() {
        if (Build.VERSION.SDK_INT >= 21 && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            checkStoragePermission();
        }
    }
    //请求存储权限
    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 21 && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            checkLocationPermission();
        }
    }
    //请求定位权限
    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 21 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        } else {
            checkCameraPermission();
        }
    }
    //请求相机权限
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= 21 && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 4);
        } else {
            checkAudioPermission();
        }
    }
    //请求录音权限
    private void checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= 21 && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 5);
        } else {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            WelcomeActivity.this.finish();
        }
    }
    //动态权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPhonePermission();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        WelcomeActivity.this.finish();
                    }
                });
                builder.setMessage("检测到没有读取手机状态权限，请开启手机状态权限后，在使用产品");
                builder.setIcon(R.mipmap.ic_travel_logo);
                builder.create();
                builder.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkStoragePermission();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        WelcomeActivity.this.finish();
                    }
                });
                builder.setMessage("检测到没有存储权限，请开启存储权限后，在使用产品");
                builder.setIcon(R.mipmap.ic_travel_logo);
                builder.create();
                builder.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == 3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        WelcomeActivity.this.finish();
                    }
                });
                builder.setMessage("检测到没有定位权限，请开启定位权限后，在使用产品");
                builder.setIcon(R.mipmap.ic_travel_logo);
                builder.create();
                builder.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == 4) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkCameraPermission();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        WelcomeActivity.this.finish();
                    }
                });
                builder.setMessage("检测到没有相机权限，请开启相机权限后，在使用产品");
                builder.setIcon(R.mipmap.ic_travel_logo);
                builder.create();
                builder.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkAudioPermission();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        WelcomeActivity.this.finish();
                    }
                });
                builder.setMessage("检测到没有录音权限，请开启录音权限后，在使用产品");
                builder.setIcon(R.mipmap.ic_travel_logo);
                builder.create();
                builder.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
