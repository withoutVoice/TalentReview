package com.will.talentreview.activity;

import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

/**
 * @author chenwei
 * @time 2018-12-04
 * 机构详情
 */

public class MechanismActivity extends BaseActivity {

    private TextView mtvContent;
    private CommonTitle commonTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_mechanism;
    }

    @Override
    protected void initView() {
        commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleLeftListener();

        mtvContent=findViewById(R.id.tv_content);
    }

    @Override
    protected void initData() {
        commonTitle.setTitleCenter(StringUtils.excludeNull(getIntent().getStringExtra(IntentKey.TITLE), "暂无"));
        mtvContent.setText(StringUtils.excludeNull(getIntent().getStringExtra(IntentKey.CONTENT), "暂无"));
    }
}
