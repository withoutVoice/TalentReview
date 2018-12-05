package com.will.talentreview.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.will.talentreview.R;
import com.will.talentreview.constant.Config;
import com.will.talentreview.utils.ExitApplication;
import com.will.talentreview.utils.StringUtils;

/**
 * 基础activity
 *
 * @author chenwei
 * @time 2018/11/19
 */

public abstract class BaseActivity extends FragmentActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected Activity activity;
    private Toast toast;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        ExitApplication.getInstance().addActivity(activity);
        addFlags();
        initUI();
    }

    protected void initUI() {
        setContentView(getContentView());
        initView();
        initData();
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    protected void addFlags() {
        //设置透明背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void clearFlags() {
        //清除透明背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置标题栏高度
     */
    public void setStatusBarTranslucentLayout(View view) {
        View llTitle = view.findViewById(R.id.ll_title_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(activity);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = activity.getResources().getDimensionPixelSize(R.dimen.dip_50) + statusBarHeight;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTitle.getLayoutParams();
            params.setMargins(0, statusBarHeight, 0, 0);
            llTitle.setLayoutParams(params);
        }
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Toast，展示信息提示
     *
     * @param content 展示内容
     */
    public void showShortToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(activity, null, Toast.LENGTH_SHORT);
            toast.setText(content);
        } else {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * Toast，展示信息提示
     *
     * @param content 展示内容资源ID
     */
    public void showShortToast(int content) {
        if (toast == null) {
            toast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * 显示进度提示框
     *
     * @param content    显示内容
     * @param cancelable 是否可被取消
     */
    public void showProgressDialog(String content, boolean cancelable) {
        if (this.isFinishing()) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(content);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * 显示进度提示框
     *
     * @param content    显示内容
     * @param cancelable 是否可被取消
     * @param tag        请求标志
     */
    public void showProgressDialog(String content, boolean cancelable, final String tag) {
        if (this.isFinishing()) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialogInterface.dismiss();
                }
                onProgressDialogCancel(tag);
                return false;
            }
        });
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(content);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    protected void onProgressDialogCancel(String tag) {

    }

    /**
     * 关闭进度条
     */
    public void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * token失效处理
     */
    protected void onTokenInvalid(int code) {
        if (code == Config.TOKEN_ERROR_CODE) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setClass(activity, LoginActivity.class);
            startActivityForResult(intent, LoginActivity.REQUEST_CODE_LOGIN);
        }
    }

    /**
     * 收起键盘
     */
    public void hideKeyboardFrom(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 收起键盘
     */
    public void showKeyboardFrom(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    private DatePickDialog datePickDialog;

    protected void showDatePicker(OnSureLisener onSureLisener) {
        if (datePickDialog == null) {
            datePickDialog = new DatePickDialog(this);
            //设置上下年分限制
            datePickDialog.setYearLimt(20);
            //设置标题
            datePickDialog.setTitle("选择时间");
            //设置类型
            datePickDialog.setType(DateType.TYPE_YMD);
            //设置消息体的显示格式，日期格式
            datePickDialog.setMessageFormat(DateType.TYPE_YMD.getFormat());
            //设置选择回调
            datePickDialog.setOnChangeLisener(null);
        } else {
            if (datePickDialog.isShowing()) {
                datePickDialog.dismiss();
            }
        }
        //设置点击确定按钮回调
        datePickDialog.setOnSureLisener(onSureLisener);
        datePickDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
        ExitApplication.getInstance().addActivity(activity);
    }
}
