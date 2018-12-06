package com.will.talentreview.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.will.talentreview.R;
import com.will.talentreview.adapter.NoticeListAdapter;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.Config;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.ListBean;
import com.will.talentreview.entity.NoticeInfo;
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
 * @time 2018-11-28
 * 平台优势列表
 */

public class AdvantageListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = getClass().getSimpleName();

    private LinearLayout mllHeader;
    private TextView mtvHeader;
    private SmartRefreshLayout mRefreshLayout;
    private ListView mListView;

    private List<NoticeInfo> mNotices;
    private NoticeListAdapter mAdapter;

    private int page;
    private int fromWhat;
    private CommonTitle commonTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_advantage_list;
    }

    @Override
    protected void initView() {
        commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleLeftListener();

        mllHeader = findViewById(R.id.ll_header);
        mtvHeader = findViewById(R.id.tv_header);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mListView = findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        fromWhat = getIntent().getIntExtra(IntentKey.FROM_WHAT, 0);
        switch (fromWhat) {
            case AppConstants.HomeDataType.TYPE3:
                mllHeader.setBackgroundResource(R.mipmap.img_new_player_header);
                mtvHeader.setText("即使是不成熟的尝试，也胜于胎死腹中的策略");
                commonTitle.setTitleCenter("新人指南");
                break;
            case AppConstants.HomeDataType.TYPE4:
                mllHeader.setBackgroundResource(R.mipmap.img_school_header);
                mtvHeader.setText("学无止境，勇攀高峰");
                commonTitle.setTitleCenter("金融学院");
                break;
            case AppConstants.HomeDataType.TYPE5:
                mllHeader.setBackgroundResource(R.mipmap.img_advantage_header);
                mtvHeader.setText("");
                commonTitle.setTitleCenter("风险警示");
                break;
        }
        mNotices = new ArrayList<>();
        mAdapter = new NoticeListAdapter(this, mNotices);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(IntentKey.WEB_CONTENT, mNotices.get(position));
        intent.putExtra(IntentKey.FROM_WHAT, AppConstants.WebFromWhat.NOTICE);
        startActivity(intent);
    }

    private void getData() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, page);
        param.addParam(RequestKey.PAGE_SIZE, Config.PAGE_SIZE);
        switch (fromWhat) {
            case AppConstants.HomeDataType.TYPE3:
                param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE3);
                break;
            case AppConstants.HomeDataType.TYPE4:
                param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE4);
                break;
            case AppConstants.HomeDataType.TYPE5:
                param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE5);
                break;
        }
        RequestUtil.getInstance(this).getNoticeList(TAG, param, new CustomRequestCallback<NoticeInfo>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
                if (requestResult.isSuccess()) {
                    if (page == 1) {
                        mNotices.clear();
                    }
                    ListBean<NoticeInfo> bean = (ListBean<NoticeInfo>) requestResult.getData();
                    if (bean == null) {
                        mRefreshLayout.setEnableLoadMore(false);
                    } else {
                        List<NoticeInfo> notices = bean.getRecords();
                        if (notices != null && !notices.isEmpty()) {
                            mNotices.addAll(notices);
                        }
                        setLoadMoreEnable(bean);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (page > 1) {
                        page--;
                    }
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "获取失败，请重试"));
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
}
