package com.will.talentreview.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tamic.novate.RxApiManager;
import com.will.talentreview.activity.LoginActivity;
import com.will.talentreview.constant.Config;
import com.will.talentreview.utils.StringUtils;


/**
 * 基础Fragment
 *
 * @author yanchenglong
 * @time 2018/5/21
 */

public abstract class BaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    protected Activity activity;
    private Toast toast;
    protected ProgressDialog progressDialog;
    protected View contentView;

    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getContentView(), null);
        initView(contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract int getContentView();

    protected abstract void initView(View contentView);

    protected abstract void initData();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    /**
     * Toast，展示信息提示
     *
     * @param content 展示内容
     */
    protected void showShortToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
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
    protected void showShortToast(int content) {
        if (toast == null) {
            toast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            hideFragment();
        } else {
            showFragment();
        }
    }

    /**
     * fragment显示时调用
     */
    protected void showFragment() {

    }

    /**
     * fragment隐藏时调用
     */
    protected void hideFragment() {

    }

    /**
     * 显示进度提示框
     *
     * @param content    显示内容
     * @param cancelable 是否可被取消
     */
    public void showProgressDialog(String content, boolean cancelable) {
        if (activity.isFinishing()) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
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
     */
    public void showProgressDialog(String content, boolean cancelable, final Object tag) {
        if (activity.isFinishing()) {
            return;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
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

    protected void onProgressDialogCancel(Object tag) {
        RxApiManager.get().cancel(tag);
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
     * 收起键盘
     */
    public void hideKeyboardFrom(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 收起键盘
     */
    public void showKeyboardFrom(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
    }
}
