package com.will.talentreview.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.entity.BrightPoint;
import com.will.talentreview.entity.HomeAdv;
import com.will.talentreview.entity.NoticeInfo;
import com.will.talentreview.utils.DateUtils;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.view.CommonTitle;
import com.will.talentreview.view.NoScrollWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by 38093 on 2018/12/1.
 * 公告详情
 */

public class WebViewActivity extends BaseActivity {

    private TextView tvTitle;
    private TextView tvDate;
    private NoScrollWebView mWebView;
    private ProgressBar mpbWeb;

    private CommonTitle commonTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void initView() {
        commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("公告详情");
        commonTitle.setTitleLeftListener();

        tvTitle = findViewById(R.id.tv_title);
        tvDate = findViewById(R.id.tv_date);
        mWebView = findViewById(R.id.web_view);
        mpbWeb = findViewById(R.id.pb_web);

    }

    @Override
    protected void initData() {
        int fromWhat = getIntent().getIntExtra(IntentKey.FROM_WHAT, AppConstants.WebFromWhat.GALLERY);
        String title = "";
        String date = "";
        String html = "";
        switch (fromWhat) {
            case AppConstants.WebFromWhat.GALLERY:
                commonTitle.setTitleCenter("详情");
                HomeAdv adv = (HomeAdv) getIntent().getSerializableExtra(IntentKey.WEB_CONTENT);
                if (adv == null) {
                    showShortToast("数据有误");
                    finish();
                }
                title = adv.getTitle();
                date = adv.getCreateTime();
                html = adv.getContent();
                break;
            case AppConstants.WebFromWhat.NOTICE:
                commonTitle.setTitleCenter("公告详情");
                NoticeInfo noticeInfo = (NoticeInfo) getIntent().getSerializableExtra(IntentKey.WEB_CONTENT);
                if (noticeInfo == null) {
                    showShortToast("数据有误");
                    finish();
                }
                title = noticeInfo.getTitle();
                date = noticeInfo.getCreateTime();
                html = noticeInfo.getContent();
                break;
            case AppConstants.WebFromWhat.BRIGHT_POINT:
                commonTitle.setTitleCenter("亮点详情");
                BrightPoint point = (BrightPoint) getIntent().getSerializableExtra(IntentKey.WEB_CONTENT);
                if (point == null) {
                    showShortToast("数据有误");
                    finish();
                }
                title = point.getTitle();
                date = point.getCreateTime();
                html = point.getContent();
                break;
        }
        tvTitle.setText(StringUtils.excludeNull(title));
        tvDate.setText(StringUtils.excludeNull(date, DateUtils.getYyyyMMdd(System.currentTimeMillis())));
        WebSettings webSettings = mWebView.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
        webSettings.setSupportZoom(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mpbWeb.setVisibility(View.GONE);//加载完网页进度条消失
                    view.scrollTo(0, 0);
                } else {
                    mpbWeb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mpbWeb.setProgress(newProgress);//设置进度值
                }
            }
        });
        html = getNewContent(html);
        mWebView.loadData(StringUtils.excludeNull(html), "text/html; charset=UTF-8", null);
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public static String getNewContent(String htmlText) {
        try {
            Document doc = Jsoup.parse(htmlText);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmlText;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
    }
}
