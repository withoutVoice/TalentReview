package com.will.talentreview.fragment;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.will.talentreview.R;
import com.will.talentreview.adapter.ProductListAdapter;
import com.will.talentreview.constant.Config;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.ListBean;
import com.will.talentreview.entity.Product;
import com.will.talentreview.entity.ProductFilter;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenwei
 * @time 2018-12-05
 */

public class ProductListFragment extends BaseFragment {

    private SmartRefreshLayout mRefreshLayout;
    private ListView mListView;
    private ProductListAdapter mAdapter;
    private List<Product> mProducts;

    private ProductFilter mFilter;
    private int page;

    @Override
    protected int getContentView() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void initView(View contentView) {
        mRefreshLayout = contentView.findViewById(R.id.refresh_layout);
        mListView = contentView.findViewById(R.id.list_view);
    }

    @Override
    protected void initData() {
        mProducts = new ArrayList<>();
        mAdapter = new ProductListAdapter(activity, mProducts);
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
    }

    private void getData() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, page);
        param.addParam(RequestKey.PAGE_SIZE, Config.PAGE_SIZE);
        param.addParam("startAgeLimit", mFilter == null ? "" : mFilter.getStartAgeLimit());
        param.addParam("endAgeLimit", mFilter == null ? "" : mFilter.getEndAgeLimit());
        param.addParam("startEarnings", mFilter == null ? "" : mFilter.getStartEarnings());
        param.addParam("endEarnings", mFilter == null ? "" : mFilter.getEndEarnings());
        param.addParam("startMoney", mFilter == null ? "" : mFilter.getStartMoney());
        param.addParam("endMoney", mFilter == null ? "" : mFilter.getEndMoney());
        param.addParam("productClassId", mFilter == null ? "" : mFilter.getProductClassId());
        param.addParam("productTypeId", mFilter == null ? "" : mFilter.getProductTypeId());
        param.addParam("underlyingTypeId", mFilter == null ? "" : mFilter.getUnderlyingTypeId());
        RequestUtil.getInstance(getActivity()).getProductList("", param, new CustomRequestCallback<Product>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    if (page == 1) {
                        mProducts.clear();
                    }
                    ListBean<Product> bean = (ListBean<Product>) requestResult.getData();
                    if (bean != null) {
                        mRefreshLayout.setEnableLoadMore(false);
                        List<Product> notices = bean.getRecords();
                        if (notices != null && !notices.isEmpty()) {
                            mProducts.addAll(notices);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    setLoadMoreEnable(bean);
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

    public void refreshData(ProductFilter filter) {
        mFilter = filter;
        mRefreshLayout.autoRefresh();
    }

}
