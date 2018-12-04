package com.will.talentreview.utils;

import android.text.TextUtils;

/**
 * 字符串处理工具类
 *
 * @author chenwei
 * @time 2018/11/19
 */

public class StringUtils {

    /**
     * 排除空字符串,如果为空，返回newStr
     */
    public static String excludeNull(String oldStr, String newStr) {
        if (TextUtils.isEmpty(oldStr)) {
            if (TextUtils.isEmpty(newStr)) {
                return "";
            } else {
                return newStr;
            }
        } else {
            return oldStr;
        }
    }

    /**
     * 排除空字符串,如果为空，返回""
     */
    public static String excludeNull(String oldStr) {
        return excludeNull(oldStr, "");
    }

    public static boolean isEquals(String leftStr, String rightStr) {
        if (TextUtils.isEmpty(leftStr) || TextUtils.isEmpty(rightStr)) {
            return false;
        }
        return leftStr.equals(rightStr);
    }

}
