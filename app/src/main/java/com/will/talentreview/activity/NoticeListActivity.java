package com.will.talentreview.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
 */

public class NoticeListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = getClass().getSimpleName();

    private SmartRefreshLayout mRefreshLayout;
    private ListView mListView;

    private List<NoticeInfo> mNotices;
    private NoticeListAdapter mAdapter;

    private int page;

    @Override
    protected int getContentView() {
        return R.layout.activity_notice_list;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("公告");
        commonTitle.setTitleLeftListener();

        mRefreshLayout = findViewById(R.id.refresh_layout);
        mListView = findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

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
        param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE2);
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
