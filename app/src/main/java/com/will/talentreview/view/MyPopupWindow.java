package com.will.talentreview.view;

import android.content.Context;
import android.os.Build;
import android.transition.Transition;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @author chenwei
 * @time 2018-11-26
 * 解决7.0bug
 */

public class MyPopupWindow extends PopupWindow {


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] a = new int[2];
            parent.getLocationInWindow(a);
            showAtLocation(parent, gravity, x, a[1] + parent.getHeight() + y);
        } else {
            super.showAsDropDown(parent, x, y);
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            showAtLocation(anchor, Gravity.NO_GRAVITY, 0, a[1] + anchor.getHeight() + 0);
        } else {
            super.showAsDropDown(anchor);
        }
    }
}
