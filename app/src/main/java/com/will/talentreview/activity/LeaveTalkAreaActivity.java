package com.will.talentreview.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.will.talentreview.R;
import com.will.talentreview.adapter.TalkListAdapter;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.Config;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.LeaveTalk;
import com.will.talentreview.entity.ListBean;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-30
 * 留言区
 */

public class LeaveTalkAreaActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG_TALK_LIST = TAG + "_talk_list";
    private final String TAG_SUBMIT_TALK = TAG + "_submit_talk";

    private SmartRefreshLayout mRefreshLayout;
    private ListView mListView;
    private EditText metContent;

    private List<LeaveTalk> mTalks;
    private TalkListAdapter mAdapter;


    private int page;

    @Override
    protected int getContentView() {
        return R.layout.activity_leave_talk_area;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleLeftListener();
        commonTitle.setTitleCenter("留言区");

        mRefreshLayout = findViewById(R.id.refresh_layout);
        mListView = findViewById(R.id.list_view);
        metContent = findViewById(R.id.et_content);

        findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    protected void initData() {

        mTalks = new ArrayList<>();
        mAdapter = new TalkListAdapter(this, mTalks);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData();
            }
        });
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (MyApplication.getInstance().getLoginInfo() == null) {
                    startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.REQUEST_CODE_LOGIN);
                    return;
                }
                if (TextUtils.isEmpty(metContent.getText().toString())) {
                    showShortToast("请填写留言");
                    return;
                }
                submitTalk();
                break;
        }
    }

    private void getData() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, page);
        param.addParam(RequestKey.PAGE_SIZE, Config.PAGE_SIZE);
        param.addParam(RequestKey.PROJECT_ID, getIntent().getStringExtra(IntentKey.PRODUCT_ID));
        RequestUtil.getInstance(this).getTalkList(TAG_TALK_LIST, param, new CustomRequestCallback<LeaveTalk>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
                if (requestResult.isSuccess()) {
                    if (page == 1) {
                        mTalks.clear();
                    }
                    ListBean<LeaveTalk> bean = (ListBean<LeaveTalk>) requestResult.getData();
                    if (bean != null) {
                        List<LeaveTalk> talks = bean.getRecords();
                        if (talks != null && !talks.isEmpty()) {
                            mTalks.addAll(talks);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    setLoadMoreEnable(bean);
                } else {
                    if (page > 1) {
                        page--;
                    }
                }
            }
        });
    }

    private void setLoadMoreEnable(ListBean bean) {
        if (bean == null || bean.getTotalPage() <= page) {
            mRefreshLayout.setEnableLoadMore(false);
        } else {
            mRefreshLayout.setEnableLoadMore(true);
        }
    }

    private void submitTalk() {
        showProgressDialog("正在提交，请稍后...", true, TAG_SUBMIT_TALK);
        RequestParam param = new RequestParam();
        param.addParam("message", metContent.getText().toString());
        param.addParam(RequestKey.PROJECT_ID, getIntent().getStringExtra(IntentKey.PRODUCT_ID));
        RequestUtil.getInstance(this).submitTalk(TAG_TALK_LIST, param, new CustomRequestCallback<LeaveTalk>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    showShortToast("新增留言成功");
                    metContent.setText("");
                    hideKeyboardFrom(metContent);
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "提交失败，请重试"));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.REQUEST_CODE_LOGIN) {
                mRefreshLayout.autoRefresh();
            }
        }
    }
}
