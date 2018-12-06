package com.will.talentreview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.will.talentreview.R;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.LeaveTalk;
import com.will.talentreview.entity.LoginInfo;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 38093 on 2018/12/2.
 */

public class UpdatePhoneActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG_SUBMIT = TAG + "_submit";
    private final String TAG_SEND_CODE = TAG + "_send_code";

    private EditText metPhone;
    private EditText metCode;
    private TextView mtvSendCode;
    private TextView mtvSubmit;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mCurrentSeconds;
    private MyHandler myHandler;

    private boolean isFirstStep;

    @Override
    protected int getContentView() {
        return R.layout.activity_update_phone;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("更换手机号");
        commonTitle.setTitleLeftListener();

        metPhone = findViewById(R.id.et_phone);
        metCode = findViewById(R.id.et_code);
        mtvSendCode = findViewById(R.id.tv_send_code);
        mtvSendCode.setOnClickListener(this);
        mtvSubmit=findViewById(R.id.tv_submit);
    }

    @Override
    protected void initData() {

        isFirstStep = getIntent().getBooleanExtra(IntentKey.IS_FIRST_STEP, true);

        if(!isFirstStep){
            metPhone.setHint("新手机号");
            mtvSubmit.setText("确定");
        }else {
            LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
            if (loginInfo != null) {
                metPhone.setText(StringUtils.excludeNull(loginInfo.getMobilePhone()));
            }
            mtvSubmit.setText("下一步");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_code:
                if (TextUtils.isEmpty(metPhone.getText().toString())) {
                    showShortToast("请输入手机号码");
                    return;
                }
                sendCode();
                break;
            case R.id.tv_submit:
                if (judgeParams()) {
                    submit();
                }
                break;
        }
    }

    private boolean judgeParams() {
        if (TextUtils.isEmpty(metPhone.getText().toString())) {
            showShortToast("请输入手机号");
            return false;
        }

        if (TextUtils.isEmpty(metPhone.getText().toString())) {
            showShortToast("请输入验证码");
            return false;
        }
        return true;
    }

    private void submit() {
        showProgressDialog("正在提交，请稍后...", true, TAG);
        RequestParam param = new RequestParam();
        param.addParam("type", isFirstStep ? 1 : 2);
        param.addParam("mobile", metPhone.getText().toString());
        param.addParam(RequestKey.CODE, metCode.getText().toString());
        RequestUtil.getInstance(this).updatePhone(TAG, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    if (isFirstStep) {
                        Intent intent = new Intent(activity, UpdatePhoneActivity.class);
                        intent.putExtra(IntentKey.IS_FIRST_STEP, false);
                        startActivityForResult(intent, 0);
                    }else {
                        showShortToast("提交成功");
                        LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
                        loginInfo.setMobilePhone(metPhone.getText().toString());
                        loginInfo.setStatusStr("认证成功");
                        if (loginInfo != null) {
                            MyApplication.getInstance().saveLoginInfo(loginInfo);
                        }
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "提交失败，请重试"));
                }
            }
        });
    }

    private void sendCode() {
        showProgressDialog("正在发送验证码，请稍后...", true, TAG_SEND_CODE);
        mtvSendCode.setEnabled(false);
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.MOBILE, metPhone.getText().toString());
        RequestUtil.getInstance(this).sendCode(TAG_SEND_CODE, param, new CustomRequestCallback<String>(){

            @Override
            public void onRequestFinished(RequestResult<String> requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if(requestResult.isSuccess()){
                    startTimer();
                }else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(),"发送失败，请重试"));
                }
            }
        });
    }

    private void startTimer() {
        mCurrentSeconds = 60;
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (myHandler == null) {
            myHandler = new MyHandler(this);
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    myHandler.sendEmptyMessage(0);
                }
            };
        }
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private static class MyHandler extends Handler {

        WeakReference<UpdatePhoneActivity> mWeakReference;

        public MyHandler(UpdatePhoneActivity activity) {
            mWeakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            UpdatePhoneActivity activity = mWeakReference.get();
            if (activity != null) {
                activity.setSeconds();
            }
        }
    }

    private void setSeconds() {
        if (mCurrentSeconds == 0) {
            cancelTimer();
            mtvSendCode.setText("重新发送");
            mtvSendCode.setEnabled(true);
        } else {
            mtvSendCode.setText(mCurrentSeconds + "秒");
            mCurrentSeconds--;
        }
    }

    private void cancelTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
