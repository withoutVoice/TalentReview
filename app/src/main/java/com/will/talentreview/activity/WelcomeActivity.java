package com.will.talentreview.activity;

import android.content.Intent;
import android.os.Handler;

import com.will.talentreview.R;

/**
 * @author chenwei
 * @time 2018-11-30
 * 欢迎页
 */

public class WelcomeActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(activity, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
