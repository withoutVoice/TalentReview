package com.will.talentreview.utils;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

import com.will.talentreview.application.MyApplication;
import com.will.talentreview.view.MyPopupWindow;

/**
 * @author chenwei
 * @time 2018-11-26
 */

public class PopUtils {

    public static void showAsDropDown(PopupWindow pop, View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT < 24) {
            pop.showAsDropDown(anchor, 0, 0, gravity);
        } else {
            pop.showAtLocation(anchor, gravity, 0, anchor.getHeight() + getStatusBarHeight());
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        try {
            Resources resource = MyApplication.getInstance().getResources();
            int resourceId = resource.getIdentifier("status_bar_height", "dimen", "Android");
            if (resourceId != 0) {
                return resource.getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
        }
        return 0;
    }
}
