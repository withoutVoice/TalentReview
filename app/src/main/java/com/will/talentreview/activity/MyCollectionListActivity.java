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
import com.will.talentreview.adapter.ProductListAdapter;
import com.will.talentreview.constant.Config;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.ListBean;
import com.will.talentreview.entity.Product;
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
 * @time 2018-11-26
 * 我的收藏
 */

public class MyCollectionListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = getClass().getSimpleName();
    private final String TAG_SEARCH = TAG + "_search";

    private SmartRefreshLayout mRefreshLayout;
    private ListView mLisView;

    private List<Product> mProducts;
    private ProductListAdapter mAdapter;

    private int page;
    private boolean isCollection;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_collection_list;
    }

    @Override
    protected void initView() {
        isCollection = getIntent().getBooleanExtra(IntentKey.IS_COLLECTION, true);
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter(isCollection ? "我的收藏" : "我的购买");
        commonTitle.setTitleLeftListener();

        mRefreshLayout = findViewById(R.id.refresh_layout);
        mLisView = findViewById(R.id.list_view);
        mLisView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        if (mProducts == null) {
            mProducts = new ArrayList<>();
        }
        mAdapter = new ProductListAdapter(this, mProducts);
        mLisView.setAdapter(mAdapter);
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
        Intent intent = new Intent(activity, ProductDetailActivity.class);
        intent.putExtra(IntentKey.PRODUCT, mProducts.get(position));
        startActivityForResult(intent, 0);
    }

    private void getData() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, page);
        param.addParam(RequestKey.PAGE_SIZE, Config.PAGE_SIZE);
        param.addParam("collectOrOrder", isCollection ? 1 : 2);
        RequestUtil.getInstance(this).getProductList(TAG_SEARCH, param, new CustomRequestCallback<Product>() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mRefreshLayout.autoRefresh();
        }
    }
}
