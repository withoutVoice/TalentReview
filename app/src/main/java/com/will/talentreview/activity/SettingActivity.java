package com.will.talentreview.activity;

import android.content.Intent;
import android.view.View;

import com.tamic.novate.RxApiManager;
import com.will.talentreview.R;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.Config;
import com.will.talentreview.entity.LoginInfo;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

import okhttp3.Call;

/**
 * @author chenwei
 * @time 2018-11-30
 * 设置
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private final int REQUEST_CODE_CHANGE_PHONE = 1;

    @Override
    protected int getContentView() {
        return R.layout.acitivity_setting;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("设置");
        commonTitle.setTitleLeftListener();

        findViewById(R.id.ll_hobby).setOnClickListener(this);
        findViewById(R.id.ll_change_phone).setOnClickListener(this);
        findViewById(R.id.ll_update_pwd).setOnClickListener(this);
        findViewById(R.id.ll_logout).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_hobby:
                startActivity(new Intent(this, HobbyActivity.class));
                break;
            case R.id.ll_change_phone:
                startActivityForResult(new Intent(this, UpdatePhoneActivity.class), REQUEST_CODE_CHANGE_PHONE);
                break;
            case R.id.ll_update_pwd:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
            case R.id.ll_logout:
                doLogout();
                break;
        }
    }

    private void doLogout() {
        showProgressDialog("正在退出登录，请稍后...", true, TAG);
        RequestParam param = new RequestParam();
        RequestUtil.getInstance(this).doLogout(TAG, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    MyApplication.getInstance().clearLoginInfo();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (requestResult.getCode() == Config.TOKEN_ERROR_CODE) {
                        MyApplication.getInstance().clearLoginInfo();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "退出登录失败，请重试"));
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CODE_CHANGE_PHONE){
                setResult(RESULT_OK);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxApiManager.get().cancel(TAG);
    }
}
