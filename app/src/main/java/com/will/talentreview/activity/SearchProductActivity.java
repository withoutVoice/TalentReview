package com.will.talentreview.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
 * 产品搜索页
 */

public class SearchProductActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private final String TAG = getClass().getSimpleName();
    private final String TAG_SEARCH = TAG + "_search";

    private SmartRefreshLayout mRefreshLayout;
    private ListView mLisView;
    private EditText metSearch;
    private TextView mtvNoData;

    private List<Product> mProducts;
    private ProductListAdapter mAdapter;

    private int page;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_product;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_8);
        commonTitle.setLlRightBtnClickListener("搜索", this);
        commonTitle.setTitleLeftListener();
        metSearch = commonTitle.getSearchEditText();

        mRefreshLayout = findViewById(R.id.refresh_layout);
        mLisView = findViewById(R.id.list_view);
        mtvNoData=findViewById(R.id.tv_no_data);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_right_btn:
                if (TextUtils.isEmpty(metSearch.getText().toString())) {
                    showShortToast("请输入关键字");
                    return;
                }
                mRefreshLayout.autoRefresh();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(activity, ProductDetailActivity.class);
        intent.putExtra(IntentKey.PRODUCT, mProducts.get(i));
        startActivityForResult(intent,0);
    }

    private void getData() {
        RequestParam param = new RequestParam();
        param.addParam("productName", metSearch.getText().toString());
        param.addParam(RequestKey.PAGE, page);
        param.addParam(RequestKey.PAGE_SIZE, Config.PAGE_SIZE);
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
                    if(page==1&&mAdapter.getCount()==0){
                        mtvNoData.setVisibility(View.VISIBLE);
                    }else {
                        mtvNoData.setVisibility(View.GONE);
                    }
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
        setResult(resultCode);
    }
}
