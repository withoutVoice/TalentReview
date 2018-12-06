package com.will.talentreview.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.will.talentreview.R;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.Config;
import com.will.talentreview.entity.LoginInfo;
import com.will.talentreview.utils.CommUtils;
import com.will.talentreview.utils.CrashHandler;

/**
 * @author chenwei
 * @time 2018-11-19
 */

public class MyApplication extends Application {

    private String packageName;

    private LoginInfo loginInfo;

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.text_color_deep, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        packageName = getPackageName();
        initConfig();
        CrashHandler.getInstance().init(this);
    }

    private void initConfig() {
        Config.SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();
        Config.CRASH_FILE_PATH = Config.SD_CARD_PATH + "/" + packageName + "crash/";
        Config.APK_FILE_PATH = Config.SD_CARD_PATH + "/" + packageName + "/apk/";
        Config.IMAGE_FILE_PATH = Config.SD_CARD_PATH + "/" + packageName + "/image/";
        Config.THUMBNAIL_FILE_PATH = Config.SD_CARD_PATH + "/" + packageName + "/thumbnail/";
        Config.CROP_PIC_PATH = Config.SD_CARD_PATH + "/" + packageName + "/crop/";
        Config.VIDEO_PATH = Config.SD_CARD_PATH + "/" + packageName + "/video/";
        Config.VOICE_PATH = Config.SD_CARD_PATH + "/" + packageName + "/voice/";
    }

    /**
     * 保存登录方式
     *
     * @param method 登录方式
     */
    public boolean saveLoginMethod(int method) {
        if (loginInfo == null) {
            return false;
        }
        boolean result = CommUtils.saveData(this, Config.SP_NAME, AppConstants.SpKey.LOGIN_METHOD, method);
        return result;
    }

    /**
     * 获取登录方式
     */
    public int getLoginMethod() {
        int result = CommUtils.getDataInt(this, Config.SP_NAME, AppConstants.SpKey.LOGIN_METHOD, -1);
        return result;
    }

    /**
     * 保存登录名
     *
     * @param loginName 登录名
     */
    public boolean saveLoginName(String loginName) {
        if (loginInfo == null) {
            return false;
        }
        boolean result = CommUtils.saveData(this, Config.SP_NAME, AppConstants.SpKey.LOGIN_NAME, loginName);
        return result;
    }

    /**
     * 获取登录名
     */
    public String getLoginName() {
        String result = CommUtils.getDataString(this, Config.SP_NAME, AppConstants.SpKey.LOGIN_NAME, null);
        return result;
    }

    /**
     * 保存用户信息
     *
     * @param loginInfo 保存用户
     */
    public boolean saveLoginInfo(LoginInfo loginInfo) {
        if (loginInfo == null) {
            return false;
        }
        boolean result = CommUtils.saveData(this, Config.SP_NAME, AppConstants.SpKey.LOGIN_INFO, loginInfo);
        if (result) {
            this.loginInfo = loginInfo;
        }
        return result;
    }

    /**
     * 清除用户信息
     */
    public boolean clearLoginInfo() {
        loginInfo = null;
        return CommUtils.saveData(this, Config.SP_NAME, AppConstants.SpKey.LOGIN_INFO, "");
    }

    /**
     * 获取用户信息
     */
    public LoginInfo getLoginInfo() {
        if (loginInfo == null) {
            loginInfo = (LoginInfo) CommUtils.getDataObject(this, Config.SP_NAME, AppConstants.SpKey.LOGIN_INFO, null);
        }
        return loginInfo;
    }

}
