package com.will.talentreview.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.BrightPoint;
import com.will.talentreview.entity.LoginInfo;
import com.will.talentreview.entity.Product;
import com.will.talentreview.entity.ProductEarn;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

import java.util.Date;
import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-29
 * 产品详情
 */

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG_COLLECT = TAG + "_collect";
    private final String TAG_PURCHASE = TAG + "_purchase";

    private LinearLayout mllInterestRate1;
    private LinearLayout mllInterestRate2;
    private LinearLayout mllInterestRate3;
    private TextView mtvInterestRate1;
    private TextView mtvInterestRate2;
    private TextView mtvInterestRate3;
    private TextView mtvRange1;
    private TextView mtvRange2;
    private TextView mtvRange3;
    private TextView mtvProductType;
    private TextView mtvProductScope;
    private TextView mtvBottomType;
    private TextView mtvYearLimit;
    private TextView mtvMoneyPurpose;
    private LinearLayout mllBrightPoint;
    private View mvCollect;
    private TextView mtvCollect;

    private CommonTitle commonTitle;

    private Product mProduct;

    @Override
    protected int getContentView() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initView() {
        commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("产品详情");
        commonTitle.setTitleLeftListener();

        mtvInterestRate1 = findViewById(R.id.tv_interest_rate1);
        mtvInterestRate2 = findViewById(R.id.tv_interest_rate2);
        mtvInterestRate3 = findViewById(R.id.tv_interest_rate3);
        mtvRange1 = findViewById(R.id.tv_range1);
        mtvRange2 = findViewById(R.id.tv_range2);
        mtvRange3 = findViewById(R.id.tv_range3);
        mtvProductType = findViewById(R.id.tv_type);
        mtvProductScope = findViewById(R.id.tv_scope);
        mtvBottomType = findViewById(R.id.tv_bottom_type);
        mtvYearLimit = findViewById(R.id.tv_year_limit);
        mtvMoneyPurpose = findViewById(R.id.tv_purpose);
        mllBrightPoint = findViewById(R.id.ll_bright_point);
        mvCollect = findViewById(R.id.iv_collect);
        mtvCollect = findViewById(R.id.tv_collect);
        mllInterestRate1 = findViewById(R.id.ll_interest_rate1);
        mllInterestRate2 = findViewById(R.id.ll_interest_rate2);
        mllInterestRate3 = findViewById(R.id.ll_interest_rate3);

        findViewById(R.id.ll_finance_mechanism).setOnClickListener(this);
        findViewById(R.id.ll_guarantee_mechanism).setOnClickListener(this);
        findViewById(R.id.ll_issue_mechanism).setOnClickListener(this);
        findViewById(R.id.ll_talk).setOnClickListener(this);
        findViewById(R.id.ll_collect).setOnClickListener(this);
        findViewById(R.id.tv_buy).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mProduct = (Product) getIntent().getSerializableExtra(IntentKey.PRODUCT);
        if (mProduct == null) {
            showShortToast("数据有误");
            finish();
            return;
        }
        commonTitle.setTitleCenter(StringUtils.excludeNull(mProduct.getProductName(), "未知"));
        mtvProductType.setText(StringUtils.excludeNull(mProduct.getProductType(), "未知"));
        mtvProductScope.setText(StringUtils.excludeNull(mProduct.getProductRule(), "0万元"));
        mtvBottomType.setText(StringUtils.excludeNull(mProduct.getBehindType(), "未知"));
        mtvYearLimit.setText(StringUtils.excludeNull(mProduct.getAgeLimit(), "0") + "个月");
        mtvMoneyPurpose.setText(StringUtils.excludeNull(mProduct.getMoenUse(), "未知"));
        mvCollect.setBackgroundResource(StringUtils.isEquals(mProduct.getIsCollection(), "0") ? R.mipmap.ic_collect : R.mipmap.ic_my_collection);
        mtvCollect.setText(StringUtils.isEquals(mProduct.getIsCollection(), "0") ? "收藏" : "取消收藏");

        initEarn(mProduct.getEarningsEntityList());
        initBrightPoint(mProduct.getBrohtLightEntityList());
    }

    private void initEarn(List<ProductEarn> earns) {
        if (earns == null || earns.isEmpty()) {
            mtvInterestRate1.setText("");
            mtvInterestRate2.setText("0.0%");
            mtvInterestRate3.setText("");
            mtvRange1.setText("");
            mtvRange2.setText("0元-0元");
            mtvRange3.setText("");
            mllInterestRate2.setVisibility(View.GONE);
            mllInterestRate3.setVisibility(View.GONE);
        } else if (earns.size() == 1) {
            ProductEarn earn = earns.get(0);
            mtvInterestRate1.setText("");
            mtvInterestRate2.setText(StringUtils.excludeNull(earns.get(0).getEarnings(), "0.0") + "%");
            mtvInterestRate3.setText("");
            mtvRange1.setText("");
            mtvRange2.setText(StringUtils.excludeNull(earn.getStartEarnings(), "0") + "万元-" + StringUtils.excludeNull(earn.getEndEarnings(), "0") + "万元");
            mtvRange3.setText("");
            mllInterestRate2.setVisibility(View.GONE);
            mllInterestRate3.setVisibility(View.GONE);
        } else if (earns.size() == 2) {
            ProductEarn earn1 = earns.get(0);
            ProductEarn earn2 = earns.get(1);
            mtvInterestRate1.setText(StringUtils.excludeNull(earn1.getEarnings(), "0.0") + "%");
            mtvInterestRate2.setText(StringUtils.excludeNull(earn2.getEarnings(), "0.0") + "%");
            mtvInterestRate3.setText("");
            mtvRange1.setText(StringUtils.excludeNull(earn1.getStartEarnings(), "0") + "万元-" + StringUtils.excludeNull(earn1.getEndEarnings(), "0") + "万元");
            mtvRange2.setText(StringUtils.excludeNull(earn2.getStartEarnings(), "0") + "万元-" + StringUtils.excludeNull(earn2.getEndEarnings(), "0") + "万元");
            mtvRange3.setText("");
            mllInterestRate3.setVisibility(View.GONE);
        } else {
            ProductEarn earn1 = earns.get(0);
            ProductEarn earn2 = earns.get(1);
            ProductEarn earn3 = earns.get(2);
            mtvInterestRate1.setText(StringUtils.excludeNull(earn1.getEarnings(), "0.0") + "%");
            mtvInterestRate2.setText(StringUtils.excludeNull(earn2.getEarnings(), "0.0") + "%");
            mtvInterestRate3.setText(StringUtils.excludeNull(earn3.getEarnings(), "0.0") + "%");
            mtvRange1.setText(StringUtils.excludeNull(earn1.getStartEarnings(), "0") + "万元-" + StringUtils.excludeNull(earn1.getEndEarnings(), "0") + "万元");
            mtvRange2.setText(StringUtils.excludeNull(earn2.getStartEarnings(), "0") + "万元-" + StringUtils.excludeNull(earn2.getEndEarnings(), "0") + "万元");
            mtvRange3.setText(StringUtils.excludeNull(earn3.getStartEarnings(), "0") + "万元-" + StringUtils.excludeNull(earn3.getEndEarnings(), "0") + "万元");
        }
    }

    private void initBrightPoint(List<BrightPoint> points) {
        mllBrightPoint.removeAllViews();
        if (points == null || points.isEmpty()) {
            return;
        }
        for (BrightPoint point : points) {
            final View item = LayoutInflater.from(this).inflate(R.layout.item_product_bright_point, null);
            TextView tvName = item.findViewById(R.id.tv_title);
            tvName.setText(StringUtils.excludeNull(point.getTitle(), "未知"));
            mllBrightPoint.addView(item);
            item.setTag(point);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
                    if (loginInfo == null) {
                        startActivityForResult(new Intent(activity, LoginActivity.class), LoginActivity.REQUEST_CODE_LOGIN);
                        return;
                    }
                    if (loginInfo.getStatus() == AppConstants.AuthenticationStatus.STATUS_2) {
                        BrightPoint data= (BrightPoint) item.getTag();
                        data.setCreateTime(mProduct.getAgeLimit());
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra(IntentKey.FROM_WHAT, AppConstants.WebFromWhat.BRIGHT_POINT);
                        intent.putExtra(IntentKey.WEB_CONTENT, (BrightPoint) item.getTag());
                        startActivity(intent);
                    } else {
                        showShortToast("请先实名认证");
                    }

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_finance_mechanism:
                //融资机构
                intent=new Intent(this,MechanismActivity.class);
                intent.putExtra(IntentKey.TITLE,"融资机构");
                intent.putExtra(IntentKey.CONTENT,mProduct.getFundingAgencies());
                startActivity(intent);
                break;
            case R.id.ll_guarantee_mechanism:
                //担保机构
                intent=new Intent(this,MechanismActivity.class);
                intent.putExtra(IntentKey.TITLE,"担保机构");
                intent.putExtra(IntentKey.CONTENT,mProduct.getGuaranteeAgencies());
                startActivity(intent);
                break;
            case R.id.ll_issue_mechanism:
                //发行机构
                intent=new Intent(this,MechanismActivity.class);
                intent.putExtra(IntentKey.TITLE,"发行机构");
                intent.putExtra(IntentKey.CONTENT,mProduct.getIssuer());
                startActivity(intent);
                break;
            case R.id.ll_talk:
                //评论
                intent = new Intent(this, LeaveTalkAreaActivity.class);
                intent.putExtra(IntentKey.PRODUCT_ID, mProduct.getId());
                startActivity(intent);
                break;
            case R.id.ll_collect:
                //收藏
                if (MyApplication.getInstance().getLoginInfo() == null) {
                    startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.REQUEST_CODE_LOGIN);
                    return;
                }
                doCollect();
                break;
            case R.id.tv_buy:
                //收藏
                if (MyApplication.getInstance().getLoginInfo() == null) {
                    startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.REQUEST_CODE_LOGIN);
                    return;
                }
                doPurchase();
                break;
        }
    }

    private void doCollect() {
        showProgressDialog("操作中，请稍后...", true, TAG_COLLECT);
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PROJECT_ID, mProduct.getId());
        param.addParam("status", "1".equals(mProduct.getIsCollection()) ? 0 : 1);
        RequestUtil.getInstance(this).collectProduct(TAG_COLLECT, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    showShortToast(requestResult.getMsg());
                    mvCollect.setBackgroundResource(StringUtils.isEquals(mProduct.getIsCollection(), "1") ? R.mipmap.ic_collect : R.mipmap.ic_my_collection);
                    mtvCollect.setText(StringUtils.isEquals(mProduct.getIsCollection(), "1") ? "收藏" : "取消收藏");
                    mProduct.setIsCollection(StringUtils.isEquals(mProduct.getIsCollection(), "1") ? "0" : "1");
                    setResult(RESULT_OK);
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "操作失败，请重试"));
                }
            }
        });
    }

    private void doPurchase() {
        showProgressDialog("购买中，请稍后...", true, TAG_PURCHASE);
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PROJECT_ID, mProduct.getId());
        RequestUtil.getInstance(this).buyProduct(TAG_COLLECT, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    showShortToast("购买成功");
                    setResult(RESULT_OK);
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "购买失败，请重试"));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.REQUEST_CODE_LOGIN) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
