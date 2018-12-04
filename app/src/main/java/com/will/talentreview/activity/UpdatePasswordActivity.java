package com.will.talentreview.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.will.talentreview.R;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.LoginInfo;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

/**
 * Created by 38093 on 2018/12/2.
 */

public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText metOldPwd;
    private EditText metNewPwd;
    private EditText metConfirmPwd;


    @Override
    protected int getContentView() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleLeftListener();
        commonTitle.setTitleCenter("密码修改");

        metOldPwd = findViewById(R.id.et_old_pwd);
        metNewPwd = findViewById(R.id.et_new_pwd);
        metConfirmPwd = findViewById(R.id.et_confirm_pwd);
        findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (judgeParams()) {
                    submit();
                }
                break;
        }
    }

    private boolean judgeParams() {
        if (TextUtils.isEmpty(metOldPwd.getText().toString())) {
            showShortToast("请输入原密码");
            return false;
        }
        if (TextUtils.isEmpty(metNewPwd.getText().toString())) {
            showShortToast("请输入新密码");
            return false;
        }
        if (TextUtils.isEmpty(metConfirmPwd.getText().toString())) {
            showShortToast("请输入确认密码");
            return false;
        }
        if (StringUtils.isEquals(metOldPwd.getText().toString(), metConfirmPwd.getText().toString())) {
            showShortToast("确认密码不一致");
            return false;
        }
        return true;
    }

    private void submit() {
        showProgressDialog("正在提交，请稍后...", true, TAG);
        RequestParam param = new RequestParam();
        param.addParam("oldPwd", metOldPwd.getText().toString());
        param.addParam("newPwd", metConfirmPwd.getText().toString());
        RequestUtil.getInstance(this).updatePassword(TAG, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    showShortToast("修改成功");
                    finish();
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "修改失败"));
                }
            }
        });
    }
}
