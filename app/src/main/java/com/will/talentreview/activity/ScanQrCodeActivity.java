package com.will.talentreview.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.utils.PermissionUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 扫描二维码
 *
 * @author chenwei
 * @time 2018-06-20
 */

public class ScanQrCodeActivity extends BaseActivity implements QRCodeView.Delegate, View.OnClickListener {

    private LinearLayout llOpenLight;
    private TextView tvOpenLight;
    private QRCodeView mQRCodeView;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    @Override
    protected int getContentView() {
        return R.layout.activity_scan_qrcode;
    }

    @Override
    protected void initView() {

        CommonTitle commonTitle = new CommonTitle(activity, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleLeftListener();
        String title = getIntent().getStringExtra(IntentKey.TITLE);
        commonTitle.setTitleCenter(TextUtils.isEmpty(title) ? "扫码" : title);
        llOpenLight = findViewById(R.id.ll_open_light);
        tvOpenLight = findViewById(R.id.tv_open_light);
        mQRCodeView = (ZXingView) findViewById(R.id.zxing_view);
        mQRCodeView.setDelegate(this);
        llOpenLight.setOnClickListener(this);
    }

    @Override
    protected void initData() {
//        //获取SensorManager对象
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        //获取Sensor对象
//        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == llOpenLight.getId()) {
//            if (sensorManager != null) {
//                sensorManager.unregisterListener(sensorEventListener);
//                lightSensor = null;
//                sensorManager = null;
//            }
//            if (llOpenLight.isSelected()) {
//                openLight(false);
//            } else {
//                openLight(true);
//            }
//
//        }
    }

    private void openLight(boolean open) {
        if (open) {
            mQRCodeView.openFlashlight();
            tvOpenLight.setText("轻触关闭");
            llOpenLight.setSelected(true);
        } else {
            mQRCodeView.closeFlashlight();
            tvOpenLight.setText("轻触照亮");
            llOpenLight.setSelected(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionUtil.OnRequestPermissionsResultCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, List<String> perms, boolean isAllGranted) {
                startScan();
            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms, boolean isAllDenied) {
                showShortToast("请开启相机权限");
            }
        });
    }

    private void startScan() {
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PermissionUtil.getCameraPermissions(this, 101)) {
            startScan();
        }

    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        mQRCodeView.startSpot();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        //注销监听器
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
            sensorManager = null;
            lightSensor = null;
        }
        super.onDestroy();
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        onScanSuccess(result);
        mQRCodeView.startSpot();
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // values数组中第一个值就是当前的光照强度
            float value = sensorEvent.values[0];
            int retval = Float.compare(value, (float) 10.0);
            if (retval > 0) {//光线强度>10.0
                openLight(false);
            } else {
                openLight(true);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onScanQRCodeOpenCameraError() {
        showShortToast("打开相机出错");
    }

    protected void onScanSuccess(String result) {
        showShortToast("扫码成功");
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra(IntentKey.CONTENT, result);
//        intent.putExtra(IntentKey.FROM_WHAT, AppConstants.WebFromWhat.SCAN_CODE);
//        startActivity(intent);
    }

}
