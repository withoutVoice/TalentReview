package com.will.talentreview.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.will.talentreview.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author chenwei
 * @time 2018-11-20
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvSendCode;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int currentSeconds;

    private MyHandler myHandler;

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        mTvSendCode = findViewById(R.id.tv_send_code);

        mTvSendCode.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                sendCode();
                break;
        }
    }

    private void sendCode() {
        showProgressDialog("正在发送验证码，请稍后...", true);
        mTvSendCode.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                startTimer();
            }
        }, 2000);
    }

    private void startTimer() {
        currentSeconds = 60;
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if(myHandler==null){
            myHandler=new MyHandler(this);
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

        WeakReference<RegisterActivity> mWeakReference;

        public MyHandler(RegisterActivity activity) {
            mWeakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterActivity activity = mWeakReference.get();
            if (activity != null) {
                activity.setSeconds();
            }
        }
    }

    private void setSeconds() {
        if (currentSeconds == 0) {
            cancelTimer();
            mTvSendCode.setText("重新发送");
            mTvSendCode.setEnabled(true);
        } else {
            mTvSendCode.setText(currentSeconds + "秒");
            currentSeconds--;
        }
    }

    private void cancelTimer() {
        if(mTimerTask!=null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
