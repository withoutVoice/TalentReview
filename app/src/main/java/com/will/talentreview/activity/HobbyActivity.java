package com.will.talentreview.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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

/**
 * Created by 38093 on 2018/12/2.
 */

public class HobbyActivity extends BaseActivity implements View.OnClickListener {

    private EditText metContent;

    @Override
    protected int getContentView() {
        return R.layout.activity_hobby;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(activity, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("兴趣爱好");
        commonTitle.setTitleLeftListener();

        metContent = findViewById(R.id.et_content);
        findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
        if (loginInfo != null) {
            metContent.setText(StringUtils.excludeNull(loginInfo.getHobby()));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(metContent.getText().toString())) {
                    showShortToast("请输入兴趣爱好");
                    return;
                }
                submitTalk();
                break;
        }
    }

    private void submitTalk() {
        showProgressDialog("正在提交，请稍后...", true, TAG);
        RequestParam param = new RequestParam();
        param.addParam("hobby", metContent.getText().toString());
        RequestUtil.getInstance(this).submitBirthday(TAG, param, new CustomRequestCallback<LeaveTalk>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    showShortToast("提交成功");

                    LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
                    loginInfo.setHobby(metContent.getText().toString());
                    loginInfo.setStatusStr("认证成功");
                    if (loginInfo != null) {
                        MyApplication.getInstance().saveLoginInfo(loginInfo);
                    }
                    metContent.setText("");
                    finish();
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "提交失败，请重试"));
                }
            }
        });
    }
}
