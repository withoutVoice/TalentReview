package com.will.talentreview.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.activity.AdvantageListActivity;
import com.will.talentreview.activity.MainActivity;
import com.will.talentreview.activity.ProductDetailActivity;
import com.will.talentreview.activity.WebViewActivity;
import com.will.talentreview.activity.NoticeListActivity;
import com.will.talentreview.activity.SearchProductActivity;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.HomeAdv;
import com.will.talentreview.entity.ListBean;
import com.will.talentreview.entity.NoticeInfo;
import com.will.talentreview.entity.Product;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.GlideUtil;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-19
 */

public class TabHomeFragment extends BaseFragment implements View.OnClickListener {

    public static synchronized TabHomeFragment getInstance() {
        return new TabHomeFragment();
    }

    private final int REQUEST_CODE_PRODUCT_DETAIL = 101;

    private ViewPager mViewPager;
    private LinearLayout mllIndication;
    private List<HomeAdv> mAdvs;
    private MyPagerAdapter pagerAdapter;
    private LayoutInflater mInflater;
    private List<View> mAdvViews;

    private LinearLayout mllNotice;
    private TextView mtvNotice;
    private TextView mtvNoticeMore;
    private LinearLayout mllInvestment;
    private LinearLayout mllGuide;
    private LinearLayout mllSchool;
    private LinearLayout mllRecommend;
    private LinearLayout mllGift;
    private TextView mtvRecommendTitle;
    private TextView mtvInterestRate;
    private TextView mtvType;
    private TextView mtvYearLimit;
    private TextView mtvMoneyLimit;
    private TextView mtvAdvantage;
    private TextView mtvBuy;

    private Product mProduct1;
    private Product mProduct2;


    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_home;
    }

    @Override
    protected void initView(View contentView) {
        CommonTitle commonTitle = new CommonTitle(activity, contentView.findViewById(R.id.include), CommonTitle.TITLE_TYPE_6);
        commonTitle.setLlTitleSearchClickListener("", this);
        commonTitle.getSearchEditText().setOnClickListener(this);

        mllIndication = contentView.findViewById(R.id.ll_indications);
        mViewPager = contentView.findViewById(R.id.view_pager);

        mllNotice = contentView.findViewById(R.id.ll_notice);
        mtvNotice = contentView.findViewById(R.id.tv_notice);
        mtvNoticeMore = contentView.findViewById(R.id.tv_more_notice);
        mllInvestment = contentView.findViewById(R.id.ll_investment);
        mllGuide = contentView.findViewById(R.id.ll_guide);
        mllSchool = contentView.findViewById(R.id.ll_school);
        mllRecommend = contentView.findViewById(R.id.ll_recommend);
        mllGift = contentView.findViewById(R.id.ll_gift);
        mtvRecommendTitle = contentView.findViewById(R.id.tv_recommend_title);
        mtvInterestRate = contentView.findViewById(R.id.tv_interest_rate);
        mtvType = contentView.findViewById(R.id.tv_type);
        mtvYearLimit = contentView.findViewById(R.id.tv_year_limit);
        mtvMoneyLimit = contentView.findViewById(R.id.tv_money_limit);
        mtvAdvantage = contentView.findViewById(R.id.tv_advantage);
        mtvBuy = contentView.findViewById(R.id.tv_buy);

        mtvNotice.setOnClickListener(this);
        mtvNoticeMore.setOnClickListener(this);
        mllInvestment.setOnClickListener(this);
        mllGuide.setOnClickListener(this);
        mllSchool.setOnClickListener(this);
        mllRecommend.setOnClickListener(this);
        mllGift.setOnClickListener(this);
        mtvBuy.setOnClickListener(this);
        mtvAdvantage.setOnClickListener(this);
        mllRecommend.performClick();
    }

    @Override
    protected void initData() {

        mInflater = LayoutInflater.from(activity);

        mAdvs = new ArrayList<>();
        mAdvViews = new ArrayList<>();
        pagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mllIndication.getChildCount(); i++) {
                    mllIndication.getChildAt(i).setSelected(false);
                }
                mllIndication.getChildAt(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getGalleryData();
        getNotice();
        getTodayRecommend();
        getNewPlayer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivity(new Intent(activity, SearchProductActivity.class));
                break;
            case R.id.tv_notice:
                Intent intentNotice = new Intent(activity, WebViewActivity.class);
                intentNotice.putExtra(IntentKey.WEB_CONTENT, (NoticeInfo) view.getTag());
                intentNotice.putExtra(IntentKey.FROM_WHAT, AppConstants.WebFromWhat.NOTICE);
                startActivity(intentNotice);
                break;
            case R.id.tv_more_notice:
                startActivity(new Intent(activity, NoticeListActivity.class));
                break;
            case R.id.ll_investment:
                ((MainActivity) getActivity()).switchToProduct();
                break;
            case R.id.ll_guide:
                Intent intentGuide = new Intent(activity, AdvantageListActivity.class);
                intentGuide.putExtra(IntentKey.FROM_WHAT, AppConstants.HomeDataType.TYPE3);
                startActivity(intentGuide);
                break;
            case R.id.ll_school:
                Intent intentSchool = new Intent(activity, AdvantageListActivity.class);
                intentSchool.putExtra(IntentKey.FROM_WHAT, AppConstants.HomeDataType.TYPE4);
                startActivity(intentSchool);
                break;
            case R.id.ll_recommend:
                mllRecommend.setSelected(true);
                mllGift.setSelected(false);
                setProduct();
                break;
            case R.id.ll_gift:
                mllRecommend.setSelected(false);
                mllGift.setSelected(true);
                setProduct();
                break;
            case R.id.tv_buy:
                if (mllRecommend.isSelected()) {
                    if (mProduct1 == null) {
                        return;
                    }
                    Intent intentBuy = new Intent(getActivity(), ProductDetailActivity.class);
                    intentBuy.putExtra(IntentKey.PRODUCT, mProduct1);
                    startActivityForResult(intentBuy, REQUEST_CODE_PRODUCT_DETAIL);
                } else {
                    if (mProduct2 == null) {
                        return;
                    }
                    Intent intentBuy = new Intent(getActivity(), ProductDetailActivity.class);
                    intentBuy.putExtra(IntentKey.PRODUCT, mProduct2);
                    startActivityForResult(intentBuy, REQUEST_CODE_PRODUCT_DETAIL);
                }
                break;
            case R.id.tv_advantage:
                Intent intentAdvantage = new Intent(activity, AdvantageListActivity.class);
                intentAdvantage.putExtra(IntentKey.FROM_WHAT, AppConstants.HomeDataType.TYPE5);
                startActivity(intentAdvantage);
                break;
        }
    }

    private void initGallery() {
        mAdvViews.clear();
        if (mAdvs == null || mAdvs.isEmpty()) {
            View view = mInflater.inflate(R.layout.item_home_gallery, null);
            ImageView img = view.findViewById(R.id.iv_gallery);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setText("暂无");
            img.setBackgroundColor(getResources().getColor(R.color.common_bg_gray));
            mAdvViews.add(view);
        } else {
            for (final HomeAdv adv : mAdvs) {
                View view = mInflater.inflate(R.layout.item_home_gallery, null);
                ImageView img = view.findViewById(R.id.iv_gallery);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                GlideUtil.showCommonImage(activity, adv.getPicPath(), img);
                tvTitle.setText(adv.getTitle());
                mAdvViews.add(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra(IntentKey.FROM_WHAT, AppConstants.WebFromWhat.GALLERY);
                        intent.putExtra(IntentKey.WEB_CONTENT, adv);
                        startActivity(intent);
                    }
                });
            }
        }
        pagerAdapter.notifyDataSetChanged();
        mllIndication.removeAllViews();
        int margin = getResources().getDimensionPixelSize(R.dimen.dip_2_5);
        int size = getResources().getDimensionPixelSize(R.dimen.dip_5);
        for (int i = 0; i < mAdvs.size(); i++) {
            View dot = new View(activity);
            dot.setBackgroundResource(R.drawable.bg_gallery_indication_selector);
            mllIndication.addView(dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(margin, 0, margin, 0);
            dot.setLayoutParams(params);
        }
        if (mllIndication.getChildCount() > 0) {
            mllIndication.getChildAt(0).setSelected(true);
        }
    }

    private void setNotice(NoticeInfo notice) {
        if (notice == null) {
            mtvNotice.setText("暂无");
            mtvNoticeMore.setVisibility(View.GONE);
            mtvNotice.setClickable(false);
        } else {
            mtvNotice.setClickable(true);
            mtvNotice.setTag(notice);
            mtvNotice.setText(StringUtils.excludeNull(notice.getTitle()));
            mtvNoticeMore.setVisibility(View.VISIBLE);
        }
    }

    private void setProduct() {
        Product product = null;
        if (mllRecommend.isSelected()) {
            product = mProduct1;
        } else {
            product = mProduct2;
        }
        if (product == null) {
            mtvRecommendTitle.setText("暂无");
            mtvInterestRate.setText("0.0");
            mtvType.setText("类型:未知");
            mtvYearLimit.setText("期限0月");
            mtvMoneyLimit.setText("起投0元");
        } else {
            mtvRecommendTitle.setText(StringUtils.excludeNull(product.getProductName()));
            mtvInterestRate.setText(StringUtils.excludeNull(product.getAnnualInterestRate(), "0.0"));
            mtvType.setText("类型:" + StringUtils.excludeNull(product.getProductType(), "未知"));
            mtvYearLimit.setText("期限" + StringUtils.excludeNull(product.getAgeLimit(), "0") + "月");
            mtvMoneyLimit.setText("起投" + StringUtils.excludeNull(product.getProductRule(), "0") + "元");
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mAdvViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mAdvViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    ;

    private void getGalleryData() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, 1);
        param.addParam(RequestKey.PAGE_SIZE, 3);
        param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE1);
        RequestUtil.getInstance(getActivity()).getHomeGallery("", param, new CustomRequestCallback<HomeAdv>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                if (requestResult.isSuccess()) {
                    ListBean<HomeAdv> bean = (ListBean<HomeAdv>) requestResult.getData();
                    if (bean != null) {
                        mAdvs = bean.getRecords();
                        initGallery();
                    } else {
                        mAdvs.clear();
                    }
                }
                initGallery();
            }
        });
    }

    private void getNotice() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, 1);
        param.addParam(RequestKey.PAGE_SIZE, 1);
        param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE2);
        RequestUtil.getInstance(getActivity()).getNoticeList("", param, new CustomRequestCallback<NoticeInfo>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                NoticeInfo notice = null;
                if (requestResult.isSuccess()) {
                    ListBean<NoticeInfo> bean = (ListBean<NoticeInfo>) requestResult.getData();
                    if (bean != null) {
                        List<NoticeInfo> notices = bean.getRecords();
                        if (notices != null && !notices.isEmpty()) {
                            notice = notices.get(0);
                        }
                    }
                }
                setNotice(notice);
            }
        });
    }

    private void getTodayRecommend() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, 1);
        param.addParam(RequestKey.PAGE_SIZE, 1);
        param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE2);
        param.addParam(RequestKey.PRODUCT_LABEL_ID, AppConstants.ProductLabel.RECOMMEND);
        RequestUtil.getInstance(getActivity()).getProductList("", param, new CustomRequestCallback<Product>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                if (requestResult.isSuccess()) {
                    ListBean<Product> bean = (ListBean<Product>) requestResult.getData();
                    if (bean != null) {
                        List<Product> products = bean.getRecords();
                        if (products != null && !products.isEmpty()) {
                            mProduct1 = products.get(0);
                        }
                    }
                }
                setProduct();
            }
        });
    }

    private void getNewPlayer() {
        RequestParam param = new RequestParam();
        param.addParam(RequestKey.PAGE, 1);
        param.addParam(RequestKey.PAGE_SIZE, 1);
        param.addParam(RequestKey.TYPE, AppConstants.HomeDataType.TYPE2);
        param.addParam(RequestKey.PRODUCT_LABEL_ID, AppConstants.ProductLabel.NEW_PLAYER);
        RequestUtil.getInstance(getActivity()).getProductList("", param, new CustomRequestCallback<Product>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                if (requestResult.isSuccess()) {
                    ListBean<Product> bean = (ListBean<Product>) requestResult.getData();
                    if (bean != null) {
                        List<Product> products = bean.getRecords();
                        if (products != null && !products.isEmpty()) {
                            mProduct2 = products.get(0);
                        }
                    }
                }
                setProduct();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getTodayRecommend();
            getNewPlayer();
        }
    }
}
