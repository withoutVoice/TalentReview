package com.will.talentreview.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 格式化时间
 *
 * @author chengwei
 * @time 2018-06-12
 */

public class DateUtils {

    private static final Locale LOCALE = new Locale("zh", "CN");
    public final static SimpleDateFormat FORMAT1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", LOCALE);
    public final static SimpleDateFormat FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", LOCALE);
    public final static SimpleDateFormat FORMAT3 = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
    public final static SimpleDateFormat FORMAT4 = new SimpleDateFormat("yyyy年MM月dd日", LOCALE);
    public final static SimpleDateFormat FORMAT5 = new SimpleDateFormat("yyyy/MM/dd", LOCALE);
    public final static SimpleDateFormat FORMAT6 = new SimpleDateFormat("HH:mm:ss", LOCALE);
    public final static SimpleDateFormat FORMAT7 = new SimpleDateFormat("HH:mm", LOCALE);

    /**
     * 获取当前时间的yyyy-MM-dd HH:mm:ss格式
     *
     * @param time 时间（毫秒）
     * @return yyyy-MM-dd HH:mm格式的当前时间
     */
    public static String getYyyyMMddHHmmss(long time) {
        try {
            return FORMAT1.format(time);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前时间的yyyy-MM-dd HH:mm格式
     *
     * @param time 时间（毫秒）
     * @return yyyy-MM-dd HH:mm格式的当前时间
     */
    public static String getYyyyMMddHHmm(long time) {
        try {
            return FORMAT2.format(time);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前时间的yyyy-MM-dd格式
     *
     * @param time 时间（毫秒）
     * @return yyyy-MM-dd格式的当前时间
     */
    public static String getYyyyMMdd(long time) {
        try {
            return FORMAT3.format(time);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前时间的yyyy年MM月dd日格式
     *
     * @param time 时间（毫秒）
     * @return yyyy年MM月dd日格式的当前时间
     */
    public static String getYyyyMMddZh(long time) {
        try {
            return FORMAT4.format(time);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前时间的yyyy/MM/dd格式
     *
     * @param time 时间（毫秒）
     * @return yyyy/MM/dd格式的当前时间
     */
    public static String getYyyyMMddSlant(long time) {
        try {
            return FORMAT5.format(time);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前时间的HH:mm:ss格式
     *
     * @param time 时间（毫秒）
     * @return HH:mm:ss格式的当前时间
     */
    public static String getHHmmss(long time) {
        try {
            return FORMAT6.format(time);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前时间的HH:mm格式
     *
     * @param time 时间（毫秒）
     * @return HH:mm格式的当前时间
     */
    public static String getHHmm(long time) {
        try {
            return FORMAT7.format(time);
        } catch (Exception e) {
            return "";
        }
    }

}
