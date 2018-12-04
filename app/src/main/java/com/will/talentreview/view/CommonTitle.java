package com.will.talentreview.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.will.talentreview.R;
import com.will.talentreview.utils.StringUtils;

/**
 * @author yanchenglong
 * @time 2018-09-13
 */

public class CommonTitle implements View.OnClickListener {
    /**
     * 显示左图片、中间文字、右文字
     */
    public static final int TITLE_TYPE_1 = 1;
    /**
     * 显示左图片、中间文字、右图片
     */
    public static final int TITLE_TYPE_2 = 2;
    /**
     * 显示中间文字、右图片
     */
    public static final int TITLE_TYPE_3 = 3;
    /**
     * 显示左图片、中间文字
     */
    public static final int TITLE_TYPE_4 = 4;
    /**
     * 显示中间文字
     */
    public static final int TITLE_TYPE_5 = 5;
    /**
     * 只显示搜索框
     */
    public static final int TITLE_TYPE_6 = 6;
    /**
     * 显示搜索框，右侧图标加文字按钮
     */
    public static final int TITLE_TYPE_7 = 7;
    /**
     * 显示搜索框，右侧文字按钮
     */
    public static final int TITLE_TYPE_8 = 8;

    private Activity activity;
    private ImageView ivTitleLeft;
    private TextView tvTitleCenter;
    private ImageView ivTitleRight;
    private TextView tvTitleRight;
    private LinearLayout llSearch;
    private EditText etSearch;
    private View vTitleBar;
    private LinearLayout llRightBtn;
    private ImageView ivRightIcon;
    private TextView tvRightText;
    private View parentView;

    public CommonTitle(Activity activity, View view, int titleType) {
        this.activity = activity;
        vTitleBar = view;
        initView(view);
        initViewVisibility(titleType);
        setStatusBarTranslucentLayout(view);
        this.parentView=view;
    }

    private void initView(View view) {
        ivTitleLeft = view.findViewById(R.id.iv_title_left);
        tvTitleCenter = view.findViewById(R.id.tv_title_center);
        ivTitleRight = view.findViewById(R.id.iv_title_right);
        tvTitleRight = view.findViewById(R.id.tv_title_right);
        llSearch=view.findViewById(R.id.ll_search);
        etSearch=view.findViewById(R.id.et_search);
        llRightBtn=view.findViewById(R.id.ll_right_btn);
        ivRightIcon=view.findViewById(R.id.iv_right_icon);
        tvRightText=view.findViewById(R.id.tv_right_text);
    }

    private void initViewVisibility(int titleType) {
        switch (titleType) {
            case TITLE_TYPE_1:
                ivTitleLeft.setVisibility(View.VISIBLE);
                tvTitleCenter.setVisibility(View.VISIBLE);
                tvTitleRight.setVisibility(View.VISIBLE);
                break;
            case TITLE_TYPE_2:
                ivTitleLeft.setVisibility(View.VISIBLE);
                tvTitleCenter.setVisibility(View.VISIBLE);
                ivTitleRight.setVisibility(View.VISIBLE);
                break;
            case TITLE_TYPE_3:
                ivTitleLeft.setVisibility(View.INVISIBLE);
                tvTitleCenter.setVisibility(View.VISIBLE);
                ivTitleRight.setVisibility(View.VISIBLE);
                break;
            case TITLE_TYPE_4:
                ivTitleLeft.setVisibility(View.VISIBLE);
                tvTitleCenter.setVisibility(View.VISIBLE);
                ivTitleRight.setVisibility(View.INVISIBLE);
                break;
            case TITLE_TYPE_5:
                tvTitleCenter.setVisibility(View.VISIBLE);
                break;
            case TITLE_TYPE_6:
                llSearch.setVisibility(View.VISIBLE);
                etSearch.setFocusable(false);
                etSearch.setVisibility(View.GONE);
                break;
            case TITLE_TYPE_7:
                llSearch.setVisibility(View.VISIBLE);
                llRightBtn.setVisibility(View.VISIBLE);
                etSearch.setFocusable(false);
                etSearch.setVisibility(View.GONE);
                break;
            case TITLE_TYPE_8:
                ivTitleLeft.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                llRightBtn.setVisibility(View.VISIBLE);
                ivRightIcon.setVisibility(View.GONE);
                etSearch.setFocusableInTouchMode(true);
                int dip15=activity.getResources().getDimensionPixelSize(R.dimen.dip_15);
                int dip7=activity.getResources().getDimensionPixelSize(R.dimen.dip_7);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
                params.setMargins(0,dip7,dip15,dip7);
                llSearch.setLayoutParams(params);
                break;
        }
    }

    public void setTitleLeftListener() {
        ivTitleLeft.setOnClickListener(this);
    }

    public void setTitleLeftListener(View.OnClickListener onClickListener) {
        ivTitleLeft.setOnClickListener(onClickListener);
    }

    public void setTitleCenter(String text) {
        tvTitleCenter.setText(text);
    }

    public void setIvTitleRightListener(int imageId, View.OnClickListener onClickListener) {
        ivTitleRight.setImageResource(imageId);
        ivTitleRight.setOnClickListener(onClickListener);
    }

    public void setTvTitleRightListener(String text, View.OnClickListener onClickListener) {
        tvTitleRight.setText(text);
        tvTitleRight.setOnClickListener(onClickListener);
    }

    public void setLlTitleSearchClickListener(String text, View.OnClickListener onClickListener){
        etSearch.setText(StringUtils.excludeNull(text));
        llSearch.setOnClickListener(onClickListener);
    }

    public void setLlRightBtnClickListener(String text, View.OnClickListener onClickListener){
        tvRightText.setText(text);
        llRightBtn.setOnClickListener(onClickListener);
    }

    public EditText getSearchEditText(){
        return etSearch;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_title_left) {
            activity.finish();
        }
    }

    /**
     * 设置标题栏高度
     */
    public void setStatusBarTranslucentLayout(View view) {
        LinearLayout llTitle = view.findViewById(R.id.ll_title_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(activity);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = activity.getResources().getDimensionPixelSize(R.dimen.dip_50) + statusBarHeight;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTitle.getLayoutParams();
            params.setMargins(0, statusBarHeight, 0, 0);
            llTitle.setLayoutParams(params);
        }
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏标题栏
     * @param isHide 是否隐藏
     * */
    public void hideTitleBar(boolean isHide) {
        vTitleBar.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    public void setBackgroundColor(int color){
        this.parentView.setBackgroundColor(color);
    }
}
