package com.will.talentreview.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.will.talentreview.R;
import com.will.talentreview.activity.ProductDetailActivity;
import com.will.talentreview.activity.SearchProductActivity;
import com.will.talentreview.adapter.ProductListAdapter;
import com.will.talentreview.adapter.ProductTypeGridAdapter;
import com.will.talentreview.constant.Config;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.Dict;
import com.will.talentreview.entity.ListBean;
import com.will.talentreview.entity.Product;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;
import com.will.talentreview.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-19
 */

public class TabProductFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static TabProductFragment getInstance() {
        return new TabProductFragment();
    }

    private final String TAG_PRODUCT_LIST = TAG + "_product_list";

    private View contentView;
    private LinearLayout mllTab;
    private LinearLayout mllFinancing;
    private LinearLayout mllAssets;
    private LinearLayout mllRecommend;
    private SmartRefreshLayout mRefreshLayout;
    private ListView mGridView;
    private ProductListAdapter mAdapter;
    private List<Product> mProducts;

    //筛选相关
    private List<Dict> mDictClasses;
    private List<Dict> mDictTypes;
    private List<Dict> mDictBottomTypes;
    private Dict currentClassDict;
    private Dict currentTypeDict;
    private Dict currentBottomTypeDict;
    private ProductTypeGridAdapter mBasicTypeAdapter;
    private ProductTypeGridAdapter mProductTypeAdapter;

    private int page;


    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_product;
    }

    @Override
    protected void initView(View contentView) {
        this.contentView = contentView;
        CommonTitle commonTitle = new CommonTitle(activity, contentView.findViewById(R.id.include), CommonTitle.TITLE_TYPE_7);
        commonTitle.setLlRightBtnClickListener("筛选", this);
        commonTitle.setBackgroundColor(getResources().getColor(R.color.black));
        commonTitle.setLlTitleSearchClickListener("",this);
        mllTab = contentView.findViewById(R.id.ll_tab);
        mRefreshLayout = contentView.findViewById(R.id.refresh_layout);
        mGridView = contentView.findViewById(R.id.list_view);

        mllFinancing = contentView.findViewById(R.id.ll_financing);
        mllAssets = contentView.findViewById(R.id.ll_assets);
        mllRecommend = contentView.findViewById(R.id.ll_recommend);

        mllFinancing.setOnClickListener(this);
        mllAssets.setOnClickListener(this);
        mllRecommend.setOnClickListener(this);
        mllFinancing.performClick();
        mGridView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

        mDictTypes = new ArrayList<>();
        mDictBottomTypes = new ArrayList<>();
        mBasicTypeAdapter = new ProductTypeGridAdapter(activity, mDictBottomTypes);
        mProductTypeAdapter = new ProductTypeGridAdapter(activity, mDictTypes);

        mProducts = new ArrayList<>();
        mAdapter = new ProductListAdapter(activity, mProducts);
        mGridView.setAdapter(mAdapter);
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
        initProductFilterDialog();
        getProductClass();
        getProductType();
        getProductBottomType();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivityForResult(new Intent(activity, SearchProductActivity.class),1);
                break;
            case R.id.ll_right_btn:
                showProductFilterDialog();
                break;
            case R.id.ll_financing:
                mllFinancing.setSelected(true);
                mllAssets.setSelected(false);
                mllRecommend.setSelected(false);
                currentClassDict = (Dict) view.getTag();
                mRefreshLayout.autoRefresh();
                break;
            case R.id.ll_assets:
                mllFinancing.setSelected(false);
                mllAssets.setSelected(true);
                mllRecommend.setSelected(false);
                break;
            case R.id.ll_recommend:
                mllFinancing.setSelected(false);
                mllAssets.setSelected(false);
                mllRecommend.setSelected(true);
                break;
            case R.id.ll_tab_item:
                if (view.isSelected()) {
                    return;
                }
                for (int i = 0; i < mllTab.getChildCount(); i++) {
                    mllTab.getChildAt(i).setSelected(false);
                }
                view.setSelected(true);
                mRefreshLayout.autoRefresh();
                currentClassDict = (Dict) view.getTag();
                mRefreshLayout.autoRefresh();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(IntentKey.PRODUCT, mProducts.get(i));
        startActivityForResult(intent,0);
    }

    private void setTopTab() {
        mllTab.removeAllViews();
        if (mDictClasses == null || mDictClasses.isEmpty()) {
            mllTab.setVisibility(View.GONE);
        } else {
            mllTab.setVisibility(View.VISIBLE);
            int width = getScreenWidth() / 3;
            for (Dict dict : mDictClasses) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_product_tab, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                TextView tvName = view.findViewById(R.id.tv_name);
                tvName.setText(StringUtils.excludeNull(dict.getValue(), "未知"));
                view.setTag(dict);
                mllTab.addView(view);
                view.setOnClickListener(this);
            }
        }
        if (mllTab.getChildCount() > 0) {
            mllTab.getChildAt(0).performClick();
        } else {
            mRefreshLayout.autoRefresh();
        }
    }

    private void getData() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, page);
        param.addParam(RequestKey.PAGE_SIZE, Config.PAGE_SIZE);
        param.addParam("startAgeLimit", metYearLimitLeft.getText().toString());
        param.addParam("endAgeLimit", metYearLimitLeft.getText().toString());
        param.addParam("startEarnings", metEarnLeft.getText().toString());
        param.addParam("endEarnings", metEarnRight == null ? "" : metYearLimitLeft.getText().toString());
        param.addParam("startMoney", metMoneyLimitLeft.getText().toString());
        param.addParam("endMoney", metMoneyLimitRight.getText().toString());
        param.addParam("productClassId", currentClassDict == null ? "" : currentClassDict.getId());
        param.addParam("productTypeId", mProductTypeAdapter.getSelectedItem() == null ? "" : mProductTypeAdapter.getSelectedItem().getId());
        param.addParam("underlyingTypeId", mBasicTypeAdapter.getSelectedItem() == null ? "" : mBasicTypeAdapter.getSelectedItem().getId());
        RequestUtil.getInstance(getActivity()).getProductList(TAG_PRODUCT_LIST, param, new CustomRequestCallback<Product>() {
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

    private Dialog filterDialog;
    private EditText metYearLimitLeft;
    private EditText metYearLimitRight;
    private EditText metEarnLeft;
    private EditText metEarnRight;
    private EditText metMoneyLimitLeft;
    private EditText metMoneyLimitRight;
    private MyGridView mgvBasicType;
    private MyGridView mgvProductType;

    private void initProductFilterDialog() {
        final View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_product_filter, null);
        metYearLimitLeft = contentView.findViewById(R.id.et_year_limit_left);
        metYearLimitRight = contentView.findViewById(R.id.et_year_limit_right);
        metEarnLeft = contentView.findViewById(R.id.et_earn_left);
        metEarnRight = contentView.findViewById(R.id.et_earn_right);
        metMoneyLimitLeft = contentView.findViewById(R.id.et_money_limit_left);
        metMoneyLimitRight = contentView.findViewById(R.id.et_money_limit_right);
        mgvBasicType = contentView.findViewById(R.id.gv_bottom_type);
        mgvProductType = contentView.findViewById(R.id.gv_type);
        mgvBasicType.setAdapter(mBasicTypeAdapter);
        mgvProductType.setAdapter(mProductTypeAdapter);
        contentView.findViewById(R.id.tv_filter_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metYearLimitLeft.setText("");
                metYearLimitRight.setText("");
                metEarnLeft.setText("");
                metEarnRight.setText("");
                metMoneyLimitLeft.setText("");
                metMoneyLimitRight.setText("");
                mProductTypeAdapter.clearSelect();
                mBasicTypeAdapter.clearSelect();
                hideKeyboardFrom(contentView.findFocus());
            }
        });
        contentView.findViewById(R.id.tv_filter_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardFrom(contentView.findFocus());
                filterDialog.dismiss();
                mRefreshLayout.autoRefresh();
            }
        });
        int width = (int) (getScreenWidth() * 0.86);
        filterDialog = new Dialog(getActivity());
        //去掉标题线
        filterDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(contentView);
        Window window = filterDialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.RIGHT;
        window.setAttributes(params);
        window.setWindowAnimations(R.style.popwin_anim_style);  //添加动画
    }

    private void showProductFilterDialog() {
        if (getActivity() != null && !filterDialog.isShowing()) {
            filterDialog.show();
        }

    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private void getProductClass() {
        showProgressDialog("加载中，请稍后...", false);
        RequestParam param = new RequestParam();
        param.addParam("dicType", "产品分类");
        RequestUtil.getInstance(getActivity()).getDictList("", param, new CustomRequestCallback<Dict>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    mDictClasses = requestResult.getDatas();
                }
                setTopTab();
            }
        });
    }

    private void getProductType() {
        RequestParam param = new RequestParam();
        param.addParam("dicType", "产品类型");
        RequestUtil.getInstance(getActivity()).getDictList("", param, new CustomRequestCallback<Dict>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    mDictTypes.clear();
                    mDictTypes.addAll(requestResult.getDatas());
                    mProductTypeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getProductBottomType() {
        showProgressDialog("加载中，请稍后...", false);
        RequestParam param = new RequestParam();
        param.addParam("dicType", "底层类型");
        RequestUtil.getInstance(getActivity()).getDictList("", param, new CustomRequestCallback<Dict>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                onTokenInvalid(requestResult.getCode());
                closeProgressDialog();
                if (requestResult.isSuccess()) {
                    mDictBottomTypes.clear();
                    mDictBottomTypes.addAll(requestResult.getDatas());
                    mBasicTypeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
