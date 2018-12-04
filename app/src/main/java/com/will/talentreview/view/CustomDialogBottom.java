package com.will.talentreview.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.will.talentreview.R;
import com.will.talentreview.adapter.DialogTextAdapter;

import java.util.List;

/**
 * 底部List菜单弹框
 *
 * @author yanchenglong
 * @time 2017-06-08
 */
public class CustomDialogBottom extends Dialog {

    private AdapterView.OnItemClickListener onItemClickListener;

    public CustomDialogBottom(@NonNull Context context, List<String> menuItems) {
        super(context, R.style.bottom_dialog);
        this.contentList = menuItems;
    }


    /**
     * 内容
     */
    private List<String> contentList;
    private ListView lvContent;
    private DialogTextAdapter adapter;

    public void refreshData(List<String> contentList) {
        if (contentList != null && contentList.size() > 0) {
            this.contentList.addAll(contentList);
        }
        adapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置点击除了对话框以外的部分就关闭
        setContentView(R.layout.dialog_custom_bottom);
        setCancelable(true);
        lvContent = findViewById(R.id.lv_dialog_bottom_list);
        lvContent.setOnItemClickListener(onItemClickListener);
        adapter = new DialogTextAdapter(getContext(), this.contentList);
        lvContent.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        changeWidowSize();
    }

    private void changeWidowSize() {
        // 设置宽度满屏
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 设置宽和高
        if (contentList.size() > 10) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (dm.heightPixels * 0.65));
        } else {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //移动弹出菜单到底部
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wlp);
    }
}
