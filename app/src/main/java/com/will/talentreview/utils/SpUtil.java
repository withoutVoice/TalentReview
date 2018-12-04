package com.will.talentreview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.will.talentreview.constant.Config;

import java.util.Set;

/**
 * SharedPreferences工具类
 *
 * @author yanchenglong
 * @time 2018-06-01
 */

public class SpUtil {

    private SharedPreferences sharedPreferences;
    private Editor editor;

    /**
     * 构造方法
     *
     * @param context 上下文对象
     */
    public SpUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.SP_NAME, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public SpUtil(Context context, String spName) {
        sharedPreferences = context.getSharedPreferences(spName, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 存值
     *
     * @param key   key
     * @param value value
     */
    public void put(String key, Object value) {
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set) value);
        } else {
            if (value == null) {
                value = "";
                editor.putString(key, (String) value);
            }
        }
        editor.commit();
    }

    /**
     * 取值
     *
     * @param key      key
     * @param defValue 默认值
     * @return 结果
     */
    public Object get(String key, Object defValue) {
        Object value = null;
        if (defValue instanceof Integer) {
            value = sharedPreferences.getInt(key, (Integer) defValue);
        } else if (defValue instanceof String) {
            value = sharedPreferences.getString(key, (String) defValue);
        } else if (defValue instanceof Boolean) {
            value = sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Long) {
            value = sharedPreferences.getLong(key, (Long) defValue);
        } else if (defValue instanceof Float) {
            value = sharedPreferences.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Set) {
            value = sharedPreferences.getStringSet(key, (Set) defValue);
        }
        return value;
    }

    /**
     * 移除键值对
     *
     * @param key key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有键值对
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

}

