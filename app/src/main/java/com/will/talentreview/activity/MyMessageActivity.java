package com.will.talentreview.activity;

import com.will.talentreview.R;
import com.will.talentreview.view.CommonTitle;

/**
 * @author chenwei
 * @time 2018-12-03
 */

public class MyMessageActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("个人消息");
        commonTitle.setTitleLeftListener();
    }

    @Override
    protected void initData() {

    }
}
