package com.will.talentreview.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 退出application 关闭所有activity
 *
 * @author chenwei
 * @time 2018/11/19
 */

public class ExitApplication {
    private List<Activity> activityList = new ArrayList<>();
    private static volatile ExitApplication exitApplication;

    private ExitApplication() {
    }

    /**
     * 单例模式中获取唯一的ExitApplication实例
     *
     * @return 实例
     */
    public static ExitApplication getInstance() {
        if (exitApplication == null) {
            synchronized (ExitApplication.class) {
                if (null == exitApplication) {
                    exitApplication = new ExitApplication();
                }
            }
        }
        return exitApplication;
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity 活动
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除容器中Activity
     *
     * @param activity 活动
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 遍历所有Activity并finish
     */
    public void exit() {
        for (Activity activity : activityList) {
            if (activity.isFinishing()) {
                continue;
            }
            activity.finish();
        }
        activityList.clear();
    }

    /**
     * 获取当前打开的activity的个数
     *
     * @return
     */
    public int getActivityCount() {
        int count = 0;
        for (Activity activity : activityList) {
            if (activity.isFinishing()) {
                continue;
            } else {
                count++;
            }
        }
        return count;
    }
}
