package com.will.talentreview.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.RequestKey;
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
 * @author chenwei
 * @time 2018-11-19
 * 登录页
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private final String TAG_LOGIN = TAG + "_login";
    private final String TAG_SEND_CODE = TAG + "_send_code";

    public static final int REQUEST_CODE_LOGIN = 1008;

    private EditText metLoginName;
    private EditText metPassword;
    private ImageView mivPwdIcon;
    private TextView mtvSendCode;
    private TextView mtvChangeMode;
    private TextView mtvForgetPwd;
    private TextView mtvClause;
    private TextView mtvRegisterTip;
    private String mTempPwd;
    private String mTempCode;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mCurrentSeconds;
    private MyHandler myHandler;


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleLeftListener();
        metLoginName = findViewById(R.id.et_login_name);
        metPassword = findViewById(R.id.et_password);
        mivPwdIcon = findViewById(R.id.iv_pwd_icon);
        mtvSendCode = findViewById(R.id.tv_send_code);
        mtvChangeMode = findViewById(R.id.tv_pwd_login);
        mtvClause = findViewById(R.id.tv_clause);
        mtvRegisterTip = findViewById(R.id.tv_register_tip);
        mtvForgetPwd = findViewById(R.id.tv_forget_password);
        findViewById(R.id.tv_login).setOnClickListener(this);
        mtvSendCode.setOnClickListener(this);
        mtvChangeMode.setOnClickListener(this);
        mtvForgetPwd.setOnClickListener(this);
        mtvClause.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        metLoginName.setText(StringUtils.excludeNull(MyApplication.getInstance().getLoginName()));

        int method = MyApplication.getInstance().getLoginMethod();
        if (AppConstants.LoginMethod.PASSWORD == method) {
            mtvSendCode.setVisibility(View.VISIBLE);
            changeLoginMethod();
        } else {
            mtvSendCode.setVisibility(View.GONE);
            changeLoginMethod();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (judgeParams()) {
                    doLogin();
                }
                break;
            case R.id.tv_send_code:
                if (TextUtils.isEmpty(metLoginName.getText().toString())) {
                    showShortToast("请输入手机号码");
                    return;
                }
                sendCode();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.tv_pwd_login:
                changeLoginMethod();
                break;
            case R.id.tv_clause:
                showShortToast("查看条款");
                break;
        }
    }

    private void changeLoginMethod() {
        if (mtvSendCode.getVisibility() == View.VISIBLE) {
            mTempCode = metPassword.getText().toString();
            metPassword.setText(StringUtils.excludeNull(mTempPwd));
            metPassword.setSelection(metPassword.getText().toString().length());
            mtvChangeMode.setText("验证码登录");
            mtvSendCode.setVisibility(View.GONE);
            mivPwdIcon.setImageResource(R.mipmap.ic_password);
            metPassword.setHint("密码");
            metPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mtvForgetPwd.setVisibility(View.VISIBLE);
            mtvRegisterTip.setVisibility(View.INVISIBLE);
        } else {
            mTempPwd = metPassword.getText().toString();
            metPassword.setText(StringUtils.excludeNull(mTempCode));
            metPassword.setSelection(metPassword.getText().toString().length());
            mtvChangeMode.setText("密码登录");
            mtvSendCode.setVisibility(View.VISIBLE);
            mivPwdIcon.setImageResource(R.mipmap.ic_verification_code);
            metPassword.setHint("验证码");
            metPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
            mtvForgetPwd.setVisibility(View.GONE);
            mtvRegisterTip.setVisibility(View.VISIBLE);
        }
    }

    private boolean judgeParams() {
        if (TextUtils.isEmpty(metLoginName.getText().toString())) {
            showShortToast("请输入手机号");
            return false;
        }
        if (mtvSendCode.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(metPassword.getText().toString())) {
                showShortToast("请输入密码");
                return false;
            }
        } else {
            if (TextUtils.isEmpty(metPassword.getText().toString())) {
                showShortToast("请输入验证码");
                return false;
            }
        }
        return true;
    }

    private void doLogin() {
        showProgressDialog("正在登陆，请稍后...", true, TAG_LOGIN);
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.MOBILE, metLoginName.getText().toString());
        if (mtvSendCode.getVisibility() == View.VISIBLE) {
            param.addParam(RequestKey.CODE, metPassword.getText().toString());
        } else {
            param.addParam(RequestKey.PASSWORD, metPassword.getText().toString());
        }
        RequestUtil.getInstance(this).doLogin(TAG_LOGIN, param, new CustomRequestCallback<LoginInfo>() {
            @Override
            public void onRequestFinished(RequestResult<LoginInfo> requestResult) {
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    LoginInfo loginInfo = requestResult.getData();
                    if (loginInfo != null) {
                        MyApplication.getInstance().saveLoginInfo(loginInfo);
                    }
                    MyApplication.getInstance().saveLoginName(metLoginName.getText().toString());
                    if (mtvSendCode.getVisibility() == View.VISIBLE) {
                        MyApplication.getInstance().saveLoginMethod(AppConstants.LoginMethod.CODE);
                    } else {
                        MyApplication.getInstance().saveLoginMethod(AppConstants.LoginMethod.PASSWORD);
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showShortToast(requestResult.getMsg());
                }
            }
        });
    }

    private void sendCode() {
        showProgressDialog("正在发送验证码，请稍后...", true, TAG_SEND_CODE);
        mtvSendCode.setEnabled(false);
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.MOBILE, metLoginName.getText().toString());
        RequestUtil.getInstance(this).sendCode(TAG_SEND_CODE, param, new CustomRequestCallback<String>() {

            @Override
            public void onRequestFinished(RequestResult<String> requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    startTimer();
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "发送失败，请重试"));
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

        WeakReference<LoginActivity> mWeakReference;

        public MyHandler(LoginActivity activity) {
            mWeakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mWeakReference.get();
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
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
