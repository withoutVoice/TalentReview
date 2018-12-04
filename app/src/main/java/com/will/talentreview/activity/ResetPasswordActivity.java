package com.will.talentreview.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.will.talentreview.R;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * @author chenwei
 * @time 2018-11-21
 * 重置密码页
 */

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private final String TAG_SEND_CODE = TAG + "_send_code";
    private final String TAG_FIND_PASSWORD = TAG + "_find_password";

    private EditText metPhone;
    private EditText metPassword;
    private EditText metCode;
    private TextView mtvSendCode;
    private TextView mtvSubmit;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mCurrentSeconds;
    private MyHandler myHandler;

    @Override
    protected int getContentView() {
        return R.layout.activity_reset_passwrod;
    }

    @Override
    protected void initView() {
        metPhone = findViewById(R.id.et_phone);
        metPassword = findViewById(R.id.et_password);
        mtvSendCode = findViewById(R.id.tv_send_code);
        metCode = findViewById(R.id.et_code);
        mtvSubmit=findViewById(R.id.tv_submit);

        mtvSubmit.setOnClickListener(this);
        findViewById(R.id.tv_send_code).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if(judgeResetParams()){
                    resetPassword();
                }
                break;
            case R.id.tv_send_code:
                if(judgeCodeParams()){
                    resetPassword();
                }
                sendCode();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    private void sendCode() {
        showProgressDialog("正在发送验证码，请稍后...", true);
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

        WeakReference<ResetPasswordActivity> mWeakReference;

        public MyHandler(ResetPasswordActivity activity) {
            mWeakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ResetPasswordActivity activity = mWeakReference.get();
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

    private boolean judgeCodeParams(){
        if(TextUtils.isEmpty(metPhone.getText().toString())){
            showShortToast("请输入手机号");
            return false;
        }
        if(TextUtils.isEmpty(metCode.getText().toString())){
            showShortToast("请输入验证码");
            return false;
        }
        return true;
    }

    private boolean judgeResetParams(){
        if(TextUtils.isEmpty(metPhone.getText().toString())){
            showShortToast("请输入手机号");
            return false;
        }
        if(TextUtils.isEmpty(metCode.getText().toString())){
            showShortToast("请输入验证码");
            return false;
        }
        if(TextUtils.isEmpty(metPassword.getText().toString())){
            showShortToast("请输入新密码");
            return false;
        }
        return true;
    }

    private void resetPassword() {
        showProgressDialog("正在设置密码，请稍后...",true);
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.MOBILE, metPhone.getText().toString());
        param.addParam(RequestKey.PASSWORD, metPassword.getText().toString());
        param.addParam(RequestKey.CODE, metCode.getText().toString());
        RequestUtil.getInstance(this).findPassword(TAG_FIND_PASSWORD, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                if(requestResult.isSuccess()){
                    finish();
                }else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(),"设置失败，请重试"));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
